package ke.co.toshngure.camera;/*
 * Copyright (c) 2018.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.widget.Toast;

import java.util.Objects;


/**
 * Builder for crop Intents and utils for handling result
 */
public class ImagePickerUtils {



    /**
     * Pick image from an Activity
     *
     * @param activity Activity to receive result
     */
    public static void pickImage(Activity activity, int requestCode) {
        try {
            activity.startActivityForResult(getImagePicker(), requestCode);
        } catch (ActivityNotFoundException e) {
            showImagePickerError(activity);
        }
    }

    /**
     * Pick image from a support library Fragment
     *
     * @param fragment Fragment to receive result
     */
    public static void pickImage(Fragment fragment, int requestCode) {
        try {
            fragment.startActivityForResult(getImagePicker(), requestCode);
        } catch (ActivityNotFoundException e) {
            showImagePickerError(Objects.requireNonNull(fragment.getContext()));
        }
    }




    private static Intent getImagePicker() {
        return new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
    }

    private static void showImagePickerError(Context context) {
        Toast.makeText(context.getApplicationContext(), "Unable to pick an image!", Toast.LENGTH_SHORT).show();
    }
}