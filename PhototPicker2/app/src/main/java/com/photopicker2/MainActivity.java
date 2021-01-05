package com.photopicker2;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.material.selection.MimeType;
import com.material.selection.Selection;
import com.material.selection.internal.utils.PickerUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void toMaterial(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 100);
        }
    }

    public void toDarkMaterial(View view) {
        Selection.from(this)
                .choose(MimeType.ofAll())
                .imageEngine(new GlideEngine())
                .showSingleMediaType(false)
                .maxSelectable(9)
                .selectionThemeId(R.style.Selection_Dark)
                .capture(true)
                .forResult(100);

    }

    public static int runApp(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent();
        try {
            intent = packageManager.getLaunchIntentForPackage(packageName);
            context.startActivity(intent);
        } catch (Exception e) {
            return -1;
        }
        return 0;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Selection.from(this)
                .choose(MimeType.ofAll())
                .imageEngine(new GlideEngine())
                .showSingleMediaType(false)
                .maxSelectable(9)
                .selectionThemeId(R.style.Selection_Light)
                .capture(false)
                .forResult(100);
    }

    public static boolean jumpSystem(Activity activity, String url) {
        try {
            Uri content_url = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, content_url);
            intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            activity.startActivity(intent);
            return true;
        } catch (Exception e) {
            System.out.println("跳转失败:" + e.getMessage());
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            PickerUtils.log("返回结果");
            ArrayList<String> listExtra = data.getStringArrayListExtra(Selection.EXTRA_RESULT_SELECTION_PATH);
            if (listExtra != null) {
                PickerUtils.log("返回结果111：" + listExtra.toString());
            }
        }
    }

    public static boolean jumpSystem(Activity activity, String url, String pkg, String name) {
        try {
            Uri content_url = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, content_url);
            intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            intent.setClassName(pkg, name);
            activity.startActivity(intent);
            return true;
        } catch (Exception e) {
            System.out.println("跳转失败:" + e.getMessage());
            return false;
        }
    }
}