/*
 * Copyright (c) 2017.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.basecode.rest;

import android.app.Application;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.SyncHttpClient;

import ke.co.toshngure.basecode.R;
import ke.co.toshngure.basecode.logging.BeeLog;


/**
 * Created by Anthony Ngure on 4/17/2016.
 * Email : anthonyngure25@gmail.com.
 * http://www.toshngure.co.ke
 */
public class Client {

    private static final String TAG = "Client";

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
        BeeLog.i(TAG, "Connecting to " + url);
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
        client.setEnableRedirects(mConfig.enableRedirects(), mConfig.enableRelativeRedirects());
        client.setLoggingEnabled(BeeLog.DEBUG);
        client.addHeader("Accept-Encoding", "gzip");
        client.addHeader("Accept", "application/json");
        client.addHeader("User-Agent", mConfig.getUserAgent());
        client.setTimeout(mConfig.getRequestTimeout());
        client.setResponseTimeout(mConfig.getResponseTimeout());
        client.setMaxRetriesAndTimeout(mConfig.getMaxRetries(), mConfig.getRequestTimeout());
    }

    public static abstract class Config {

        private static final int DEFAULT_REQUEST_TIMEOUT = 10000;
        private static final int DEFAULT_RESPONSE_TIMEOUT = 30000;

        public Config() {

        }

        protected abstract Application getContext();

        protected abstract String getBaseUrl();

        protected abstract String getToken();

        protected abstract ResponseDefinition getResponseDefinition();

        protected int getRequestTimeout() {
            return DEFAULT_REQUEST_TIMEOUT;
        }

        protected int getResponseTimeout() {
            return DEFAULT_RESPONSE_TIMEOUT;
        }

        protected int getMaxRetries() {
            return 2;
        }

        protected void onError(int statusCode) {
        }

        protected boolean enableRedirects() {
            return false;
        }

        protected boolean enableRelativeRedirects() {
            return true;
        }

        protected String getUserAgent() {
            return getContext().getString(R.string.app_name);
        }
    }
}
