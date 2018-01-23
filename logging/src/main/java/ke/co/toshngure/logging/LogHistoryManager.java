/*
 * Copyright (c) 2018.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.logging;


import java.util.ArrayList;
import java.util.List;


/**
 * Created by Anthony Ngure on 9/11/2016.
 * Email : anthonyngure25@gmail.com.
 * Company : Laysan Incorporation
 */
public class LogHistoryManager {

    private static LogHistoryManager mInstance;
    private List<LogItem> logItems = new ArrayList<>();

    public static LogHistoryManager getInstance() {
        LogHistoryManager localInstance = mInstance;
        if (localInstance == null) {
            synchronized (LogHistoryManager.class) {
                localInstance = mInstance;
                if (localInstance == null) {
                    mInstance = localInstance = new LogHistoryManager();
                }
            }
        }
        return localInstance;
    }

    void add(LogItem logItem) {
        logItems.add(logItem);
    }

    public List<LogItem> getLogItems() {
        return logItems;
    }
}
