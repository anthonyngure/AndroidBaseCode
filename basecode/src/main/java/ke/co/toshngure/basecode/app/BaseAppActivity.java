/*
 * Copyright (c) 2018.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.basecode.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import ke.co.toshngure.basecode.R;
import ke.co.toshngure.basecode.fragment.LogItemsFragment;
import ke.co.toshngure.basecode.logging.BeeLog;
import ke.co.toshngure.basecode.util.BaseUtils;

/**
 * Created by Anthony Ngure on 18/09/2017.
 * Email : anthonyngure25@gmail.com.
 */

public class BaseAppActivity extends AppCompatActivity {

    private static final String TAG = "BaseAppActivity";

    private static int sessionDepth = 0;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    protected BaseProgressDialog mProgressDialog;
    private Toolbar toolbar;


    public BaseAppActivity() {
    }

    protected static int getSessionDepth() {
        return sessionDepth;
    }

    @Override
    protected void onStart() {
        super.onStart();
        sessionDepth++;
    }

    @Override
    protected void onPause() {
        try {
            if ((mProgressDialog != null) && mProgressDialog.isShowing()) {
                hideProgressDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (sessionDepth > 0) {
            sessionDepth--;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        setupToolbar();
        setUpTitleFromLabel();
    }

    protected void setUpTitleFromLabel() {
        String label = null;
        try {
            label = getResources().getString(
                    getPackageManager().getActivityInfo(getComponentName(), 0).labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Activity Label: " + label);
        if (getToolbar() != null && !TextUtils.isEmpty(label)) {
            getToolbar().setTitle(label);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(getStartEnterAnim(), getStartExitAnim());
    }


    public void startNewTaskActivity(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public AppCompatActivity getThis() {
        return this;
    }

    protected void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    public void viewLogs(){
        ReusableFragmentActivity.start(this, LogItemsFragment.newInstance(), "Log Items");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        BaseUtils.tintMenu(this, menu, Color.WHITE);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void hideKeyboardFrom(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void showProgressDialog() {
        showProgressDialog(getString(R.string.message_waiting));
    }

    public void showProgressDialog(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new BaseProgressDialog(this)
                    .setMessage(message)
                    .setCancelable(false);
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if ((mProgressDialog != null) && (mProgressDialog.isShowing())) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    public void updateProgressDialogMessage(String newMessage) {
        if ((mProgressDialog != null) && (mProgressDialog.isShowing())) {
            mProgressDialog.setMessage(newMessage);
        }
    }

    public void toast(Object message) {

        try {
            Toast.makeText(this, String.valueOf(message), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void toastDebug(Object msg) {
        if (BeeLog.DEBUG) {
            toast(msg);
        }
    }

    public void toast(@StringRes int string) {
        toast(getString(string));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (sessionDepth > 1) {
            overridePendingTransition(getLeaveEnterAnim(), getLeaveExitAnim());
        }
    }

    protected int getStartEnterAnim() {
        return R.anim.slide_left_in;
    }

    protected int getStartExitAnim() {
        return R.anim.hold;
    }

    protected int getLeaveEnterAnim() {
        return R.anim.hold;
    }

    protected int getLeaveExitAnim() {
        return R.anim.slide_right_out;
    }


}
