package ke.co.toshngure.dataloading2;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

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
        onError(statusCode, responseString);
    }

    void onError(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        onError(statusCode, errorResponse);
    }

    void onError(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
        onError(statusCode, errorResponse);
    }

    void onRetry(int retryNo) {
        Log.i(TAG, "onRetry = " + retryNo);
    }

    private void onError(int statusCode, Object error) {
        if (mDataLoadingFragmentImpl.mFastItemAdapter.getAdapterItemCount() == 0) {
            mDataLoadingFragmentImpl.mSwipeRefreshLayout.setVisibility(View.GONE);
            freshLoadContainer.setVisibility(View.VISIBLE);
            errorLL.setVisibility(View.VISIBLE);
            errorLL.setOnClickListener(view1 -> mDataLoadingFragmentImpl.connect());
            loadingLL.setVisibility(View.GONE);
            errorTV.setText(mDataLoadingFragmentImpl.mDataLoadingConfig.getErrorMessage());
            errorTV.setTextColor(ContextCompat.getColor(errorTV.getContext(),
                    mDataLoadingFragmentImpl.mDataLoadingConfig.getErrorMessageColor()));
            Log.e(TAG, String.valueOf("StatusCode = " + statusCode + ", ERROR: " + error));
        }
    }

    void onStartLoading() {
        if (mDataLoadingFragmentImpl.mFastItemAdapter.getAdapterItemCount() == 0) {
            mDataLoadingFragmentImpl.mSwipeRefreshLayout.setVisibility(View.GONE);
            freshLoadContainer.setVisibility(View.VISIBLE);
            errorLL.setVisibility(View.GONE);
            loadingLL.setVisibility(View.VISIBLE);
        }
    }

    void onDataParsed() {
        if (mDataLoadingFragmentImpl.mFastItemAdapter.getAdapterItemCount() == 0) {
            mDataLoadingFragmentImpl.mSwipeRefreshLayout.setVisibility(View.GONE);
            freshLoadContainer.setVisibility(View.VISIBLE);
            errorLL.setVisibility(View.VISIBLE);
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
        errorLL.setOnClickListener(null);
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
