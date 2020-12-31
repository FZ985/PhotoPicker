package com.material.selection.engine;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.material.selection.widget.PickerTouchImageView;

public interface ImageEngine {

    void loadFolderImage(Context context, ImageView imageView, Uri uri);

    void loadImage(Context context, ImageView imageView, Uri uri);

    void loadGif(Context context, ImageView imageView, Uri uri);

    void loadPreviewImage(Context context, PickerTouchImageView imageView, Uri uri);

    void loadPreviewGif(Context context, PickerTouchImageView imageView, Uri uri);

}