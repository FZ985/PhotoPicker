package jiang.photo.picker.utils.statusbar;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.lang.ref.WeakReference;

import jiang.photo.picker.R;

/**
 * Create by JFZ
 * date: 2020-06-05 10:09
 **/
public class PhotoBar {

    private static final String MIUI_STATUS_BAR_DARK = "EXTRA_FLAG_STATUS_BAR_DARK_MODE";
    private Activity mActivity;
    private Window mWindow;
    private ViewGroup mDecorView;

    private PhotoBar(Activity activity) {
        mActivity = new WeakReference<>(activity).get();
        mWindow = mActivity.getWindow();
        mDecorView = (ViewGroup) mWindow.getDecorView();
    }

    public static PhotoBar with(@NonNull Activity activity) {
        return new PhotoBar(activity);
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
                //适配刘海屏
                fitsNotchScreen();
                //初始化5.0以上，包含5.0
                uiFlags = initBarAboveLOLLIPOP(uiFlags);
                //android 6.0以上设置状态栏字体为暗色
                uiFlags = setStatusBarDarkFont(uiFlags);
            } else {
                //初始化5.0以下，4.4以上沉浸式
                initBarBelowLOLLIPOP();
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
     * 初始化android 4.4和emui3.1状态栏和导航栏
     */
    private void initBarBelowLOLLIPOP() {
        //透明状态栏
        mWindow.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //创建一个假的状态栏
        setupStatusBarView();
    }

    /**
     * 设置一个可以自定义颜色的状态栏
     */
    private void setupStatusBarView() {
        View statusBarView = mDecorView.findViewById(R.id.photo_status_bar_view);
        if (statusBarView == null) {
            statusBarView = new View(mActivity);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    OS.getStatusBarHeight(mActivity.getApplicationContext()));
            params.gravity = Gravity.TOP;
            statusBarView.setLayoutParams(params);
            statusBarView.setVisibility(View.VISIBLE);
            statusBarView.setId(R.id.photo_status_bar_view);
            mDecorView.addView(statusBarView);
        }
        statusBarView.setBackgroundColor(ContextCompat.getColor(mActivity.getApplicationContext(), R.color.photo_default_gray));
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

    /**
     * 初始化android 5.0以上状态栏和导航栏
     *
     * @param uiFlags the ui flags
     * @return the int
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private int initBarAboveLOLLIPOP(int uiFlags) {
        //Activity全屏显示，但状态栏不会被隐藏覆盖，状态栏依然可见，Activity顶端布局部分会被状态栏遮住。
        uiFlags |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        mWindow.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //需要设置这个才能设置状态栏和导航栏颜色
        mWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        mWindow.setStatusBarColor(Color.TRANSPARENT);
        return uiFlags;
    }

    /**
     * 适配刘海屏
     * Fits notch screen.
     */
    private boolean mIsFitsNotch;

    private void fitsNotchScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && !mIsFitsNotch) {
            WindowManager.LayoutParams lp = mWindow.getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            mWindow.setAttributes(lp);
            mIsFitsNotch = true;
        }
    }
}
