/*
 * Copyright (c) 2018.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.basecode.app;

import android.app.Activity;
import android.os.AsyncTask;

import ke.co.toshngure.basecode.R;

/**
 * Created by Anthony Ngure on 03/09/2017.
 * Email : anthonyngure25@gmail.com.
 *
 */
public abstract class DialogAsyncTask<Params, Progress, Result>
        extends AsyncTask<Params, Progress, Result> {

    private BaseProgressDialog progressDialog;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try {
            progressDialog = new BaseProgressDialog(getActivity());
            progressDialog.setMessage(getActivity().getString(R.string.message_waiting))
                    .setCancelable(false)
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract Activity getActivity();

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
