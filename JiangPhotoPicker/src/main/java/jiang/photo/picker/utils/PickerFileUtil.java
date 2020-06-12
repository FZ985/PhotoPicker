package jiang.photo.picker.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Created by JFZ on 2017/6/15 11:16.
 */

public class PickerFileUtil {

    public static final String POSTFIX = ".png";
    public static final String POSTFIX_ = ".png1";
    public static final String PHOTO_PICKER_DIR = "PhotoPicker";
    public static final String CAMERA_PATH = File.separator + "CameraImage" + File.separator;
    public static final String CROP_PATH = File.separator + "CropImage" + File.separator;

    public static boolean fileIsExists(String path) {
        if (path == null || path.trim().length() <= 0) {
            return false;
        }
        try {
            File f = new File(path);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static File createCameraFile(Context context) {
        String path = File.separator + context.getPackageName() + CAMERA_PATH;
        return createMediaFile(context, path, false);
    }

    public static File createCropFile(Context context) {
        String state = Environment.getExternalStorageState();
        File rootDir = state.equals(Environment.MEDIA_MOUNTED) ? context.getExternalFilesDir(PHOTO_PICKER_DIR) : context.getCacheDir();
        File folderDir = new File(rootDir.getAbsolutePath() + CROP_PATH);
        if (!folderDir.exists() && folderDir.mkdirs()) {
        }
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(new Date());
        String fileName = PHOTO_PICKER_DIR + "_" + timeStamp + "";
        return new File(folderDir, fileName + POSTFIX);
    }

    private static File createMediaFile(Context context, String parentPath, boolean is) {
        String state = Environment.getExternalStorageState();
//        File rootDir = state.equals(Environment.MEDIA_MOUNTED) ? context.getExternalFilesDir(PHOTO_PICKER_DIR) : context.getCacheDir();
        File rootDir = state.equals(Environment.MEDIA_MOUNTED) ? Environment
                .getExternalStorageDirectory() : context.getCacheDir();

        File folderDir = new File(rootDir.getAbsolutePath() + parentPath);
        if (!folderDir.exists() && folderDir.mkdirs()) {

        }
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(new Date());
        String fileName = PHOTO_PICKER_DIR + "_" + timeStamp + "";
        File tmpFile = new File(folderDir, fileName + (is ? POSTFIX_ : POSTFIX));
        return tmpFile;
    }

    /**
     * 更新图库
     */
    public static void updatePictrueLib(Context mContext, String path) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        // Uri uri = Uri.fromFile(new File("/sdcard/image.jpg"));
        Uri uri = Uri.fromFile(new File(path));
        intent.setData(uri);
        mContext.sendBroadcast(intent);
    }
}
