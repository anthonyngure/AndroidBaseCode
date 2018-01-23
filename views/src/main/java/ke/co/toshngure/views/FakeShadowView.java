/*
 * Copyright (c) 2018.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.views;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class FakeShadowView extends View {

    public FakeShadowView(Context context) {
        this(context, null);
    }

    public FakeShadowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FakeShadowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setBackgroundResource(R.drawable.background_fake_shadow);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setVisibility(GONE);
        }
    }
}
