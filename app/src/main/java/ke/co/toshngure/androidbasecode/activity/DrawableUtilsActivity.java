/*
 * Copyright (c) 2018.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.androidbasecode.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ke.co.toshngure.basecode.util.DrawableUtils;
import ke.co.toshngure.androidbasecode.R;

public class DrawableUtilsActivity extends BaseActivity {

    @BindView(R.id.androidIV)
    ImageView androidIV;
    @BindView(R.id.changeColorBtn)
    Button changeColorBtn;

    private int[] colors = new int[]{
            R.color.colorAccent,
            R.color.colorPrimary,
            R.color.colorPrimaryDark,
            android.R.color.holo_red_dark,
            android.R.color.holo_blue_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_green_dark,
            android.R.color.holo_purple,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawable_utils);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.changeColorBtn)
    public void onViewClicked() {
        Random random = new Random();
        int color = colors[random.nextInt(colors.length)];
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_android_black_96dp);
        androidIV.setImageDrawable(DrawableUtils.tintDrawable(this, drawable, color));
        toast(String.valueOf(color));
    }
}
