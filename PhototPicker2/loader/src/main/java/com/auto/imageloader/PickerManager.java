package com.auto.imageloader;

import java.util.LinkedHashMap;

/**
 * Description: 图库管理
 * Author: jfz
 * Date: 2021-01-29 17:33
 */
public class PickerManager {
    private LinkedHashMap<Long, Item> selectMap = new LinkedHashMap<>();
    public static String defaultColor = "#6200EE";
    private boolean isCapture = false;
    private int maxNumber = 1;
    private String themeColor = defaultColor;
    private String checkColor = defaultColor;
    private int spanCount = 3;
    private PickerCallback callback;
    private PickerManager() {
    }

    public static PickerManager getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public static PickerManager getCleanInstance() {
        PickerManager manager = getInstance();
        manager.reset();
        return manager;
    }

    private static final class InstanceHolder {
        private static final PickerManager INSTANCE = new PickerManager();
    }

    public LinkedHashMap<Long, Item> getSelectMap() {
        if (selectMap == null) selectMap = new LinkedHashMap<>();
        return selectMap;
    }

    public boolean isCapture() {
        return isCapture;
    }

    public int getMaxNumber() {
        return maxNumber;
    }

    public String getThemeColor() {
        return themeColor;
    }

    public String getCheckColor() {
        return checkColor;
    }

    public int getSpanCount() {
        return spanCount;
    }

    public PickerManager setCapture(boolean capture) {
        isCapture = capture;
        return this;
    }

    public PickerManager setMaxNumber(int maxNumber) {
        this.maxNumber = maxNumber;
        return this;
    }

    public PickerManager setThemeColor(String themeColor) {
        this.themeColor = themeColor;
        return this;
    }

    public PickerManager setCheckColor(String checkColor) {
        this.checkColor = checkColor;
        return this;
    }

    public PickerManager setSpanCount(int spanCount) {
        if (spanCount <= 0) spanCount = 3;
        this.spanCount = spanCount;
        return this;
    }

    public PickerCallback getCallback() {
        return baseCall;
    }

    public void setCallback(PickerCallback callback) {
        this.callback = callback;
    }

    private void reset() {
        if (selectMap != null) {
            selectMap.clear();
        }
        isCapture = false;
        maxNumber = 1;
        themeColor = defaultColor;
        checkColor = defaultColor;
        spanCount = 3;
    }

    private PickerCallback baseCall = new PickerCallback() {
        @Override
        public void onResult(String datas) {
            if (callback != null){
                baseCall.onResult(datas);
            }
        }
    };
}