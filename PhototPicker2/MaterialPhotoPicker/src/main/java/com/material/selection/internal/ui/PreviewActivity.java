package com.material.selection.internal.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.material.selection.R;
import com.material.selection.adapter.PreviewAdapter;
import com.material.selection.databinding.ActivityPreviewBinding;
import com.material.selection.internal.entiy.Item;
import com.material.selection.internal.entiy.SelectCheckIns;
import com.material.selection.internal.entiy.SelectionSpec;
import com.material.selection.internal.utils.PathUtils;
import com.material.selection.internal.utils.PickerUtils;
import com.material.selection.internal.utils.bar.BarFontDark;

import java.io.File;

/**
 * Description:
 * Author: jfz
 * Date: 2020-12-29 18:19
 */
public class PreviewActivity extends BaseSelectionActivity implements View.OnClickListener {
    private ActivityPreviewBinding binding;
    private SelectionSpec mSpec;
    private PreviewAdapter adapter;
    private String commitText;
    private Drawable normalDrawable, selectDrawable;
    private int index;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mSpec = SelectionSpec.getInstance();
        setTheme(mSpec.themeId_selection);
        super.onCreate(savedInstanceState);
        binding = ActivityPreviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (mSpec.needOrientationRestriction()) {
            setRequestedOrientation(mSpec.orientation);
        }
        osConfig();
        init();
    }

    private void init() {
        TypedArray typedArray = getTheme().obtainStyledAttributes(new int[]{R.attr.preview_check_normal, R.attr.preview_check_selected});
        normalDrawable = typedArray.getDrawable(0);
        selectDrawable = typedArray.getDrawable(1);
        typedArray.recycle();
        binding.preivewCommit.setOnClickListener(this);
        binding.previewCheckLl.setOnClickListener(this);
        binding.previewBack.setOnClickListener(this);
        commitText = binding.preivewCommit.getText().toString();
        adapter = new PreviewAdapter(getSupportFragmentManager());
        binding.previewViewpager.setAdapter(adapter);
        binding.previewViewpager.setOffscreenPageLimit(2);
        index = SelectCheckIns.getInstance().getIndex();
        binding.previewViewpager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                selectIndex(position);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.previewViewpager.setCurrentItem(index);
        SelectCheckIns.getInstance().checkBadId(this);
        selectIndex(index);
        selectState();
    }

    @Override
    protected void onPause() {
        super.onPause();
        index = binding.previewViewpager.getCurrentItem();
    }

    @SuppressLint("SetTextI18n")
    private void selectIndex(int index) {
        binding.previewNums.setText((index + 1) + "/" + adapter.getCount());
        Item item = SelectCheckIns.getInstance().getPreviewItems().get(index);
        boolean isHas = SelectCheckIns.getInstance().getSelectMaps().containsKey(item.id);
        binding.previewCheckiv.setImageDrawable(isHas ? selectDrawable : normalDrawable);
    }

    private void selectState() {
        int selectNums = SelectCheckIns.getInstance().getSelectNums();
        binding.preivewCommit.setEnabled(selectNums > 0);
        binding.preivewCommit.setText((selectNums > 0) ? (commitText + "(" + selectNums + "/" + mSpec.maxSelectable + ")") : commitText);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.preview_touch) {
            if (binding.previewToolbarRoot.getVisibility() == View.VISIBLE) {
                // 隐藏状态栏
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }
                binding.previewToolbarRoot.setVisibility(View.GONE);
                binding.previewBottomRl.setVisibility(View.GONE);
            } else {
                // 显示状态栏
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }
                binding.previewToolbarRoot.setVisibility(View.VISIBLE);
                binding.previewBottomRl.setVisibility(View.VISIBLE);
            }
        } else if (id == R.id.preview_check_ll) {
            int selectNums = SelectCheckIns.getInstance().getSelectNums();
            Item item = SelectCheckIns.getInstance().getPreviewItems().get(binding.previewViewpager.getCurrentItem());
            boolean isHas = SelectCheckIns.getInstance().getSelectMaps().containsKey(item.id);
            if (isHas) {
                SelectCheckIns.getInstance().remove(item.id);
            } else {
                if (selectNums >= mSpec.maxSelectable) {
                    Toast.makeText(v.getContext(), "You have reached max selectable", Toast.LENGTH_SHORT).show();
                    return;
                }
                String path = PathUtils.getPath(this, item.getContentUri());
                if (TextUtils.isEmpty(path)) {
                    Toast.makeText(this, "Resource damage", Toast.LENGTH_SHORT).show();
                    return;
                }
                File file = new File(path);
                if (!file.exists()) {
                    Toast.makeText(this, "Resource damage", Toast.LENGTH_SHORT).show();
                    return;
                }
                SelectCheckIns.getInstance().put(item);
            }
            selectIndex(binding.previewViewpager.getCurrentItem());
            selectState();
            if (SelectCheckIns.getInstance().getCallback() != null) {
                SelectCheckIns.getInstance().getCallback().notifyDataChanged();
            }
        } else if (id == R.id.preivew_commit) {
            setResult(RESULT_OK, getIntent());
            finish();
        } else if (id == R.id.preview_back) {
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("back", true);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void osConfig() {
        binding.previewStatus.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, PickerUtils.getStatusHeight(this)));
        TypedArray typedArray = getTheme().obtainStyledAttributes(new int[]{R.attr.preview_status_isDarkFont, R.attr.preview_navigationBarColor,
                R.attr.preview_titleBarColorAlpha, R.attr.preview_statusBarColorAlpha,
                R.attr.preview_toolbarBgAlpha, R.attr.preview_bottomBarColorAlpha});
        PickerUtils.viewBackgroundAlpha(binding.previewToolbarRoot, typedArray.getInteger(2, -1));
        PickerUtils.viewBackgroundAlpha(binding.previewStatus, typedArray.getInteger(3, -1));
        PickerUtils.viewBackgroundAlpha(binding.previewToolbar, typedArray.getInteger(4, -1));
        PickerUtils.viewBackgroundAlpha(binding.previewBottomRl, typedArray.getInteger(5, -1));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            BarFontDark.with(this).init(typedArray.getBoolean(0, false));
        int navigationBarColor = typedArray.getColor(1, 0);
        typedArray.recycle();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(navigationBarColor);
        }
    }
}