/*
 * Copyright (c) 2018.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.dataloading;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.jaychang.srv.SimpleCell;
import com.jaychang.srv.SimpleRecyclerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anthony Ngure on 04/06/2017.
 * Email : anthonyngure25@gmail.com.
 */

/**
 * If the response is a {@link org.json.JSONObject} then the list of items should be in a key named data
 * If the response is a {@link org.json.JSONObject} then the list of items should be in a key named cursors
 *
 * @param <M>
 * @param <C>
 */
public class ModelListFragment<M, C extends SimpleCell<M, ?>> extends Fragment implements
        DataLoadingFragmentImpl.Listener<M, C> {


    private DataLoadingFragmentImpl<M, C> mDataLoadingFragmentImpl;

    public ModelListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataLoadingFragmentImpl = new DataLoadingFragmentImpl<>(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return mDataLoadingFragmentImpl.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDataLoadingFragmentImpl.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mDataLoadingFragmentImpl.onStart();
    }

    @Override
    public DataLoadingConfig getDataLoadingConfig() {
        return new DataLoadingConfig();
    }

    @Override
    public boolean hasCollapsibleTopView() {
        return false;
    }


    @Override
    public void setUpTopView(FrameLayout topViewContainer) {

    }

    @Override
    public void setUpBottomView(FrameLayout bottomViewContainer) {

    }

    @Override
    public List<M> onLoadCaches() {
        return new ArrayList<>();
    }

    @Override
    public String getCursorKeyPrefix() {
        return getModelClass().getSimpleName().toLowerCase() + "_" + addUniqueCacheKey();
    }

    @Override
    public AsyncHttpClient getClient() {
        return null;
    }

    @Override
    public RequestParams getRequestParams() {
        return new RequestParams();
    }

    @Override
    public C onCreateCell(M item) {
        return null;
    }

    @Override
    public void onSaveItem(M item) {

    }

    @Override
    public Class<M> getModelClass() {
        return null;
    }

    @Override
    public SimpleRecyclerView onCreateSimpleRecyclerView() {
        return (SimpleRecyclerView) View.inflate(getContext(),
                R.layout.layout_default_simple_recycler_view, null);
    }

    @Override
    public int getFreshLoadGravity() {
        return Gravity.CENTER;
    }

    @Override
    public SimpleRecyclerView getSimpleRecyclerView() {
        return mDataLoadingFragmentImpl.mSimpleRecyclerView;
    }

    @Override
    public String getLoadingMessage() {
        return getString(R.string.message_loading);
    }

    @Override
    public String getErrorMessage() {
        return getString(R.string.message_connection_error);
    }

    @Override
    public String getEmptyDataMessage() {
        return getString(R.string.message_empty_data);
    }

    @Override
    public String getNoMoreDataMessage() {
        return getString(R.string.message_no_more_data);
    }

    @Override
    public CursorImpl getCursorImpl() {
        return new DefaultCursorImpl();
    }


    protected int addUniqueCacheKey() {
        return 0;
    }


}
