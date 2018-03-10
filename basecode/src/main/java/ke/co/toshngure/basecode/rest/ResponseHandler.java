package ke.co.toshngure.basecode.rest;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import ke.co.toshngure.basecode.utils.BaseUtils;

/**
 * Created by Anthony Ngure on 10/03/2018.
 * Email : anthonyngure25@gmail.com.
 */

public class ResponseHandler extends JsonHttpResponseHandler {

    public static final String TAG = ResponseHandler.class.getSimpleName();

    private Callback mCallback;

    public ResponseHandler(Callback callback) {
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
            Logger.log(e);
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