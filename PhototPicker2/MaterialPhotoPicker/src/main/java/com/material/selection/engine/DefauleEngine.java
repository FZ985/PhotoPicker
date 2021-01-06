package com.material.selection.engine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.material.selection.R;
import com.material.selection.internal.entiy.CaptureStrategy;
import com.material.selection.internal.entiy.Item;
import com.material.selection.internal.entiy.SelectionSpec;
import com.material.selection.internal.ui.PreviewActivity;
import com.material.selection.widget.TouchImageView;

/**
 * Description:默认引擎只提供默认的预览布局而已,别想太多,我不会给你设置加载框架
 * 此引擎只是实现默认的预览手势缩放界面功能,不想使用默认的缩放,完全可自定义你的预览界面的显示及缩放控件
 * Author: jfz
 * Date: 2021-01-06 14:48
 */
public abstract class DefauleEngine implements ImageEngine {

    @Override
    public void loadPreview(Context context, View preview, Item item) {
        TouchImageView image = preview.findViewById(R.id.preview_touch);
        ImageView video = preview.findViewById(R.id.preview_video);
        if (item != null) {
            if (item.isVideo()) {
                video.setVisibility(View.VISIBLE);
                video.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SelectionSpec.getInstance().captureStrategy != null) {
                            SelectionSpec.getInstance().captureStrategy.playVideo((Activity) context, item.uri);
                        } else {
                            captureStrategy.playVideo((Activity) context, item.uri);
                        }
                    }
                });
            } else video.setVisibility(View.GONE);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (context != null && context instanceof PreviewActivity) {
                        ((PreviewActivity) context).onClick(v);
                    }
                }
            });
            startLoadPreview(context, image, item);
        }
    }

    protected abstract void startLoadPreview(Context context, ImageView preview, Item item);

    @Override
    public View getPreview(Context context, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.preview_default, null);
        parent.addView(view);
        return view;
    }

    private final CaptureStrategy captureStrategy = new CaptureStrategy() {
        @Override
        public void startActivityForResult(Activity activity) {

        }

        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, @Nullable Intent data) {

        }

        @Override
        public String getAbsPath(Context context) {
            return null;
        }

        @Override
        public boolean isVideo() {
            return false;
        }

        @Override
        public String getAuthority(Context context) {
            return null;
        }
    };
}