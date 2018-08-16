package ke.co.toshngure.basecode.fragment;

import android.os.Bundle;

import java.util.List;

import ke.co.toshngure.basecode.R;
import ke.co.toshngure.basecode.dataloading.DataLoadingConfig;
import ke.co.toshngure.basecode.dataloading.ModelListFragment;
import ke.co.toshngure.basecode.logging.LogHistoryManager;
import ke.co.toshngure.basecode.logging.LogItem;

public class LogItemsFragment extends ModelListFragment<LogItem> {


    public static LogItemsFragment newInstance() {

        Bundle args = new Bundle();

        LogItemsFragment fragment = new LogItemsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public DataLoadingConfig<LogItem> getDataLoadingConfig() {
        return super.getDataLoadingConfig()
                .withCacheEnabled(R.id.item_type_log_item);
    }

    @Override
    public List<LogItem> onLoadCaches() {
        return LogHistoryManager.getInstance().getLogItems();
    }
}
