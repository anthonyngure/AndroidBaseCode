/*
 * Copyright (c) 2017.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.androidbasecode;

import android.app.Application;

import ke.co.toshngure.logging.BeeLog;


/**
 * Created by Anthony Ngure on 24/11/2017.
 * Email : anthonyngure25@gmail.com.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        BeeLog.init(true, "ToshNgure");
    }
}
