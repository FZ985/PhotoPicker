package com.material.selection;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.StyleRes;
import androidx.fragment.app.Fragment;

import com.material.selection.engine.ImageEngine;
import com.material.selection.internal.entiy.CaptureStrategy;
import com.material.selection.internal.entiy.SelectionSpec;
import com.material.selection.internal.listener.OnSelectionUI;
import com.material.selection.internal.ui.MaterialSelectionActivity;

import java.util.Set;

/**
 * Description:
 * Author: jfz
 * Date: 2020-12-22 14:14
 */
public final class SelectionCreator {
    private Selection mSelection;
    private SelectionSpec mSelectionSpec;

    public SelectionCreator(Selection selection, Set<MimeType> mimeTypes) {
        this.mSelection = selection;
        this.mSelectionSpec = SelectionSpec.getCleanInstance();
        this.mSelectionSpec.mimeTypeSet = mimeTypes;
    }

    public SelectionCreator selectionThemeId(@StyleRes int themeId) {
        this.mSelectionSpec.themeId_selection = themeId;
        return this;
    }

    public SelectionCreator spanCount(int spanCount) {
        if (spanCount < 1) spanCount = 1;
        mSelectionSpec.spanCount = spanCount;
        return this;
    }

    public SelectionCreator imageEngine(ImageEngine imageEngine) {
        mSelectionSpec.imageEngine = imageEngine;
        return this;
    }

    public SelectionCreator capture(boolean enable) {
        mSelectionSpec.capture = enable;
        return this;
    }

    public SelectionCreator captureStrategy(CaptureStrategy captureStrategy) {
        mSelectionSpec.captureStrategy = captureStrategy;
        return this;
    }

    public SelectionCreator setOnSelectionUI(OnSelectionUI selectionUI) {
        mSelectionSpec.selectionUI = selectionUI;
        return this;
    }

    /**
     * Maximum selectable count.
     *
     * @param maxSelectable Maximum selectable count. Default value is 1.
     * @return {@link SelectionCreator} for fluent API.
     */
    public SelectionCreator maxSelectable(int maxSelectable) {
        if (maxSelectable < 1) maxSelectable = 1;
        mSelectionSpec.maxSelectable = maxSelectable;
        return this;
    }

    /**
     * Whether to show only one media type if choosing medias are only images or videos.
     * 是否只显示一种媒体类型
     *
     * @param showSingleMediaType whether to show only one media type, either images or videos.
     * @return {@link SelectionCreator} for fluent API.
     * @see SelectionSpec#onlyShowImages()
     * @see SelectionSpec#onlyShowVideos()
     */
    public SelectionCreator showSingleMediaType(boolean showSingleMediaType) {
        mSelectionSpec.showSingleMediaType = showSingleMediaType;
        return this;
    }

    /**
     * Start to select media and wait for result.
     *
     * @param requestCode Identity of the request Activity or Fragment.
     */
    public void forResult(int requestCode) {
        Activity activity = mSelection.getActivity();
        if (activity == null) {
            return;
        }

        Intent intent = new Intent(activity, MaterialSelectionActivity.class);

        Fragment fragment = mSelection.getFragment();
        if (fragment != null) {
            fragment.startActivityForResult(intent, requestCode);
        } else {
            activity.startActivityForResult(intent, requestCode);
        }
    }
}