/*
 * Copyright (c) 2018.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.basecode.networking;

import android.content.Context;

import org.json.JSONObject;

/**
 * Created by Anthony Ngure on 6/10/2016.
 * Email : anthonyngure25@gmail.com.
 *
 */
public interface ConnectionListener {

    void connect();

    void onConnectionStarted();

    void onConnectionFailed(int statusCode, JSONObject response);

    void onConnectionSuccess(JSONObject response);

    void onConnectionProgress(int progress);

    Context getListenerContext();

}
