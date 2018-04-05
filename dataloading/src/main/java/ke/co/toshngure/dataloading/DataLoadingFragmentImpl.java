package ke.co.toshngure.dataloading;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.jaychang.srv.OnLoadMoreListener;
import com.jaychang.srv.SimpleCell;
import com.jaychang.srv.SimpleRecyclerView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by Anthony Ngure on 25/01/2018.
 * Email : anthonyngure25@gmail.com.
 */

class DataLoadingFragmentImpl<M, C extends SimpleCell<M, ?>> implements
        PtrHandler,
        OnLoadMoreListener,
        LoaderManager.LoaderCallbacks<List<C>> {


    private static final String TAG = "DataLoadingFragmentImpl";
    private static final String SHARED_PREFS_NAME = "data_loading_prefs";
    Listener<M, C> mListener;
    SimpleRecyclerView mSimpleRecyclerView;
    DataLoadingConfig mDataLoadingConfig;
    boolean isLoadingMore = false;
    CursorImpl mCursorImpl;
    private PtrClassicFrameLayout mPtrClassicFrameLayout;
    private FragmentActivity mActivity;
    private boolean mAppBarIsExpanded = true;
    private ModelCursor mTempModelCursors;
    private boolean hasMoreToBottom = true;
    private boolean hasMoreToTop = true;
    private FreshLoadManager mFreshLoadManager;
    private LoadingMoreViewManager mLoadingMoreViewManager;

    DataLoadingFragmentImpl(Listener<M, C> listener) {
        if (listener instanceof Fragment) {
            this.mListener = listener;
            this.mDataLoadingConfig = listener.getDataLoadingConfig();
            this.mActivity = ((Fragment) listener).getActivity();
            this.mCursorImpl = listener.getCursorImpl();
            this.mSimpleRecyclerView = mListener.onCreateSimpleRecyclerView();
            this.mFreshLoadManager = new FreshLoadManager(this);
            this.mLoadingMoreViewManager = new LoadingMoreViewManager(this, mSimpleRecyclerView);
        } else {
            throw new IllegalArgumentException("Listener must be an instance of android.support.v4.app.Fragment");
        }
    }

    private SharedPreferences getSharedPreferences() {
        return mActivity.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        if (mListener.hasCollapsibleTopView()) {
            view = inflater.inflate(R.layout.fragment_model_list_collapsible, container, false);
        } else {
            view = inflater.inflate(R.layout.fragment_model_list_not_collapsible, container, false);
        }

        mPtrClassicFrameLayout = view.findViewById(R.id.ptrClassicFrameLayout);
        FrameLayout simpleRecyclerViewContainer = view.findViewById(R.id.simpleRecyclerViewContainer);
        FrameLayout freshLoadContainer = view.findViewById(R.id.freshLoadContainer);

        mFreshLoadManager.onCreateView(mPtrClassicFrameLayout, freshLoadContainer);


        simpleRecyclerViewContainer.addView(mSimpleRecyclerView);
        mListener.setUpTopView(view.findViewById(R.id.topViewContainer));
        mListener.setUpBottomView(view.findViewById(R.id.bottomViewContainer));
        mListener.setUpBackground(view.findViewById(R.id.backgroundIV));

        if (mListener.hasCollapsibleTopView()) {
            AppBarLayout appBarLayout = view.findViewById(R.id.appBarLayout);
            appBarLayout.addOnOffsetChangedListener((appBarLayout1, verticalOffset)
                    -> mAppBarIsExpanded = (verticalOffset == 0));
        }
        return view;
    }

    void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mPtrClassicFrameLayout.setPtrHandler(this);
        mSimpleRecyclerView.setAutoLoadMoreThreshold(mDataLoadingConfig.getLoadMoreThreshold());
        mSimpleRecyclerView.setOnLoadMoreListener(this);
        mPtrClassicFrameLayout.setHeaderView(View.inflate(mActivity, R.layout.layout_default_refresh_header, null));
    }

    void onStart() {
        if (mSimpleRecyclerView.isEmpty()) {
            //Load cache data
            if (mDataLoadingConfig.isCacheEnabled()) {
                mActivity.getSupportLoaderManager().initLoader(mDataLoadingConfig.getLoaderId(), null, this);
            } else if (mDataLoadingConfig.isAutoRefreshEnabled()) {
                mTempModelCursors = new ModelCursor(0, 0);
                mPtrClassicFrameLayout.autoRefresh();
            }
        }
    }

    private void log(Object msg) {
        if (mDataLoadingConfig != null && mDataLoadingConfig.isDebugEnabled()) {
            Log.d(TAG, String.valueOf(msg));
        }
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        boolean canDoRefresh = (mSimpleRecyclerView.isEmpty() || !mSimpleRecyclerView.canScrollVertically(-1))
                && !isLoadingMore
                && mDataLoadingConfig.isCursorsEnabled()
                && mDataLoadingConfig.isRefreshEnabled()
                && mAppBarIsExpanded;

        log("checkCanDoRefresh = " + canDoRefresh);
        return canDoRefresh;
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        log("onRefreshBegin");
        if (hasMoreToTop) {
            connect();
        } else {
            log("Refresh Halted");
            if (mDataLoadingConfig.isDebugEnabled()) {
                new Handler().postDelayed(() -> mPtrClassicFrameLayout.refreshComplete(), 3000);
            } else {
                mPtrClassicFrameLayout.refreshComplete();
            }
        }
    }

    @Override
    public Loader<List<C>> onCreateLoader(int id, Bundle args) {
        log("onCreateLoader");
        return new CacheLoader<>(mActivity, mListener);
    }

    @Override
    public void onLoadFinished(Loader<List<C>> loader, List<C> data) {
        log("onLoadFinished");
        log("Data Size = " + data.size());
        mSimpleRecyclerView.removeAllCells();
        mSimpleRecyclerView.addCells(data);

        mFreshLoadManager.onLoadFinished();

        //Auto refresh if refresh is enabled
        if (mDataLoadingConfig.isAutoRefreshEnabled()) {
            mPtrClassicFrameLayout.autoRefresh();
        }

    }

    @Override
    public void onLoaderReset(Loader<List<C>> loader) {
        log("onLoaderReset");
    }

    void connect() {
        log("connect");
        RequestParams requestParams = mListener.getRequestParams();

        if (mDataLoadingConfig.isCursorsEnabled()) {
            ModelCursor modelCursor = getModelCursor();
            requestParams.put(mCursorImpl.getAfterKey(), modelCursor.getAfter());
            requestParams.put(mCursorImpl.getBeforeKey(), modelCursor.getBefore());
            requestParams.put(mCursorImpl.getRecentFlagKey(), !isLoadingMore);
            requestParams.put(mCursorImpl.getPerPageKey(), mDataLoadingConfig.getPerPage());
        }

        log("Params : " + requestParams.toString());
        log("Url : " + mDataLoadingConfig.getUrl());

        mDataLoadingConfig.getAsyncHttpClient().get(mActivity, mDataLoadingConfig.getUrl(), requestParams, new ResponseHandler());
    }

    void updateModelCursor(ModelCursor modelCursor) {
        log("updateModelCursor");
        if (mDataLoadingConfig.isCacheEnabled()) {
            getSharedPreferences().edit()
                    .putLong(mListener.getCursorKeyPrefix() + "_" + mCursorImpl.getAfterKey(), modelCursor.getAfter())
                    .putLong(mListener.getCursorKeyPrefix() + "_" + mCursorImpl.getBeforeKey(), modelCursor.getBefore())
                    .apply();
        } else {
            mTempModelCursors = modelCursor;
        }
    }

    private ModelCursor getModelCursor() {
        log("getModelCursor");
        ModelCursor modelCursor = new ModelCursor();
        if (mDataLoadingConfig.isCacheEnabled()) {
            modelCursor.setAfter(getSharedPreferences().getLong(mListener.getCursorKeyPrefix() + "_" + mCursorImpl.getAfterKey(), 0));
            modelCursor.setBefore(getSharedPreferences().getLong(mListener.getCursorKeyPrefix() + "_" + mCursorImpl.getBeforeKey(), 0));
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

    @Override
    public boolean shouldLoadMore() {

        log("LoadMore Halted");
        log("mPtrClassicFrameLayout.isRefreshing() " + mPtrClassicFrameLayout.isRefreshing());
        log("mSimpleRecyclerView.isEmpty() " + mSimpleRecyclerView.isEmpty());
        log("hasMoreToBottom " + hasMoreToBottom);
        log("isLoadingMore " + isLoadingMore);
        log("mAppBarIsExpanded " + mAppBarIsExpanded);
        log("mDataLoadingConfig.isLoadingMoreEnabled() " + mDataLoadingConfig.isLoadingMoreEnabled());

        //Should not load more when empty and when loading fresh and when cursors are disabled
        boolean shouldLoadMore = !mPtrClassicFrameLayout.isRefreshing()
                && !mSimpleRecyclerView.isEmpty()
                && hasMoreToBottom
                && !isLoadingMore
                && mDataLoadingConfig.isCursorsEnabled()
                && mDataLoadingConfig.isLoadingMoreEnabled();
        if (shouldLoadMore) {
            mSimpleRecyclerView.hideEmptyStateView();
        }
        return shouldLoadMore;
    }

    @Override
    public void onLoadMore() {
        isLoadingMore = true;
        connect();
    }

    void onDataParsed(List<C> cells) {
        log("onPostExecute");
        log("Data Size : " + cells.size());
        if (mPtrClassicFrameLayout.isRefreshing() || mPtrClassicFrameLayout.isAutoRefresh()) {
            //Collections.reverse(cells);
            mSimpleRecyclerView.addCells(0, cells);
            mSimpleRecyclerView.smoothScrollToPosition(0);
            mPtrClassicFrameLayout.refreshComplete();
            hasMoreToTop = cells.size() != 0;
        } else {
            mSimpleRecyclerView.addCells(cells);
            isLoadingMore = false;
            hasMoreToBottom = cells.size() != 0;
        }

        //In case of cursor problems
        if (mSimpleRecyclerView.isEmpty()
                && mDataLoadingConfig.isCacheEnabled()
                && mDataLoadingConfig.isCursorsEnabled()) {
            resetCursors();
        }
        mFreshLoadManager.onDataParsed();
        mLoadingMoreViewManager.onDataParsed(cells);
    }

    void refresh() {
        mSimpleRecyclerView.removeAllCells();
        hasMoreToTop = true;
        hasMoreToBottom = true;
        mPtrClassicFrameLayout.autoRefresh();
    }

    interface Listener<M, C> {

        DataLoadingConfig getDataLoadingConfig();

        boolean hasCollapsibleTopView();

        void setUpTopView(FrameLayout topViewContainer);

        void setUpBackground(ImageView background);

        void setUpBottomView(FrameLayout bottomViewContainer);


        /*Override this method to read cached items*/
        List<M> onLoadCaches();

        String getCursorKeyPrefix();

        RequestParams getRequestParams();

        C onCreateCell(M item);

        //Called in the background after the item has be parsed
        void onSaveItem(M item);

        Class<M> getModelClass();

        SimpleRecyclerView onCreateSimpleRecyclerView();

        SimpleRecyclerView getSimpleRecyclerView();

        int getFreshLoadGravity();

        CursorImpl getCursorImpl();

        void refresh();

    }

    private static final class CacheLoader<M, C> extends BaseLoader<C> {

        private Listener<M, C> mListener;

        public CacheLoader(Context context, Listener<M, C> listener) {
            super(context);
            this.mListener = listener;
        }

        @Override
        public List<C> onLoad() {
            List<M> mList = mListener.onLoadCaches();
            List<C> cList = new ArrayList<>();
            for (M item : mList) {
                cList.add(mListener.onCreateCell(item));
            }
            return cList;
        }
    }

    private final class ResponseHandler extends JsonHttpResponseHandler {
        @Override
        public void onStart() {
            super.onStart();
            if (mSimpleRecyclerView.isEmpty()) {
                mFreshLoadManager.onStartLoading();
            } else {
                mLoadingMoreViewManager.onStartLoading();
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
            mLoadingMoreViewManager.onRetry(retryNo);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            mFreshLoadManager.onError(statusCode, headers, responseString, throwable);
            mLoadingMoreViewManager.onError(statusCode, headers, responseString, throwable);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            mFreshLoadManager.onError(statusCode, headers, throwable, errorResponse);
            mLoadingMoreViewManager.onError(statusCode, headers, throwable, errorResponse);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            mFreshLoadManager.onError(statusCode, headers, throwable, errorResponse);
            mLoadingMoreViewManager.onError(statusCode, headers, throwable, errorResponse);
        }

    }


}
