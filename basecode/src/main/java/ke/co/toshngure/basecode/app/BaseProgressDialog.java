/*
 * Copyright (c) 2018.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.basecode.app;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import ke.co.toshngure.basecode.R;


/**
 * Created by Anthony Ngure on 03/09/2017.
 * Email : anthonyngure25@gmail.com.
 *
 */

public class BaseProgressDialog {

    private AlertDialog mAlertDialog;
    private AlertDialog.Builder mBuilder;
    private Context mContext;
    private View mView;
    private TextView mMessageTV;

    public BaseProgressDialog(Context context) {
        this.mBuilder = new AlertDialog.Builder(context);
        this.mContext = context;
        this.mView = LayoutInflater.from(context).inflate(R.layout.view_progress_dialog, null);
        this.mMessageTV = mView.findViewById(R.id.messageTV);
    }


    public BaseProgressDialog setMessage(String message) {
        mMessageTV.setText(String.valueOf(message));
        return this;
    }

    public BaseProgressDialog setMessage(@StringRes int message) {
        setMessage(mContext.getString(message));
        return this;
    }

    public BaseProgressDialog setCancelable(boolean cancellable) {
        mBuilder.setCancelable(cancellable);
        return this;
    }

    public boolean isShowing() {
        return ((mAlertDialog != null) && mAlertDialog.isShowing());
    }

    public void show() {
        mAlertDialog = mBuilder
                .setView(mView)
                .create();
        mAlertDialog.show();
    }

    public void dismiss() {
        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
        }
    }


    public void hide() {
        if (mAlertDialog != null) {
            mAlertDialog.hide();
        }
    }
}
