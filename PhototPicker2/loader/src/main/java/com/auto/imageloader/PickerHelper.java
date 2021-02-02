package com.auto.imageloader;

import android.content.Context;

import androidx.collection.ArraySet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

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
                    .setSelectionType(obj.optString("selectionType", PickerManager.onlyImage))
                    .setSpanCount(Math.min(obj.optInt("spanCount", 3), 6))
                    .setCameraVideo(obj.optBoolean("isCameraVideo", false));
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

    public enum MimeType {

        // ============== images ==============
        JPEG("image/jpeg", arraySetOf(
                "jpg",
                "jpeg"
        )),
        PNG("image/png", arraySetOf(
                "png"
        )),
        GIF("image/gif", arraySetOf(
                "gif"
        )),
        BMP("image/x-ms-bmp", arraySetOf(
                "bmp"
        )),
        WEBP("image/webp", arraySetOf(
                "webp"
        )),

        // ============== videos ==============
        MPEG("video/mpeg", arraySetOf(
                "mpeg",
                "mpg"
        )),
        MP4("video/mp4", arraySetOf(
                "mp4",
                "m4v"
        )),
        QUICKTIME("video/quicktime", arraySetOf(
                "mov"
        )),
        THREEGPP("video/3gpp", arraySetOf(
                "3gp",
                "3gpp"
        )),
        THREEGPP2("video/3gpp2", arraySetOf(
                "3g2",
                "3gpp2"
        )),
        MKV("video/x-matroska", arraySetOf(
                "mkv"
        )),
        WEBM("video/webm", arraySetOf(
                "webm"
        )),
        TS("video/mp2ts", arraySetOf(
                "ts"
        )),
        AVI("video/avi", arraySetOf(
                "avi"
        ));

        private final String mMimeTypeName;
        private final Set<String> mExtensions;

        MimeType(String mimeTypeName, Set<String> extensions) {
            mMimeTypeName = mimeTypeName;
            mExtensions = extensions;
        }

        private static Set<String> arraySetOf(String... suffixes) {
            return new ArraySet<>(Arrays.asList(suffixes));
        }

        public static boolean isImage(String mimeType) {
            if (mimeType == null) return false;
            return mimeType.startsWith("image");
        }

        public static boolean isVideo(String mimeType) {
            if (mimeType == null) return false;
            return mimeType.startsWith("video");
        }

        public static boolean isGif(String mimeType) {
            if (mimeType == null) return false;
            return mimeType.equals(MimeType.GIF.toString());
        }
    }

}