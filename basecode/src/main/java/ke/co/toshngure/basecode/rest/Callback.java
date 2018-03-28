package ke.co.toshngure.basecode.rest;

import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ke.co.toshngure.basecode.R;
import ke.co.toshngure.basecode.app.BaseAppActivity;
import ke.co.toshngure.basecode.utils.BaseUtils;

/**
 * Created by Anthony Ngure on 10/03/2018.
 * Email : anthonyngure25@gmail.com.
 */

public abstract class Callback<M> {

    BaseAppActivity baseAppActivity;
    private boolean showDialog;
    @Nullable
    private Class<M> mClass;
    private boolean hasMetaAndData;

    public Callback(BaseAppActivity baseAppActivity) {
        this.baseAppActivity = baseAppActivity;
        this.showDialog = true;
        this.hasMetaAndData = true;
        this.mClass = null;
    }

    public Callback(BaseAppActivity baseAppActivity, @Nullable Class<M> mClass) {
        this.baseAppActivity = baseAppActivity;
        this.showDialog = true;
        this.hasMetaAndData = true;
        this.mClass = mClass;
    }

    public Callback(BaseAppActivity baseAppActivity, @Nullable Class<M> mClass, boolean hasMetaAndData) {
        this.baseAppActivity = baseAppActivity;
        this.showDialog = true;
        this.hasMetaAndData = hasMetaAndData;
        this.mClass = mClass;
    }

    public Callback(BaseAppActivity baseAppActivity, @Nullable Class<M> mClass, boolean hasMetaAndData, boolean showDialog) {
        this.baseAppActivity = baseAppActivity;
        this.hasMetaAndData = hasMetaAndData;
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

    protected void onResponse(M item, @Nullable JSONObject meta) {
    }

    protected void onResponse(JSONObject data, @Nullable JSONObject meta) {
    }

    protected void onResponse(List<M> items, @Nullable JSONObject meta) {
    }

    protected void onResponse(JSONArray data, @Nullable JSONObject meta) {
    }

    protected void onError(String errorCode, String message, JSONObject data) {
        switch (errorCode) {
            case CommonErrorCodes.VALIDATION:
                StringBuilder sb = new StringBuilder();
                Iterator<String> iterator = data.keys();
                while (iterator != null && iterator.hasNext()) {
                    String name = iterator.next();
                    try {
                        JSONArray valueErrors = data.getJSONArray(name);
                        sb.append(name.toUpperCase()).append("\n");
                        for (int i = 0; i < valueErrors.length(); i++) {
                            sb.append(valueErrors.get(i)).append("\n");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                showErrorAlertDialog(sb.toString());
                break;
            default:
                showErrorAlertDialog(message);
        }
    }

    protected void onError(String errorCode, String message, JSONArray data) {
        showErrorAlertDialog(message);
    }

    protected void onConnectionStarted() {
        Logger.log("onConnectionStarted");
        if (showDialog) {
            baseAppActivity.showProgressDialog();
        }
    }

    protected void onCantConnect() {
        Logger.log("onCantConnect");
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

    protected void onFail(int statusCode, JSONArray response) {
        Logger.log("Connection failed! " + statusCode + ", " + String.valueOf(response));
        if (showDialog) {
            baseAppActivity.hideProgressDialog();
        }
    }

    protected void onFail(int statusCode, JSONObject response) {
        Logger.log("Connection failed! " + statusCode + ", " + String.valueOf(response));
        if (showDialog) {
            baseAppActivity.hideProgressDialog();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(baseAppActivity);
        if ((statusCode == 422 || statusCode == 400) && response != null) {
            try {
                JSONObject meta = response.getJSONObject(Response.META);

                if (response.get(Response.DATA) instanceof JSONObject) {
                    //Data is Object
                    onError(meta.getString(Response.ERROR_CODE), meta.getString(Response.MESSAGE),
                            response.getJSONObject(Response.DATA));
                } else {
                    //Data is Array
                    onError(meta.getString(Response.ERROR_CODE), meta.getString(Response.MESSAGE),
                            response.getJSONArray(Response.DATA));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (statusCode == 500) {
            builder.setCancelable(true)
                    .setTitle(R.string.server_error)
                    .setMessage(baseAppActivity.getString(R.string.error_application))
                    .setNegativeButton(R.string.report, (dialog, which) -> {
                    })
                    .setPositiveButton(android.R.string.ok, null)
                    .create().show();
        } else if (statusCode == 404) {
            builder.setCancelable(true)
                    .setTitle("Not Found")
                    .setMessage(String.valueOf(response))
                    .setNegativeButton(R.string.report, (dialog, which) -> {
                    })
                    .setPositiveButton(android.R.string.ok, null)
                    .create().show();
        } else {
            onCantConnect();
        }
    }

    void onSuccess(JSONObject response) {
        Logger.log("onSuccess, Response = " + String.valueOf(response));
        if (showDialog) {
            baseAppActivity.hideProgressDialog();
        }
        try {
            if (hasMetaAndData && mClass != null) {
                if (response.get(Response.DATA) instanceof JSONObject) {
                    //Data is Object
                    M item = BaseUtils.getSafeGson().fromJson(response.getJSONObject(Response.DATA).toString(), mClass);
                    onResponse(item, response.getJSONObject(Response.META));
                } else {
                    //Data is Array
                    JSONArray data = response.getJSONArray(Response.DATA);
                    List<M> mList = new ArrayList<>();
                    for (int i = 0; i < data.length(); i++) {
                        M item = BaseUtils.getSafeGson().fromJson(data.getJSONObject(i).toString(), mClass);
                        mList.add(item);
                    }
                    onResponse(mList, response.getJSONObject(Response.META));
                }
            } else if (!hasMetaAndData && mClass != null) {
                //Data is Object
                M item = BaseUtils.getSafeGson().fromJson(response.getJSONObject(Response.DATA).toString(), mClass);
                onResponse(item, null);
            } else if (hasMetaAndData) {
                if (response.get(Response.DATA) instanceof JSONObject) {
                    //Data is Object
                    onResponse(response.getJSONObject(Response.DATA), response.getJSONObject(Response.META));
                } else {
                    //Data is Array
                    onResponse(response.getJSONArray(Response.DATA), response.getJSONObject(Response.META));
                }
            } else {
                onResponse(response, null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void onSuccess(JSONArray response) {
        Logger.log("onSuccess, Response = " + String.valueOf(response));
        if (showDialog) {
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
                onResponse(mList, null);
            } else {
                onResponse(response, null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void onProgress(int progress) {
        String newMessage = baseAppActivity.getString(R.string.message_waiting) + " " + String.valueOf(progress);
        baseAppActivity.updateProgressDialogMessage(newMessage);
    }

    protected void showErrorAlertDialog(String message) {
        new AlertDialog.Builder(baseAppActivity)
                .setCancelable(true)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .create()
                .show();
    }

    protected RequestParams getRequestParams() {
        return new RequestParams();
    }

}

