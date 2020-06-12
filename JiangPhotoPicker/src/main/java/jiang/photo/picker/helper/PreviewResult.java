package jiang.photo.picker.helper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;

import java.util.ArrayList;

import jiang.photo.picker.activity.PreviewImageActivity;

/**
 * Create by JFZ
 * date: 2020-06-08 14:15
 * 避免intent传输大数据量的限制，创建个临时保存的单例来接收数据
 * ------------------------------------------------------------
 * 预览结果
 **/
public class PreviewResult {

    private ArrayList<String> images;
    private int index;
    //预览界面的标题栏颜色
    private int toolbarColor = Color.parseColor("#333333");
    //预览界面的背景颜色
    private int backgroundColor = Color.BLACK;
    //预览界面的标题栏透明度
    private int alphaToolbar = 255;


    private PreviewResult() {
    }

    public static PreviewResult get() {
        return PreviewResultHolder.image;
    }

    private static class PreviewResultHolder {
        static volatile PreviewResult image = new PreviewResult();
    }

    public PreviewResult images(ArrayList<String> images) {
        if (this.images == null) {
            this.images = new ArrayList<>();
        }
        this.images.clear();
        if (images != null && images.size() > 0) {
            this.images.addAll(images);
        }
        return this;
    }

    public int getToolbarColor() {
        return toolbarColor;
    }

    public PreviewResult toolbarColor(int toolbarColor) {
        this.toolbarColor = toolbarColor;
        return this;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public PreviewResult backgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public int getIndex() {
        return index;
    }

    public PreviewResult index(int index) {
        this.index = index;
        return this;
    }

    public int getAlphaToolbar() {
        return alphaToolbar;
    }

    public PreviewResult alphaToolbar(int alphaToolbar) {
        this.alphaToolbar = alphaToolbar;
        return this;
    }

    public void reset() {
        images = null;
        index = 0;
        alphaToolbar = 255;
        toolbarColor = Color.parseColor("#333333");
        backgroundColor = Color.BLACK;
    }

    public void preview(Activity activity) {
        activity.startActivity(new Intent(activity, PreviewImageActivity.class));
    }
}
