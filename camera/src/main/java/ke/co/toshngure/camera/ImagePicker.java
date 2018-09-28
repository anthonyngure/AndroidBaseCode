package ke.co.toshngure.camera;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    FrameLayout permissionsFL;
    TextView textTV;
    TextView permissionTV;
    private File file;

    private Context mContext;
    private AppCompatActivity mActivity;
    private int mRequestCode;
    private Fragment mFragment;

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
        permissionsFL = findViewById(R.id.permissionsFL);
        permissionsFL.setOnClickListener(this::onPermissionFLClicked);
        textTV = findViewById(R.id.textTV);
        deleteIV = findViewById(R.id.deleteIV);
        findViewById(R.id.deleteIV).setOnClickListener(this::onDeleteIVClicked);
        addPhotoLL = findViewById(R.id.addPhotoLL);
        findViewById(R.id.addPhotoLL).setOnClickListener(this::onAddPhotoLLClicked);
        permissionTV = findViewById(R.id.permissionsTV);
        permissionTV.setText(R.string.error_camera_storage_permissions);

    }

    public void setActivity(AppCompatActivity activity, int requestCode, boolean required) {
        this.mActivity = activity;
        this.mContext = activity;
        this.mRequestCode = requestCode;
        if (required) {
            textTV.setText(Html.fromHtml(mActivity.getString(R.string.add_a_photo_required)));
        }
        updatePermissionsUI();
    }

    public void setFragment(Fragment fragment, int requestCode, boolean required) {
        this.mFragment = fragment;
        this.mContext = fragment.getActivity();
        this.mRequestCode = requestCode;
        if (required) {
            textTV.setText(Html.fromHtml(Objects.requireNonNull(mContext).getString(R.string.add_a_photo_required)));
        }
        updatePermissionsUI();
    }

    public void setText(String text) {
        textTV.setText(text);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        updatePermissionsUI();
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
                        return new ImageCompressor.Builder(mContext)
                                .setDestinationDirectoryPath(FileUtil.getAppExternalDirectoryFolder(mContext))
                                .setFileName("image_" + mRequestCode)
                                .build().compressToFile(FileUtil.from(mContext, uris[0]));
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
                        Toast.makeText(mContext, "Image loading Failed", Toast.LENGTH_SHORT).show();
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
                .setTitle(R.string.add_a_photo_from)
                .setItems(new String[]{"Gallery", "Camera"}, (dialogInterface, which) -> {
                    if (which == 0) {
                        if (mActivity != null) {
                            ImagePickerUtils.pickImage(mActivity, mRequestCode);
                        } else if (mFragment != null) {
                            ImagePickerUtils.pickImage(mFragment, mRequestCode);
                        } else {
                            throw new IllegalArgumentException("You should have set an activity or fragment");
                        }
                    } else {
                        Intent cameraIntent = new Intent(mContext, CameraActivity.class);
                        if (mActivity != null) {
                            mActivity.startActivityForResult(cameraIntent, mRequestCode);
                        } else if (mFragment != null) {
                            mFragment.startActivityForResult(cameraIntent, mRequestCode);
                        } else {
                            throw new IllegalArgumentException("You should have set an activity or fragment");
                        }
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

    private void setPermissionsMessage(String message){
        permissionTV.setText(message);
    }

    private void updatePermissionsUI() {
        if (getMissingPermissions().size() > 0) {
            addPhotoLL.setVisibility(GONE);
            photoFL.setVisibility(View.GONE);
            loaderFL.setVisibility(View.GONE);
            permissionsFL.setVisibility(View.VISIBLE);
        } else {
            addPhotoLL.setVisibility(VISIBLE);
            photoFL.setVisibility(View.GONE);
            loaderFL.setVisibility(View.GONE);
            permissionsFL.setVisibility(View.GONE);
        }
    }

    public void onPermissionFLClicked(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> permissions = getMissingPermissions();
            if (mActivity != null){
                mActivity.requestPermissions(permissions.toArray(new String[permissions.size()]), 0);
            } else {
                mFragment.requestPermissions(permissions.toArray(new String[permissions.size()]), 0);
            }

        }
    }

    private List<String> getMissingPermissions() {
        ArrayList<String> permissions = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AppCompatActivity appCompatActivity = mActivity;
            if (appCompatActivity == null){
                appCompatActivity = (AppCompatActivity) mContext;
            }

            if (appCompatActivity.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.CAMERA);
            }

            if (appCompatActivity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

            if (appCompatActivity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
        return permissions;
    }
}
