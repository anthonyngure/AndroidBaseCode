package ke.co.toshngure.basecode.rest;

import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ke.co.toshngure.basecode.R;
import ke.co.toshngure.basecode.app.BaseAppActivity;
import ke.co.toshngure.basecode.utils.BaseUtils;

/**
 * Created by Anthony Ngure on 10/03/2018.
 * Email : anthonyngure25@gmail.com.
 */

public abstract class Callback<M> {

    @Nullable
    private
    BaseAppActivity baseAppActivity;
    private boolean showDialog;
    @Nullable
    private Class<M> mClass;

    public Callback(@Nullable BaseAppActivity baseAppActivity) {
        this(baseAppActivity, null);
    }

    public Callback(@Nullable BaseAppActivity baseAppActivity, @Nullable Class<M> mClass) {
        this(baseAppActivity, mClass, true);
    }

    public Callback(@Nullable BaseAppActivity baseAppActivity, @Nullable Class<M> mClass, boolean showDialog) {
        this.baseAppActivity = baseAppActivity;
        this.showDialog = showDialog;
        this.mClass = mClass;
    }

    Class<M> getModelClass() {
        return mClass;
    }

    protected void onRetry() {
    }

    protected void onCancel() {
    }

    /**
     * If you provide a model class this is the method to be called after parsing the response
     *
     * @param item
     */
    protected void onResponse(M item) {
    }

    protected void onResponse(JSONObject data) {
    }

    protected void onResponse(List<M> items) {
    }

    protected void onResponse(JSONArray data) {
    }


    protected void onConnectionStarted() {
        Logger.log("onConnectionStarted");
        if (showDialog && baseAppActivity != null) {
            baseAppActivity.showProgressDialog();
        }
    }

    protected void onCantConnect() {
        Logger.log("onCantConnect");
        if (baseAppActivity != null) {
            if (showDialog) {
                baseAppActivity.hideProgressDialog();
            }
            new AlertDialog.Builder(baseAppActivity)
                    .setTitle(R.string.connection_timed_out)
                    .setMessage(R.string.error_connection)
                    .setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> onCancel())
                    .setPositiveButton(R.string.retry, (dialog, which) -> onRetry())
                    .create().show();
        }
    }

    protected void onFailure(int statusCode, JSONArray response) {
        Logger.log("Connection failed! " + statusCode + ", " + String.valueOf(response));
        if (showDialog && baseAppActivity != null) {
            baseAppActivity.hideProgressDialog();
        }
        showErrorAlertDialog(String.valueOf(response));
    }

    protected void onFailure(int statusCode, JSONObject response) {
        Logger.log("Connection failed! " + statusCode + ", " + String.valueOf(response));
        if (baseAppActivity != null) {
            if (showDialog) {
                baseAppActivity.hideProgressDialog();
            }

            String message = Client.getConfig().getResponseDefinition().message(statusCode, response);

            AlertDialog.Builder builder = new AlertDialog.Builder(baseAppActivity)
                    .setCancelable(true);
            if (!TextUtils.isEmpty(message)) {
                builder.setMessage(message)
                        .setPositiveButton(android.R.string.ok, null)
                        .setPositiveButton(R.string.retry, (dialog, which) -> onRetry())
                        .create().show();
            } else if (statusCode == 500) {
                builder.setTitle(R.string.server_error)
                        .setMessage(baseAppActivity.getString(R.string.error_application))
                        .setNegativeButton(R.string.report, (dialog, which) -> Client.getConfig().onReportError(response))
                        .setNegativeButton(android.R.string.ok, null)
                        .create().show();
            } else if (statusCode == 404) {
                builder.setTitle(R.string.not_found)
                        .setMessage(message)
                        .setNegativeButton(R.string.report, (dialog, which) -> Client.getConfig().onReportError(response))
                        .setPositiveButton(R.string.retry, (dialog, which) -> onRetry())
                        .create().show();
            } else {
                onCantConnect();
            }
        }
    }

    void onSuccess(JSONObject response) {
        Logger.log("onSuccess, Response = " + String.valueOf(response));
        if (showDialog && baseAppActivity != null) {
            baseAppActivity.hideProgressDialog();
        }
        try {
            String dataKey = Client.getConfig().getResponseDefinition().dataKey(response);
            if (dataKey == null) {
                //Data is not in a key in the response returned
                // the response JSONObject is the dataKey and if model class is provided it will be parsed
                if (mClass != null) {
                    M item = BaseUtils.getSafeGson().fromJson(response.getJSONObject(Response.DATA).toString(), mClass);
                    onResponse(item);
                } else {
                    onResponse(response);
                }

            } else {
                // Data is in a key in the response JSONObject returned
                // The dataKey could be a JSONArray or JSONObject
                if (response.get(dataKey) instanceof JSONObject) {
                    //Data is a JSONObject
                    if (mClass != null) {
                        M item = BaseUtils.getSafeGson().fromJson(response.getJSONObject(Response.DATA).toString(), mClass);
                        onResponse(item);
                    } else {
                        onResponse(response.getJSONObject(dataKey));
                    }
                } else {
                    //Data is a JSONArray
                    if (mClass != null) {
                        JSONArray data = response.getJSONArray(dataKey);
                        List<M> mList = new ArrayList<>();
                        for (int i = 0; i < data.length(); i++) {
                            M item = BaseUtils.getSafeGson().fromJson(data.getJSONObject(i).toString(), mClass);
                            mList.add(item);
                        }
                        onResponse(mList);
                    } else {
                        onResponse(response.getJSONArray(dataKey));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void onSuccess(JSONArray response) {
        Logger.log("onSuccess, Response = " + String.valueOf(response));
        if (showDialog && baseAppActivity != null) {
            baseAppActivity.hideProgressDialog();
        }
        //Data is Array
        try {
            if (mClass != null) {
                List<M> mList = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    M item = BaseUtils.getSafeGson().fromJson(response.getJSONObject(i).toString(), mClass);
                    mList.add(item);
                }
                onResponse(mList);
            } else {
                onResponse(response);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void onProgress(int progress) {
        if (baseAppActivity != null) {
            String newMessage = baseAppActivity.getString(R.string.message_waiting) + " " + String.valueOf(progress);
            baseAppActivity.updateProgressDialogMessage(newMessage);
        }
    }

    protected void showErrorAlertDialog(String message) {
        if (baseAppActivity != null) {
            new AlertDialog.Builder(baseAppActivity)
                    .setCancelable(true)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, null)
                    .create()
                    .show();
        }
    }

    protected RequestParams getRequestParams() {
        return new RequestParams();
    }

}

