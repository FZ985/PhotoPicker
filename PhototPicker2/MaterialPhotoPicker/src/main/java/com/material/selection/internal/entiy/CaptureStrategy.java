package com.material.selection.internal.entiy;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Description:相机策略
 * Author: jfz
 * Date: 2021-01-05 16:28
 */
public abstract class CaptureStrategy {

    public Uri uri;

    public void start(Activity activity) {
        int max = SelectionSpec.getInstance().maxSelectable;
        if (SelectCheckIns.getInstance().getSelectNums() >= max) {
            Toast.makeText(activity.getApplicationContext(), "You have reached max selectable", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivityForResult(activity);
    }

    public abstract void startActivityForResult(Activity activity);

    public abstract void onActivityResult(Activity activity, int requestCode, int resultCode, @Nullable Intent data);

    public abstract String getAbsPath(Context context);

    public abstract boolean isVideo();

    public abstract String getAuthority(Context context);

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

}