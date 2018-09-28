/*
 * Copyright (c) 2018.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.basecode.database;

import android.content.Context;
import androidx.loader.content.AsyncTaskLoader;

/**
 * Created by Anthony Ngure on 8/26/2016.
 * Email : anthonyngure25@gmail.com.
 * Company : Laysan Incorporation
 */
public abstract class BaseAsyncTaskLoader<T> extends AsyncTaskLoader<T> {

    private static final String TAG = "BaseAsyncTaskLoader";

    private T mData;


    public BaseAsyncTaskLoader(Context context) {
        super(context);
    }

    @Override
    public T loadInBackground() {
        return onLoad();
    }

    public abstract T onLoad();

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
    public void onCanceled(T data) {
        super.onCanceled(data);
        releaseResources(mData);
    }

    @Override
    public void deliverResult(T data) {
        if (isReset()) {
            releaseResources(data);
            return;
        }

        T oldData = mData;
        mData = data;
        if (isStarted()) {
            super.deliverResult(data);
        }

        if ((oldData != null) && (oldData != data)) {
            releaseResources(oldData);
        }
    }

    private void releaseResources(T data) {

    }
}
