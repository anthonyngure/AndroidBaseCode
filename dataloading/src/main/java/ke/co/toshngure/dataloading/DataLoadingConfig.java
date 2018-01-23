/*
 * Copyright (c) 2018.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.dataloading;

/**
 * Created by Anthony Ngure on 15/09/2017.
 * Email : anthonyngure25@gmail.com.
 */

public class DataLoadingConfig {

    private int loaderId = 0;

    /*If should immediately load fresh data after loading cache*/
    private boolean autoRefreshEnabled = true;
    /*If should load data when a user pulls down*/
    private boolean refreshEnabled = true;
    /*If should load data when a user reaches at the bottom*/
    private boolean loadingMoreEnabled = true;
    /*If items are cached*/
    private boolean cacheEnabled = false;
    private int loadMoreThreshold = 0;
    private String url;
    private boolean debugEnabled = false;
    private int perPage = 10;
    private boolean connectionEnabled = true;
    private boolean cursorsEnabled = true;

    public DataLoadingConfig() {
    }

    public DataLoadingConfig disableConnection() {
        this.refreshEnabled = false;
        this.loadingMoreEnabled = false;
        this.autoRefreshEnabled = false;
        this.connectionEnabled = false;
        return this;
    }

    public DataLoadingConfig disableConnection(boolean disableConnection) {
        if (disableConnection) {
            this.refreshEnabled = false;
            this.loadingMoreEnabled = false;
            this.autoRefreshEnabled = false;
            this.connectionEnabled = false;
        }
        return this;
    }

    public boolean isCursorsEnabled() {
        return cursorsEnabled;
    }

    public DataLoadingConfig setCursorsEnabled(boolean cursorsEnabled) {
        this.cursorsEnabled = cursorsEnabled;
        return this;
    }

    int getLoaderId() {
        return loaderId;
    }

    public DataLoadingConfig setLoaderId(int loaderId) {
        this.loaderId = loaderId;
        return this;
    }

    boolean isAutoRefreshEnabled() {
        return autoRefreshEnabled;
    }

    public DataLoadingConfig setAutoRefreshEnabled(boolean autoRefreshEnabled) {
        this.autoRefreshEnabled = autoRefreshEnabled;
        return this;
    }

    public boolean isLoadingMoreEnabled() {
        return loadingMoreEnabled;
    }

    public DataLoadingConfig setLoadingMoreEnabled(boolean loadingMoreEnabled) {
        this.loadingMoreEnabled = loadingMoreEnabled;
        return this;
    }

    int getLoadMoreThreshold() {
        return loadMoreThreshold;
    }

    public DataLoadingConfig setLoadMoreThreshold(int loadMoreThreshold) {
        this.loadMoreThreshold = loadMoreThreshold;
        return this;
    }

    String getUrl() {
        return url;
    }

    public DataLoadingConfig setUrl(String url) {
        this.url = url;
        return this;
    }

    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    public DataLoadingConfig setDebugEnabled(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
        return this;
    }

    public boolean isCacheEnabled() {
        return cacheEnabled;
    }

    public DataLoadingConfig setCacheEnabled(boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
        return this;
    }

    public boolean isRefreshEnabled() {
        return refreshEnabled;
    }

    public DataLoadingConfig setRefreshEnabled(boolean refreshEnabled) {
        this.refreshEnabled = refreshEnabled;
        return this;
    }

    @Override
    public String toString() {
        return "DataLoadingConfig{" +
                "loaderId=" + loaderId +
                ", autoRefreshEnabled=" + autoRefreshEnabled +
                ", refreshEnabled=" + refreshEnabled +
                ", loadingMoreEnabled=" + loadingMoreEnabled +
                ", cacheEnabled=" + cacheEnabled +
                ", loadMoreThreshold=" + loadMoreThreshold +
                ", url='" + url + '\'' +
                ", debugEnabled=" + debugEnabled +
                '}';
    }

    public int getPerPage() {
        return perPage;
    }

    public DataLoadingConfig setPerPage(int perPage) {
        this.perPage = perPage;
        return this;
    }

    public boolean isConnectionEnabled() {
        return connectionEnabled;
    }
}
