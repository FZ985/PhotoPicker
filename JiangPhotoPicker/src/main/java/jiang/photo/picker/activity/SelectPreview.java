package jiang.photo.picker.activity;

import android.content.Context;
import android.content.Intent;

import java.util.LinkedHashMap;
import java.util.List;

import jiang.photo.picker.helper.FileBean;

/**
 * Create by JFZ
 * date: 2020-06-10 12:08
 **/
class SelectPreview {
    private List<FileBean> list;
    private int index;
    private LinkedHashMap<String, FileBean> selectMaps;

    public static SelectPreview get() {
        return SelectPreviewHolder.sInstance;
    }

    public List<FileBean> getList() {
        return list;
    }

    public LinkedHashMap<String, FileBean> getSelectMap() {
        return selectMaps;
    }

    public SelectPreview selectMap(LinkedHashMap<String, FileBean> selectMaps) {
        this.selectMaps = selectMaps;
        return this;
    }

    public SelectPreview setList(List<FileBean> list) {
        this.list = list;
        return this;
    }

    public int getIndex() {
        return index;
    }

    public SelectPreview setIndex(int index) {
        this.index = index;
        return this;
    }

    private static class SelectPreviewHolder {
        private static final SelectPreview sInstance = new SelectPreview();
    }

    public void into(Context context) {
        Intent intent = new Intent(context, SelectPreviewActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    public void reset() {
        list = null;
        index = 0;
    }
}
