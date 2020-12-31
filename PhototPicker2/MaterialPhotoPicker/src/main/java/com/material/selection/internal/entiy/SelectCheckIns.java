package com.material.selection.internal.entiy;

import android.content.Context;
import android.text.TextUtils;

import com.material.selection.internal.listener.SelectCheckCallback;
import com.material.selection.internal.utils.PathUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Description:
 * Author: jfz
 * Date: 2020-12-29 17:29
 */
public final class SelectCheckIns {

    private LinkedHashMap<Long, Item> selectMaps;
    private List<Item> previewItems;
    private int index = -1;

    private SelectCheckIns() {
        selectMaps = new LinkedHashMap<>();
    }

    private static class SelectCheckInsHolder {
        public static final SelectCheckIns HOLDER = new SelectCheckIns();
    }

    public static SelectCheckIns getInstance() {
        return SelectCheckInsHolder.HOLDER;
    }

    private SelectCheckCallback callback;

    public void registerCallback(SelectCheckCallback callback) {
        this.callback = callback;
    }

    public SelectCheckIns setPreviewItems(List<Item> previewItems) {
        this.previewItems = previewItems;
        return this;
    }

    public List<Item> getPreviewItems() {
        return this.previewItems;
    }

    public int getIndex() {
        return index;
    }

    public void put(Item bean) {
        if (selectMaps != null && !selectMaps.containsKey(bean.id)) {
            selectMaps.put(bean.id, bean);
        }
    }

    public int getSelectNums() {
        return (selectMaps != null) ? selectMaps.size() : 0;
    }

    public void remove(long id) {
        if (selectMaps != null) {
            selectMaps.remove(id);
        }
    }

    public void checkBadId(Context context) {
        //检查选中的是否删除过, 如果删除过,则应该将缓存中的图片删除
        Collection<Item> values = SelectCheckIns.getInstance().getSelectMaps().values();
        List<Long> badId = new ArrayList<>();
        for (Item i : values) {
            String path = PathUtils.getPath(context.getApplicationContext(), i.getContentUri());
            if (TextUtils.isEmpty(path)) {
                badId.add(i.id);
            } else {
                File file = new File(path);
                if (file == null || !file.exists()) {
                    badId.add(i.id);
                }
            }
        }
        for (long l : badId) {
            remove(l);
        }
    }

    public SelectCheckIns setIndex(int index) {
        this.index = index;
        return this;
    }

    public void unRegisterCallback() {
        this.callback = null;
        this.previewItems = null;
        this.index = -1;
        if (selectMaps != null) selectMaps.clear();
    }

    public LinkedHashMap<Long, Item> getSelectMaps() {
        return selectMaps;
    }

    public SelectCheckCallback getCallback() {
        return this.callback;
    }
}