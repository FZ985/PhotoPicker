package com.material.selection.internal.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.graphics.ColorUtils;
import androidx.core.os.EnvironmentCompat;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Description:
 * Author: jfz
 * Date: 2020-12-22 14:07
 */
public class PickerUtils {
    private static final String SCHEME_CONTENT = "content";

    public static void log(String l) {
        Log.e("material_selection", l);
    }

    public void playVideo(Activity activity, Fragment fragment, Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "video/*");
        try {
            if (fragment != null) {
                fragment.startActivity(intent);
            } else activity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity.getApplicationContext(), "No App found supporting video preview", Toast.LENGTH_SHORT).show();
        }
    }

    public static String getPath(ContentResolver resolver, Uri uri) {
        if (uri == null) {
            return null;
        }

        if (SCHEME_CONTENT.equals(uri.getScheme())) {
            Cursor cursor = null;
            try {
                cursor = resolver.query(uri, new String[]{MediaStore.Images.ImageColumns.DATA},
                        null, null, null);
                if (cursor == null || !cursor.moveToFirst()) {
                    return null;
                }
                return cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return uri.getPath();
    }

    /**
     * 是否是亮色
     *
     * @param color
     * @return 大于0.5位亮色, 否则为暗色
     */
    public static boolean isLightColor(int color) {
        double v = ColorUtils.calculateLuminance(color);
        return (v >= 0.5);
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

    public static boolean hasICS() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static void viewBackgroundAlpha(View view, int alpha) {
        if (alpha != -1) {
            if (alpha > 255) alpha = 255;
            Drawable background = view.getBackground();
            if (background != null) {
                log(background.getClass().getName());
            }
            if (background != null && background instanceof ColorDrawable) {
                int color = ((ColorDrawable) background).getColor();
                view.setBackground(new ColorDrawable(ColorUtils.setAlphaComponent(color, alpha)));
            }
        }
    }

    public static int getStatusHeight(Context context) {
        int statusHeight = dip2px(context, 30);
        try {
            Class clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    public static File createImageFile(boolean isVideo) throws IOException {
        // Create an image file name
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = String.format("JPEG_%s.%s", timeStamp, isVideo ? "mp4" : "jpg");
        File storageDir;
        storageDir = Environment.getExternalStoragePublicDirectory(
                isVideo ? Environment.DIRECTORY_MOVIES : Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) storageDir.mkdirs();

        // Avoid joining path components manually
        File tempFile = new File(storageDir, imageFileName);
        // Handle the situation that user's external storage is not ready
        if (!Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(tempFile))) {
            return null;
        }
        return tempFile;
    }

}