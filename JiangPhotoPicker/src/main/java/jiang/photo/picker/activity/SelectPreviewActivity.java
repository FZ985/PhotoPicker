package jiang.photo.picker.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import jiang.photo.picker.Photo;
import jiang.photo.picker.R;
import jiang.photo.picker.adapter.PreviewFragmentAdapter;
import jiang.photo.picker.helper.FileBean;
import jiang.photo.picker.helper.SelectPreviewMode;
import jiang.photo.picker.listener.PreviewCheckChangeListener;
import jiang.photo.picker.utils.PhotoUtils;
import jiang.photo.picker.utils.PickerSelectorUtil;
import jiang.photo.picker.utils.statusbar.PhotoBar;

/**
 * Create by JFZ
 * date: 2020-06-10 11:42
 **/
public class SelectPreviewActivity extends AppCompatActivity implements PreviewCheckChangeListener, View.OnClickListener {
    private ViewPager spreview_pager;
    private LinearLayout spreview_toolbar, spreview_checkll;
    private RelativeLayout spreview_root, spreview_backrl, spreview_bottom;
    private ImageView spreview_back, spreview_checkiv;
    private TextView spreview_nums, spreview_choosetv, spreview_commit;
    private PreviewFragmentAdapter adapter;
    private List<FileBean> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_activity_select_preview);
        initView();
        config();
        initData();
    }

    SelectPreviewMode mode;
    boolean fontDark;

    private void config() {
        mode = Photo.with().getSelectPreviewMode();
        if (mode != null) {
            fontDark = PhotoUtils.checkWhiteColor(mode.getThemeColor());
            if (fontDark) {
                PickerSelectorUtil.toViewBackgraound(spreview_backrl, 0, Color.TRANSPARENT, PhotoUtils.alphaColor(Color.GRAY, 30));
                spreview_back.setColorFilter(mode.getDefaultThemeColor());
                spreview_nums.setTextColor(defaultMode.getDefaultThemeColor());
                spreview_choosetv.setTextColor(defaultMode.getDefaultThemeColor());
            } else {
                PickerSelectorUtil.toViewBackgraound(spreview_backrl, 0, Color.TRANSPARENT, Color.parseColor("#20ffffff"));
                spreview_nums.setTextColor(Color.WHITE);
                spreview_choosetv.setTextColor(Color.WHITE);
            }
            spreview_toolbar.setBackgroundColor(PhotoUtils.alphaColor(mode.getThemeColor(), mode.getThemeAlpha()));
            spreview_bottom.setBackgroundColor(PhotoUtils.alphaColor(mode.getThemeColor(), mode.getThemeAlpha()));
            spreview_root.setBackgroundColor(mode.getBackgroundColor());
        } else {
            spreview_nums.setTextColor(Color.WHITE);
            spreview_choosetv.setTextColor(Color.WHITE);
            spreview_toolbar.setBackgroundColor(PhotoUtils.alphaColor(defaultMode.getDefaultThemeColor(), defaultMode.getDefaultThemeAlpha()));
            spreview_bottom.setBackgroundColor(PhotoUtils.alphaColor(defaultMode.getDefaultThemeColor(), defaultMode.getDefaultThemeAlpha()));
            PickerSelectorUtil.toViewBackgraound(spreview_backrl, 0, Color.TRANSPARENT, Color.parseColor("#20ffffff"));
            spreview_root.setBackgroundColor(defaultMode.getBackgroundColor());
            fontDark = false;
        }
        PhotoBar.with(this).init(fontDark);
    }

    private void initView() {
        spreview_pager = findViewById(R.id.spreview_pager);
        spreview_toolbar = findViewById(R.id.spreview_toolbar);
        spreview_backrl = findViewById(R.id.spreview_backrl);
        spreview_back = findViewById(R.id.spreview_back);
        spreview_bottom = findViewById(R.id.spreview_bottom);
        spreview_nums = findViewById(R.id.spreview_nums);
        spreview_root = findViewById(R.id.spreview_root);
        spreview_checkll = findViewById(R.id.spreview_checkll);
        spreview_checkiv = findViewById(R.id.spreview_checkiv);
        spreview_choosetv = findViewById(R.id.spreview_choosetv);
        spreview_commit = findViewById(R.id.spreview_commit);
        spreview_backrl.setOnClickListener(this);
        spreview_checkll.setOnClickListener(this);
        spreview_commit.setOnClickListener(this);
    }

    private void checkState(boolean checked) {
        if (checked) {
            spreview_checkiv.setImageResource(R.drawable.photo_icon_select);
            spreview_checkiv.setColorFilter(mode == null ? defaultMode.getCheckColor() : mode.getCheckColor());
        } else {
            spreview_checkiv.setImageResource(R.drawable.photo_icon_normal);
            spreview_checkiv.setColorFilter(fontDark ? defaultMode.getDefaultThemeColor() : Color.WHITE);
        }
    }

    private LinkedHashMap<String, FileBean> selectMaps;

    private void commitState() {
        if (selectMaps != null) {
            int size = selectMaps.size();
            int max = Photo.with().getMaxSelectNums();
            int radius = PhotoUtils.dip2px(this, 2);
            if (mode != null) {
                if (size > 0) {
                    spreview_commit.setClickable(true);
                    spreview_commit.setEnabled(true);
                    spreview_commit.setText("确定(" + size + "/" + max + ")");
                    spreview_commit.setTextColor(PhotoUtils.checkWhiteColor(mode.getCommitColor()) ? defaultMode.getDefaultThemeColor() : Color.WHITE);
                    PickerSelectorUtil.toViewBackgraound(spreview_commit, radius, mode.getCommitColor(), PhotoUtils.darkColor(mode.getCommitColor(), 0.2f));
                } else {
                    spreview_commit.setClickable(false);
                    spreview_commit.setEnabled(false);
                    spreview_commit.setTextColor(Color.WHITE);
                    PickerSelectorUtil.toViewBackgraound(spreview_commit, radius, Color.parseColor("#4e4e4e"), Color.parseColor("#4e4e4e"));
                    spreview_commit.setText("确定");
                }
            } else {
                if (size > 0) {
                    spreview_commit.setClickable(true);
                    spreview_commit.setEnabled(true);
                    spreview_commit.setTextColor(Color.WHITE);
                    PickerSelectorUtil.toViewBackgraound(spreview_commit, radius, defaultMode.getCommitColor(), PhotoUtils.darkColor(defaultMode.getCommitColor(), 0.2f));
                    spreview_commit.setText("确定(" + size + "/" + max + ")");
                } else {
                    spreview_commit.setTextColor(Color.parseColor("#a1a1a1"));
                    spreview_commit.setClickable(false);
                    spreview_commit.setEnabled(false);
                    PickerSelectorUtil.toViewBackgraound(spreview_commit, radius, Color.parseColor("#4e4e4e"), Color.parseColor("#4e4e4e"));
                    spreview_commit.setText("确定");
                }
            }
        }
    }

    private void initData() {
        list = SelectPreview.get().getList();
        selectMaps = SelectPreview.get().getSelectMap();
        int index = SelectPreview.get().getIndex();
        if (list != null) {
            spreview_nums.setText((index + 1) + "/" + list.size());
            List<SelectPreviewFragment> fragments = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                fragments.add(SelectPreviewFragment.instance(this, list.get(i).getPath()));
                File file = new File(list.get(i).getPath());
                if (list.get(i).isSelect && file.exists() && selectMaps != null) {
                    selectMaps.put(list.get(i).getPath(), list.get(i));
                }
            }
            adapter = new PreviewFragmentAdapter(getSupportFragmentManager(), fragments);
            spreview_pager.setAdapter(adapter);
            spreview_pager.setCurrentItem(index, false);
            spreview_pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    spreview_nums.setText((position + 1) + "/" + list.size());
                    checkState(list.get(position).isSelect);
                }
            });
            checkState(list.get(index).isSelect);
            commitState();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.spreview_backrl) {
            onBackPressed();
        } else if (id == R.id.spreview_checkll) {
            int currentIndex = spreview_pager.getCurrentItem();
            FileBean data = list.get(currentIndex);
            Intent intent = new Intent(SelectImageActivity.action_choose_change);
            intent.putExtra("position", currentIndex);
            intent.putExtra("path", data.getPath());
            if (data.isSelect) {
                list.get(currentIndex).isSelect = false;
                checkState(false);
                removeImage(data);
                intent.putExtra("select", false);
            } else {
                int max = Photo.with().getMaxSelectNums();
                if (selectMaps.size() >= max) {
                    Toast.makeText(v.getContext(), "你最多只能选择" + max + "张照片", Toast.LENGTH_SHORT).show();
                    return;
                }
                File file = new File(data.getPath());
                if (!file.exists()) {
                    Toast.makeText(this, "图片已损坏", Toast.LENGTH_SHORT).show();
                    return;
                }
                list.get(currentIndex).isSelect = true;
                checkState(true);
                putImage(data);
                intent.putExtra("select", true);
            }
            commitState();
            sendBroadcast(intent);
        } else if (id == R.id.spreview_commit) {
            Photo.with().getListener().onSelectImages(getImages());
            sendBroadcast(new Intent(SelectImageActivity.action_commit));
            onBackPressed();
        }
    }

    private void putImage(FileBean bean) {
        if (selectMaps != null && !selectMaps.containsKey(bean.getPath())) {
            selectMaps.put(bean.getPath(), bean);
        }
    }

    private void removeImage(FileBean bean) {
        if (selectMaps != null) {
            selectMaps.remove(bean.getPath());
        }
    }

    private ArrayList<String> getImages() {
        ArrayList<String> list = new ArrayList<>();
        Collection<FileBean> values = selectMaps.values();
        if (values != null) {
            for (FileBean bean : values) {
                list.add(bean.getPath());
            }
        }
        return list;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SelectPreview.get().reset();
        adapter = null;
        list = null;
        selectMaps = null;
    }

    private SelectPreviewMode defaultMode = new SelectPreviewMode() {
        @Override
        public int getBackgroundColor() {
            return getDefaultBackgroundColor();
        }

        @Override
        public int getThemeColor() {
            return getDefaultThemeColor();
        }

        @Override
        public int getCheckColor() {
            return getDefaultCheckColor();
        }

        @Override
        public int getCommitColor() {
            return getDefaultCommitColor();
        }

        @Override
        public int getThemeAlpha() {
            return getDefaultThemeAlpha();
        }
    };

    private boolean show = true;
    @Override
    public void onImageClick() {
        if (show) {
            show = false;
            // 隐藏状态栏
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
            spreview_toolbar.setVisibility(View.GONE);
            spreview_bottom.setVisibility(View.GONE);
        } else {
            show = true;
            // 显示状态栏
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
            spreview_toolbar.setVisibility(View.VISIBLE);
            spreview_bottom.setVisibility(View.VISIBLE);
        }
    }
}
