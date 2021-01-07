package com.material.selection.internal.listener;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 返回列表界面的部分 UI 控件,可提供ui的样式修改,优先级最高
 */
public interface OnSelectionUI {

    /**
     * @param toolbarRoot     标题栏根布局
     * @param statusBar       状态栏
     * @param toolbar         标题栏
     * @param bottomBar       底部布局
     * @param recyclerView    列表
     * @param folderListRoot  文件夹列表根布局
     * @param folderList      文件夹列表
     * @param back            返回按钮
     * @param chooseFoldeRoot 选中文件夹根布局
     * @param folderTv        选中文件夹名称
     * @param folderArrow     文件夹小箭头
     */
    void onUI(LinearLayout toolbarRoot,
              View statusBar,
              RelativeLayout toolbar,
              RelativeLayout bottomBar,
              RecyclerView recyclerView,
              LinearLayout folderListRoot,
              ListView folderList,
              ImageView back,
              LinearLayout chooseFoldeRoot,
              TextView folderTv,
              ImageView folderArrow);

    void onLoadFinishUI(LinearLayout toolbarRoot,
                        View statusBar,
                        RelativeLayout toolbar,
                        RelativeLayout bottomBar,
                        RecyclerView recyclerView,
                        LinearLayout folderListRoot,
                        ListView folderList,
                        ImageView back,
                        LinearLayout chooseFoldeRoot,
                        TextView folderTv,
                        ImageView folderArrow);

    void onChooseFolderUI(LinearLayout toolbarRoot, RelativeLayout toolbar,
                          RelativeLayout bottomBar, LinearLayout folderListRoot,
                          ListView folderList,
                          ImageView back,
                          LinearLayout chooseFoldeRoot);
}