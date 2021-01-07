package com.photopicker2.impl;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.material.selection.internal.listener.OnSelectionUI;
import com.material.selection.internal.utils.PickerUtils;

/**
 * Description:
 * Author: jfz
 * Date: 2021-01-07 17:59
 */
public class OnSelectionUIImpl implements OnSelectionUI {
    @Override
    public void onUI(LinearLayout toolbarRoot, View statusBar, RelativeLayout toolbar, RelativeLayout bottomBar, RecyclerView recyclerView, LinearLayout folderListRoot, ListView folderList, ImageView back, LinearLayout chooseFoldeRoot, TextView folderTv, ImageView folderArrow) {

    }

    @Override
    public void onLoadFinishUI(LinearLayout toolbarRoot, View statusBar, RelativeLayout toolbar, RelativeLayout bottomBar, RecyclerView recyclerView, LinearLayout folderListRoot, ListView folderList, ImageView back, LinearLayout chooseFoldeRoot, TextView folderTv, ImageView folderArrow) {

    }

    @Override
    public void onChooseFolderUI(LinearLayout toolbarRoot, RelativeLayout toolbar, RelativeLayout bottomBar, LinearLayout folderListRoot, ListView folderList, ImageView back, LinearLayout chooseFoldeRoot) {
        folderList.post(new Runnable() {
            @Override
            public void run() {
                RelativeLayout.LayoutParams backParams = (RelativeLayout.LayoutParams) back.getLayoutParams();
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) folderList.getLayoutParams();
                int lvHeight = params.height;
                int dp70 = PickerUtils.dip2px(toolbar.getContext(), 90);
                RelativeLayout.LayoutParams chooseParams = (RelativeLayout.LayoutParams) chooseFoldeRoot.getLayoutParams();
                int left = backParams.leftMargin + back.getWidth() + chooseParams.leftMargin;
                folderListRoot.setPadding(left, 0, 0, 0);
                PickerUtils.log("chooseParams.width:" + chooseFoldeRoot.getWidth());
                folderList.setLayoutParams(new LinearLayout.LayoutParams(dp70 + chooseFoldeRoot.getWidth(), lvHeight));
            }
        });
    }
}