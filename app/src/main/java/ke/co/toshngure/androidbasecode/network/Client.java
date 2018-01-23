/*
 * Copyright (c) 2017.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.androidbasecode.network;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.SyncHttpClient;

import ke.co.toshngure.androidbasecode.BuildConfig;
import ke.co.toshngure.logging.BeeLog;


/**
 * Created by Anthony Ngure on 4/17/2016.
 * Email : anthonyngure25@gmail.com.
 * http://www.toshngure.co.ke
 */
public class Client {

    private static final String TAG = Client.class.getSimpleName();
    public static Client mInstance;
    private AsyncHttpClient mClient;
    private SyncHttpClient syncHttpClient;

    private Client() {
        mClient = getClient();
    }

    @Deprecated
    public static synchronized Client getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Client();
        }
        return mInstance;
    }

    public static synchronized Client getInstance() {
        if (mInstance == null) {
            mInstance = new Client();
        }
        return mInstance;
    }

    /*public static String absoluteUrl(String relativeUrl) {
        String url = BackEnd.BASE_URL + relativeUrl;
        Log.i(TAG, "Connecting to " + url);
        return url;
    }*/

    public AsyncHttpClient getClient() {
        if (mClient == null) {
            /**
             * Client setup
             */
            mClient = new AsyncHttpClient();
            setUpClient(mClient);
        }
        return mClient;
    }

    public SyncHttpClient getSyncHttpClient() {
        if (syncHttpClient == null) {
            syncHttpClient = new SyncHttpClient();
            setUpClient(syncHttpClient);
        }
        return syncHttpClient;
    }

    private void setUpClient(AsyncHttpClient client) {
        client.setUserAgent(BuildConfig.APPLICATION_ID);
        client.setEnableRedirects(false, true);
        client.setLoggingEnabled(BeeLog.DEBUG);
        client.addHeader("Accept-Encoding", "gzip");
        client.addHeader("Accept", "application/json");
        /*client.setBasicAuth(
                PrefUtils.getInstance().getString(R.string.pref_email),
                PrefUtils.getInstance().getString(R.string.pref_password)
        );*/

        /*client.setTimeout(30000);
        client.setResponseTimeout(60000);*/

        client.setTimeout(10000);
        client.setResponseTimeout(20000);
        client.setMaxRetriesAndTimeout(1, 10000);
    }


    public void invalidate() {
        mInstance = null;
        mClient = null;
    }
}
