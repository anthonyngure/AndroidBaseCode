/*
 * Copyright (c) 2017. Laysan Incorporation
 * Website http://laysan.co.ke
 * Tel +254723203475/+254706356815
 */

package ke.co.toshngure.basecode.app;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import ke.co.toshngure.basecode.R;


public class ReusableFragmentActivity extends BaseAppActivity {

    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_SUB_TITLE = "extra_sub_title";
    private static Fragment mFragment;

    public static void start(AppCompatActivity context, Fragment fragment, String title, @Nullable String subTitle) {
        Intent starter = new Intent(context, ReusableFragmentActivity.class);
        starter.putExtra(EXTRA_TITLE, title);
        starter.putExtra(EXTRA_SUB_TITLE, subTitle);
        mFragment = fragment;
        context.startActivity(starter);
    }

    public static void start(AppCompatActivity context, Fragment fragment, String title) {
        start(context, fragment, title, null);
    }

    public static void start(AppCompatActivity context, Fragment fragment, int requestCode, String title, @Nullable String subTitle) {
        Intent starter = new Intent(context, ReusableFragmentActivity.class);
        starter.putExtra(EXTRA_TITLE, title);
        starter.putExtra(EXTRA_SUB_TITLE, subTitle);
        mFragment = fragment;
        context.startActivityForResult(starter, requestCode);
    }

    public static void start(AppCompatActivity context, Fragment fragment, int requestCode, String title) {
        start(context, fragment, requestCode, title, null);
    }

    @Override
    protected void setUpTitleFromLabel() {
        //super.setUpTitleFromLabel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        if (getIntent().getStringExtra(EXTRA_TITLE) == null) {
            throw new IllegalArgumentException("Title can not be null!");
        }

        setTitle(getIntent().getStringExtra(EXTRA_TITLE));
        getToolbar().setTitle(getIntent().getStringExtra(EXTRA_TITLE));

        if (getIntent().getStringExtra(EXTRA_SUB_TITLE) != null && getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle(getIntent().getStringExtra(EXTRA_SUB_TITLE));
        }


        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentsContainer, mFragment,
                        mFragment.getClass().getSimpleName())
                .commit();

    }

    public void addFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentsContainer, fragment,
                        fragment.getClass().getSimpleName())
                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();
    }

    public void addFragment(Fragment fragment, String title) {
        setTitle(title);
        addFragment(fragment);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStackImmediate();
        } else {
            super.onBackPressed();
        }
    }
}
