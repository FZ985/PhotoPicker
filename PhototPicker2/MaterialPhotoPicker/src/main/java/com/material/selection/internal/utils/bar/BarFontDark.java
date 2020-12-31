package com.material.selection.internal.utils.bar;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;


/**
 * Create by JFZ
 * date: 2020-06-05 10:09
 **/
public class BarFontDark {

    private static final String MIUI_STATUS_BAR_DARK = "EXTRA_FLAG_STATUS_BAR_DARK_MODE";
    private Activity mActivity;
    private Window mWindow;
    private ViewGroup mDecorView;

    private BarFontDark(Activity activity) {
        mActivity = new WeakReference<>(activity).get();
        mWindow = mActivity.getWindow();
        mDecorView = (ViewGroup) mWindow.getDecorView();
    }

    public static BarFontDark with(@NonNull Activity activity) {
        return new BarFontDark(activity);
    }

    public void init(boolean fontDark) {
        this.statusBarDarkFont = fontDark;
        setBar();
    }

    private void setBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //防止系统栏隐藏时内容区域大小发生变化
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !OS.isEMUI3_1()) {
                //android 6.0以上设置状态栏字体为暗色
                uiFlags = setStatusBarDarkFont(uiFlags);
            }
            //隐藏状态栏或者导航栏
            uiFlags = hideBar(uiFlags);
            mDecorView.setSystemUiVisibility(uiFlags);
        }
        if (OS.isMIUI6Later()) {
            //修改miui状态栏字体颜色
            OS.setMIUIBarDark(mWindow, MIUI_STATUS_BAR_DARK, statusBarDarkFont);
        }
        // 修改Flyme OS状态栏字体颜色
        if (OS.isFlymeOS4Later()) {
            FlymeOSStatusBarFontUtils.setStatusBarDarkIcon(mActivity, statusBarDarkFont);
        }
    }

    private int hideBar(int uiFlags) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            uiFlags |= View.SYSTEM_UI_FLAG_VISIBLE;
        }
        return uiFlags | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
    }

    /**
     * Sets status bar dark font.
     * 设置状态栏字体颜色，android6.0以上
     */
    boolean statusBarDarkFont = false;

    private int setStatusBarDarkFont(int uiFlags) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && statusBarDarkFont) {
            return uiFlags | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        } else {
            return uiFlags;
        }
    }

}
