package com.auto.imageloader.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.auto.imageloader.Item;
import com.auto.imageloader.TouchImageView;

import java.util.List;

/**
 * Description:
 * Author: jfz
 * Date: 2021-02-01 15:33
 */
public class PreviewAdapter extends PagerAdapter {
    private List<Item> datas;
    private Context context;
    private PreviewCallback previewCallback;

    public PreviewAdapter(Context context, List<Item> datas) {
        this.datas = datas;
        this.context = context;
    }

    public void setPreviewCallback(PreviewCallback previewCallback) {
        this.previewCallback = previewCallback;
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        RelativeLayout root = new RelativeLayout(context);
        root.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        TouchImageView touch = new TouchImageView(context);
        touch.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        root.addView(touch);

        ImageView video = new ImageView(context);
        RelativeLayout.LayoutParams videoParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        videoParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        video.setLayoutParams(videoParams);
        root.addView(video);
        previewCallback.call(root, touch, video, position, datas.get(position));
        container.addView(root, new ViewGroup.LayoutParams(-1, -1));
        return root;
    }

    public interface PreviewCallback {
        void call(RelativeLayout root, TouchImageView imageView, ImageView video, int position, Item item);
    }
}