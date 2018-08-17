/*
 * Copyright (c) 2018.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.androidbasecode.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import ke.co.toshngure.androidbasecode.R;
import ke.co.toshngure.androidbasecode.fragment.ImagePickerFragment;
import ke.co.toshngure.androidbasecode.fragment.PostsFragment;
import ke.co.toshngure.androidbasecode.fragment.PostFragment;
import ke.co.toshngure.basecode.app.ReusableFragmentActivity;

public class MainActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @OnClick(R.id.fullModelListFragmentSLIV)
    public void onFullFragmentSLIVSLIVClick() {
        ReusableFragmentActivity.start(this, PostsFragment.newInstance(), "Model Data Loading");
    }

    @OnClick(R.id.bottomSheetModelListFragmentSLIV)
    public void onBottomSheetModelListFragmentSLIVClick() {
        ReusableFragmentActivity.start(this, PostsFragment.newInstance(), "Model Data Loading");
    }

    @OnClick(R.id.modelFragmentSLIV)
    public void onModelDataLoadingFragmentSLIVClick() {
        ReusableFragmentActivity.start(this, PostFragment.newInstance(), "Model Fragment");
    }


    @OnClick(R.id.drawableUtilsSLIV)
    public void onDrawableUtilsSLIVClick() {
        startActivity(new Intent(this, DrawableUtilsActivity.class));
    }

    @OnClick(R.id.dialogSLIV)
    public void onDialogSLIVClick(View view) {
        showProgressDialog();
        view.postDelayed(this::hideProgressDialog, 5000);
    }

    @OnClick(R.id.customMessageDialogSLIV)
    public void onCustomMessageSLIVClick(View view) {
        showProgressDialog(getString(R.string.appbar_scrolling_view_behavior));
        view.postDelayed(this::hideProgressDialog, 5000);
    }

    @OnClick(R.id.networkImageSLIV)
    public void onNetworkImageSLIV() {
        NetworkImageActivity.start(this);
    }

    @OnClick(R.id.prefUtilsImplSLIV)
    public void onPrefUtilsImplSLIV() {
        PrefUtilsImplActivity.start(this);
    }

    @OnClick(R.id.imagePickerSLIV)
    public void onImagePickerSLIVSLIV() {
        ReusableFragmentActivity.start(this, ImagePickerFragment.newInstance(), "Image Picker Test");
    }

    @OnClick(R.id.connectionHandlerSLIV)
    public void onConnectionHandlerSLIV() {
    }


}
