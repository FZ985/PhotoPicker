package com.cropview;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.cropview.callback.crop.LoadCallback;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private CropImageView cropImageView;
    private String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "qq.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cropImageView = findViewById(R.id.cropImageView);
        cropImageView.setCropMode(CropImageView.CropMode.SQUARE_FIT_WIDTH);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }
    }

    public void crop(View view) {
        cropImageView.startLoad(Uri.fromFile(new File(path)), new LoadCallback() {
            @Override
            public void onSuccess() {
                System.out.println("loadSuccess");
            }

            @Override
            public void onError() {
                System.out.println("loadError");
            }
        });
    }
}