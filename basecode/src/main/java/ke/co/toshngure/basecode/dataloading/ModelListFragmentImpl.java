package ke.co.toshngure.basecode.dataloading;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.loopj.android.http.RequestParams;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter_extensions.scroll.EndlessRecyclerOnScrollListener;

import java.util.List;

import ke.co.toshngure.basecode.R;
import ke.co.toshngure.basecode.app.BaseAppActivity;
import ke.co.toshngure.basecode.database.BaseAsyncTaskLoader;
import ke.co.toshngure.basecode.decoration.HorizontalDividerItemDecoration;
import ke.co.toshngure.basecode.logging.BeeLog;
import ke.co.toshngure.basecode.rest.Client;
import ke.co.toshngure.basecode.rest.ResponseHandler;
import ke.co.toshngure.basecode.util.DrawableUtils;

/**
 * Created by Anthony Ngure on 25/01/2018.
 * Email : anthonyngure25@gmail.com.
 */

class ModelListFragmentImpl<M extends IItem<M, ?>> implements
        LoaderManager.LoaderCallbacks<List<M>>, SwipeRefreshLayout.OnRefreshListener {


    private static final String TAG = "ModelListFragmentImpl";
    private static final String SHARED_PREFS_NAME = "data_loading2_prefs";
    private Listener<M> mListener;
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

    ModelListFragmentImpl(Listener<M> listener) {
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
            view = inflater.inflate(R.layout.fragment_list_collapsible, container, false);
        } else {
            view = inflater.inflate(R.layout.fragment_list_not_collapsible, container, false);
        }

        //Configure fab
        FloatingActionButton fab = view.findViewById(R.id.fab);
        Utils.configureFab(fab, mDataLoadingConfig);
        fab.setOnClickListener(v -> mListener.onFabClicked());
        mListener.onSetUpFab(fab);

        //Configure Swipe Refresh Layout
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        Utils.configureSwipeRefreshLayout(mSwipeRefreshLayout, mDataLoadingConfig);
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
        if (mDataLoadingConfig.isHorizontalDividerEnabled()) {
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
                mFreshLoadManager.onStartLoading();
                mActivity.getSupportLoaderManager().initLoader(mDataLoadingConfig.getLoaderId(), null, this);
            } else if (mDataLoadingConfig.isAutoRefreshEnabled()) {
                mTempModelCursors = new ModelCursor(0, 0);
                connect();
            }
        }

    }


    @NonNull
    @Override
    public Loader<List<M>> onCreateLoader(int id, Bundle args) {
        BeeLog.i(TAG, "onCreateLoader");
        return new CacheLoader<>(mActivity, mListener);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<M>> loader, List<M> data) {
        BeeLog.i(TAG, "onLoadFinished");
        BeeLog.i(TAG, "Data Size = " + data.size());

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
        BeeLog.i(TAG, "onLoaderReset");
    }

    void connect() {
        BeeLog.i(TAG, "connect");
        RequestParams requestParams = mListener.getRequestParams();

        if (mDataLoadingConfig.isRefreshEnabled() || mDataLoadingConfig.isLoadingMoreEnabled()) {
            ModelCursor modelCursor = getModelCursor();
            requestParams.put(mDataLoadingConfig.getCursorImpl().getAfterKey(), modelCursor.getAfter());
            requestParams.put(mDataLoadingConfig.getCursorImpl().getBeforeKey(), modelCursor.getBefore());
            requestParams.put(mDataLoadingConfig.getCursorImpl().getRecentFlagKey(), mSwipeRefreshLayout.isRefreshing());
            requestParams.put(mDataLoadingConfig.getCursorImpl().getPerPageKey(), mDataLoadingConfig.getPerPage());
        }

        BeeLog.i(TAG, "Params : " + requestParams.toString());
        String url = Client.absoluteUrl(mDataLoadingConfig.getRelativeUrl());
        BeeLog.i(TAG, "Url : " + url);

        Client.getInstance().getClient().get(mActivity, url, requestParams, new ModelsResponseHandler());
    }

    private void updateModelCursor(ModelCursor modelCursor) {
        BeeLog.i(TAG, "updateModelCursor");
        if (mDataLoadingConfig.isCacheEnabled()) {
            getSharedPreferences().edit()
                    .putLong(getCursorKeyPrefix() + "_" + mDataLoadingConfig.getCursorImpl().getAfterKey(), modelCursor.getAfter())
                    .putLong(getCursorKeyPrefix() + "_" + mDataLoadingConfig.getCursorImpl().getBeforeKey(), modelCursor.getBefore())
                    .apply();
        } else {
            mTempModelCursors = modelCursor;
        }
    }

    private String getCursorKeyPrefix() {
        return mDataLoadingConfig.getModelClass().getSimpleName().toLowerCase() + "_" + mListener.addUniqueCacheKey();
    }

    private ModelCursor getModelCursor() {
        BeeLog.i(TAG, "getModelCursor");
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
        BeeLog.i(TAG, "resetCursors");
        ModelCursor modelCursor = new ModelCursor(0, 0);
        updateModelCursor(modelCursor);
    }


    @Override
    public void onRefresh() {
        BeeLog.i(TAG, "onRefresh");
        if (!isLoadingMore && mDataLoadingConfig.isRefreshEnabled()) {
            mSwipeRefreshLayout.setRefreshing(true);
            connect();
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * Restarts the loader if cache is enabled
     * Connects if cache is not enabled
     */
    public void refresh() {
        //Load cache data
        mItemAdapter.clear();
        if (mDataLoadingConfig.isCacheEnabled()) {
            mFreshLoadManager.onStartLoading();
            mActivity.getSupportLoaderManager().restartLoader(mDataLoadingConfig.getLoaderId(), null, this);
        } else if (mDataLoadingConfig.isAutoRefreshEnabled()) {
            mTempModelCursors = new ModelCursor(0, 0);
            connect();
        }
    }

    public void refresh(DataLoadingConfig<M> dataLoadingConfig) {
        this.mDataLoadingConfig = dataLoadingConfig;
        refresh();
    }

    interface Listener<M extends IItem<M, ?>> {

        DataLoadingConfig<M> getDataLoadingConfig();

        void onFabClicked();

        void setUpTopView(FrameLayout topViewContainer);

        void setUpBackground(ImageView backgroundIV);

        void setUpBottomView(FrameLayout bottomViewContainer);

        /*Override this method to read cached items*/
        List<M> onLoadCaches();

        RequestParams getRequestParams();

        void onDataReady(List<M> items);

        int addUniqueCacheKey();

        void onSetUpSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout);

        void onSetUpRecyclerView(RecyclerView recyclerView);

        void onSetUpAdapter(ItemAdapter<M> itemAdapter, FastAdapter<M> fastAdapter);

        void refresh();

        void refresh(DataLoadingConfig<M> dataLoadingConfig);

        void onSetUpFab(FloatingActionButton fab);
    }


    private static final class CacheLoader<M extends IItem<M, ?>> extends BaseAsyncTaskLoader<List<M>> {

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
            BeeLog.i(TAG, "onLoadMore, Page = " + currentPage);
            if (hasMoreToBottom
                    && !mSwipeRefreshLayout.isRefreshing()
                    && mItemAdapter.getAdapterItemCount() > 0
                    && mDataLoadingConfig.isLoadingMoreEnabled()) {
                isLoadingMore = true;
                connect();
            }
        }
    }


    private final class ModelsResponseHandler extends ResponseHandler<M> {

        ModelsResponseHandler() {
            super((BaseAppActivity) mActivity, mDataLoadingConfig.getModelClass(), false);
        }

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
        protected void onResponse(List<M> items, @Nullable String message) {
            super.onResponse(items, message);
            BeeLog.i(TAG, "onPostExecute");
            BeeLog.i(TAG, "Data Size : " + items.size());
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
            mListener.onDataReady(items);
        }


        @Override
        protected void onError(@Nullable String message) {
            super.onError(message);
            mFreshLoadManager.onError(message);
            mMoreLoadManager.onError(message);
            if (mSwipeRefreshLayout.isRefreshing()) {
                Snackbar.make(mSwipeRefreshLayout, R.string.unable_to_refresh, Snackbar.LENGTH_LONG);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }

    }


}
