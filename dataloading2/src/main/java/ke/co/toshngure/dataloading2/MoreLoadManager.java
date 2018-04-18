package ke.co.toshngure.dataloading2;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Anthony Ngure on 25/01/2018.
 * Email : anthonyngure25@gmail.com.
 */
class MoreLoadManager {

    private static final String TAG = "MoreLoadManager";
    /**
     * Loading View
     */
    private ProgressBar loadingPB;
    private TextView messageTV;
    private View moreLoadContainer;
    private DataLoadingFragmentImpl mDataLoadingFragmentImpl;


    MoreLoadManager(DataLoadingFragmentImpl dataLoadingFragmentImpl) {
        this.mDataLoadingFragmentImpl = dataLoadingFragmentImpl;
    }

    void onCreateView(View view) {

        moreLoadContainer = view.findViewById(R.id.moreLoadContainer);
        moreLoadContainer.setVisibility(View.GONE);
        loadingPB = view.findViewById(R.id.loadingPB);
        messageTV = view.findViewById(R.id.messageTV);
        messageTV.setOnClickListener(view1 -> {
            mDataLoadingFragmentImpl.isLoadingMore = true;
            loadingPB.setVisibility(View.VISIBLE);
            messageTV.setVisibility(View.GONE);
            mDataLoadingFragmentImpl.connect();
        });
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

    }

    private void onError(int statusCode, Object error) {
        messageTV.setVisibility(View.VISIBLE);
        loadingPB.setVisibility(View.GONE);
        messageTV.setText(mDataLoadingFragmentImpl.mDataLoadingConfig.getErrorMessage());
        messageTV.setTextColor(ContextCompat.getColor(messageTV.getContext(),
                mDataLoadingFragmentImpl.mDataLoadingConfig.getErrorMessageColor()));
        Log.e(TAG, String.valueOf("StatusCode = " + statusCode + ", ERROR: " + error));
    }


    void onStartLoading() {
        messageTV.setVisibility(View.GONE);
        loadingPB.setVisibility(View.VISIBLE);
        moreLoadContainer.setVisibility(View.VISIBLE);
    }

    void onDataParsed() {
        moreLoadContainer.setVisibility(View.GONE);
    }
}
