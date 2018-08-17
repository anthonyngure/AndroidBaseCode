package ke.co.toshngure.basecode.dataloading;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ke.co.toshngure.basecode.R;
import ke.co.toshngure.basecode.app.BaseAppActivity;
import ke.co.toshngure.basecode.database.BaseAsyncTaskLoader;
import ke.co.toshngure.basecode.rest.Client;
import ke.co.toshngure.basecode.rest.ResponseHandler;

abstract class AbstractModelFragment<M> extends Fragment
        implements LoaderManager.LoaderCallbacks<List<M>>,
        SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "ModelFragment";

    private DataLoadingConfig<M> mDataLoadingConfig;

    /**
     * Loading View
     */
    private LinearLayout loadingLL;

    /**
     * Error View
     */
    private LinearLayout errorLL;
    private TextView errorTV;
    private FrameLayout freshLoadContainer;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    protected FrameLayout mBottomViewContainer;
    protected FrameLayout mTopViewContainer;
    private M mData = null;
    private List<M> mDataList = null;
    private View errorIV;
    protected FloatingActionButton mFloatingActionButton;


    protected DataLoadingConfig<M> getDataLoadingConfig() {
        return new DataLoadingConfig<>();
    }

    protected RequestParams getRequestParams() {
        return new RequestParams();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataLoadingConfig = getDataLoadingConfig();
    }

    protected List<M> onLoadCache() {
        return new ArrayList<>();
    }

    protected void onDataReady(M data) {
        this.mData = data;
        onDataReady(data != null);
    }

    protected void onDataReady(List<M> data) {
        this.mDataList = data;
        onDataReady(data.size() > 0);
    }

    private void onDataReady(boolean dataIsAvailable){
        mSwipeRefreshLayout.setRefreshing(false);
        if (!dataIsAvailable){
            mSwipeRefreshLayout.setVisibility(View.GONE);
            freshLoadContainer.setVisibility(View.VISIBLE);
            errorLL.setVisibility(View.VISIBLE);
            errorLL.setOnClickListener(view1 -> refresh());
            loadingLL.setVisibility(View.GONE);
            errorIV.setVisibility(mDataLoadingConfig.getMessageIconVisibility());
            errorTV.setText(mDataLoadingConfig.getEmptyDataMessage());
            errorTV.setTextColor(ContextCompat.getColor(errorTV.getContext(),
                    mDataLoadingConfig.getEmptyDataMessageColor()));
        } else {
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            freshLoadContainer.setVisibility(View.GONE);

        }
    }

    /**
     * Restarts the loader if cache is enabled
     * Connects if cache is not enabled
     */
    protected void refresh() {
        //Load cache data
        if (mDataLoadingConfig.isCacheEnabled()) {
            mSwipeRefreshLayout.setVisibility(View.GONE);
            freshLoadContainer.setVisibility(View.VISIBLE);
            errorLL.setVisibility(View.GONE);
            loadingLL.setVisibility(View.VISIBLE);
            Objects.requireNonNull(getActivity()).getSupportLoaderManager()
                    .restartLoader(mDataLoadingConfig.getLoaderId(),
                            null, this);
        } else if (mDataLoadingConfig.isAutoRefreshEnabled()) {
            connect();
        }
    }

    protected void refresh(DataLoadingConfig<M> dataLoadingConfig) {
        this.mDataLoadingConfig = dataLoadingConfig;
        refresh();
    }


    @Override
    public void onStart() {
        super.onStart();
        if (mData == null || mDataList == null){
            if (mDataLoadingConfig.isCacheEnabled()) {
                mSwipeRefreshLayout.setVisibility(View.GONE);
                freshLoadContainer.setVisibility(View.VISIBLE);
                errorLL.setVisibility(View.GONE);
                loadingLL.setVisibility(View.VISIBLE);
                Objects.requireNonNull(getActivity()).getSupportLoaderManager()
                        .initLoader(mDataLoadingConfig.getLoaderId(),
                                null, this);
            } else if (mDataLoadingConfig.isAutoRefreshEnabled()) {
                connect();
            }
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        if (mDataLoadingConfig.isTopViewCollapsible()) {
            view = inflater.inflate(R.layout.fragment_item_collapsible, container, false);
        } else {
            view = inflater.inflate(R.layout.fragment_item_not_collapsible, container, false);
        }

        //Configure fab
        FloatingActionButton fab = view.findViewById(R.id.fab);
        Utils.configureFab(fab, mDataLoadingConfig);
        fab.setOnClickListener(v -> onFabClicked());
        onSetUpFab(fab);

        //Configure Swipe Refresh Layout
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        Utils.configureSwipeRefreshLayout(mSwipeRefreshLayout, mDataLoadingConfig);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        onSetUpSwipeRefreshLayout(mSwipeRefreshLayout);


        //Configure mContentView
        FrameLayout contentView = view.findViewById(R.id.contentView);
        onSetUpContentView(contentView);

        setUpTopView(view.findViewById(R.id.topViewContainer));
        setUpBottomView(view.findViewById(R.id.bottomViewContainer));
        setUpBackground(view.findViewById(R.id.backgroundIV));


        this.freshLoadContainer = view.findViewById(R.id.freshLoadContainer);
        this.freshLoadContainer.setVisibility(View.GONE);

        this.loadingLL = view.findViewById(R.id.loadingLL);
        TextView loadingTV = view.findViewById(R.id.loadingTV);
        loadingTV.setText(mDataLoadingConfig.getLoadingMessage());
        loadingTV.setTextColor(ContextCompat.getColor(loadingTV.getContext(),
                mDataLoadingConfig.getLoadingMessageColor()));

        this.errorLL = view.findViewById(R.id.errorLL);
        this.errorTV = view.findViewById(R.id.errorTV);
        this.errorIV = view.findViewById(R.id.errorIV);

        return view;
    }

    protected void onSetUpFab(FloatingActionButton fab) {
        this.mFloatingActionButton = fab;
    }

    protected void onFabClicked() {

    }

    protected void setUpBackground(ImageView backgroundIV) {

    }

    protected void setUpBottomView(FrameLayout bottomViewContainer) {
        this.mBottomViewContainer = bottomViewContainer;
    }

    protected void setUpTopView(FrameLayout topViewContainer) {
        this.mTopViewContainer = topViewContainer;

    }

    protected void onSetUpContentView(FrameLayout contentView) {

    }

    protected void onSetUpSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
    }

    private void connect() {
        log("connect");
        RequestParams requestParams = getRequestParams();
        log("Params : " + requestParams.toString());
        log("Endpoint : " + mDataLoadingConfig.getUrl());
        String url = mDataLoadingConfig.getUrl();
        log("Url : " + url);
        Client.getInstance().getClient().get(getActivity(), url, requestParams, new ModelResponseHandler());
    }


    private class ModelResponseHandler extends ResponseHandler<M> {

        ModelResponseHandler() {
            super((BaseAppActivity) getActivity(), mDataLoadingConfig.getModelClass(), false);
        }

        @Override
        public void onStart() {
            super.onStart();
            if (mData == null && mDataList == null) {
                mSwipeRefreshLayout.setVisibility(View.GONE);
                freshLoadContainer.setVisibility(View.VISIBLE);
                errorLL.setVisibility(View.GONE);
                loadingLL.setVisibility(View.VISIBLE);
            } else {
                //This is when AutoRefresh is enabled and there is cache
                mSwipeRefreshLayout.setRefreshing(true);
            }
        }

        @Override
        protected void onResponse(M item, @Nullable String message) {
            super.onResponse(item, message);
            onDataReady(item);
        }

        @Override
        protected void onResponse(List<M> items, @Nullable String message) {
            super.onResponse(items, message);
            onDataReady(items);
        }

        @Override
        protected void onError(@Nullable String message) {
            super.onError(message);
            if (mData == null && mDataList == null) {
                mSwipeRefreshLayout.setVisibility(View.GONE);
                freshLoadContainer.setVisibility(View.VISIBLE);
                errorLL.setVisibility(View.VISIBLE);
                errorLL.setOnClickListener(view1 -> connect());
                loadingLL.setVisibility(View.GONE);
                errorTV.setText(message);
                errorTV.setTextColor(ContextCompat.getColor(errorTV.getContext(),
                        mDataLoadingConfig.getErrorMessageColor()));
            } else if (mSwipeRefreshLayout.isRefreshing()) {
                Snackbar.make(mSwipeRefreshLayout, R.string.unable_to_refresh, Snackbar.LENGTH_LONG);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }
    }


    private void log(Object msg) {
        if (mDataLoadingConfig != null) {
            Log.d(TAG, String.valueOf(msg));
        }
    }

    private static class CacheLoader<M> extends BaseAsyncTaskLoader<List<M>> {

        private final AbstractModelFragment<M> modelFragment;

        CacheLoader(Context context, AbstractModelFragment<M> modelFragment) {
            super(context);
            this.modelFragment = modelFragment;
        }

        @Override
        public List<M> onLoad() {
            return modelFragment.onLoadCache();
        }
    }

    @NonNull
    @Override
    public Loader<List<M>> onCreateLoader(int id, @Nullable Bundle args) {
        return new CacheLoader<>(getActivity(), this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<M>> loader, List<M> data) {
        if (loadsSingleItem() && data.size() > 0){
            onDataReady(data.get(0));
        } else {
            onDataReady(data);
        }

        //Auto refresh if refresh is enabled
        if (mDataLoadingConfig.isAutoRefreshEnabled()) {
            connect();
        }
    }


    @Override
    public void onLoaderReset(@NonNull Loader<List<M>> loader) {

    }

    @Override
    public void onRefresh() {
        if (mDataLoadingConfig.isRefreshEnabled()) {
            mSwipeRefreshLayout.setRefreshing(true);
            connect();
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }



    abstract boolean loadsSingleItem();
}
