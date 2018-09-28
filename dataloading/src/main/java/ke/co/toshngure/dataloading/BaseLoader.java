/*
 * Copyright (c) 2018.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.dataloading;

import android.content.Context;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

/**
 * Created by Anthony Ngure on 8/26/2016.
 * Email : anthonyngure25@gmail.com.
 * Company : Laysan Incorporation
 */
abstract class BaseLoader<T> extends AsyncTaskLoader<List<T>> {

    private static final String TAG = BaseLoader.class.getSimpleName();

    private List<T> mData;


    BaseLoader(Context context) {
        super(context);
    }

    @Override
    public List<T> loadInBackground() {
        return onLoad();
    }

    public abstract List<T> onLoad();

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            deliverResult(mData);
        }

        if (takeContentChanged() || mData == null) {
            forceLoad();
        }
        //super.onStartLoading();
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        cancelLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();

        if (mData != null) {
            releaseResources(mData);
        }
    }

    @Override
    public void onCanceled(List<T> data) {
        super.onCanceled(data);
        releaseResources(mData);
    }

    @Override
    public void deliverResult(List<T> data) {
        if (isReset()) {
            releaseResources(data);
            return;
        }

        List<T> oldData = mData;
        mData = data;
        if (isStarted()) {
            super.deliverResult(data);
        }

        if ((oldData != null) && (oldData != data)) {
            releaseResources(oldData);
        }
    }

    private void releaseResources(List<T> data) {

    }
}
