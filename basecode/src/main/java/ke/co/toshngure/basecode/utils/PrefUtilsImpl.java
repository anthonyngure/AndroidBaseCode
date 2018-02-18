/*
 * Copyright (c) 2018.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.basecode.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

/**
 * Created by Anthony Ngure on 16/02/2017.
 * Email : anthonyngure25@gmail.com.
 */

public abstract class PrefUtilsImpl {

    protected abstract SharedPreferences getSharedPreferences();

    protected abstract Context getContext();

    protected abstract void invalidate();

    public void remove(@StringRes int key) {
        getSharedPreferences().edit().remove(resolveKey(key)).apply();
        invalidate();
    }

    public void writeString(@StringRes int key, String value) {
        getSharedPreferences().edit().putString(resolveKey(key), value).apply();
        invalidate();
    }

    public String getString(@StringRes int key) {
        return getSharedPreferences().getString(resolveKey(key), "");
    }

    public String getString(@StringRes int key, String defVal) {
        return getSharedPreferences().getString(resolveKey(key), defVal);
    }

    public int getInt(@StringRes int key) {
        return getSharedPreferences().getInt(resolveKey(key), 0);
    }

    public void writeLong(@StringRes int key, long value) {
        getSharedPreferences().edit().putLong(resolveKey(key), value).apply();
        invalidate();
    }

    public long getLong(@StringRes int key) {
        return getSharedPreferences().getLong(resolveKey(key), 0);
    }

    public long getLong(@StringRes int key, int defVal) {
        return getSharedPreferences().getLong(resolveKey(key), defVal);
    }


    public boolean getBoolean(@StringRes int key, boolean defVal) {
        return getSharedPreferences().getBoolean(resolveKey(key), defVal);
    }

    public void writeInt(@StringRes int key, int val) {
        getSharedPreferences().edit().putInt(resolveKey(key), val).apply();
    }

    public void writeBoolean(@StringRes int key, boolean value) {
        getSharedPreferences().edit().putBoolean(resolveKey(key), value).apply();
        invalidate();
    }

    public boolean getBoolean(@StringRes int key) {
        return getSharedPreferences().getBoolean(resolveKey(key), false);
    }

    public void clear() {
        getSharedPreferences().edit().clear().apply();
        invalidate();
    }

    @NonNull
    private String resolveKey(@StringRes int key) {
        return String.valueOf("key_" + String.valueOf(key).replace("R.string.", ""));
        //return getContext().getString(key).trim().replaceAll(" ","");
    }
}
