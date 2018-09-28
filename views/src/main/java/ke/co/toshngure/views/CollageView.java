/*
 * Copyright (c) 2018.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.views;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tosh on 08.12.16.
 */

public class CollageView extends LinearLayout {
    private static final String TAG = CollageView.class.getSimpleName();
    private List<String> paths;
    private boolean useFirstAsHeader = false;

    private ImageForm photosForm = ImageForm.IMAGE_FORM_SQUARE;
    private ImageForm headerForm = ImageForm.IMAGE_FORM_SQUARE;

    private int defaultPhotosForLine = 2;


    private OnPhotoClickListener onPhotoClickListener;
    private boolean fromNetwork;

    public CollageView(Context context) {
        super(context);
    }

    public CollageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CollageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void loadFromNetwork(List<String> paths) {
        this.paths = paths;
        this.fromNetwork = true;
        init();
    }

    public void loadFromMediaStore(List<String> paths) {
        this.paths = paths;
        this.fromNetwork = false;
        init();
    }

    private void init() {

        setOrientation(VERTICAL);
        setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.darker_gray));
        removeAllViews();
        ArrayList<String> addPaths = new ArrayList<>();

        ArrayList<Integer> photosCount = buildPhotosCounts();

        if (paths != null) {
            int size = getPhotosSize();
            if (size > 0) {
                int number = 0;
                int i = 0;
                while (i < size) {
                    int photosInLine = photosCount.get(getChildCount());
                    addPaths.add(paths.get(i));
                    number++;
                    if (number == photosInLine || size == i + 1) {
                        final LinearLayout photosLine = new LinearLayout(getContext());
                        photosLine.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        photosLine.setOrientation(LinearLayout.HORIZONTAL);
                        photosLine.setWeightSum(photosInLine * 1f);
                        for (int j = 0; j < photosInLine; j++) {
                            ViewGroup photoFrame = new FrameLayout(getContext());
                            LayoutParams layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
                            //layoutParams.setMargins(photoMargin, photoMargin, photoMargin, photoMargin);
                            photoFrame.setLayoutParams(layoutParams);
                            ImageForm imageForm = useFirstAsHeader && i == 0 ? headerForm : photosForm;
                            CollageViewNetworkImage fineCollageViewNetworkImage = new CollageViewNetworkImage(getContext(), imageForm);
                            photoFrame.addView(fineCollageViewNetworkImage);
                            String path = addPaths.get(j);
                            if (fromNetwork) {
                                fineCollageViewNetworkImage.loadImageFromNetwork(path);
                            } else {
                                fineCollageViewNetworkImage.loadImageFromMediaStore(path);
                            }
                            photoFrame.setOnClickListener(null);
                            final int finalI = i - (photosInLine - j) + 1;
                            photoFrame.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (onPhotoClickListener != null) {
                                        onPhotoClickListener.onPhotoClick(finalI);
                                    }
                                }
                            });
                            photosLine.addView(photoFrame);
                        }
                        addView(photosLine);
                        addPaths.clear();
                        number = 0;
                    }
                    i++;
                }
            }
        }
    }

    public void setDefaultPhotosForLine(int defaultPhotosForLine) {
        this.defaultPhotosForLine = defaultPhotosForLine;
    }

    public void setUseFirstAsHeader(boolean useFirstAsHeader) {
        this.useFirstAsHeader = useFirstAsHeader;
    }

    private int getPhotosSize() {
        int size = 0;
        if (paths != null) {
            size = paths.size();
        }
        return size;
    }

    private ArrayList<Integer> buildPhotosCounts() {
        int headerDecreaser = useFirstAsHeader ? 1 : 0;
        int photosSize = getPhotosSize() - headerDecreaser;
        int remainder = photosSize % defaultPhotosForLine;
        int lineCount = photosSize / defaultPhotosForLine;
        ArrayList<Integer> photosCounts = new ArrayList<>();
        if (useFirstAsHeader) {
            photosCounts.add(1);
            lineCount++;
        }
        for (int i = 0; i < lineCount; i++) {
            photosCounts.add(defaultPhotosForLine);
        }
        if (remainder >= lineCount) {
            photosCounts.add(headerDecreaser, remainder);
        } else {
            for (int i = lineCount - 1; i > lineCount - remainder - 1; i--) {
                photosCounts.set(i, photosCounts.get(i) + 1);
            }
        }
        return photosCounts;
    }

    public void setOnPhotoClickListener(OnPhotoClickListener onPhotoClickListener) {
        this.onPhotoClickListener = onPhotoClickListener;
    }

    public enum ImageForm {
        IMAGE_FORM_SQUARE(1), IMAGE_FORM_HALF_HEIGHT(2);

        private int divider = 1;

        ImageForm(int divider) {
            this.divider = divider;
        }

        public int getDivider() {
            return divider;
        }
    }


    public interface OnPhotoClickListener {
        void onPhotoClick(int position);
    }
}
