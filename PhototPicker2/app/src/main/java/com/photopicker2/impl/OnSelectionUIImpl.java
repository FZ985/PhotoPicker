package com.photopicker2.impl;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.material.selection.internal.listener.OnSelectionUI;
import com.material.selection.internal.utils.PickerUtils;
import com.photopicker2.R;

/**
 * Description:
 * Author: jfz
 * Date: 2021-01-07 17:59
 */
public class OnSelectionUIImpl implements OnSelectionUI {
    private boolean isHide;

    public OnSelectionUIImpl(boolean isHide) {
        this.isHide = isHide;
    }

    @Override
    public void onUI(final LinearLayout toolbarRoot, View statusBar, RelativeLayout toolbar, RelativeLayout bottomBar, RecyclerView recyclerView, LinearLayout folderListRoot, ListView folderList, final ImageView back, LinearLayout chooseFoldeRoot, final TextView folderTv, final ImageView folderArrow) {
        onScrolled(0, toolbarRoot, back, folderTv, folderArrow);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                OnSelectionUIImpl.this.onScrolled(dy, toolbarRoot, back, folderTv, folderArrow);
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (isHide) {
                    int visibility = (newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_SETTLING) ? View.GONE : View.VISIBLE;
                    bottomBar.setVisibility(visibility);
                    toolbarRoot.setVisibility(visibility);
                }
            }
        });
    }

    int dy = 0;

    private void onScrolled(int y, LinearLayout toolbarRoot, ImageView back, TextView folderTv, ImageView arrow) {
        dy = dy + y;
        PickerUtils.log("toolbarRootHeight:" + toolbarRoot.getHeight() + ",y:" + y);
        if (dy <= toolbarRoot.getHeight()) {
            folderTv.setTextColor(ContextCompat.getColor(back.getContext(), R.color.white));
            back.setImageResource(R.drawable.ic_back_white);
            arrow.setImageResource(R.drawable.photo_jiantou);
        } else {
            folderTv.setTextColor(ContextCompat.getColor(back.getContext(), R.color.c0d156c));
            back.setImageResource(R.drawable.ic_back_black);
            arrow.setImageResource(R.mipmap.bilibili_arrow);
        }
    }

    @Override
    public void onLoadFinishUI(LinearLayout toolbarRoot, View statusBar, RelativeLayout toolbar, RelativeLayout bottomBar, RecyclerView recyclerView, LinearLayout folderListRoot, ListView folderList, ImageView back, LinearLayout chooseFoldeRoot, TextView folderTv, ImageView folderArrow) {

    }

    @Override
    public void onChooseFolderUI(LinearLayout toolbarRoot, RelativeLayout toolbar, RelativeLayout bottomBar, LinearLayout folderListRoot, ListView folderList, ImageView back, LinearLayout chooseFoldeRoot) {
        folderList.post(new Runnable() {
            @Override
            public void run() {
//                RelativeLayout.LayoutParams backParams = (RelativeLayout.LayoutParams) back.getLayoutParams();
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) folderList.getLayoutParams();
                int lvHeight = params.height;
                int dp70 = PickerUtils.dip2px(toolbar.getContext(), 90);
//                RelativeLayout.LayoutParams chooseParams = (RelativeLayout.LayoutParams) chooseFoldeRoot.getLayoutParams();
//                int left = backParams.leftMargin + back.getWidth() + chooseParams.leftMargin;
//                folderListRoot.setPadding(left, 0, 0, 0);
                int max = Math.max((dp70 + chooseFoldeRoot.getWidth()), PickerUtils.dip2px(toolbar.getContext(), 180));
                folderList.setLayoutParams(new LinearLayout.LayoutParams(max, lvHeight));
            }
        });
    }
}