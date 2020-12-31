package com.material.selection.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.material.selection.internal.entiy.SelectCheckIns;
import com.material.selection.internal.ui.PreviewFragment;

/**
 * Description:
 * Author: jfz
 * Date: 2020-12-30 14:09
 */
public class PreviewAdapter extends FragmentStatePagerAdapter {
    public PreviewAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_SET_USER_VISIBLE_HINT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return PreviewFragment.instance(SelectCheckIns.getInstance().getPreviewItems().get(position));
    }

    @Override
    public int getCount() {
        return SelectCheckIns.getInstance().getPreviewItems() == null ? 0 : SelectCheckIns.getInstance().getPreviewItems().size();
    }
}