/*
 * Copyright (c) 2017. Laysan Incorporation
 * Website http://laysan.co.ke
 * Tel +254723203475/+254706356815
 */

package ke.co.toshngure.androidbasecode.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import ke.co.toshngure.androidbasecode.App;
import ke.co.toshngure.basecode.util.PrefUtilsImpl;


/**
 * Created by Anthony Ngure on 7/1/2016.
 * Email : anthonyngure25@gmail.com.
 */
public class PrefUtils extends PrefUtilsImpl {

    private static final String TAG = PrefUtils.class.getSimpleName();
    private static volatile PrefUtils mInstance;
    private Context mContext;
    private SharedPreferences mSharedPreferences;

    private PrefUtils(Context context) {
        this.mContext = context;
        this.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public static PrefUtils getInstance() {
        PrefUtils localInstance = mInstance;
        if (localInstance == null) {
            synchronized (PrefUtils.class) {
                localInstance = mInstance;
                if (localInstance == null) {
                    mInstance = localInstance = new PrefUtils(App.getInstance());
                }
            }
        }
        return localInstance;
    }


    public void signOut() {
        mSharedPreferences.edit().clear().apply();
        invalidate();
    }


    @Override
    protected SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }

    @Override
    protected Context getContext() {
        return mContext;
    }


    /**
     * Load shared prefs again
     */
    @Override
    protected void invalidate() {
        mInstance = null;
        mSharedPreferences = null;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }
}
