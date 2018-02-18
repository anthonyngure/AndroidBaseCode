/*
 * Copyright (c) 2018.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.dataloading;

import com.loopj.android.http.AsyncHttpClient;

/**
 * Created by Anthony Ngure on 15/09/2017.
 * Email : anthonyngure25@gmail.com.
 */

public class DataLoadingConfig {

    private int loaderId = 0;

    /*If should immediately load fresh data after loading cache*/
    private boolean autoRefreshEnabled = true;
    /*If should load data when a user pulls down*/
    private boolean refreshEnabled = false;
    /*If should load data when a user reaches at the bottom*/
    private boolean loadingMoreEnabled = false;
    /*If items are cached*/
    private boolean cacheEnabled = false;
    private int loadMoreThreshold = 0;
    private String url;
    private boolean debugEnabled = false;
    private int perPage = 10;
    private boolean cursorsEnabled = false;
    private AsyncHttpClient asyncHttpClient;

    public DataLoadingConfig() {
    }

    boolean isAutoRefreshEnabled() {
        return autoRefreshEnabled;
    }

    public DataLoadingConfig withAutoRefreshDisabled() {
        this.autoRefreshEnabled = false;
        return this;
    }

    boolean isRefreshEnabled() {
        return refreshEnabled;
    }

    public DataLoadingConfig withRefreshEnabled() {
        this.refreshEnabled = true;
        return this;
    }

    boolean isLoadingMoreEnabled() {
        return loadingMoreEnabled;
    }

    public DataLoadingConfig withLoadingMoreEnabled() {
        this.loadingMoreEnabled = true;
        return this;
    }

    int getLoaderId() {
        return loaderId;
    }

    boolean isCacheEnabled() {
        return cacheEnabled;
    }

    public DataLoadingConfig withCacheEnabled(int loaderId) {
        this.cacheEnabled = true;
        this.loaderId = loaderId;
        return this;
    }

    int getLoadMoreThreshold() {
        return loadMoreThreshold;
    }

    public DataLoadingConfig withLoadMoreThreshold(int loadMoreThreshold) {
        this.loadMoreThreshold = loadMoreThreshold;
        return this;
    }

    String getUrl() {
        return url;
    }

    public DataLoadingConfig withUrl(String url, AsyncHttpClient asyncHttpClient) {
        this.url = url;
        this.asyncHttpClient = asyncHttpClient;
        return this;
    }

    boolean isDebugEnabled() {
        return debugEnabled;
    }

    public DataLoadingConfig withDebugEnabled() {
        this.debugEnabled = true;
        return this;
    }

    int getPerPage() {
        return perPage;
    }

    public DataLoadingConfig withPerPage(int perPage) {
        this.perPage = perPage;
        return this;
    }

    boolean isCursorsEnabled() {
        return cursorsEnabled;
    }

    public DataLoadingConfig withCursorsEnabled() {
        this.cursorsEnabled = true;
        return this;
    }

    public AsyncHttpClient getAsyncHttpClient() {
        return asyncHttpClient;
    }
}
