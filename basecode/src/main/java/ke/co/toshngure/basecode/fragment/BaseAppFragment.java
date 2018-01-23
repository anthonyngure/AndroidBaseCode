/*
 * Copyright (c) 2018.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.basecode.fragment;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import org.json.JSONObject;

import ke.co.toshngure.basecode.R;
import ke.co.toshngure.basecode.app.BaseAppActivity;
import ke.co.toshngure.basecode.networking.ConnectionListener;
import ke.co.toshngure.basecode.utils.BaseUtils;

/**
 * Created by Anthony Ngure on 11/06/2017.
 * Email : anthonyngure25@gmail.com.
 * Company : VibeCampo Social Network..
 */

public abstract class BaseAppFragment extends Fragment implements ConnectionListener {

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

    @Override
    public void connect() {

    }

    @Override
    public void onConnectionStarted() {
        showProgressDialog();
    }


    @Override
    public void onConnectionFailed(int statusCode, JSONObject response) {
        log("Connection failed! " + statusCode + ", " + String.valueOf(response));
        hideProgressDialog();
        if ((statusCode == 0) || (statusCode == 408)) {
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.connection_timed_out)
                    .setMessage(R.string.error_connection)
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(R.string.retry, (dialog, which) -> connect()).create().show();
        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setCancelable(true)
                    .setTitle(R.string.server_error)
                    .setMessage(response.toString())
                    .setNegativeButton(R.string.report, (dialog, which) -> {

                    })
                    .setPositiveButton(android.R.string.ok, null);
            builder.create().show();
        }

    }

    private void log(Object msg) {
        if ( ((BaseAppActivity) getActivity()).isDebuggable()){
            Log.d(TAG, String.valueOf(msg));
        }
    }


    protected void showErrorAlertDialog(String message) {
        new AlertDialog.Builder(getActivity()).setCancelable(true)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .create()
                .show();
    }

    @Override
    public void onConnectionSuccess(JSONObject response) {
        log("onConnectionSuccess, Response = " + String.valueOf(response));
        hideProgressDialog();
    }

    @Override
    public void onConnectionProgress(int progress) {

    }

    @Override
    public Context getListenerContext() {
        return getActivity();
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
