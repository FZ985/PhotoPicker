package com.auto.imageloader;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Description: 图库操作帮助类
 * Author: jfz
 * Date: 2021-01-29 17:37
 */
public class PickerHelper {

    public PickerHelper() {

    }

    public void config(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            PickerManager.getCleanInstance();
            PickerManager.getInstance()
                    .setCapture(obj.optBoolean("capture", false))
                    .setMaxNumber(obj.optInt("maxCount", 1))
                    .setThemeColor(obj.optString("themeColor", PickerManager.defaultColor))
                    .setCheckColor(obj.optString("checkColor", PickerManager.defaultColor))
                    .setSpanCount(Math.min(obj.optInt("spanCount", 3), 6));
        } catch (JSONException e) {

        }
    }

    public boolean hasItem(Item item) {
        return PickerManager.getInstance().getSelectMap().containsKey(item.getId());
    }

    public void addItem(Item item) {
        PickerManager.getInstance().getSelectMap().put(item.getId(), item);
    }

    public void removeItem(Item item) {
        PickerManager.getInstance().getSelectMap().remove(item.getId());
    }

    public List<Item> getList() {
        Collection<Item> values = PickerManager.getInstance().getSelectMap().values();
        List<Item> list = new ArrayList<>();
        for (Item item : values) {
            list.add(item);
        }
        return list;
    }

    public List<String> getListPath(Context context) {
        List<String> paths = new ArrayList<>();
        List<Item> list = getList();
        for (Item item : list) {
            paths.add(PathUtils.getPath(context, item.getContentUri()));
        }
        return paths;
    }

    public PickerManager getConfig() {
        return PickerManager.getInstance();
    }

    public void reset() {
        PickerManager.getCleanInstance();
    }

    public void setCallback(PickerCallback callback) {
        PickerManager.getInstance().setCallback(callback);
    }

    public void setPathData(Context context) {
        List<String> listPath = getListPath(context);
        JSONArray array = new JSONArray();
        for (String p : listPath) {
            array.put(p);
        }
        String json = array.toString();
        context.getSharedPreferences("picker_data", context.MODE_PRIVATE).edit().putString("picker_data", json).commit();
    }

    public void setDatas(Context context, String json) {
        context.getSharedPreferences("picker_data", context.MODE_PRIVATE).edit().putString("picker_data", json).commit();
    }

    public String getDatas(Context context) {
        String data = context.getSharedPreferences("picker_data", context.MODE_PRIVATE).getString("picker_data", "");
        String newData = data;
        context.getSharedPreferences("picker_data", context.MODE_PRIVATE).edit().putString("picker_data", "").commit();
        return newData;
    }

    public PickerCallback getCallback() {
        return PickerManager.getInstance().getCallback();
    }
}