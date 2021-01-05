package com.photopicker2;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.material.selection.engine.ImageEngine;
import com.material.selection.internal.utils.PathUtils;
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
                .load(uri)
                .asBitmap()
                .thumbnail(0.3f)
                .centerCrop()
                .into(imageView);
    }

    @Override
    public void loadImage(Context context, ImageView imageView, Uri uri) {
        Glide.with(context)
                .load(uri)
                .asBitmap()
                .thumbnail(0.3f)
                .centerCrop()
                .into(imageView);
//        PickerUtils.log("load:" + uri);
    }

    @Override
    public void loadGif(Context context, ImageView imageView, Uri uri) {
        Glide.with(context)
                .load(uri)
                .asGif()
                .thumbnail(0.3f)
                .centerCrop()
                .into(imageView);
    }

    @Override
    public void loadPreviewImage(Context context, PickerTouchImageView imageView, Uri uri) {
//        Glide.with(imageView.getContext())
//                .asBitmap()
//                .load(uri)
//                .error(R.mipmap.ic_launcher_round)
//                .thumbnail(0.1f)
//                .into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                        imageView.setImageBitmap(resource);
//                    }
//                });

        Glide.with(imageView.getContext())
                .load(PathUtils.getPath(context, uri))
                .thumbnail(0.3f)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        imageView.setImageDrawable(resource);
                    }
                });
    }

    @Override
    public void loadPreviewGif(Context context, PickerTouchImageView imageView, Uri uri) {
        Glide.with(context)
                .load(uri)
                .asGif()
                .thumbnail(0.1f)
                .centerCrop()
                .into(imageView);
    }
}