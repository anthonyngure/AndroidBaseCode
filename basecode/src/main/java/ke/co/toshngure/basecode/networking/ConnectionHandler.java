/*
 * Copyright (c) 2018.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.basecode.networking;

import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cz.msebera.android.httpclient.Header;
import ke.co.toshngure.basecode.R;
import ke.co.toshngure.basecode.app.BaseAppActivity;
import ke.co.toshngure.basecode.utils.BaseUtils;

/**
 * Created by Anthony Ngure on 27/07/2017.
 * Email : anthonyngure25@gmail.com.
 */

public class ConnectionHandler extends JsonHttpResponseHandler {

    public static final String TAG = ConnectionHandler.class.getSimpleName();

    private Callback mCallback;

    public ConnectionHandler(Callback callback) {
        this.mCallback = callback;
    }


    public void onStart() {
        super.onStart();
        if (BaseUtils.canConnect(mCallback.baseAppActivity)) {
            mCallback.onConnectionStarted();
        } else {
            mCallback.onCantConnect();
        }
    }

    private void log(Object msg) {
        Log.d(TAG, String.valueOf(msg));
    }

    @Override
    public void onProgress(long bytesWritten, long totalSize) {
        super.onProgress(bytesWritten, totalSize);
        try {
            /*if (bytesWritten == totalSize){
                long currentBytes = PrefUtils.getInstance().getLong(R.string.pref_bytes_count, 0);
                long newBytes = currentBytes+totalSize;
                PrefUtils.getInstance().writeLong(R.string.pref_bytes_count, newBytes);
            }*/
            double progress = (totalSize > 0) ? (bytesWritten * 1.0 / totalSize) * 100 : -1;
            mCallback.onProgress((int) progress);
        } catch (Exception e) {
            log(e);
        }
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        super.onSuccess(statusCode, headers, response);
        mCallback.onSuccess(response);
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        super.onSuccess(statusCode, headers, response);
        mCallback.onSuccess(response);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        mCallback.onFail(statusCode, errorResponse);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        mCallback.onFail(statusCode, errorResponse);
    }

    public static abstract class Callback {

        private boolean showDialog;
        private BaseAppActivity baseAppActivity;

        public Callback(BaseAppActivity baseAppActivity) {
            this.baseAppActivity = baseAppActivity;
            this.showDialog = true;
        }

        public Callback(BaseAppActivity baseAppActivity, boolean showDialog) {
            this.baseAppActivity = baseAppActivity;
            this.showDialog = showDialog;
        }

        protected void onRetry() {
        }

        protected void onResponse(JSONObject data, JSONObject meta) {
        }

        protected void onResponse(JSONArray data, JSONObject meta) {
        }

        protected void onError(String errorCode, String message, JSONObject data) {
            switch (errorCode) {
                case CommonErrorCodes.VALIDATION_ERROR:
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
        }

        protected void onConnectionStarted() {
            Log.e(TAG, "onConnectionStarted");
            if (showDialog) {
                baseAppActivity.showProgressDialog();
            }
        }

        protected void onCantConnect() {
            Log.e(TAG, "onCantConnect");
            if (showDialog) {
                baseAppActivity.hideProgressDialog();
            }
            new AlertDialog.Builder(baseAppActivity)
                    .setTitle(R.string.connection_timed_out)
                    .setMessage(R.string.error_connection)
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(R.string.retry, (dialog, which) -> onRetry())
                    .create().show();
        }

        protected void onFail(int statusCode, JSONArray response) {
            Log.e(TAG, "Connection failed! " + statusCode + ", " + String.valueOf(response));
            if (showDialog) {
                baseAppActivity.hideProgressDialog();
            }
        }

        protected void onFail(int statusCode, JSONObject response) {
            Log.e(TAG, "Connection failed! " + statusCode + ", " + String.valueOf(response));
            if (showDialog) {
                baseAppActivity.hideProgressDialog();
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(baseAppActivity);
            if ((statusCode == 422 || statusCode == 400) && response != null) {
                try {
                    JSONObject meta = response.getJSONObject(RESPONSE.META);

                    if (response.get(RESPONSE.DATA) instanceof JSONObject) {
                        //Data is Object
                        onError(meta.getString(RESPONSE.ERROR_CODE),
                                meta.getString(RESPONSE.MESSAGE), response.getJSONObject(RESPONSE.DATA));
                    } else {
                        //Data is Array
                        onError(meta.getString(RESPONSE.ERROR_CODE),
                                meta.getString(RESPONSE.MESSAGE), response.getJSONArray(RESPONSE.DATA));
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
            } else {
                onCantConnect();
            }
        }

        protected void onSuccess(JSONObject response) {
            Log.d(TAG, "onSuccess, RESPONSE = " + String.valueOf(response));
            if (showDialog) {
                baseAppActivity.hideProgressDialog();
            }
            try {
                if (response.get(RESPONSE.DATA) instanceof JSONObject) {
                    //Data is Object
                    onResponse(response.getJSONObject(RESPONSE.DATA), response.getJSONObject(RESPONSE.META));
                } else {
                    //Data is Array
                    onResponse(response.getJSONArray(RESPONSE.DATA), response.getJSONObject(RESPONSE.META));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        protected void onSuccess(JSONArray response) {
            Log.d(TAG, "onSuccess, RESPONSE = " + String.valueOf(response));
            if (showDialog) {
                baseAppActivity.hideProgressDialog();
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
    }
}
