package ke.co.toshngure.dataloading2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager;
import androidx.core.content.ContextCompat;
import androidx.loader.content.Loader;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter_extensions.scroll.EndlessRecyclerOnScrollListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import ke.co.toshngure.dataloading2.decoration.HorizontalDividerItemDecoration;

/**
 * Created by Anthony Ngure on 25/01/2018.
 * Email : anthonyngure25@gmail.com.
 */

class DataLoadingFragmentImpl<M extends IItem<M, ?>> implements
        LoaderManager.LoaderCallbacks<List<M>>, SwipeRefreshLayout.OnRefreshListener {


    private static final String TAG = "DataLoadingFragmentImpl";
    private static final String SHARED_PREFS_NAME = "data_loading2_prefs";
    Listener<M> mListener;
    SwipeRefreshLayout mSwipeRefreshLayout;
    DataLoadingConfig<M> mDataLoadingConfig;
    ItemAdapter<M> mItemAdapter;
    boolean isLoadingMore;
    private RecyclerView mRecyclerView;
    private FragmentActivity mActivity;
    private ModelCursor mTempModelCursors;
    private boolean hasMoreToBottom = true;
    private FreshLoadManager mFreshLoadManager;
    private MoreLoadManager mMoreLoadManager;

    DataLoadingFragmentImpl(Listener<M> listener) {
        if (listener instanceof Fragment) {
            this.mListener = listener;
            this.mDataLoadingConfig = listener.getDataLoadingConfig();
            this.mActivity = ((Fragment) listener).getActivity();
            this.mFreshLoadManager = new FreshLoadManager(this);
            this.mMoreLoadManager = new MoreLoadManager(this);
        } else {
            throw new IllegalArgumentException("Listener must be an instance of android.support.v4.app.Fragment");
        }
    }

    private SharedPreferences getSharedPreferences() {
        return mActivity.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        if (mDataLoadingConfig.isTopViewCollapsible()) {
            view = inflater.inflate(mListener.getCollapsibleLayoutRes(), container, false);
        } else {
            view = inflater.inflate(mListener.getNotCollapsibleLayoutRes(), container, false);
        }


        //Configure Swipe Refresh Layout
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(mActivity, R.color.colorPrimary),
                ContextCompat.getColor(mActivity, R.color.colorAccent),
                ContextCompat.getColor(mActivity, R.color.colorPrimaryDark)
        );
        mSwipeRefreshLayout.setEnabled(mDataLoadingConfig.isRefreshEnabled());
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mListener.onSetUpSwipeRefreshLayout(mSwipeRefreshLayout);

        //Configure adapter
        mItemAdapter = new ItemAdapter<>(); //Will hold the items
        FastAdapter<M> fastAdapter = FastAdapter.with(mItemAdapter);
        mListener.onSetUpAdapter(mItemAdapter, fastAdapter);

        //Configure recyclerView
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.setAdapter(fastAdapter);
        mRecyclerView.addOnScrollListener(new ScrollListener());
        if (mDataLoadingConfig.isHorizontalDividerEnabled()){
            mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mActivity)
                    .showLastDivider()
                    .build());
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        }
        mListener.onSetUpRecyclerView(mRecyclerView);

        mListener.setUpTopView(view.findViewById(R.id.topViewContainer));
        mListener.setUpBottomView(view.findViewById(R.id.bottomViewContainer));
        mListener.setUpBackground(view.findViewById(R.id.backgroundIV));
        mFreshLoadManager.onCreateView(view);
        mMoreLoadManager.onCreateView(view);
        return view;
    }

    void onStart() {
        if (mItemAdapter.getAdapterItemCount() == 0) {
            //Load cache data
            if (mDataLoadingConfig.isCacheEnabled()) {

                mActivity.getSupportLoaderManager().initLoader(mDataLoadingConfig.getLoaderId(), null, this);
            } else if (mDataLoadingConfig.isAutoRefreshEnabled()) {
                mTempModelCursors = new ModelCursor(0, 0);
                connect();
            }
        }

    }


    private void log(Object msg) {
        if (mDataLoadingConfig != null && mDataLoadingConfig.isDebugEnabled()) {
            Log.d(TAG, String.valueOf(msg));
        }
    }

    @NonNull
    @Override
    public Loader<List<M>> onCreateLoader(int id, Bundle args) {
        log("onCreateLoader");
        return new CacheLoader<>(mActivity, mListener);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<M>> loader, List<M> data) {
        log("onLoadFinished");
        log("Data Size = " + data.size());

        mItemAdapter.clear();

        mItemAdapter.add(0, data);


        mFreshLoadManager.onLoadFinished();

        //Auto refresh if refresh is enabled
        if (mDataLoadingConfig.isAutoRefreshEnabled()) {
            connect();
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<M>> loader) {
        log("onLoaderReset");
    }

    void connect() {
        log("connect");
        RequestParams requestParams = mListener.getRequestParams();

        if (mDataLoadingConfig.isRefreshEnabled() || mDataLoadingConfig.isLoadingMoreEnabled()) {
            ModelCursor modelCursor = getModelCursor();
            requestParams.put(mDataLoadingConfig.getCursorImpl().getAfterKey(), modelCursor.getAfter());
            requestParams.put(mDataLoadingConfig.getCursorImpl().getBeforeKey(), modelCursor.getBefore());
            requestParams.put(mDataLoadingConfig.getCursorImpl().getRecentFlagKey(), mSwipeRefreshLayout.isRefreshing());
            requestParams.put(mDataLoadingConfig.getCursorImpl().getPerPageKey(), mDataLoadingConfig.getPerPage());
        }

        log("Params : " + requestParams.toString());
        log("Url : " + mDataLoadingConfig.getUrl());

        mDataLoadingConfig.getAsyncHttpClient().get(mActivity, mDataLoadingConfig.getUrl(), requestParams, new ResponseHandler());
    }

    void updateModelCursor(ModelCursor modelCursor) {
        log("updateModelCursor");
        if (mDataLoadingConfig.isCacheEnabled()) {
            getSharedPreferences().edit()
                    .putLong(getCursorKeyPrefix() + "_" + mDataLoadingConfig.getCursorImpl().getAfterKey(), modelCursor.getAfter())
                    .putLong(getCursorKeyPrefix() + "_" + mDataLoadingConfig.getCursorImpl().getBeforeKey(), modelCursor.getBefore())
                    .apply();
        } else {
            mTempModelCursors = modelCursor;
        }
    }

    public String getCursorKeyPrefix() {
        return mDataLoadingConfig.getModelClass().getSimpleName().toLowerCase() + "_" + mListener.addUniqueCacheKey();
    }

    private ModelCursor getModelCursor() {
        log("getModelCursor");
        ModelCursor modelCursor = new ModelCursor();
        if (mDataLoadingConfig.isCacheEnabled()) {
            modelCursor.setAfter(getSharedPreferences().getLong(getCursorKeyPrefix() + "_" + mDataLoadingConfig.getCursorImpl().getAfterKey(), 0));
            modelCursor.setBefore(getSharedPreferences().getLong(getCursorKeyPrefix() + "_" + mDataLoadingConfig.getCursorImpl().getBeforeKey(), 0));
        } else {
            if (mTempModelCursors == null) {
                mTempModelCursors = new ModelCursor();
            }
            modelCursor = mTempModelCursors;
        }
        return modelCursor;
    }

    private void resetCursors() {
        log("resetCursors");
        ModelCursor modelCursor = new ModelCursor(0, 0);
        updateModelCursor(modelCursor);
    }

    void onDataParsed(List<M> items) {
        log("onPostExecute");
        log("Data Size : " + items.size());
        if (isLoadingMore) {
            mItemAdapter.add(items);
            isLoadingMore = false;
            hasMoreToBottom = items.size() != 0;
        } else {
            mItemAdapter.add(0, items);
            mRecyclerView.smoothScrollToPosition(0);
            mSwipeRefreshLayout.setRefreshing(false);
        }

        //In case of cursor problems
        if (mItemAdapter.getAdapterItemCount() == 0 && mDataLoadingConfig.isCacheEnabled()) {
            resetCursors();
        }

        mFreshLoadManager.onDataParsed();
        mMoreLoadManager.onDataParsed();
    }

    @Override
    public void onRefresh() {
        log("onRefresh");
        if (!isLoadingMore && mDataLoadingConfig.isRefreshEnabled()) {
            mSwipeRefreshLayout.setRefreshing(true);
            connect();
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * Restarts the loader if cache is enable
     * Connects if cache is not enable
     */
    public void refresh() {
        //Load cache data
        mItemAdapter.clear();
        if (mDataLoadingConfig.isCacheEnabled()) {
            mActivity.getSupportLoaderManager().restartLoader(mDataLoadingConfig.getLoaderId(), null, this);
        } else if (mDataLoadingConfig.isAutoRefreshEnabled()) {
            mTempModelCursors = new ModelCursor(0, 0);
            connect();
        }
    }

    interface Listener<M extends IItem<M, ?>> {

        DataLoadingConfig<M> getDataLoadingConfig();

        void setUpTopView(FrameLayout topViewContainer);

        void setUpBackground(ImageView backgroundIV);

        void setUpBottomView(FrameLayout bottomViewContainer);

        /*Override this method to read cached items*/
        List<M> onLoadCaches();

        RequestParams getRequestParams();

        //Called in the background after the item has be parsed
        void onSaveItem(M item);

        int addUniqueCacheKey();

        void onSetUpSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout);

        void onSetUpRecyclerView(RecyclerView recyclerView);

        void onSetUpAdapter(ItemAdapter<M> itemAdapter, FastAdapter<M> fastAdapter);

        void refresh();

        @LayoutRes
        int getCollapsibleLayoutRes();

        @LayoutRes
        int getNotCollapsibleLayoutRes();
    }


    private static final class CacheLoader<M extends IItem<M, ?>> extends DataLoader<List<M>> {

        private Listener<M> mListener;

        private CacheLoader(Context context, Listener<M> listener) {
            super(context);
            this.mListener = listener;
        }


        @Override
        public List<M> onLoad() {
            return mListener.onLoadCaches();
        }
    }

    private class ScrollListener extends EndlessRecyclerOnScrollListener {

        @Override
        public void onLoadMore(int currentPage) {
            log("onLoadMore, Page = " + currentPage);
            if (hasMoreToBottom
                    && !mSwipeRefreshLayout.isRefreshing()
                    && mItemAdapter.getAdapterItemCount() > 0
                    && mDataLoadingConfig.isLoadingMoreEnabled()) {
                isLoadingMore = true;
                connect();
            }
        }
    }

    private final class ResponseHandler extends JsonHttpResponseHandler {
        @Override
        public void onStart() {
            super.onStart();
            if (isLoadingMore) {
                mMoreLoadManager.onStartLoading();

            } else {
                if (mItemAdapter.getAdapterItemCount() == 0) {
                    mFreshLoadManager.onStartLoading();
                } else {
                    //This is when AutoRefresh is enabled and there is cache
                    mSwipeRefreshLayout.setRefreshing(true);
                }
            }
        }

        @Override
        public void onProgress(long bytesWritten, long totalSize) {
            super.onProgress(bytesWritten, totalSize);
            mFreshLoadManager.onProgress(bytesWritten, totalSize);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            new DataParserTask<>(DataLoadingFragmentImpl.this).execute(response);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            super.onSuccess(statusCode, headers, response);
            new DataParserTask<>(DataLoadingFragmentImpl.this).execute(response);
        }

        @Override
        public void onRetry(int retryNo) {
            super.onRetry(retryNo);
            mFreshLoadManager.onRetry(retryNo);
            mMoreLoadManager.onRetry(retryNo);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            mFreshLoadManager.onError(statusCode, headers, responseString, throwable);
            mMoreLoadManager.onError(statusCode, headers, responseString, throwable);
            if (mSwipeRefreshLayout.isRefreshing()) {
                Snackbar.make(mSwipeRefreshLayout, R.string.unable_to_refresh, Snackbar.LENGTH_LONG);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            mFreshLoadManager.onError(statusCode, headers, throwable, errorResponse);
            mMoreLoadManager.onError(statusCode, headers, throwable, errorResponse);
            if (mSwipeRefreshLayout.isRefreshing()) {
                Snackbar.make(mSwipeRefreshLayout, R.string.unable_to_refresh, Snackbar.LENGTH_LONG);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            mFreshLoadManager.onError(statusCode, headers, throwable, errorResponse);
            mMoreLoadManager.onError(statusCode, headers, throwable, errorResponse);
            if (mSwipeRefreshLayout.isRefreshing()) {
                Snackbar.make(mSwipeRefreshLayout, R.string.unable_to_refresh, Snackbar.LENGTH_LONG);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }

    }


}
