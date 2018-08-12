package ke.co.toshngure.basecode.dataloading;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import ke.co.toshngure.basecode.R;

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
    private ModelListFragmentImpl mModelListFragmentImpl;


    MoreLoadManager(ModelListFragmentImpl modelListFragmentImpl) {
        this.mModelListFragmentImpl = modelListFragmentImpl;
    }

    void onCreateView(View view) {

        moreLoadContainer = view.findViewById(R.id.moreLoadContainer);
        moreLoadContainer.setVisibility(View.GONE);
        loadingPB = view.findViewById(R.id.loadingPB);
        messageTV = view.findViewById(R.id.messageTV);
        messageTV.setOnClickListener(view1 -> {
            mModelListFragmentImpl.isLoadingMore = true;
            loadingPB.setVisibility(View.VISIBLE);
            messageTV.setText(R.string.message_loading);
            mModelListFragmentImpl.connect();
        });
    }

    void onError(Object error) {
        loadingPB.setVisibility(View.GONE);
        messageTV.setText(mModelListFragmentImpl.mDataLoadingConfig.getErrorMessage());
        messageTV.setTextColor(ContextCompat.getColor(messageTV.getContext(),
                mModelListFragmentImpl.mDataLoadingConfig.getErrorMessageColor()));
    }


    void onStartLoading() {
        messageTV.setText(R.string.message_loading);
        loadingPB.setVisibility(View.VISIBLE);
        moreLoadContainer.setVisibility(View.VISIBLE);
    }

    void onDataParsed() {
        moreLoadContainer.setVisibility(View.GONE);
    }
}
