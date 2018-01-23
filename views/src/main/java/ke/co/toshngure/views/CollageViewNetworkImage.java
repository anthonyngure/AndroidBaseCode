/*
 * Copyright (c) 2018.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.views;

import android.annotation.SuppressLint;
import android.content.Context;


/**
 * Created by Tosh on 08.12.16.
 */

@SuppressLint("ViewConstructor")
class CollageViewNetworkImage extends NetworkImage {

    private CollageView.ImageForm imageForm = CollageView.ImageForm.IMAGE_FORM_SQUARE;

    public CollageViewNetworkImage(Context context, CollageView.ImageForm imageForm) {
        super(context);
        this.imageForm = imageForm;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getParent() != null) {
            getLayoutParams().height = widthMeasureSpec / imageForm.getDivider();
            setMeasuredDimension(widthMeasureSpec, widthMeasureSpec / imageForm.getDivider());
        }
    }
}