package ke.co.toshngure.androidbasecode.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ke.co.toshngure.androidbasecode.R;
import ke.co.toshngure.camera.ImagePicker;

/**
 * Created by Anthony Ngure on 02/02/2018.
 * Email : anthonyngure25@gmail.com.
 */

public class ImagePickerFragment extends Fragment {

    @BindView(R.id.photoIP)
    ImagePicker photoIP;
    Unbinder unbinder;

    public ImagePickerFragment() {
    }

    public static ImagePickerFragment newInstance() {

        Bundle args = new Bundle();

        ImagePickerFragment fragment = new ImagePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_picker, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        photoIP.setFragment(this, 200, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        photoIP.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        photoIP.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
