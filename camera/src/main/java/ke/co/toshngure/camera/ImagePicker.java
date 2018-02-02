package ke.co.toshngure.camera;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * Created by Anthony Ngure on 31/12/2017.
 * Email : anthonyngure25@gmail.com.
 */

public class ImagePicker extends FrameLayout {

    private static final String TAG = "ImagePicker";

    ImageView photoIV;
    ImageView deleteIV;
    LinearLayout addPhotoLL;
    FrameLayout photoFL;
    FrameLayout loaderFL;
    TextView textTV;
    private File file;

    private AppCompatActivity mActivity;
    private int mRequestCode;

    public ImagePicker(@NonNull Context context) {
        this(context, null);
    }

    public ImagePicker(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImagePicker(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_image_picker, this, true);
        photoIV = findViewById(R.id.photoIV);
        photoFL = findViewById(R.id.photoFL);
        loaderFL = findViewById(R.id.loaderFL);
        textTV = findViewById(R.id.textTV);
        findViewById(R.id.deleteIV).setOnClickListener(this::onDeleteIVClicked);
        findViewById(R.id.addPhotoLL).setOnClickListener(this::onAddPhotoLLClicked);
    }

    public void setActivity(AppCompatActivity activity, int requestCode, boolean required) {
        this.mActivity = activity;
        this.mRequestCode = requestCode;
        if (required) {
            textTV.setText(Html.fromHtml(mActivity.getString(R.string.add_a_photo_required)));
        }
    }

    public void setText(String text) {
        textTV.setText(text);
    }

    @SuppressLint("StaticFieldLeak")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*BeeLog.i(TAG, "Request Code: " + String.valueOf(requestCode));
        BeeLog.i(TAG, "Result Code: " + String.valueOf(resultCode));
        BeeLog.i(TAG, "Data: " + String.valueOf(data));*/
        if ((requestCode == mRequestCode) && (resultCode == Activity.RESULT_OK) && data != null) {
            new AsyncTask<Uri, Void, File>() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    addPhotoLL.setVisibility(GONE);
                    photoFL.setVisibility(GONE);
                    loaderFL.setVisibility(VISIBLE);
                }

                @Override
                protected File doInBackground(Uri... uris) {
                    try {
                        return new ImageCompressor.Builder(mActivity)
                                .setDestinationDirectoryPath(FileUtil.getAppExternalDirectoryFolder(mActivity))
                                .setFileName("image_" + mRequestCode)
                                .build().compressToFile(FileUtil.from(mActivity, uris[0]));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(File file) {
                    super.onPostExecute(file);
                    if (file != null) {
                        setFile(file);
                    } else {
                        Toast.makeText(mActivity, "Image loading Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }.execute(data.getData());
        }
    }

    private void onDeleteIVClicked(View view) {
        addPhotoLL.setVisibility(VISIBLE);
        loaderFL.setVisibility(GONE);
        photoFL.setVisibility(GONE);
        if (file != null) {
            file.delete();
            file = null;
        }
    }

    private void onAddPhotoLLClicked(View view) {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.add_a_photo)
                .setItems(new String[]{"Gallery", "Camera"}, (dialogInterface, which) -> {
                    if (which == 0) {
                        ImagePickerUtils.pickImage(mActivity, mRequestCode);
                    } else {
                        Intent startCustomCameraIntent = new Intent(mActivity, CameraActivity.class);
                        mActivity.startActivityForResult(startCustomCameraIntent, mRequestCode);
                    }
                })
                .setPositiveButton(android.R.string.cancel, (dialogInterface, i) -> {

                })
                .show();
    }

    public File getFile() {
        return file;
    }

    public void setFile(@NonNull File file) {
        this.file = file;
        photoIV.setImageURI(Uri.fromFile(file));
        addPhotoLL.setVisibility(GONE);
        loaderFL.setVisibility(GONE);
        photoFL.setVisibility(VISIBLE);
    }
}
