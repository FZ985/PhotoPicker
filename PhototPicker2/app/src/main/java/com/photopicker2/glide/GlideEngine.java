package com.photopicker2.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.material.selection.engine.DefauleEngine;
import com.material.selection.internal.entiy.Item;

/**
 * Description:
 * Author: jfz
 * Date: 2020-12-24 17:16
 */
public class GlideEngine extends DefauleEngine {
    private int size;

    @Override
    protected void startLoadPreview(Context context, ImageView preview, Item item) {
        Glide.with(preview.getContext())
                .asDrawable()
                .load(item.getContentUri())
                .override(context.getResources().getDisplayMetrics().widthPixels, context.getResources().getDisplayMetrics().heightPixels)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        preview.setImageDrawable(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    @Override
    public void loadFolderImage(Context context, ImageView imageView, Item item) {
        loadImage(context, imageView, item);
    }

    @Override
    public void loadImage(Context context, ImageView imageView, Item item) {
        if (size == 0) {
            size = context.getResources().getDisplayMetrics().widthPixels / 3;
        }
        Glide.with(imageView.getContext())
                .asDrawable()
                .load(item.getContentUri())
                .thumbnail(0.1f)//图像的 百分之十
                .centerCrop()
                .override(size, size)//屏幕宽度的三分之一大小
                .into(imageView);
    }

    @Override
    public void loadGif(Context context, ImageView imageView, Item item) {
        loadImage(context, imageView, item);
    }

    @Override
    public void loadVideo(Context context, ImageView imageView, Item item) {
        loadImage(context, imageView, item);
    }
}