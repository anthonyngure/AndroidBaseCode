package ke.co.toshngure.dataloading;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;

/**
 * Created by Anthony Ngure on 25/01/2018.
 * Email : anthonyngure25@gmail.com.
 */

public class FreshLoadManager {

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
    private PtrClassicFrameLayout ptrClassicFrameLayout;
    private FrameLayout freshLoadContainer;

    public FreshLoadManager(DataLoadingFragmentImpl dataLoadingFragmentImpl) {
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
        if (mDataLoadingFragmentImpl.mSimpleRecyclerView.isEmpty()) {
            ptrClassicFrameLayout.setVisibility(View.GONE);
            freshLoadContainer.setVisibility(View.VISIBLE);
            errorLL.setVisibility(View.VISIBLE);
            errorLL.setOnClickListener(view1 -> mDataLoadingFragmentImpl.connect());
            loadingLL.setVisibility(View.GONE);
            errorTV.setText(mDataLoadingFragmentImpl.mDataLoadingConfig.getErrorMessage());
            errorTV.setTextColor(mDataLoadingFragmentImpl.mDataLoadingConfig.getErrorMessageColor());
            Log.e(TAG, String.valueOf("StatusCode = " + statusCode + ", ERROR: " + error));
        }
    }

    void onStartLoading() {
        if (mDataLoadingFragmentImpl.mSimpleRecyclerView.isEmpty()) {
            ptrClassicFrameLayout.setVisibility(View.GONE);
            freshLoadContainer.setVisibility(View.VISIBLE);
            errorLL.setVisibility(View.GONE);
            loadingLL.setVisibility(View.VISIBLE);
        }
    }

    void onDataParsed() {
        if (mDataLoadingFragmentImpl.mSimpleRecyclerView.isEmpty()) {
            ptrClassicFrameLayout.setVisibility(View.GONE);
            freshLoadContainer.setVisibility(View.VISIBLE);
            errorLL.setVisibility(View.VISIBLE);
            loadingLL.setVisibility(View.GONE);
            errorTV.setText(mDataLoadingFragmentImpl.mDataLoadingConfig.getEmptyDataMessage());
            errorTV.setTextColor(mDataLoadingFragmentImpl.mDataLoadingConfig.getEmptyDataMessageColor());
        } else {
            ptrClassicFrameLayout.setVisibility(View.VISIBLE);
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

    public void onCreateView(PtrClassicFrameLayout ptrClassicFrameLayout, FrameLayout freshLoadContainer) {

        this.ptrClassicFrameLayout = ptrClassicFrameLayout;
        this.freshLoadContainer = freshLoadContainer;

        View view = LayoutInflater.from(ptrClassicFrameLayout.getContext())
                .inflate(R.layout.layout_fresh_load, freshLoadContainer);

        loadingLL = view.findViewById(R.id.loadingLL);
        loadingLL.setGravity(mDataLoadingFragmentImpl.mListener.getFreshLoadGravity());
        loadingTV = view.findViewById(R.id.loadingTV);
        loadingTV.setText(mDataLoadingFragmentImpl.mDataLoadingConfig.getLoadingMessage());
        loadingTV.setTextColor(mDataLoadingFragmentImpl.mDataLoadingConfig.getLoadingMessageColor());

        errorLL = view.findViewById(R.id.errorLL);
        errorLL.setGravity(mDataLoadingFragmentImpl.mListener.getFreshLoadGravity());
        errorTV = view.findViewById(R.id.errorTV);

    }

}
