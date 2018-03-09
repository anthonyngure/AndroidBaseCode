/*
 * Copyright (c) 2017.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.basecode.networking;

import android.app.Application;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import ke.co.toshngure.basecode.R;
import ke.co.toshngure.basecode.app.BaseAppActivity;
import ke.co.toshngure.basecode.networking.annotations.Param;
import ke.co.toshngure.basecode.networking.annotations.Resource;
import ke.co.toshngure.basecode.utils.BaseUtils;


/**
 * Created by Anthony Ngure on 4/17/2016.
 * Email : anthonyngure25@gmail.com.
 * http://www.toshngure.co.ke
 */
public class RESTClient {

    private static final String TAG = RESTClient.class.getSimpleName();
    private static RESTClient mInstance;
    private static Config mConfig;
    private AsyncHttpClient mClient;
    private SyncHttpClient mSyncHttpClient;


    private RESTClient(Config config) {
        mConfig = config;
    }

    public static void init(Config config) {
        if (mInstance == null) {
            mInstance = new RESTClient(config);
        } else {
            throw new IllegalArgumentException("RESTClient should only be initialized once," +
                    " most probably in the Application class");
        }
    }

    public static synchronized RESTClient getInstance() {
        if (mInstance == null) {
            throw new IllegalArgumentException("RESTClient has not been initialized," +
                    " it should be initialized once, most probably in the Application class");
        }
        return mInstance;
    }

    public static String absoluteUrl(String relativeUrl) {
        String url = mConfig.getBaseUrl() + relativeUrl;
        if (!TextUtils.isEmpty(mConfig.getToken())) {
            url = url + "?token=" + mConfig.getToken();
        }
        log("Connecting to " + url);
        return url;
    }

    private static void log(Object msg) {
        if (mConfig.withLoggingEnabled()) {
            Log.i(TAG, String.valueOf(msg));
        }
    }

    public AsyncHttpClient getClient() {
        if (mClient == null) {
            /**
             * RESTClient setup
             */
            //HttpsURLConnection.setDefaultSSLSocketFactory(new NoSSLv3Factory());
            mClient = new AsyncHttpClient(true, 80, 443);
            setUpClient(mClient);
        }
        return mClient;
    }

    public SyncHttpClient getSyncHttpClient() {
        if (mSyncHttpClient == null) {
            mSyncHttpClient = new SyncHttpClient();
            setUpClient(mSyncHttpClient);
        }
        return mSyncHttpClient;
    }

    private void setUpClient(AsyncHttpClient client) {
        client.setUserAgent(mConfig.getContext().getPackageName());
        client.setEnableRedirects(false, true);
        client.setLoggingEnabled(mConfig.withLoggingEnabled());
        client.addHeader("Accept-Encoding", "gzip");
        client.addHeader("Accept", "application/json");
        client.setTimeout(mConfig.getRequestTimeout());
        client.setResponseTimeout(mConfig.getResponseTimeout());
        client.setMaxRetriesAndTimeout(mConfig.getMaxRetries(), mConfig.getRequestTimeout());
    }

    /**
     * List Of Items
     *
     * @param mClass
     * @param callback
     * @param <M>
     */
    public <M> void index(Class<M> mClass, Callback<M> callback) {
        Resource resource = getResource(mClass);
        String url = absoluteUrl(resource.relativeUrl());
        getClient().get(url, callback.getRequestParams(), new ResponseHandler(callback));
    }

    /**
     * Single Item
     *
     * @param id
     * @param mClass
     * @param callback
     * @param <M>
     */
    public <M> void show(Class<M> mClass, long id, Callback<M> callback) {
        Resource resource = getResource(mClass);
        String url = absoluteUrl(resource.relativeUrl() + "/" + id);
        getClient().get(url, callback.getRequestParams(), new ResponseHandler(callback));
    }

    /**
     * Store a single item
     *
     * @param item
     * @param callback
     * @param <M>
     */
    public <M> void store(M item, Callback<M> callback) {
        Resource resource = getResource(item.getClass());
        String url = absoluteUrl(resource.relativeUrl());
        getClient().post(mConfig.getContext(), url, getBody(item), RequestParams.APPLICATION_JSON,
                new ResponseHandler(callback));
    }

    /**
     * Store multiple items
     *
     * @param items
     * @param callback
     * @param <M>
     */
    public <M> void store(List<M> items, Callback<M> callback) {
        Resource resource = getResource(items.get(0).getClass());
        String url = absoluteUrl(resource.relativeUrl());
        getClient().post(mConfig.getContext(), url, getBody(items), RequestParams.APPLICATION_JSON,
                new ResponseHandler(callback));
    }

    /**
     * Update single item
     *
     * @param item
     * @param callback
     * @param <M>
     */
    public <M> void update(M item, Callback<M> callback) {
        Resource resource = getResource(item.getClass());
        String url = absoluteUrl(resource.relativeUrl());
        getClient().put(mConfig.getContext(), url, getBody(item), RequestParams.APPLICATION_JSON,
                new ResponseHandler(callback));
    }

    /**
     * Update multiple items
     *
     * @param items
     * @param callback
     * @param <M>
     */
    private <M> void update(List<M> items, Callback<M> callback) {
        Resource resource = getResource(items.get(0).getClass());
        String url = absoluteUrl(resource.relativeUrl());
        getClient().post(mConfig.getContext(), url, getBody(items), RequestParams.APPLICATION_JSON,
                new ResponseHandler(callback));
    }

    /**
     * Delete an item
     *
     * @param mClass
     * @param id
     * @param callback
     * @param <M>
     */
    public <M> void destroy(Class<M> mClass, long id, Callback<M> callback) {
        Resource resource = getResource(mClass);
        String url = absoluteUrl(resource.relativeUrl() + "/" + id);
        getClient().delete(url, callback.getRequestParams(), new ResponseHandler(callback));
    }

    private <M> StringEntity getBody(List<M> items) {
        StringEntity jsonEntity = null;
        Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                return ((fieldAttributes.getAnnotation(Param.class) == null));
            }

            @Override
            public boolean shouldSkipClass(Class<?> aClass) {
                return false;
            }
        }).create();

        String data = gson.toJson(items);
        try {
            jsonEntity = new StringEntity(data);
            log(jsonEntity);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return jsonEntity;
    }

    private <M> StringEntity getBody(M item) {
        StringEntity jsonEntity = null;
        Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                return ((fieldAttributes.getAnnotation(Param.class) == null));
            }

            @Override
            public boolean shouldSkipClass(Class<?> aClass) {
                return false;
            }
        }).create();

        String data = gson.toJson(item);
        try {
            jsonEntity = new StringEntity(data);
            log(jsonEntity);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return jsonEntity;
    }

    private <M> Resource getResource(Class<M> item) {
        Resource resource = null;
        try {
            resource = item.getAnnotation(Resource.class);
            if (TextUtils.isEmpty(resource.relativeUrl())) {
                throw new IllegalArgumentException("Used an item without specifying a resource relative Url");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return resource;
    }

    public void invalidate() {
        mInstance = null;
        mClient = null;
    }

    public static abstract class Config {

        public Config() {
        }

        protected abstract Application getContext();

        protected abstract String getBaseUrl();

        protected abstract String getToken();

        protected abstract boolean withLoggingEnabled();

        protected int getRequestTimeout() {
            return 10000;
        }

        protected int getResponseTimeout() {
            return 20000;
        }

        protected int getMaxRetries() {
            return 2;
        }


    }

    public static abstract class Callback<M> {

        BaseAppActivity baseAppActivity;
        private boolean showDialog;
        private Class<M> mClass;
        private boolean hasMetaAndData;

        public Callback(BaseAppActivity baseAppActivity, Class<M> mClass) {
            this.baseAppActivity = baseAppActivity;
            this.showDialog = true;
            this.hasMetaAndData = true;
            this.mClass = mClass;
        }

        public Callback(BaseAppActivity baseAppActivity, Class<M> mClass, boolean hasMetaAndData) {
            this.baseAppActivity = baseAppActivity;
            this.showDialog = true;
            this.hasMetaAndData = hasMetaAndData;
            this.mClass = mClass;
        }

        public Callback(BaseAppActivity baseAppActivity, Class<M> mClass, boolean hasMetaAndData, boolean showDialog) {
            this.baseAppActivity = baseAppActivity;
            this.hasMetaAndData = hasMetaAndData;
            this.showDialog = showDialog;
            this.mClass = mClass;
        }

        protected void onRetry() {
        }

        protected void onResponse(M item, @Nullable JSONObject meta) {
        }

        protected void onResponse(List<M> items, @Nullable JSONObject meta) {
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
            showErrorAlertDialog(message);
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
                        onError(meta.getString(RESPONSE.ERROR_CODE), meta.getString(RESPONSE.MESSAGE),
                                response.getJSONObject(RESPONSE.DATA));
                    } else {
                        //Data is Array
                        onError(meta.getString(RESPONSE.ERROR_CODE), meta.getString(RESPONSE.MESSAGE),
                                response.getJSONArray(RESPONSE.DATA));
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
                if (hasMetaAndData) {
                    if (response.get(RESPONSE.DATA) instanceof JSONObject) {
                        //Data is Object
                        M item = BaseUtils.getSafeGson().fromJson(response.getJSONObject(RESPONSE.DATA).toString(), mClass);
                        onResponse(item, response.getJSONObject(RESPONSE.META));
                    } else {
                        //Data is Array
                        JSONArray data = response.getJSONArray(RESPONSE.DATA);
                        List<M> mList = new ArrayList<>();
                        for (int i = 0; i < data.length(); i++) {
                            M item = BaseUtils.getSafeGson().fromJson(data.getJSONObject(i).toString(), mClass);
                            mList.add(item);
                        }
                        onResponse(mList, response.getJSONObject(RESPONSE.META));
                    }
                } else {
                    //Data is Object
                    M item = BaseUtils.getSafeGson().fromJson(response.getJSONObject(RESPONSE.DATA).toString(), mClass);
                    onResponse(item, null);
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
            //Data is Array
            try {
                List<M> mList = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    M item = BaseUtils.getSafeGson().fromJson(response.getJSONObject(i).toString(), mClass);
                    mList.add(item);
                }
                onResponse(mList, null);
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

    private static class ResponseHandler extends JsonHttpResponseHandler {

        public static final String TAG = ResponseHandler.class.getSimpleName();

        private Callback mCallback;

        private ResponseHandler(Callback callback) {
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

    }
}
