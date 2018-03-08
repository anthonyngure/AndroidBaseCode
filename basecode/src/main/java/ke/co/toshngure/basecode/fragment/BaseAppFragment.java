/*
 * Copyright (c) 2018.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.basecode.fragment;

import android.graphics.Color;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;

import ke.co.toshngure.basecode.app.BaseAppActivity;
import ke.co.toshngure.basecode.utils.BaseUtils;

/**
 * Created by Anthony Ngure on 11/06/2017.
 * Email : anthonyngure25@gmail.com.
 * Company : VibeCampo Social Network..
 */

public abstract class BaseAppFragment extends Fragment {

    private static final String TAG = "BaseAppFragment";

    public void showProgressDialog() {
        ((BaseAppActivity) getActivity()).showProgressDialog();
    }

    protected void showProgressDialog(@StringRes int message) {
        showProgressDialog(getString(message));
    }

    protected void showProgressDialog(String message) {
        ((BaseAppActivity) getActivity()).showProgressDialog(message);
    }

    public void hideProgressDialog() {
        ((BaseAppActivity) getActivity()).hideProgressDialog();
    }

    public void toast(Object message) {
        ((BaseAppActivity) getActivity()).toast(message);
    }

    public void toast(@StringRes int string) {
        toast(getString(string));
    }

    public void toastDebug(Object msg) {
        ((BaseAppActivity) getActivity()).toastDebug(msg);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        BaseUtils.tintMenu(getActivity(), menu, Color.WHITE);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
