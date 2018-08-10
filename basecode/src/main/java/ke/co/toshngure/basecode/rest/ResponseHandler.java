package ke.co.toshngure.basecode.rest;

import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import ke.co.toshngure.basecode.R;
import ke.co.toshngure.basecode.app.BaseAppActivity;
import ke.co.toshngure.basecode.logging.BeeLog;
import ke.co.toshngure.basecode.util.BaseUtils;

/**
 * Created by Anthony Ngure on 10/03/2018.
 * Email : anthonyngure25@gmail.com.
 */

public class ResponseHandler<M> extends JsonHttpResponseHandler {

    private static final String TAG = "ResponseHandler";

    private BaseAppActivity baseAppActivity;
    private boolean showDialog;
    @Nullable
    private Class<M> mClass;

    public ResponseHandler(BaseAppActivity baseAppActivity) {
        this(baseAppActivity, null);
    }

    public ResponseHandler(BaseAppActivity baseAppActivity, @Nullable Class<M> mClass) {
        this(baseAppActivity, mClass, true);
    }

    public ResponseHandler(BaseAppActivity baseAppActivity, @Nullable Class<M> mClass, boolean showDialog) {
        this.baseAppActivity = baseAppActivity;
        this.showDialog = showDialog;
        this.mClass = mClass;
    }

    Class<M> getModelClass() {
        return mClass;
    }

    protected void onUserRetry() {
    }

    protected void onUserCancel() {
    }

    /**
     * If you provide a model class this is the method to be called after parsing the response
     *
     * @param item
     */
    protected void onResponse(M item, @Nullable String message) {
    }

    protected void onResponse(JSONObject data) {
    }

    protected void onResponse(List<M> items, @Nullable String message) {
    }

    protected void onResponse(JSONArray data) {
    }

    protected void onError(@Nullable String message) {
        if (showDialog && !TextUtils.isEmpty(message)) {
            new AlertDialog.Builder(baseAppActivity)
                    .setCancelable(true)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, null)
                    .create()
                    .show();
        }
    }


    public void onStart() {
        super.onStart();
        if (BaseUtils.canConnect(Client.getConfig().getContext())) {
            BeeLog.i(TAG, "onConnectionStarted");
            if (showDialog) {
                baseAppActivity.showProgressDialog();
            }
        } else {
            onError(baseAppActivity.getString(R.string.message_server_error));
        }
    }


    @Override
    public void onProgress(long bytesWritten, long totalSize) {
        super.onProgress(bytesWritten, totalSize);
        try {
            /*if (bytesWritten == totalSize){
                long currentBytes = PrefUtilsImpl.getInstance().getLong(R.string.pref_bytes_count, 0);
                long newBytes = currentBytes+totalSize;
                PrefUtils.getInstance().writeLong(R.string.pref_bytes_count, newBytes);
            }*/
            double progress = (totalSize > 0) ? (bytesWritten * 1.0 / totalSize) * 100 : -1;
            if (baseAppActivity != null) {
                String newMessage = baseAppActivity.getString(R.string.message_waiting) + " " + String.valueOf(((int) progress)) + "%";
                baseAppActivity.updateProgressDialogMessage(newMessage);
            }
        } catch (Exception e) {
            BeeLog.i(TAG, e);
        }
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        super.onSuccess(statusCode, headers, response);
        BeeLog.i(TAG, "onSuccess, Response = " + String.valueOf(response));
        if (showDialog) {
            baseAppActivity.hideProgressDialog();
        }
        try {
            String dataKey = Client.getConfig().getResponseDefinition().dataKey(response);
            if (dataKey == null) {
                //Data is not in a key in the response returned
                // the response JSONObject is the dataKey and if model class is provided it will be parsed
                if (mClass != null) {
                    M item = BaseUtils.getSafeGson().fromJson(response.toString(), mClass);
                    onResponse(item, Client.getConfig().getResponseDefinition().message(statusCode, response));
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
                        onResponse(item, Client.getConfig().getResponseDefinition().message(statusCode, response));
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
                        onResponse(mList, Client.getConfig().getResponseDefinition().message(statusCode, response));
                    } else {
                        onResponse(response.getJSONArray(dataKey));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        super.onSuccess(statusCode, headers, response);
        BeeLog.i(TAG, "onSuccess, Response = " + String.valueOf(response));
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
                onResponse(mList, Client.getConfig().getResponseDefinition().message(statusCode, response));
            } else {
                onResponse(response);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        handleFailure(statusCode, String.valueOf(errorResponse),
                Client.getConfig().getResponseDefinition().message(statusCode, errorResponse));
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        handleFailure(statusCode, String.valueOf(errorResponse),
                Client.getConfig().getResponseDefinition().message(statusCode, errorResponse));
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        super.onFailure(statusCode, headers, responseString, throwable);
        handleFailure(statusCode, String.valueOf(responseString), responseString);
    }

    private void handleFailure(int statusCode, String response, String message) {
        BeeLog.i(TAG, "Connection failed! " + statusCode + ", " + response);
        switch (statusCode) {
            case 500:
                onError(baseAppActivity.getString(R.string.message_server_error));
                break;
            case 0:
                onError(baseAppActivity.getString(R.string.message_no_internet_connetion));
                break;
            default:
                Client.getConfig().onError(statusCode);
        }
        if (showDialog) {
            baseAppActivity.hideProgressDialog();
        }
        onError(message);
    }

}