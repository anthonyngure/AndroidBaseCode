package ke.co.toshngure.basecode.dataloading;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cz.msebera.android.httpclient.Header;
import ke.co.toshngure.basecode.R;

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
    private ImageView errorIV;

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



    void onError(String error) {
        if (mDataLoadingFragmentImpl.mItemAdapter.getAdapterItemCount() == 0) {
            mDataLoadingFragmentImpl.mSwipeRefreshLayout.setVisibility(View.GONE);
            freshLoadContainer.setVisibility(View.VISIBLE);
            errorLL.setVisibility(View.VISIBLE);
            errorLL.setOnClickListener(view1 -> mDataLoadingFragmentImpl.onStart());
            loadingLL.setVisibility(View.GONE);
            errorTV.setText(error);
            errorIV.setVisibility(mDataLoadingFragmentImpl.mDataLoadingConfig.getMessageIconVisibility());
            errorTV.setTextColor(ContextCompat.getColor(errorTV.getContext(),
                    mDataLoadingFragmentImpl.mDataLoadingConfig.getErrorMessageColor()));
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
                mDataLoadingFragmentImpl.connect();
            });
            loadingLL.setVisibility(View.GONE);
            errorIV.setVisibility(mDataLoadingFragmentImpl.mDataLoadingConfig.getMessageIconVisibility());
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
        this.errorIV = view.findViewById(R.id.errorIV);
        this.errorIV.setVisibility(mDataLoadingFragmentImpl.mDataLoadingConfig.getMessageIconVisibility());

    }

}
