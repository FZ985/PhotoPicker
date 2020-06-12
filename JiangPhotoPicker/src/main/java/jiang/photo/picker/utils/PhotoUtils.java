package jiang.photo.picker.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.graphics.ColorUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.File;
import java.util.List;
import java.util.Random;

import jiang.photo.picker.helper.FileBean;
import jiang.photo.picker.helper.LocalMediaFolder;

/**
 * Create by JFZ
 * date: 2020-06-05 11:52
 **/
public class PhotoUtils {

    public static boolean checkWhiteColor(int color) {
        return color == Color.WHITE || color == Color.parseColor("#FFFFFF");
    }

    /**
     * @description :获取屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        return getMetrics(context).widthPixels;
    }

    /**
     * @description :获取屏幕高度
     */
    public static int getScreenHeight(Context context) {
        return getMetrics(context).heightPixels;
    }

    /**
     * @description :获取屏幕密度
     */
    public static int getScreenDensity(Context context) {
        float density = getMetrics(context).density;
        return (int) density;
    }

    public static DisplayMetrics getMetrics(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int randomColor() {
        Random random = new Random();
        int ranColor = 0xff000000 | random.nextInt(0x00ffffff);
        return ranColor;
    }

    public static int alphaColor(int color, int alpha) {
        int a = alpha;
        if (a < 0) {
            a = 0;
        }
        if (a > 255) {
            a = 255;
        }
        return ColorUtils.setAlphaComponent(color, a);
    }

    public static int darkColor(int color, float factor) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= (1f - factor);
        color = Color.HSVToColor(hsv);
        return color;
    }

    //判断图片是否存在
    public static boolean checkFile(String path) {
        File file = new File(path);
        return file.exists();
    }

    public static List<LocalMediaFolder> removal(List<LocalMediaFolder> old, List<LocalMediaFolder> directories) {
        if (old == null) return directories;
        for (int i = 0; i < directories.size(); i++) {
            String name = directories.get(i).getName();
            int hasName = hasFolerName(name, old);
            if (hasName != -1 && directories.get(i).getPhotos() != null) {//有此文件夹
                //将老的选中属性赋给新的
                for (int k = 0; k < directories.get(i).getPhotos().size(); k++) {
                    String path = directories.get(i).getPhotos().get(k).getPath();
                    FileBean bean = getOldBean(path, old.get(hasName).getPhotos());
                    if (bean != null) {
                        directories.get(i).getPhotos().get(k).isSelect = bean.isSelect;
                    }
                }
            }
        }
        return directories;
    }

    public static FileBean getOldBean(String path, List<FileBean> oldPhotos) {
        if (oldPhotos != null) {
            for (int i = 0; i < oldPhotos.size(); i++) {
                if (oldPhotos.get(i).getPath().equals(path)) {
                    return oldPhotos.get(i);
                }
            }
        }
        return null;
    }

    //返回老列表的角标
    private static int hasFolerName(String name, List<LocalMediaFolder> old) {
        for (int i = 0; i < old.size(); i++) {
            if (old.get(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }
}
