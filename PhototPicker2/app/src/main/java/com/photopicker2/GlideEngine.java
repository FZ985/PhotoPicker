package com.photopicker2;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.material.selection.engine.ImageEngine;
import com.material.selection.widget.PickerTouchImageView;

/**
 * Description:
 * Author: jfz
 * Date: 2020-12-24 17:16
 */
public class GlideEngine implements ImageEngine {

    @Override
    public void loadFolderImage(Context context, ImageView imageView, Uri uri) {
        Glide.with(context)
                .asBitmap()
                .load(uri)
                .centerCrop()
                .into(imageView);
    }

    @Override
    public void loadImage(Context context, ImageView imageView, Uri uri) {
        Glide.with(context)
                .asBitmap()
                .load(uri)
                .centerCrop()
                .into(imageView);
//        PickerUtils.log("load:" + uri);
    }

    @Override
    public void loadGif(Context context, ImageView imageView, Uri uri) {
        Glide.with(context)
                .asGif()
                .load(uri)
                .centerCrop()
                .into(imageView);
    }

    @Override
    public void loadPreviewImage(Context context, PickerTouchImageView imageView, Uri uri) {
        Glide.with(imageView.getContext())
                .load(uri)
                .error(R.mipmap.ic_launcher_round)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        imageView.setImageDrawable(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    @Override
    public void loadPreviewGif(Context context, PickerTouchImageView imageView, Uri uri) {
        Glide.with(context)
                .asGif()
                .load(uri)
                .centerCrop()
                .into(imageView);
    }
}