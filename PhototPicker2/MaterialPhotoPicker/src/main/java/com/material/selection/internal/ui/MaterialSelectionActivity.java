package com.material.selection.internal.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.material.selection.R;
import com.material.selection.Selection;
import com.material.selection.adapter.AlbumAdapter;
import com.material.selection.adapter.SelectionListAdapter;
import com.material.selection.databinding.ActivityMaterialSelectionBinding;
import com.material.selection.internal.entiy.Item;
import com.material.selection.internal.entiy.SelectCheckIns;
import com.material.selection.internal.entiy.SelectionSpec;
import com.material.selection.internal.listener.SelectCheckCallback;
import com.material.selection.internal.listener.SelectionListener;
import com.material.selection.internal.loader.SeceltionMediaCollection;
import com.material.selection.internal.utils.AnimalHelp;
import com.material.selection.internal.utils.MediaGridInset;
import com.material.selection.internal.utils.PathUtils;
import com.material.selection.internal.utils.PickerUtils;
import com.material.selection.internal.utils.bar.BarFontDark;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Description: 图片选择界面
 * Author: jfz
 * Date: 2020-12-21 18:03
 */
public class MaterialSelectionActivity extends BaseSelectionActivity implements SelectCheckCallback, SeceltionMediaCollection.LoaderMediaCallback, View.OnClickListener, SelectionListener {
    private ActivityMaterialSelectionBinding binding;
    private SelectionSpec mSpec;
    private SeceltionMediaCollection mediaCollection = new SeceltionMediaCollection();
    private SelectionListAdapter adapter;
    private AlbumAdapter albumAdapter;
    private String previewString;
    private static final int START_PREVIEW_CODE = 0x111;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mSpec = SelectionSpec.getInstance();
        setTheme(mSpec.themeId_selection);
        super.onCreate(savedInstanceState);
        binding = ActivityMaterialSelectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (mSpec.imageEngine == null) {
            Toast.makeText(this, "ImageEngine must be set ", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (mSpec.needOrientationRestriction()) {
            setRequestedOrientation(mSpec.orientation);
        }
        SelectCheckIns.getInstance().registerCallback(this);
        binding.selectionBack.setOnClickListener(this);
        binding.selectionFolderLl.setOnClickListener(this);
        binding.photoFolderLl.setOnClickListener(this);
        binding.selectionCommit.setOnClickListener(this);
        binding.selectionPreviewTv.setOnClickListener(this);
        adapter = new SelectionListAdapter(this, mSpec.spanCount, binding.selectionToolbar, this);
        GridLayoutManager manager = new GridLayoutManager(this, mSpec.spanCount);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = adapter.getItemViewType(position);
                if (viewType == SelectionListAdapter.FOOTER || viewType == SelectionListAdapter.HEADER) {
                    return mSpec.spanCount;
                }
                return 1;
            }
        });
        binding.selectionRecycle.setHasFixedSize(true);
        binding.selectionRecycle.setLayoutManager(manager);
        int spacing = getResources().getDimensionPixelSize(R.dimen.media_grid_spacing);
        binding.selectionRecycle.addItemDecoration(new MediaGridInset(mSpec.spanCount, spacing, false));
        binding.selectionRecycle.setAdapter(adapter);
        binding.photoFolderlv.setAdapter(albumAdapter = new AlbumAdapter(binding.photoFolderlv, this));
        mediaCollection.onCreate(this);
        mediaCollection.load(this);
        TypedArray typedArray = getTheme().obtainStyledAttributes(new int[]{R.attr.selection_status_isDarkFont, R.attr.selection_bottom_preview_text});
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            BarFontDark.with(this).init(typedArray.getBoolean(0, false));
        previewString = typedArray.getString(1);
        typedArray.recycle();
    }

    @Override
    public void onLoadFinish(List<List<Item>> datas) {
        if (datas != null && datas.size() > 0) {
            if (datas.get(0).size() > 0) {
                binding.selectionFolderLl.setVisibility(View.VISIBLE);
                int lvHeight = Math.min(5, datas.size()) * PickerUtils.dip2px(this, 80);
                binding.photoFolderlv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, lvHeight));
                albumAdapter.bindAlbums(datas);
                selectAlbum(datas.get(albumAdapter.getSelectIndex()));
                changeSatate();
            } else {
                showEmpty();
            }
        }
    }

    private void showEmpty() {
        if (adapter.getItemCount() == 0) {
            binding.selectionFolderLl.setVisibility(View.GONE);
            TypedArray typedArray = getTheme().obtainStyledAttributes(new int[]{R.attr.selection_emptyView});
            int resId = typedArray.getResourceId(0, -1);
            typedArray.recycle();
            if (resId != -1) {
                binding.selectionEmpty.removeAllViews();
                binding.selectionEmpty.addView(LayoutInflater.from(this).inflate(resId, null));
            }
        }
    }

    @Override
    public void onCapture() {

    }

    @Override
    public void selectAlbum(List<Item> albums) {
        adapter.setData(albums);
        binding.selectionFolderTv.setText(albumAdapter.getFolderName());
    }

    @Override
    public void onAlbumAnim() {
        if (isShowFolder()) {
            AnimalHelp.folderAnims(binding.photoFolderLl, binding.photoFolderlv, binding.selectionFolderArrow, View.GONE);
        } else {
            AnimalHelp.folderAnims(binding.photoFolderLl, binding.photoFolderlv, binding.selectionFolderArrow, View.VISIBLE);
        }
    }

    private boolean isShowFolder() {
        return binding.photoFolderLl.getVisibility() == View.VISIBLE;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void changeSatate() {
        int size = SelectCheckIns.getInstance().getSelectNums();
        binding.selectionPreviewTv.setText(previewString + "(" + size + "/" + mSpec.maxSelectable + ")");
        binding.selectionPreviewTv.setEnabled(size > 0);
        binding.selectionCommit.setEnabled(size > 0);
    }

    @Override
    public void onPreview() {
        startActivityForResult(new Intent(this, PreviewActivity.class), START_PREVIEW_CODE);
    }

    @Override
    public void notifyDataChanged() {
        if (adapter != null) adapter.notifyDataSetChanged();
        changeSatate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == START_PREVIEW_CODE && data != null) {
            boolean back = data.getBooleanExtra("back", false);
            if (!back) commit();
        }
    }

    private void commit() {
        Intent result = new Intent();
        ArrayList<Uri> selectedUris = new ArrayList<>();
        ArrayList<String> selectedPaths = new ArrayList<>();
        Collection<Item> selected = SelectCheckIns.getInstance().getSelectMaps().values();
        if (selected != null) {
            for (Item item : selected) {
                selectedUris.add(item.getContentUri());
                selectedPaths.add(PathUtils.getPath(this, item.getContentUri()));
            }
        }
        result.putParcelableArrayListExtra(Selection.EXTRA_RESULT_SELECTION, selectedUris);
        result.putStringArrayListExtra(Selection.EXTRA_RESULT_SELECTION_PATH, selectedPaths);
        SelectCheckIns.getInstance().unRegisterCallback();
        setResult(RESULT_OK, result);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (isShowFolder()) {
            onAlbumAnim();
        } else {
            Intent result = new Intent();
            result.putParcelableArrayListExtra(Selection.EXTRA_RESULT_SELECTION, new ArrayList<>());
            result.putStringArrayListExtra(Selection.EXTRA_RESULT_SELECTION_PATH, new ArrayList<>());
            SelectCheckIns.getInstance().unRegisterCallback();
            setResult(RESULT_CANCELED, result);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        if (vId == R.id.selection_folder_ll || vId == R.id.photo_folder_ll) {
            onAlbumAnim();
        } else if (vId == R.id.selection_commit) {
            commit();
        } else if (vId == R.id.selection_preview_tv) {
            List<Item> items = new ArrayList<>(SelectCheckIns.getInstance().getSelectMaps().values());
            SelectCheckIns.getInstance().setIndex(0).setPreviewItems(items);
            onPreview();
        } else if (vId == R.id.selection_back) {
            onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        mediaCollection.onDestroy();
        super.onDestroy();
        mediaCollection = null;
        adapter = null;
        albumAdapter = null;
    }
}