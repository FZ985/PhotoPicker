package jiang.photo.picker.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Create by JFZ
 * date: 2020-06-05 17:38
 **/
public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
    private boolean cWv;
    private int leftSpacing;
    private int rightSpacing;
    private int topSpacing;
    private int bottomSpacing;
    private int spanCount;

    public GridSpacingItemDecoration(int spanCount, int leftSpacing, int rightSpacing, int topSpacing, int bottomSpacing) {
        this.cWv = false;
        this.spanCount = spanCount;
        this.leftSpacing = leftSpacing;
        this.rightSpacing = rightSpacing;
        this.topSpacing = topSpacing;
        this.bottomSpacing = bottomSpacing;
    }

    public GridSpacingItemDecoration(int spancount, int offsets, boolean paramBoolean) {
        this.spanCount = spancount;
        this.leftSpacing = offsets;
        this.rightSpacing = offsets;
        this.topSpacing = offsets;
        this.bottomSpacing = offsets;
        this.cWv = paramBoolean;
    }

    public void getItemOffsets(Rect paramRect, View paramView, RecyclerView paramRecyclerView, RecyclerView.State paramState) {
        int i = paramRecyclerView.getChildAdapterPosition(paramView);
        int j = i % this.spanCount;
        if (this.cWv) {
            paramRect.left = (this.leftSpacing - this.leftSpacing * j / this.spanCount);
            paramRect.right = ((j + 1) * this.rightSpacing / this.spanCount);
            if (i < this.spanCount)
                paramRect.top = this.topSpacing;
            paramRect.bottom = this.bottomSpacing;
            return;
        }
        paramRect.left = (this.leftSpacing * j / this.spanCount);
        paramRect.right = (this.rightSpacing - (j + 1) * this.rightSpacing / this.spanCount);
        if (i < this.spanCount)
            paramRect.top = 0;
        paramRect.bottom = this.bottomSpacing;
    }
}
