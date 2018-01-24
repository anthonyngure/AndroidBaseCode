/*
 * Copyright (c) 2017.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.androidbasecode.network;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.SyncHttpClient;

import javax.net.ssl.HttpsURLConnection;

import ke.co.toshngure.androidbasecode.BuildConfig;
import ke.co.toshngure.basecode.networking.NoSSLv3Factory;
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


    public static synchronized Client getInstance() {
        if (mInstance == null) {
            mInstance = new Client();
        }
        return mInstance;
    }

    public AsyncHttpClient getClient() {
        if (mClient == null) {
            /**
             * Client setup
             */
            HttpsURLConnection.setDefaultSSLSocketFactory(new NoSSLv3Factory());
            mClient = new AsyncHttpClient(true, 80, 443);
            setUpClient(mClient);
        }
        return mClient;
    }

    private void setUpClient(AsyncHttpClient client) {
        client.setUserAgent(BuildConfig.APPLICATION_ID);
        client.setEnableRedirects(false, true);
        client.setLoggingEnabled(BeeLog.DEBUG);
        client.addHeader("Accept-Encoding", "gzip");
        client.addHeader("Accept", "application/json");
        client.setTimeout(10000);
        client.setResponseTimeout(20000);
        client.setMaxRetriesAndTimeout(1, 10000);
    }


    public void invalidate() {
        mInstance = null;
        mClient = null;
    }
}
