/*
 * Copyright (c) 2018.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.androidbasecode.fragment;

import android.os.Bundle;

import java.util.List;

import ke.co.toshngure.dataloading.DataLoadingConfig;
import ke.co.toshngure.dataloading.ModelListBottomSheetFragment;
import ke.co.toshngure.androidbasecode.cell.LogItemCell;
import ke.co.toshngure.logging.LogHistoryManager;
import ke.co.toshngure.logging.LogItem;

/**
 * Created by Anthony Ngure on 22/01/2018.
 * Email : anthonyngure25@gmail.com.
 */

public class LogItemsFragment extends ModelListBottomSheetFragment<LogItem, LogItemCell> {

    public static LogItemsFragment newInstance() {
        Bundle args = new Bundle();
        LogItemsFragment fragment = new LogItemsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected LogItemCell onCreateCell(LogItem item) {
        return new LogItemCell(item);
    }

    @Override
    protected DataLoadingConfig getDataLoadingConfig() {
        return new DataLoadingConfig().disableConnection()
                .setCacheEnabled(true);
    }

    @Override
    protected Class<LogItem> getModelClass() {
        return LogItem.class;
    }

    @Override
    protected List<LogItem> onLoadCaches() {
        return LogHistoryManager.getInstance().getLogItems();
    }
}
