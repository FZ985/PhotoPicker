package com.material.selection.internal.entiy;

import android.content.pm.ActivityInfo;
import android.os.Build;

import androidx.annotation.StyleRes;

import com.material.selection.MimeType;
import com.material.selection.R;
import com.material.selection.engine.ImageEngine;
import com.material.selection.internal.listener.OnSelectionUI;

import java.util.Set;

/**
 * Description:
 * Author: jfz
 * Date: 2020-12-22 14:15
 */
public final class SelectionSpec {
    public Set<MimeType> mimeTypeSet;
    /**
     * 选择界面主题
     */
    @StyleRes
    public int themeId_selection;
    public boolean showSingleMediaType;
    public int spanCount;
    public ImageEngine imageEngine;
    public boolean capture;
    public int maxSelectable;
    public int orientation;
    public CaptureStrategy captureStrategy;
    public OnSelectionUI selectionUI;
    private SelectionSpec() {
    }

    public static SelectionSpec getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public static SelectionSpec getCleanInstance() {
        SelectionSpec selectionSpec = getInstance();
        selectionSpec.reset();
        return selectionSpec;
    }

    private void reset() {
        mimeTypeSet = null;
        imageEngine = null;
        showSingleMediaType = false;
        capture = false;
        spanCount = 3;
        maxSelectable = 1;
        captureStrategy = null;
        selectionUI = null;
        orientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
        themeId_selection = R.style.Material_Selection_Base;
    }

    public boolean needOrientationRestriction() {
        return orientation != ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED && (Build.VERSION.SDK_INT != Build.VERSION_CODES.O);
    }

    public boolean onlyShowImages() {
        return showSingleMediaType && MimeType.ofImage().containsAll(mimeTypeSet);
    }

    public boolean onlyShowVideos() {
        return showSingleMediaType && MimeType.ofVideo().containsAll(mimeTypeSet);
    }

    public boolean onlyShowGif() {
        return showSingleMediaType && MimeType.ofGif().equals(mimeTypeSet);
    }

    private static final class InstanceHolder {
        private static final SelectionSpec INSTANCE = new SelectionSpec();
    }
}