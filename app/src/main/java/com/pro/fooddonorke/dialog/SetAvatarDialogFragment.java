package com.pro.fooddonorke.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.pro.fooddonorke.R;
import com.pro.fooddonorke.interfaces.ImageCaptureListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SetAvatarDialogFragment extends DialogFragment {
  @BindView(R.id.camera_icon)
  ImageView cameraIcon;
  @BindView(R.id.gallery_icon)
  ImageView galleryIcon;

  public static final String TAG = SetAvatarDialogFragment.class.getSimpleName();
  private ImageCaptureListener mCaptureListener;

  ActivityResultLauncher<String> openGallery = registerForActivityResult(new ActivityResultContracts.GetContent(),
          new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri uri) {
              if (uri != null){
                mCaptureListener.getImageUri(uri);
              }
            }
          });

  ActivityResultLauncher<Void> openCamera = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(),
          new ActivityResultCallback<Bitmap>() {
            @Override
            public void onActivityResult(Bitmap bitmap) {
              if (bitmap != null){
                mCaptureListener.getImageBitmap(bitmap);
              }
            }
          });

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.frament_dialog_avatar, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.bind(this, view);
    initializeSelections();
  }

  private void initializeSelections(){
    cameraIcon.setOnClickListener(view -> {
      Log.d(TAG, "Opening camera ...");
      openCamera.launch(null);
    });

    galleryIcon.setOnClickListener(view -> {
      Log.d(TAG, "Opening gallery ...");
      openGallery.launch("image/*");
    });
  }

  @Override
  public void onAttach(@NonNull Context context) {
    try {
      mCaptureListener = (ImageCaptureListener) getActivity();
    } catch (ClassCastException exception){
      Log.e(TAG, "onAttach: ClassCastException", exception.getCause());
    }
    super.onAttach(context);
  }
}
