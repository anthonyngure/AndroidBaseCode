package ke.co.toshngure.dataloading;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jaychang.srv.SimpleRecyclerView;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Anthony Ngure on 25/01/2018.
 * Email : anthonyngure25@gmail.com.
 */

public class LoadingMoreViewManager {

    private static final String TAG = "LoadingMoreViewManager";
    /**
     * Loading View
     */
    private AVLoadingIndicatorView loadingIV;
    private TextView messageTV;
    private DataLoadingFragmentImpl mDataLoadingFragmentImpl;


    public LoadingMoreViewManager(DataLoadingFragmentImpl dataLoadingFragmentImpl, SimpleRecyclerView simpleRecyclerView) {
        this.mDataLoadingFragmentImpl = dataLoadingFragmentImpl;
        View view = View.inflate(simpleRecyclerView.getContext(),
                R.layout.layout_loading_more_simple_recycler_view, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        loadingIV = view.findViewById(R.id.loadingIV);
        messageTV = view.findViewById(R.id.messageTV);
        messageTV.setOnClickListener(view1 -> {
            mDataLoadingFragmentImpl.isLoadingMore = true;
            loadingIV.setVisibility(View.VISIBLE);
            messageTV.setVisibility(View.GONE);
            mDataLoadingFragmentImpl.connect();
        });
        simpleRecyclerView.setLoadMoreView(view);
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
        loadingIV.setVisibility(View.GONE);
        messageTV.setText(mDataLoadingFragmentImpl.mListener.getErrorMessage());
        Log.e(TAG, String.valueOf("StatusCode = " + statusCode + ", ERROR: " + error));
    }


    void onStartLoading() {
        messageTV.setVisibility(View.GONE);
        loadingIV.setVisibility(View.VISIBLE);
    }

    void onDataParsed(List<?> cells) {
        if (cells.size() == 0) {
            messageTV.setVisibility(View.VISIBLE);
            loadingIV.setVisibility(View.GONE);
            messageTV.setText(mDataLoadingFragmentImpl.mListener.getNoMoreDataMessage());
        }
    }
}
