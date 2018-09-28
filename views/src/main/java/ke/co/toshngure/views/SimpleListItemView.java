/*
 * Copyright (c) 2018.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ke.co.toshngure.views.utils.Utils;


/**
 * Created by Anthony Ngure on 7/25/2016.
 * Email : anthonyngure25@gmail.com.
 */
public class SimpleListItemView extends FrameLayout {

    private TextView mTitleTV;
    private TextView mSubTitleTV;
    private ImageView mDrawableIV;
    private LineView dividerLV;

    public SimpleListItemView(@NonNull Context context) {
        this(context, null);
    }

    public SimpleListItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public SimpleListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SimpleListItemView);
        LayoutInflater.from(getContext()).inflate(R.layout.view_simple_list_item, this, true);

        mTitleTV = findViewById(R.id.titleTV);
        dividerLV = findViewById(R.id.dividerLV);
        mSubTitleTV = findViewById(R.id.subTitleTV);
        mDrawableIV = findViewById(R.id.drawableIV);

        setTitle(typedArray.getString(R.styleable.SimpleListItemView_sliTitle));
        int titleColor = typedArray.getColor(R.styleable.SimpleListItemView_sliTitleColor, Color.BLACK);
        mTitleTV.setTextColor(titleColor);

        int titleTopBottomPadding = typedArray.getDimensionPixelSize(R.styleable.SimpleListItemView_sliTitleTopBottomPadding, 0);
        if (titleTopBottomPadding != 0){
            int padding = Utils.dpToPx(titleTopBottomPadding);
            mTitleTV.setPadding(mTitleTV.getPaddingLeft(), padding, mTitleTV.getPaddingRight(), padding);
        }

        setSubTitle(typedArray.getString(R.styleable.SimpleListItemView_sliSubTitle));
        int subTitleColor = typedArray.getColor(R.styleable.SimpleListItemView_sliSubTitleColor, Color.DKGRAY);
        mSubTitleTV.setTextColor(subTitleColor);

        setItemDrawable(typedArray.getDrawable(R.styleable.SimpleListItemView_sliDrawable));
        int drawableTint = typedArray.getColor(R.styleable.SimpleListItemView_sliDrawableTint,
                Utils.getColor(getContext(), R.attr.colorPrimary));
        mDrawableIV.setColorFilter(drawableTint);

        boolean drawableCentered = typedArray.getBoolean(R.styleable.SimpleListItemView_sliDrawableCentered, false);
        if (drawableCentered){
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mDrawableIV.getLayoutParams();
            params.gravity = Gravity.CENTER;
        }


        boolean showDivider = typedArray.getBoolean(R.styleable.SimpleListItemView_sliDividerVisible, true);
        dividerLV.setVisibility(showDivider ? VISIBLE : GONE);
        typedArray.recycle();
    }

    public void setTitle(String title) {
        if ((title != null) && (!title.isEmpty())) {
            mTitleTV.setVisibility(VISIBLE);
            mTitleTV.setText(title);
        } else {
            mTitleTV.setVisibility(GONE);
        }
    }

    public void setTitleColor(@ColorInt int color){
        mTitleTV.setTextColor(color);
    }


    public void setSubTitle(String subTitle) {
        if (subTitle == null || subTitle.isEmpty()) {
            mSubTitleTV.setVisibility(GONE);
        } else {
            mSubTitleTV.setText(subTitle);
            mSubTitleTV.setVisibility(VISIBLE);
        }
    }

    public void setSubTitleColor(@ColorInt int color){
        mSubTitleTV.setTextColor(color);
    }

    public void setItemDrawable(Drawable itemDrawable) {
        if (itemDrawable == null) {
            mDrawableIV.setVisibility(GONE);
        } else {
            mDrawableIV.setImageDrawable(itemDrawable);
            mDrawableIV.setVisibility(VISIBLE);
        }
    }

    public void setDrawableTint(@ColorInt int drawableTint){
        mDrawableIV.setColorFilter(drawableTint);
    }
}
