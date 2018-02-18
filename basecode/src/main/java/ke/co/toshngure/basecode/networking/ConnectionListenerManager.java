package ke.co.toshngure.basecode.networking;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import ke.co.toshngure.basecode.R;


/**
 * Created by Anthony Ngure on 11/02/2018.
 * Email : anthonyngure25@gmail.com.
 */

public class ConnectionListenerManager {

    private static final String TAG = "ConnectionListenerMan";

    public static void onConnectionProgress(int progress) {

    }

    public static void onConnectionFailed(int statusCode, JSONObject response, Listener listener) {
        Log.e(TAG, "Connection failed! " + statusCode + ", " + String.valueOf(response));
        AlertDialog.Builder builder = new AlertDialog.Builder(listener.getConnectionListener().getListenerContext());
        if (statusCode == 422) {
            try {
                if (response != null) {
                    JSONObject meta = response.getJSONObject(Response.META);

                    if (response.get(Response.DATA) instanceof JSONObject) {
                        //Data is Object
                        listener.onErrorResponse(meta.getString(Response.ERROR_CODE),
                                meta.getString(Response.MESSAGE), meta.getJSONObject(Response.DATA));
                    } else {
                        //Data is Array
                        listener.onErrorResponse(meta.getString(Response.ERROR_CODE),
                                meta.getString(Response.MESSAGE), meta.getJSONArray(Response.DATA));
                    }
                } else {
                    onConnectionFailed(0, null, listener);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (statusCode == 500) {
            builder.setCancelable(true)
                    .setTitle(R.string.server_error)
                    .setMessage(listener.getConnectionListener().getListenerContext().getString(R.string.error_application))
                    .setNegativeButton(R.string.report, (dialog, which) -> {
                    })
                    .setPositiveButton(android.R.string.ok, null).create().show();
        } else {
            builder.setTitle(R.string.connection_timed_out)
                    .setMessage(R.string.error_connection)
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(R.string.retry, (dialog, which) -> listener.getConnectionListener().connect()).create().show();
        }
    }

    public static void onErrorResponse(String errorCode, String message, JSONObject data, Listener listener) {
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
                showErrorAlertDialog(sb.toString(), listener.getConnectionListener().getListenerContext());
                break;
            default:
                showErrorAlertDialog(message, listener.getConnectionListener().getListenerContext());
        }
    }

    public static void onErrorResponse(String errorCode, String message, JSONArray data, Listener listener) {
        showErrorAlertDialog(message, listener.getConnectionListener().getListenerContext());
    }

    public static void onConnectionSuccess(JSONObject response, Listener listener) {
        Log.d(TAG, "onConnectionSuccess, Response = " + String.valueOf(response));
        try {
            if (response.get(Response.DATA) instanceof JSONObject) {
                //Data is Object
                listener.onSuccessResponse(response.getJSONObject(Response.DATA),
                        response.getJSONObject(Response.META));
            } else {
                //Data is Array
                listener.onSuccessResponse(response.getJSONArray(Response.DATA),
                        response.getJSONObject(Response.META));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static void showErrorAlertDialog(String message, Context context) {
        new AlertDialog.Builder(context)
                .setCancelable(true)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .create()
                .show();
    }

    public interface Listener {
        ConnectionListener getConnectionListener();

        void onSuccessResponse(JSONObject data, JSONObject meta);

        void onSuccessResponse(JSONArray data, JSONObject meta);

        void onErrorResponse(String errorCode, String message, JSONObject data);

        void onErrorResponse(String errorCode, String message, JSONArray data);
    }


}
