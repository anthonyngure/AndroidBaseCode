/*
 * Copyright (c) 2018.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.androidbasecode.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.widget.TextView;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ke.co.toshngure.androidbasecode.R;
import ke.co.toshngure.basecode.util.BaseUtils;
import ke.co.toshngure.basecode.util.DrawableUtils;
import ke.co.toshngure.views.NetworkImage;

public class NetworkImageActivity extends BaseActivity {

    @BindView(R.id.sourceTV)
    TextView sourceTV;
    @BindView(R.id.circledNI)
    NetworkImage circledNI;
    @BindView(R.id.normalNI)
    NetworkImage normalNI;
    private boolean hasBackground;

    public static void start(Context context) {
        Intent starter = new Intent(context, NetworkImageActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_image);
        ButterKnife.bind(this);

        circledNI.setLoadingCallBack(resource -> {
            toastDebug("Circle Image Loaded");
            applyPalette(DrawableUtils.drawableToBitmap(resource));
        });
        normalNI.setLoadingCallBack(drawable -> toastDebug("Normal Image Loaded"));
    }

    private void applyPalette(Bitmap bitmap) {
        Palette.from(bitmap).generate(palette -> {
            int primaryDark = BaseUtils.getColor(getThis(), R.attr.colorPrimaryDark);
            int primary = BaseUtils.getColor(getThis(), R.attr.colorPrimary);
            getToolbar().setBackgroundColor(palette.getMutedColor(primary));
            supportStartPostponedEnterTransition();
        });
    }

    @OnClick(R.id.networkBtn)
    public void onNetworkBtnClicked() {
        resetThemeColors();
        sourceTV.setText("Network");
        Random random = new Random();
        circledNI.loadImageFromNetwork("https://lorempixel.com/400/400/transport/?" + random.nextInt());
        normalNI.loadImageFromNetwork("https://lorempixel.com/400/400/transport/?" + random.nextInt());
    }

    @OnClick(R.id.mediaStoreBtn)
    public void onMediaStoreBtnClicked() {
        resetThemeColors();
        sourceTV.setText("Media Store");
    }

    @OnClick(R.id.drawableBtn)
    public void onDrawableBtnClicked() {
        resetThemeColors();
        sourceTV.setText("Drawable");
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.header);
        circledNI.setImageResource(R.drawable.header);
        normalNI.setImageResource(R.drawable.header);
        applyPalette(DrawableUtils.drawableToBitmap(drawable));
    }

    private void resetThemeColors() {
        getToolbar().setBackgroundColor(BaseUtils.getColor(this, R.attr.colorPrimary));
    }

}
