package com.photopicker2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.material.selection.MimeType;
import com.material.selection.Selection;
import com.material.selection.engine.ImageEngine;
import com.material.selection.internal.entiy.Item;
import com.material.selection.internal.listener.OnSelectionUI;
import com.material.selection.internal.utils.PickerUtils;
import com.photopicker2.databinding.ActivityMainBinding;
import com.photopicker2.glide.GlideEngine;
import com.photopicker2.impl.OnSelectionUIImpl;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Set;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private final int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA}, 100);
        }
        binding.chooseMoreRg.setOnCheckedChangeListener((group, checkedId) -> {
            binding.seekNums.setVisibility(checkedId == R.id.rb_more ? View.VISIBLE : View.GONE);
        });
    }

    public void open(View view) {
        OnSelectionUI selectionUI = null;
        ImageEngine engine = new GlideEngine();
        int themeId = R.style.Material_Selection_Base;
        if (binding.themeDef.isChecked())
            themeId = R.style.Material_Selection_Base;
        if (binding.themeLight.isChecked()) themeId = R.style.Selection_Light;
        if (binding.themeDark.isChecked()) themeId = R.style.Selection_Dark;
        if (binding.themeCustom.isChecked()) themeId = R.style.Custom_PickerStyle;
        if (binding.themeCustom2.isChecked()) themeId = R.style.Custom_PickerStyle2;
        if (binding.themeCustom3.isChecked() || binding.themeCustom4.isChecked()) {
            themeId = R.style.Custom_PickerStyle3;
            selectionUI = new OnSelectionUIImpl(binding.themeCustom4.isChecked());
            engine = new GlideEngine() {
                @Override
                public void loadFolderImage(Context context, ImageView imageView, Item item) {
                    Glide.with(imageView.getContext())
                            .asBitmap()
                            .load(item.getContentUri())
                            .transform(new CenterCrop(), new RoundedCorners(PickerUtils.dip2px(context, 5)))
                            .into(imageView);
                }
            };
        }

        boolean isSingle = false;
        Set<MimeType> mimeTypes = MimeType.ofAll();
        if (binding.choose1.isChecked()) mimeTypes = MimeType.ofAll();
        if (binding.choose2.isChecked()) {
            mimeTypes = MimeType.ofImage();
            isSingle = true;
        }
        if (binding.choose3.isChecked()) {
            isSingle = true;
            mimeTypes = EnumSet.of(MimeType.JPEG, MimeType.PNG, MimeType.BMP, MimeType.WEBP);
        }
        if (binding.choose4.isChecked()) {
            isSingle = true;
            mimeTypes = MimeType.ofGif();
        }
        if (binding.choose5.isChecked()) {
            isSingle = true;
            mimeTypes = MimeType.ofVideo();
        }

        Selection.from(this)
                .choose(mimeTypes)
                .showSingleMediaType(isSingle)
                .imageEngine(engine)
                .capture(binding.cameraCb.isChecked())
                .selectionThemeId(themeId)
                .setOnSelectionUI(selectionUI)
                .spanCount(binding.seekSpan.getProgress())
                .maxSelectable(binding.rbSingle.isChecked() ? 1 : binding.seekNums.getProgress())
                .forResult(REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null) {
            PickerUtils.log("返回结果");
            ArrayList<String> listExtra = data.getStringArrayListExtra(Selection.EXTRA_RESULT_SELECTION_PATH);
            if (listExtra != null && listExtra.size() > 0) {
                PickerUtils.log("返回结果111：" + listExtra.toString());
                StringBuilder sb = new StringBuilder();
                for (String path : listExtra) {
                    sb.append(path).append("\n\n");
                }
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("结果").setMessage(sb).show();

            }
        }
    }

}