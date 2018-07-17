package ke.co.toshngure.dataloading2;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Anthony Ngure on 25/01/2018.
 * Email : anthonyngure25@gmail.com.
 */
class FreshLoadManager {

    private static final String TAG = "FreshLoadManager";
    /**
     * Loading View
     */
    private LinearLayout loadingLL;
    private TextView loadingTV;

    /**
     * Error View
     */
    private LinearLayout errorLL;
    private TextView errorTV;

    private DataLoadingFragmentImpl mDataLoadingFragmentImpl;
    private FrameLayout freshLoadContainer;

    FreshLoadManager(DataLoadingFragmentImpl dataLoadingFragmentImpl) {
        this.mDataLoadingFragmentImpl = dataLoadingFragmentImpl;
    }

    void onError(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        onError(statusCode, String.valueOf(responseString));
    }

    void onError(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
        String message = loadingTV.getContext().getString(R.string.message_connection_error);
        if (response != null) {
            try {
                if (statusCode == 422 && response.get(DataParserTask.DATA) instanceof JSONObject) {
                    JSONObject data = response.getJSONObject(DataParserTask.DATA);
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
                            message = String.valueOf(response);
                        }
                    }

                    message = sb.toString();

                } else {
                    JSONObject meta = response.getJSONObject(DataParserTask.META);
                    message = meta.getString(DataParserTask.MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        onError(statusCode, String.valueOf(message));
    }

    void onError(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
        onError(statusCode, String.valueOf(errorResponse));
    }

    void onRetry(int retryNo) {
        Log.i(TAG, "onRetry = " + retryNo);
    }

    private void onError(int statusCode, String error) {
        if (mDataLoadingFragmentImpl.mItemAdapter.getAdapterItemCount() == 0) {
            mDataLoadingFragmentImpl.mSwipeRefreshLayout.setVisibility(View.GONE);
            freshLoadContainer.setVisibility(View.VISIBLE);
            errorLL.setVisibility(View.VISIBLE);

            errorLL.setOnClickListener(view1 -> mDataLoadingFragmentImpl.onStart());
            loadingLL.setVisibility(View.GONE);
            errorTV.setText(error);
            errorTV.setTextColor(ContextCompat.getColor(errorTV.getContext(),
                    mDataLoadingFragmentImpl.mDataLoadingConfig.getErrorMessageColor()));
            Log.e(TAG, String.valueOf("StatusCode = " + statusCode + ", ERROR: " + error));
        }
    }

    void onStartLoading() {
        if (mDataLoadingFragmentImpl.mItemAdapter.getAdapterItemCount() == 0) {
            mDataLoadingFragmentImpl.mSwipeRefreshLayout.setVisibility(View.GONE);
            freshLoadContainer.setVisibility(View.VISIBLE);
            errorLL.setVisibility(View.GONE);
            loadingLL.setVisibility(View.VISIBLE);
        }
    }

    void onDataParsed() {
        if (mDataLoadingFragmentImpl.mItemAdapter.getAdapterItemCount() == 0) {
            mDataLoadingFragmentImpl.mSwipeRefreshLayout.setVisibility(View.GONE);
            freshLoadContainer.setVisibility(View.VISIBLE);
            errorLL.setVisibility(View.VISIBLE);
            errorLL.setOnClickListener(view1 -> {
                if (mDataLoadingFragmentImpl.mDataLoadingConfig.getAsyncHttpClient() != null){
                    mDataLoadingFragmentImpl.connect();
                }
            });
            loadingLL.setVisibility(View.GONE);
            errorTV.setText(mDataLoadingFragmentImpl.mDataLoadingConfig.getEmptyDataMessage());
            errorTV.setTextColor(ContextCompat.getColor(errorTV.getContext(),
                    mDataLoadingFragmentImpl.mDataLoadingConfig.getEmptyDataMessageColor()));
        } else {
            mDataLoadingFragmentImpl.mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            freshLoadContainer.setVisibility(View.GONE);
        }
    }


    void onLoadFinished() {
        onDataParsed();
    }

    void onProgress(long bytesWritten, long totalSize) {
        double progress = (totalSize > 0) ? (bytesWritten * 1.0 / totalSize) * 100 : -1;
        Log.d(TAG, "Progress = " + progress);
    }

    public void onCreateView(View view) {

        this.freshLoadContainer = view.findViewById(R.id.freshLoadContainer);
        this.freshLoadContainer.setVisibility(View.GONE);

        this.loadingLL = view.findViewById(R.id.loadingLL);
        this.loadingTV = view.findViewById(R.id.loadingTV);
        this.loadingTV.setText(mDataLoadingFragmentImpl.mDataLoadingConfig.getLoadingMessage());
        this.loadingTV.setTextColor(ContextCompat.getColor(loadingTV.getContext(),
                mDataLoadingFragmentImpl.mDataLoadingConfig.getLoadingMessageColor()));

        this.errorLL = view.findViewById(R.id.errorLL);
        this.errorTV = view.findViewById(R.id.errorTV);

    }

}
