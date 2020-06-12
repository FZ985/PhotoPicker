package jiang.photo.picker.listener.crop;

import android.graphics.Bitmap;

public interface CropCallback extends Callback {
    void onSuccess(Bitmap cropped);
    void onError();
}
