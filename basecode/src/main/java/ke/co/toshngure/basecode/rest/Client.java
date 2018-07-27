/*
 * Copyright (c) 2017.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.basecode.rest;

import android.app.Application;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;
import ke.co.toshngure.basecode.rest.annotations.Param;
import ke.co.toshngure.basecode.rest.annotations.Resource;


/**
 * Created by Anthony Ngure on 4/17/2016.
 * Email : anthonyngure25@gmail.com.
 * http://www.toshngure.co.ke
 */
public class Client {

    private static final String TAG = Client.class.getSimpleName();
    private static Client mInstance;
    private static Config mConfig;
    private AsyncHttpClient mClient;
    private SyncHttpClient mSyncHttpClient;


    private Client(Config config) {
        mConfig = config;
    }

    public static void init(Config config) {
        if (mInstance == null) {
            mInstance = new Client(config);
            Logger.init(config.withLoggingEnabled());
        } else {
            throw new IllegalArgumentException("Client should only be initialized once," +
                    " most probably in the Application class");
        }
    }

    public static synchronized Client getInstance() {
        if (mInstance == null) {
            throw new IllegalArgumentException("Client has not been initialized," +
                    " it should be initialized once, most probably in the Application class");
        }
        return mInstance;
    }

    public static Config getConfig() {
        return mConfig;
    }

    public static String absoluteUrl(String relativeUrl) {
        String url = mConfig.getBaseUrl() + relativeUrl;
        if (!TextUtils.isEmpty(mConfig.getToken())) {
            url = url + "?token=" + mConfig.getToken();
        }
        Logger.log("Connecting to " + url);
        return url;
    }


    public AsyncHttpClient getClient() {
        if (mClient == null) {
            /**
             * Client setup
             */
            //HttpsURLConnection.setDefaultSSLSocketFactory(new NoSSLv3Factory());
            mClient = new AsyncHttpClient(true, 80, 443);
            setUpClient(mClient);
        }
        return mClient;
    }

    public SyncHttpClient getSyncHttpClient() {
        if (mSyncHttpClient == null) {
            mSyncHttpClient = new SyncHttpClient(true, 80, 443);
            setUpClient(mSyncHttpClient);
        }
        return mSyncHttpClient;
    }

    private void setUpClient(AsyncHttpClient client) {
        client.setUserAgent(mConfig.getContext().getPackageName());
        client.setEnableRedirects(false, false);
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
     * @param callback
     * @param <M>
     */
    public <M> void index(Callback<M> callback) {
        Resource resource = getResource(callback.getModelClass());
        String url = absoluteUrl(resource.relativeUrl());
        getClient().get(url, callback.getRequestParams(), new ResponseHandler(callback));
    }

    /**
     * Single Item
     *
     * @param id
     * @param callback
     * @param <M>
     */
    public <M> void show(long id, Callback<M> callback) {
        Resource resource = getResource(callback.getModelClass());
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
    public <M> void store(@NonNull M item, @NonNull Callback<M> callback) {
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
    public <M> void store(@NonNull List<M> items, @NonNull Callback<M> callback) {
        if (items.size() >= 1) {
            Resource resource = getResource(items.get(0).getClass());
            String url = absoluteUrl(resource.relativeUrl());
            getClient().post(mConfig.getContext(), url, getBody(items), RequestParams.APPLICATION_JSON,
                    new ResponseHandler(callback));
        } else {
            throw new IllegalArgumentException("The items.size() must be >= 1");
        }
    }

    /**
     * Update single item
     *
     * @param id
     * @param item
     * @param callback
     * @param <M>
     */
    public <M> void update(long id, @NonNull M item, @NonNull Callback<M> callback) {
        Resource resource = getResource(item.getClass());
        String url = absoluteUrl(resource.relativeUrl() + "/" + id);
        getClient().put(mConfig.getContext(), url, getBody(item), RequestParams.APPLICATION_JSON,
                new ResponseHandler(callback));
    }

    /**
     * Delete an item
     *
     * @param id
     * @param callback
     * @param <M>
     */
    public <M> void destroy(long id, Callback<M> callback) {
        Resource resource = getResource(callback.getModelClass());
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
        Logger.log(data);
        jsonEntity = new StringEntity(data, ContentType.APPLICATION_JSON);
        Logger.log(jsonEntity);
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
        Logger.log(data);
        jsonEntity = new StringEntity(data, ContentType.APPLICATION_JSON);
        Logger.log(jsonEntity);
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
            throw new IllegalArgumentException("Used an item without a Resource Annotation");
        }
        return resource;
    }

    public static abstract class Config {

        public Config() {

        }

        protected abstract Application getContext();

        protected abstract String getBaseUrl();

        protected abstract String getToken();

        protected abstract ResponseDefinition getResponseDefinition();

        protected boolean withLoggingEnabled() {
            return true;
        }

        protected int getRequestTimeout() {
            return 10000;
        }

        protected int getResponseTimeout() {
            return 20000;
        }

        protected int getMaxRetries() {
            return 2;
        }

        protected void onReportError(JSONObject response) {

        }
    }
}
