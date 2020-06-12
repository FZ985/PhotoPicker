package jiang.photo.picker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import java.util.ArrayList;

import jiang.photo.picker.activity.PreviewImageActivity;
import jiang.photo.picker.activity.SelectImageActivity;
import jiang.photo.picker.helper.PreviewResult;
import jiang.photo.picker.helper.SelectPreviewMode;
import jiang.photo.picker.listener.SelectImageListener;
import jiang.photo.picker.widget.CropImageView;

/**
 * Create by JFZ
 * date: 2020-06-05 11:22
 **/
public class Photo {

    private SelectImageListener listener;
    private SelectImageListener baseSelectLisntenr = new SelectImageListener() {
        @Override
        public void onSelectImages(ArrayList<String> images) {
            if (listener != null) {
                listener.onSelectImages(images);
            }
        }
    };

    /**
     * 展示列数，默认 3列； max ：5
     */
    private int spanCount = 3;

    /**
     * 是否使用相机
     */
    private boolean isCamera = false;
    /**
     * title 背景色
     */
    private int toolBarColor = defaultColor();

    /**
     * 单选还是多选
     */
    public enum SelectMode {
        SINGLE, MULTIPLE
    }

    private SelectMode mode = SelectMode.SINGLE;

    /**
     * 是否裁剪
     */
    private boolean isCrop = false;
    /**
     * 裁剪模式
     */
    private CropImageView.CropMode crop_mode = CropImageView.CropMode.CIRCLE;
    /**
     * 裁剪框颜色
     */
    private int cropFrameColor = Color.WHITE;
    /**
     * 裁剪toolbar
     */
    private int cropToolbarColor = defaultColor();
    /**
     * 裁剪背景
     */
    private int cropBackgroundColor = Color.parseColor("#1C1C1C");
    /**
     * 按钮颜色
     */
    private int cropButtonColor = Color.WHITE;
    /**
     * 裁剪框crop_overlay
     */
    private int cropOverlayColor = Color.parseColor("#AA1C1C1C");
    private int cropBottomColor = defaultColor();
    /**
     * 文件夹选中颜色
     */
    private int folderSelectColor = defaultGreenColor();
    private int imageSelectColor = defaultGreenColor();

    /**
     * 最多选择图片数量
     */
    private int maxSelectNums = 9;
    /**
     * 预览文字颜色
     */
    private int previewColor = defaultGreenColor();
    /**
     * 确定按钮颜色
     */
    private int commitColor = defaultGreenColor();

    /**
     * 图片选择预览的样式配置
     */
    private SelectPreviewMode selectPreviewMode;

    private Photo() {
    }

    public static synchronized Photo with() {
        return PhotoHolder.holder;
    }

    static class PhotoHolder {
        static volatile Photo holder = new Photo();
    }

    public SelectMode getMode() {
        return mode;
    }

    public SelectImageListener getListener() {
        return baseSelectLisntenr;
    }

    public Photo mode(SelectMode mode) {
        this.mode = mode;
        return this;
    }

    public Photo camera(boolean camera) {
        isCamera = camera;
        return this;
    }

    public boolean isCamera() {
        return isCamera;
    }

    public Photo toolBarColor(int toolBarColor) {
        this.toolBarColor = toolBarColor;
        return this;
    }

    public int getCropFrameColor() {
        return cropFrameColor;
    }

    public Photo cropFrameColor(int cropFrameColor) {
        this.cropFrameColor = cropFrameColor;
        return this;
    }

    public int getToolBarColor() {
        return toolBarColor;
    }

    public Photo spanCount(int spanCount) {
        this.spanCount = spanCount;
        if (spanCount < 3) {
            this.spanCount = 3;
        }
        if (spanCount > 5) {
            this.spanCount = 5;
        }
        return this;
    }

    public int getSpanCount() {
        return spanCount;
    }

    public boolean isCrop() {
        return isCrop;
    }

    public Photo crop(boolean crop) {
        isCrop = crop;
        return this;
    }

    public CropImageView.CropMode getCropMode() {
        return crop_mode;
    }

    public Photo cropMode(CropImageView.CropMode crop_mode) {
        this.crop_mode = crop_mode;
        return this;
    }

    public void into(Context context) {
        into(context, null);
    }

    public void into(Context context, SelectImageListener listener) {
        if (mode == SelectMode.MULTIPLE && maxSelectNums > 1) {
            crop(false);//多选状态先不进行裁剪
        }
        this.listener = listener;
        Intent intent = new Intent(context, SelectImageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void defaultPreview(Activity activity, ArrayList<String> imgs) {
        defaultPreview(activity, imgs, 0);
    }

    public void defaultPreview(Activity activity, ArrayList<String> imgs, int position) {
        PreviewResult.get().images(imgs).index(position).alphaToolbar(230);
        activity.startActivity(new Intent(activity, PreviewImageActivity.class));
    }

    public int getFolderSelectColor() {
        return folderSelectColor;
    }

    public Photo folderSelectColor(int folderSelectColor) {
        this.folderSelectColor = folderSelectColor;
        return this;
    }

    public int getImageSelectColor() {
        return imageSelectColor;
    }

    public Photo imageSelectColor(int imageSelectColor) {
        this.imageSelectColor = imageSelectColor;
        return this;
    }

    public int getMaxSelectNums() {
        return maxSelectNums;
    }

    public Photo maxSelectNums(int maxSelectNums) {
        this.maxSelectNums = maxSelectNums;
        return this;
    }

    public int getPreviewColor() {
        return previewColor;
    }

    public Photo previewColor(int previewColor) {
        this.previewColor = previewColor;
        return this;
    }

    public int getCommitColor() {
        return commitColor;
    }

    public Photo commitColor(int commitColor) {
        this.commitColor = commitColor;
        return this;
    }

    public SelectPreviewMode getSelectPreviewMode() {
        return selectPreviewMode;
    }

    public Photo selectPreviewMode(SelectPreviewMode selectPreviewMode) {
        this.selectPreviewMode = selectPreviewMode;
        return this;
    }

    public int getCropToolbarColor() {
        return cropToolbarColor;
    }

    public Photo cropToolbarColor(int cropToolbarColor) {
        this.cropToolbarColor = cropToolbarColor;
        return this;
    }

    public int getCropBackgroundColor() {
        return cropBackgroundColor;
    }

    public Photo cropBackgroundColor(int cropBackgroundColor) {
        this.cropBackgroundColor = cropBackgroundColor;
        return this;
    }

    public int getCropButtonColor() {
        return cropButtonColor;
    }

    public Photo cropButtonColor(int cropButtonColor) {
        this.cropButtonColor = cropButtonColor;
        return this;
    }

    public int getCropOverlayColor() {
        return cropOverlayColor;
    }

    public Photo cropOverlayColor(int cropOverlayColor) {
        this.cropOverlayColor = cropOverlayColor;
        return this;
    }

    public int getCropBottomColor() {
        return cropBottomColor;
    }

    public Photo cropBottomColor(int cropBottomColor) {
        this.cropBottomColor = cropBottomColor;
        return this;
    }

    public void reset() {
        this.mode = SelectMode.SINGLE;
        this.isCamera = false;
        this.toolBarColor = defaultColor();
        this.cropFrameColor = Color.WHITE;
        this.spanCount = 3;
        this.isCrop = false;
        this.crop_mode = CropImageView.CropMode.CIRCLE;
        int defColor = defaultGreenColor();
        this.folderSelectColor = defColor;
        this.imageSelectColor = defColor;
        this.previewColor = defColor;
        this.commitColor = defColor;
        this.maxSelectNums = 9;
        this.listener = null;
        this.selectPreviewMode = null;
        resetCrop();
    }

    public void resetCrop(){
        this.cropToolbarColor = defaultColor();
        this.cropBackgroundColor = Color.parseColor("#1C1C1C");
        this.cropButtonColor = Color.WHITE;
        this.cropOverlayColor = Color.parseColor("#AA1C1C1C");
        this.cropBottomColor = defaultColor();
    }

    public int defaultColor() {
        return Color.parseColor("#333333");
    }

    public int defaultGreenColor() {
        return Color.parseColor("#09BB07");
    }
}
