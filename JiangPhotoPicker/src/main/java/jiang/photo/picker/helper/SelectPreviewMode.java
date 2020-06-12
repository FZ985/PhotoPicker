package jiang.photo.picker.helper;

import android.graphics.Color;

/**
 * Create by JFZ
 * date: 2020-06-10 14:31
 **/
public abstract class SelectPreviewMode {

    public SelectPreviewMode() {
    }

    public abstract int getBackgroundColor();

    public abstract int getThemeColor();

    public abstract int getCheckColor();

    public abstract int getCommitColor();

    public abstract int getThemeAlpha();

    public int getDefaultThemeAlpha() {
        return 240;
    }

    public int getDefaultThemeColor() {
        return Color.parseColor("#333333");
    }

    protected int getDefaultBackgroundColor() {
        return Color.parseColor("#000000");
    }

    protected int getDefaultCheckColor() {
        return Color.parseColor("#09BB07");
    }

    protected int getDefaultCommitColor() {
        return Color.parseColor("#09BB07");
    }
}
