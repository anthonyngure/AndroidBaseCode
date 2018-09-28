package ke.co.toshngure.basecode.dataloading;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    private ModelListFragmentImpl mModelListFragmentImpl;
    private FrameLayout freshLoadContainer;

    FreshLoadManager(ModelListFragmentImpl modelListFragmentImpl) {
        this.mModelListFragmentImpl = modelListFragmentImpl;
    }



    void onError(String error) {
        if (mModelListFragmentImpl.mItemAdapter.getAdapterItemCount() == 0) {
            mModelListFragmentImpl.mSwipeRefreshLayout.setVisibility(View.GONE);
            freshLoadContainer.setVisibility(View.VISIBLE);
            errorLL.setVisibility(View.VISIBLE);
            errorLL.setOnClickListener(view1 -> mModelListFragmentImpl.onStart());
            loadingLL.setVisibility(View.GONE);
            errorTV.setText(error);
            errorIV.setVisibility(mModelListFragmentImpl.mDataLoadingConfig.getMessageIconVisibility());
            errorTV.setTextColor(ContextCompat.getColor(errorTV.getContext(),
                    mModelListFragmentImpl.mDataLoadingConfig.getErrorMessageColor()));
        }
    }

    void onStartLoading() {
        if (mModelListFragmentImpl.mItemAdapter.getAdapterItemCount() == 0) {
            freshLoadContainer.setVisibility(View.VISIBLE);
            loadingLL.setVisibility(View.VISIBLE);
            mModelListFragmentImpl.mSwipeRefreshLayout.setVisibility(View.GONE);
            errorLL.setVisibility(View.GONE);
        }
    }

    void onDataParsed() {
        if (mModelListFragmentImpl.mItemAdapter.getAdapterItemCount() == 0) {
            mModelListFragmentImpl.mSwipeRefreshLayout.setVisibility(View.GONE);
            freshLoadContainer.setVisibility(View.VISIBLE);
            errorLL.setVisibility(View.VISIBLE);
            errorLL.setOnClickListener(view1 -> mModelListFragmentImpl.refresh());
            loadingLL.setVisibility(View.GONE);
            errorIV.setVisibility(mModelListFragmentImpl.mDataLoadingConfig.getMessageIconVisibility());
            errorTV.setText(mModelListFragmentImpl.mDataLoadingConfig.getEmptyDataMessage());
            errorTV.setTextColor(ContextCompat.getColor(errorTV.getContext(),
                    mModelListFragmentImpl.mDataLoadingConfig.getEmptyDataMessageColor()));
        } else {
            mModelListFragmentImpl.mSwipeRefreshLayout.setVisibility(View.VISIBLE);
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
        this.loadingTV.setText(mModelListFragmentImpl.mDataLoadingConfig.getLoadingMessage());
        this.loadingTV.setTextColor(ContextCompat.getColor(loadingTV.getContext(),
                mModelListFragmentImpl.mDataLoadingConfig.getLoadingMessageColor()));

        this.errorLL = view.findViewById(R.id.errorLL);
        this.errorTV = view.findViewById(R.id.errorTV);
        this.errorIV = view.findViewById(R.id.errorIV);
        this.errorIV.setVisibility(mModelListFragmentImpl.mDataLoadingConfig.getMessageIconVisibility());

    }

}
