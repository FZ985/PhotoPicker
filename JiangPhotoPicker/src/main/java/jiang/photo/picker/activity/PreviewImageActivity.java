package jiang.photo.picker.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import jiang.photo.picker.R;
import jiang.photo.picker.helper.PreviewResult;
import jiang.photo.picker.utils.PhotoUtils;
import jiang.photo.picker.utils.PickerSelectorUtil;
import jiang.photo.picker.utils.statusbar.PhotoBar;
import jiang.photo.picker.widget.PickerTouchImageView;


/**
 * Created by JFZ on 2017/6/19 17:24.
 */

public class PreviewImageActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout root, backRl;
    private ViewPager pager;
    private ImageView back;
    private LinearLayout toolbar;
    private TextView title;
    private ArrayList<String> list;
    private int position;
    private PreviewAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayoutResId());
        initView();
        initData();
    }

    public int setLayoutResId() {
        return R.layout.photo_activity_preview;
    }

    public void initView() {
        list = PreviewResult.get().getImages();
        position = PreviewResult.get().getIndex();
        if (position < 0) position = 0;
        pager = findViewById(R.id.pager);
        root = findViewById(R.id.preview_root);
        toolbar = findViewById(R.id.preview_toobar);
        back = findViewById(R.id.preview_back);
        title = findViewById(R.id.preview_title);
        backRl = findViewById(R.id.preview_backrl);
        backRl.setOnClickListener(this);
    }

    public void initData() {
        int toolBarColor = PreviewResult.get().getToolbarColor();
        boolean isWhiteColor = PhotoUtils.checkWhiteColor(toolBarColor);
        PhotoBar.with(this).init(isWhiteColor);
        int alpha = PreviewResult.get().getAlphaToolbar();
        root.setBackgroundColor(PreviewResult.get().getBackgroundColor());
        toolbar.setBackgroundColor(PhotoUtils.alphaColor(toolBarColor, alpha));
        if (isWhiteColor) {
            PickerSelectorUtil.toViewBackgraound(backRl, 0, Color.TRANSPARENT, Color.parseColor("#f3f3f3"));
            back.setColorFilter(Color.parseColor("#333333"));
            title.setTextColor(Color.parseColor("#333333"));
        } else {
            PickerSelectorUtil.toViewBackgraound(backRl, 0, Color.TRANSPARENT, Color.parseColor("#20ffffff"));
            title.setTextColor(Color.WHITE);
        }

        if (list != null && list.size() > 0) {
            if (position >= list.size()) position = 0;
            if (list.size() > 1) {
                title.setText("(" + (position + 1) + "/" + list.size() + ")");
            } else {
                title.setText("预览");
            }
            adapter = new PreviewAdapter(this);
            pager.setAdapter(adapter);
            pager.setCurrentItem(position);
            pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    title.setText("(" + (position + 1) + "/" + list.size() + ")");
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

    private boolean show = true;

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.preview_backrl) {
            onBackPressed();
        } else if (id == R.id.touch) {
            if (show) {
                show = false;
                // 隐藏状态栏
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }
                toolbar.setVisibility(View.GONE);
            } else {
                show = true;
                // 显示状态栏
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }
                toolbar.setVisibility(View.VISIBLE);
            }
        }
    }

    class PreviewAdapter extends PagerAdapter {
        View.OnClickListener mListener;

        PreviewAdapter(View.OnClickListener listener) {
            this.mListener = listener;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        public Object instantiateItem(ViewGroup container, int position) {
            View contentView = LayoutInflater.from(container.getContext()).inflate(R.layout.photo_item_preview, null);
            final PickerTouchImageView photo = (PickerTouchImageView) contentView.findViewById(R.id.touch);
            String url = list.get(position);
            Glide.with(photo.getContext()).load(url).placeholder(R.drawable.photo_default).thumbnail(0.3f).error(R.drawable.photo_default).into(photo);
            photo.setOnClickListener(mListener);
            container.addView(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return contentView;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        list = null;
        adapter = null;
        PreviewResult.get().reset();
    }
}
