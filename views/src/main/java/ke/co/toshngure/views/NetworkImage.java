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
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.lang.ref.WeakReference;


/**
 * Created by Anthony Ngure on 20/02/2017.
 * Email : anthonyngure25@gmail.com.
 *
 */

public class NetworkImage extends FrameLayout {

    private static final String TAG = NetworkImage.class.getSimpleName();

    protected ImageView mImageView;
    protected ImageView mBackgroundImageView;
    protected ImageView mErrorButton;
    protected ProgressBar mProgressBar;
    private LoadingCallBack loadingCallBack;

    public NetworkImage(Context context) {
        this(context, null);
    }

    public NetworkImage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NetworkImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.view_network_image, this, true);

        mErrorButton = findViewById(R.id.errorButton);
        mProgressBar = findViewById(R.id.progressBar);

        FrameLayout imageFL = findViewById(R.id.imageFL);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NetworkImage);

        boolean circled = typedArray.getBoolean(R.styleable.NetworkImage_niCircled, false);
        FrameLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        if (circled) {
            mImageView = new CircleImageView(context);
            mBackgroundImageView = new CircleImageView(context);
        } else {
            mImageView = new ImageView(context);
            mBackgroundImageView = new ImageView(context);
            mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mBackgroundImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        imageFL.addView(mBackgroundImageView, layoutParams);
        imageFL.addView(mImageView, layoutParams);

        /*Image*/
        setImageDrawable(typedArray.getDrawable(R.styleable.NetworkImage_niSrc));

        /*Background*/
        mBackgroundImageView.setImageDrawable(typedArray.getDrawable(R.styleable.NetworkImage_niBackground));

        typedArray.recycle();

    }

    public void loadImageFromNetwork(final String networkPath) {
        mProgressBar.setVisibility(VISIBLE);
        mErrorButton.setOnClickListener(v -> {
            Log.d(TAG, "Retrying to loadFromNetwork image");
            mProgressBar.setVisibility(VISIBLE);
            mErrorButton.setVisibility(GONE);
            loadImageFromNetwork(networkPath);
        });

        Glide.with(getContext().getApplicationContext())
                .load(networkPath)
                .dontAnimate()
                .dontTransform()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(new Listener(mImageView, mProgressBar, mErrorButton, loadingCallBack))
                .into(mImageView);
    }

    public void loadImageFromMediaStore(String path) {
        mProgressBar.setVisibility(VISIBLE);
        File file = new File(path);
        Glide.with(getContext().getApplicationContext())
                .loadFromMediaStore(Uri.fromFile(file))
                .dontAnimate()
                .dontTransform()
                .listener(new Listener(mImageView, mProgressBar, mErrorButton, loadingCallBack))
                .into(mImageView);
    }


    public void loadImageFromMediaStore(Uri uri) {
        mProgressBar.setVisibility(VISIBLE);
        Glide.with(getContext().getApplicationContext())
                .loadFromMediaStore(uri)
                .dontAnimate()
                .dontTransform()
                .listener(new Listener(mImageView, mProgressBar, mErrorButton, loadingCallBack))
                .into(mImageView);
    }

    public NetworkImage setLoadingCallBack(LoadingCallBack loadingCallBack) {
        this.loadingCallBack = loadingCallBack;
        return this;
    }

    public void setImageDrawable(Drawable drawable) {
        mImageView.setImageDrawable(drawable);
        mImageView.setVisibility(VISIBLE);
        mProgressBar.setVisibility(GONE);
        mErrorButton.setVisibility(GONE);
    }

    public void setImageResource(@DrawableRes int resId) {
        mImageView.setImageResource(resId);
        setImageDrawable(ContextCompat.getDrawable(getContext(), resId));
    }


    public interface LoadingCallBack {
        void onSuccess(GlideDrawable drawable);
    }

    /**
     * Glide Callback which clears the ImageView's background onSuccess. This is done to reduce
     * overdraw. A weak reference is used to avoid leaking the Activity context because the Callback
     * will be strongly referenced by Glide.
     */
    static class Listener implements RequestListener<Object, GlideDrawable> {

        final WeakReference<ImageView> imageViewWeakReference;
        final WeakReference<ProgressBar> progressBarWeakReference;
        final WeakReference<ImageView> errorImageViewWeakReference;
        final WeakReference<LoadingCallBack> loadingCallBackWeakReference;

        public Listener(ImageView imageView, ProgressBar progressBar, ImageView errorImageView, LoadingCallBack loadingCallBack) {
            imageViewWeakReference = new WeakReference<>(imageView);
            progressBarWeakReference = new WeakReference<>(progressBar);
            errorImageViewWeakReference = new WeakReference<>(errorImageView);
            loadingCallBackWeakReference = new WeakReference<>(loadingCallBack);
        }


        @Override
        public boolean onException(Exception e, Object model, Target<GlideDrawable> target, boolean isFirstResource) {
            try {
                Log.e(TAG, e.toString());
            } catch (Exception e1) {
                Log.e(TAG, e1.toString());
            }

            ProgressBar progressBar = progressBarWeakReference.get();
            if (progressBar != null) {
                progressBar.setVisibility(GONE);
            }

            ImageView errorImageView = errorImageViewWeakReference.get();
            if (errorImageView != null) {
                errorImageView.setVisibility(VISIBLE);
            }

            return false;
        }

        @Override
        public boolean onResourceReady(GlideDrawable resource, Object model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
            final ImageView imageView = imageViewWeakReference.get();
            LoadingCallBack loadingCallBack = loadingCallBackWeakReference.get();
            if (loadingCallBack != null) {
                loadingCallBack.onSuccess(resource);
            }
            ProgressBar progressBar = progressBarWeakReference.get();
            if (progressBar != null) {
                progressBar.setVisibility(GONE);
            }

            ImageView errorImageView = errorImageViewWeakReference.get();
            if (errorImageView != null) {
                errorImageView.setVisibility(GONE);
            }
            return false;
        }
    }

}
