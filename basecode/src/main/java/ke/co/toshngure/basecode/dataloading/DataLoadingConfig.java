/*
 * Copyright (c) 2018.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.basecode.dataloading;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import ke.co.toshngure.basecode.R;

/**
 * Created by Anthony Ngure on 15/09/2017.
 * Email : anthonyngure25@gmail.com.
 */

public class DataLoadingConfig<M> {

    private int loaderId = 0;

    /*If should immediately load fresh data after loading cache*/
    private boolean autoRefreshEnabled = false;
    /*If should load data when a user pulls down*/
    private boolean refreshEnabled = false;
    /*If should load data when a user reaches at the bottom*/
    private boolean loadingMoreEnabled = false;
    /*If items are cached*/
    private boolean cacheEnabled = false;
    private int loadMoreThreshold = 0;
    private String relativeUrl;
    private int perPage = 10;
    private CursorImpl cursorImpl;
    private Class<M> modelClass;
    private boolean topViewCollapsible = false;
    private boolean horizontalDividerEnabled = true;

    @StringRes
    private int loadingMessage = R.string.message_loading;
    @ColorRes
    private int loadingMessageColor = android.R.color.black;
    @StringRes
    private int errorMessage = R.string.message_connection_error;
    @ColorRes
    private int errorMessageColor = android.R.color.black;
    @StringRes
    private int emptyDataMessage = R.string.message_empty_data;
    @ColorRes
    private int emptyDataMessageColor = android.R.color.black;
    @StringRes
    private int noMoreDataMessage = R.string.message_no_more_data;
    @ColorRes
    private int noMoreDataMessageColor = android.R.color.black;

    private int messageIconVisibility = View.VISIBLE;
    @FloatingActionButton.Size
    private int fabSize = FloatingActionButton.SIZE_NORMAL;
    private boolean fabShown = false;
    @DrawableRes
    private int fabIcon = R.drawable.ic_add_black_24dp;

    public DataLoadingConfig() {
    }


    boolean isFabShown() {
        return fabShown;
    }

    @FloatingActionButton.Size
    int getFabSize() {
        return fabSize;
    }

    @DrawableRes
    int getFabIcon() {
        return fabIcon;
    }

    boolean isAutoRefreshEnabled() {
        return autoRefreshEnabled;
    }


    boolean isRefreshEnabled() {
        return refreshEnabled;
    }

    boolean isLoadingMoreEnabled() {
        return loadingMoreEnabled;
    }

    int getLoaderId() {
        return loaderId;
    }

    boolean isCacheEnabled() {
        return cacheEnabled;
    }

    public DataLoadingConfig<M> withFab(@DrawableRes int fabIcon, @FloatingActionButton.Size int fabSize) {
        this.fabSize = fabSize;
        this.fabIcon = fabIcon;
        this.fabShown = true;
        return this;
    }

    public DataLoadingConfig<M> withCacheEnabled(int loaderId) {
        this.cacheEnabled = true;
        this.loaderId = loaderId;
        return this;
    }


    int getLoadMoreThreshold() {
        return loadMoreThreshold;
    }

    public DataLoadingConfig<M> withLoadMoreThreshold(int loadMoreThreshold) {
        this.loadMoreThreshold = loadMoreThreshold;
        return this;
    }

    String getRelativeUrl() {
        return relativeUrl;
    }

    public DataLoadingConfig<M> withRelativeUrl(String relativeUrl, Class<M> modelClass, boolean autoRefreshEnabled) {
        this.relativeUrl = relativeUrl;
        this.modelClass = modelClass;
        this.autoRefreshEnabled = autoRefreshEnabled;
        return this;
    }


    int getPerPage() {
        return perPage;
    }

    public DataLoadingConfig<M> withPerPage(int perPage) {
        this.perPage = perPage;
        return this;
    }

    public DataLoadingConfig<M> withCursors(CursorImpl cursorImpl, boolean refreshEnabled, boolean loadingMoreEnabled) {
        this.refreshEnabled = refreshEnabled;
        this.loadingMoreEnabled = loadingMoreEnabled;
        this.cursorImpl = cursorImpl;
        return this;
    }

    int getLoadingMessage() {
        return loadingMessage;
    }

    public DataLoadingConfig<M> withLoadingMessage(@StringRes int loadingMessage, @ColorRes int loadingMessageColor) {
        this.loadingMessage = loadingMessage;
        this.loadingMessageColor = loadingMessageColor;
        return this;
    }

    public DataLoadingConfig<M> withLoadingMessage(@StringRes int loadingMessage) {
        this.loadingMessage = loadingMessage;
        return this;
    }

    int getErrorMessage() {
        return errorMessage;
    }

    public DataLoadingConfig<M> withErrorMessage(@StringRes int errorMessage, @ColorRes int errorMessageColor) {
        this.errorMessage = errorMessage;
        this.errorMessageColor = errorMessageColor;
        return this;
    }

    public DataLoadingConfig<M> withErrorMessage(@StringRes int errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    int getEmptyDataMessage() {
        return emptyDataMessage;
    }

    public DataLoadingConfig<M> withEmptyDataMessage(@StringRes int emptyDataMessage, @ColorRes int emptyDataMessageColor) {
        this.emptyDataMessage = emptyDataMessage;
        this.emptyDataMessageColor = emptyDataMessageColor;
        return this;
    }

    public DataLoadingConfig<M> withEmptyDataMessage(@StringRes int emptyDataMessage) {
        this.emptyDataMessage = emptyDataMessage;
        return this;
    }

    int getNoMoreDataMessage() {
        return noMoreDataMessage;
    }

    public DataLoadingConfig<M> withNoMoreDataMessage(@StringRes int noMoreDataMessage, @ColorRes int noMoreDataMessageColor) {
        this.noMoreDataMessage = noMoreDataMessage;
        this.noMoreDataMessageColor = noMoreDataMessageColor;
        return this;
    }

    public DataLoadingConfig<M> withNoMoreDataMessage(@StringRes int noMoreDataMessage) {
        this.noMoreDataMessage = noMoreDataMessage;
        return this;
    }

    int getLoadingMessageColor() {
        return loadingMessageColor;
    }

    int getErrorMessageColor() {
        return errorMessageColor;
    }

    int getEmptyDataMessageColor() {
        return emptyDataMessageColor;
    }

    int getNoMoreDataMessageColor() {
        return noMoreDataMessageColor;
    }

    public CursorImpl getCursorImpl() {
        return cursorImpl;
    }

    public Class<M> getModelClass() {
        return modelClass;
    }

    public boolean isTopViewCollapsible() {
        return topViewCollapsible;
    }

    public DataLoadingConfig<M> withTopViewCollapsible() {
        this.topViewCollapsible = true;
        return this;
    }

    public DataLoadingConfig<M> withMessageIconHidden() {
        this.messageIconVisibility = View.GONE;
        return this;
    }

    public int getMessageIconVisibility() {
        return messageIconVisibility;
    }

    public DataLoadingConfig<M> withRefreshEnabled() {
        this.refreshEnabled = true;
        return this;
    }

    public DataLoadingConfig<M> withHorizontalDividerDisabled() {
        this.horizontalDividerEnabled = false;
        return this;
    }

    public boolean isHorizontalDividerEnabled() {
        return horizontalDividerEnabled;
    }
}
