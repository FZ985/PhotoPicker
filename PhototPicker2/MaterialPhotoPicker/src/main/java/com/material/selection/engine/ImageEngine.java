package com.material.selection.engine;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.material.selection.internal.entiy.Item;

public interface ImageEngine {

    void loadFolderImage(Context context, ImageView imageView, Item item);

    void loadImage(Context context, ImageView imageView, Item item);

    void loadGif(Context context, ImageView imageView, Item item);

    void loadVideo(Context context, ImageView imageView, Item item);

    void loadPreview(Context context, View preview, Item item);

    View getPreview(Context context, ViewGroup parent);
}