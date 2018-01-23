/*
 * Copyright (c) 2018.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.filters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by Anthony Ngure on 26/02/2017.
 * Email : anthonyngure25@gmail.com.
 *
 */

public class FilterBitmapTransformation extends BitmapTransformation {

    private static final ImageProcessor mImageProcessor = new ImageProcessor();
    private int mPosition;
    private FilterCategory mFilterCategory = FilterCategory.ROTATION;

    public FilterBitmapTransformation(Context context, int position, FilterCategory category) {
        super(context);
        this.mPosition = position;
        this.mFilterCategory = category;
    }

    public static Bitmap applyFilter(Bitmap originalBitmap, int position, FilterCategory category){
        switch (category){
            case ROTATION:
                return applyRotationFilters(originalBitmap, position);
            case CORNERS:
                return applyCornerFilters(originalBitmap, position);
            case COLOR:
                return applyColorFilters(originalBitmap, position);
            default:
                return applyRandomFilter(originalBitmap, position);
        }
    }

    private static Bitmap applyRandomFilter(Bitmap bitmap, int position) {
        switch ((position)) {
            case 1:
                return bitmap;
            case 2:
                return mImageProcessor.doHighlightImage(bitmap, 15, Color.RED);
            case 3:
                return mImageProcessor.doInvert(bitmap);
            case 4:
                return mImageProcessor.doGreyScale(bitmap);
            case 5:
                return mImageProcessor.doGamma(bitmap, 0.6, 0.6, 0.6);
            case 6:
                return mImageProcessor.doGamma(bitmap, 1.8, 1.8, 1.8);
            case 7:
                return mImageProcessor.doColorFilter(bitmap, 1, 0, 0);
            case 8:
                return mImageProcessor.doColorFilter(bitmap, 0, 1, 0);
            case 9:
                return mImageProcessor.doColorFilter(bitmap, 0, 0, 1);
            case 10:
                return mImageProcessor.doColorFilter(bitmap, 0.5, 0.5, 0.5);
            case 11:
                return mImageProcessor.doColorFilter(bitmap, 1.5, 1.5, 1.5);
            case 12:
                return mImageProcessor.createSepiaToningEffect(bitmap, 150, 0.7, 0.3, 0.12);
            case 13:
                return mImageProcessor.createSepiaToningEffect(bitmap, 150, 0.8, 0.2, 0);
            case 14:
                return mImageProcessor.createSepiaToningEffect(bitmap, 150, 0.12, 0.7, 0.3);
            case 15:
                return mImageProcessor.createSepiaToningEffect(bitmap, 150, 0.12, 0.3, 0.7);
            case 16:
                return mImageProcessor.decreaseColorDepth(bitmap, 32);
            case 17:
                return mImageProcessor.decreaseColorDepth(bitmap, 64);
            case 18:
                return mImageProcessor.decreaseColorDepth(bitmap, 128);
            case 19:
                return mImageProcessor.createContrast(bitmap, 50);
            case 20:
                return mImageProcessor.createContrast(bitmap, 100);
            case 21:
                return mImageProcessor.rotate(bitmap, 40);
            case 22:
                return mImageProcessor.rotate(bitmap, 340);
            case 23:
                return mImageProcessor.doBrightness(bitmap, -60);
            case 24:
                return mImageProcessor.doBrightness(bitmap, 30);
            case 25:
                return mImageProcessor.doBrightness(bitmap, 80);
            case 26:
                return mImageProcessor.applyGaussianBlur(bitmap);
            case 27:
                return mImageProcessor.createShadow(bitmap);
            case 28:
                return mImageProcessor.sharpen(bitmap, 11);
            case 29:
                return mImageProcessor.applyMeanRemoval(bitmap);
            case 30:
                return mImageProcessor.smooth(bitmap, 100);
            case 31:
                return mImageProcessor.emboss(bitmap);
            case 32:
                return mImageProcessor.engrave(bitmap);
            case 33:
                return mImageProcessor.boost(bitmap, ImageProcessingConstants.RED, 1.5);
            case 34:
                return mImageProcessor.boost(bitmap, ImageProcessingConstants.GREEN, 0.5);
            case 35:
                return mImageProcessor.boost(bitmap, ImageProcessingConstants.BLUE, 0.67);
            case 36:
                return mImageProcessor.roundCorner(bitmap, 45);
            case 37:
                return mImageProcessor.flip(bitmap, ImageProcessingConstants.FLIP_VERTICAL);
            case 38:
                return mImageProcessor.tintImage(bitmap, 50);
            case 39:
                return mImageProcessor.replaceColor(bitmap, Color.BLACK, Color.BLUE);
            case 40:
                return mImageProcessor.applyFleaEffect(bitmap);
            case 41:
                return mImageProcessor.applyBlackFilter(bitmap);
            case 42:
                return mImageProcessor.applySnowEffect(bitmap);
            case 43:
                return mImageProcessor.applyShadingFilter(bitmap, Color.MAGENTA);
            case 44:
                return mImageProcessor.applyShadingFilter(bitmap, Color.BLUE);
            case 45:
                return mImageProcessor.applySaturationFilter(bitmap, 1);
            case 46:
                return mImageProcessor.applySaturationFilter(bitmap, 5);
            case 47:
                return mImageProcessor.applyHueFilter(bitmap, 1);
            case 48:
                return mImageProcessor.applyHueFilter(bitmap, 5);
            case 49:
                return mImageProcessor.applyReflection(bitmap);
            default:
                return bitmap;
        }
    }

    private static Bitmap applyRotationFilters(Bitmap originalBitmap, int position){
        switch (position){
            case 0:
                return originalBitmap;
            case 1:
                return mImageProcessor.rotate(originalBitmap, 45);
            case 2:
                return mImageProcessor.rotate(originalBitmap, 90);
            case 3:
                return mImageProcessor.rotate(originalBitmap, 135);
            case 4:
                return mImageProcessor.rotate(originalBitmap, 180);
            case 5:
                return mImageProcessor.rotate(originalBitmap, 225);
            case 6:
                return mImageProcessor.rotate(originalBitmap, 270);
            case 7:
                return mImageProcessor.rotate(originalBitmap, 315);
            default:
                return originalBitmap;
        }
    }

    private static Bitmap applyCornerFilters(Bitmap originalBitmap, int position){
        switch (position){
            case 0:
                return originalBitmap;
            case 1:
                return mImageProcessor.roundCorner(originalBitmap, 45);
            case 2:
                return mImageProcessor.roundCorner(originalBitmap, 90);
            case 3:
                return mImageProcessor.roundCorner(originalBitmap, 135);
            case 4:
                return mImageProcessor.roundCorner(originalBitmap, 180);
            case 5:
                return mImageProcessor.roundCorner(originalBitmap, 225);
            case 6:
                return mImageProcessor.roundCorner(originalBitmap, 270);
            case 7:
                return mImageProcessor.roundCorner(originalBitmap, 315);
            default:
                return originalBitmap;
        }
    }

    private static Bitmap applyColorFilters(Bitmap originalBitmap, int position){
        switch (position){
            case 0:
                return originalBitmap;
            case 1:
                //Red
                return mImageProcessor.doColorFilter(originalBitmap, 1, 0, 0);
            case 2:
                //Green
                return mImageProcessor.doColorFilter(originalBitmap, 0, 1, 0);
            case 3:
                //Blue
                return mImageProcessor.doColorFilter(originalBitmap, 0, 0, 1);
            case 4:
                //Yellow
                return mImageProcessor.doColorFilter(originalBitmap, 1, 1, 0);
            case 5:
                //Magenta/Fuchsia
                return mImageProcessor.doColorFilter(originalBitmap, 1, 0, 1);
            case 6:
                //Cyan/Aqua
                return mImageProcessor.doColorFilter(originalBitmap, 0, 1, 1);
            default:
                return originalBitmap;
        }
    }

    public static int getAvailableFilters(FilterCategory filterCategory){
        switch (filterCategory){
            case CORNERS:
            case ROTATION:
                return 8;
            case COLOR:
                return 7;
            default:
                return 50;
        }
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return applyFilter(toTransform, mPosition, mFilterCategory);
    }

    @Override
    public String getId() {
        return getClass().getName() + "-" + mPosition + "-" + mFilterCategory.ordinal();
    }

    /**
     * Created by Anthony Ngure on 26/02/2017.
     * Email : anthonyngure25@gmail.com.
     *
     */

    public enum FilterCategory {

        ALL(0),
        HIGHLIGHT(1),
        BLACK_AND_WHITE(2),
        COLOR(3),
        HUE(4),
        SATURATION(5),
        REFLECTION(6),
        SNOW(7),
        SEPIA(8),
        CONTRAST(9),
        EXTRAS(10),

        ROTATION(11),
        CORNERS(12);

        FilterCategory(int value) {
        }
    }
}
