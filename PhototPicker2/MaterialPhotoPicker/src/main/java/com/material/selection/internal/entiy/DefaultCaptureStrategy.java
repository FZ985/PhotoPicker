package com.material.selection.internal.entiy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.material.selection.Selection;
import com.material.selection.internal.utils.PathUtils;
import com.material.selection.internal.utils.PickerUtils;
import com.material.selection.internal.utils.SingleMediaScanner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Description:默认相机策略
 * Author: jfz
 * Date: 2021-01-05 16:38
 */
public class DefaultCaptureStrategy extends CaptureStrategy {
    public String absPath;
    private final static int REQUEST_CAMERA = 67;

    public DefaultCaptureStrategy() {
        try {
            File file = PickerUtils.createImageFile(isVideo());
            if (file != null) absPath = file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startActivityForResult(Activity activity) {
        if (isVideo()) {
            Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            if (!TextUtils.isEmpty(absPath) && videoIntent.resolveActivity(activity.getPackageManager()) != null) {
                uri = FileProvider.getUriForFile(activity,
                        getAuthority(activity), new File(absPath));
                videoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                videoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                videoIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    List<ResolveInfo> resInfoList = activity.getPackageManager()
                            .queryIntentActivities(videoIntent, PackageManager.MATCH_DEFAULT_ONLY);
                    for (ResolveInfo resolveInfo : resInfoList) {
                        String packageName = resolveInfo.activityInfo.packageName;
                        activity.grantUriPermission(packageName, uri,
                                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                }
                activity.startActivityForResult(videoIntent, REQUEST_CAMERA);
            }
        } else {
            Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (!TextUtils.isEmpty(absPath) && imageIntent.resolveActivity(activity.getPackageManager()) != null) {
                uri = FileProvider.getUriForFile(activity,
                        getAuthority(activity), new File(absPath));
                imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                imageIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    List<ResolveInfo> resInfoList = activity.getPackageManager()
                            .queryIntentActivities(imageIntent, PackageManager.MATCH_DEFAULT_ONLY);
                    for (ResolveInfo resolveInfo : resInfoList) {
                        String packageName = resolveInfo.activityInfo.packageName;
                        activity.grantUriPermission(packageName, uri,
                                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                }
                activity.startActivityForResult(imageIntent, REQUEST_CAMERA);
            }
        }
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Intent result = new Intent();
                ArrayList<Uri> selectedUris = new ArrayList<>();
                ArrayList<String> selectedPaths = new ArrayList<>();
                Collection<Item> selected = SelectCheckIns.getInstance().getSelectMaps().values();
                if (selected != null) {
                    for (Item item : selected) {
                        selectedUris.add(item.getContentUri());
                        selectedPaths.add(PathUtils.getPath(activity.getApplicationContext(), item.getContentUri()));
                    }
                }
                selectedUris.add(uri);
                selectedPaths.add(absPath);
                result.putParcelableArrayListExtra(Selection.EXTRA_RESULT_SELECTION, selectedUris);
                result.putStringArrayListExtra(Selection.EXTRA_RESULT_SELECTION_PATH, selectedPaths);
                SelectCheckIns.getInstance().unRegisterCallback();
                activity.setResult(Activity.RESULT_OK, result);
                new SingleMediaScanner(activity.getApplicationContext(), absPath, new SingleMediaScanner.ScanListener() {
                    @Override
                    public void onScanFinish() {
                        PickerUtils.log("scan finish!");
                    }
                });
                activity.finish();
//                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            }
        }
    }

    @Override
    public String getAbsPath(Context context) {
        return absPath;
    }

    @Override
    public boolean isVideo() {
        return false;
    }

    @Override
    public String getAuthority(Context context) {
        return context.getPackageName() + ".fileprovider";
    }
}