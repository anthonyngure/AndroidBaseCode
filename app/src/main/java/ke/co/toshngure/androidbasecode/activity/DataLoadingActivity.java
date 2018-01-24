/*
 * Copyright (c) 2017.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.androidbasecode.activity;

import android.os.Bundle;

import ke.co.toshngure.androidbasecode.R;
import ke.co.toshngure.androidbasecode.fragment.UsersFragment;

public class DataLoadingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_loading);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentsContainer, UsersFragment.newInstance())
                .commit();
    }

}
