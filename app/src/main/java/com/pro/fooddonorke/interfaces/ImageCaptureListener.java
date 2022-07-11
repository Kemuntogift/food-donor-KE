package com.pro.fooddonorke.interfaces;

import android.graphics.Bitmap;
import android.net.Uri;

public interface ImageCaptureListener {
  void getImageUri(Uri uri);
  void getImageBitmap(Bitmap bitmap);
}
