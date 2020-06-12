package jiang.photo.picker.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import jiang.photo.picker.Photo;
import jiang.photo.picker.R;
import jiang.photo.picker.listener.crop.CropCallback;
import jiang.photo.picker.listener.crop.LoadCallback;
import jiang.photo.picker.listener.crop.SaveCallback;
import jiang.photo.picker.utils.PhotoUtils;
import jiang.photo.picker.utils.PickerFileUtil;
import jiang.photo.picker.utils.PickerLog;
import jiang.photo.picker.utils.PickerSelectorUtil;
import jiang.photo.picker.utils.statusbar.PhotoBar;
import jiang.photo.picker.widget.CropImageView;
import jiang.photo.picker.widget.PhotoProgressBar;

/**
 * Create by JFZ
 * date: 2020-06-08 11:19
 **/
public class CropImageActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout toolbar, crop_root;
    private RelativeLayout backRl, crop_bottom;
    private ImageView backIv;
    private CropImageView mCropView;
    private PhotoProgressBar progress;
    private TextView crop_title;
    private TextView cancel;
    private TextView commit;

    public static final String KEY_CROP_PATH = "key_crop_path";
    public Uri uri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_activity_crop);
        String path = getIntent().getStringExtra(KEY_CROP_PATH);
        uri = Uri.fromFile(new File(path));
        initView();
        initData();
    }

    private void initView() {
        crop_bottom = findViewById(R.id.crop_bottom);
        crop_root = findViewById(R.id.crop_root);
        crop_title = findViewById(R.id.crop_title);
        toolbar = findViewById(R.id.crop_toolbar);
        backRl = findViewById(R.id.crop_back_rl);
        backIv = findViewById(R.id.crop_back);
        backRl.setOnClickListener(this);
        mCropView = findViewById(R.id.cropImageView);
        progress = findViewById(R.id.crop_progress);
        cancel = findViewById(R.id.crop_cancel);
        commit = findViewById(R.id.crop_commit);
        cancel.setOnClickListener(this);
        commit.setOnClickListener(this);
        toolbar.setBackgroundColor(Photo.with().getCropToolbarColor());
        crop_root.setBackgroundColor(Photo.with().getCropBackgroundColor());
        crop_bottom.setBackgroundColor(Photo.with().getCropBottomColor());
        boolean isWhite = PhotoUtils.checkWhiteColor(Photo.with().getCropToolbarColor());
        boolean isBottomWhite = PhotoUtils.checkWhiteColor(Photo.with().getCropBottomColor());
        boolean isWhiteText = PhotoUtils.checkWhiteColor(Photo.with().getCropButtonColor());
        PhotoBar.with(this).init(isWhite);
        if (isWhite) {
            crop_title.setTextColor(Photo.with().defaultColor());
            PickerSelectorUtil.toViewBackgraound(backRl, 0, Color.TRANSPARENT, PhotoUtils.alphaColor(Color.GRAY, 30));
            backIv.setImageResource(R.drawable.photo_back_black);
            progress.setColor(Photo.with().defaultColor());
        } else {
            crop_title.setTextColor(Color.WHITE);
            PickerSelectorUtil.toViewBackgraound(backRl, 0, Color.TRANSPARENT, PhotoUtils.alphaColor(Color.WHITE, 30));
            backIv.setImageResource(R.drawable.photo_back_white);
            progress.setColor(Photo.with().getCropToolbarColor());
        }
        if (isBottomWhite) {
            if (isWhiteText) {
                PickerSelectorUtil.toTextViewSelector(cancel, Photo.with().defaultColor(), PhotoUtils.alphaColor(Photo.with().defaultColor(), 100));
                PickerSelectorUtil.toTextViewSelector(commit, Photo.with().defaultColor(), PhotoUtils.alphaColor(Photo.with().defaultColor(), 100));
            } else {
                PickerSelectorUtil.toTextViewSelector(cancel, Photo.with().getCropButtonColor(), PhotoUtils.alphaColor(Photo.with().getCropButtonColor(), 100));
                PickerSelectorUtil.toTextViewSelector(commit, Photo.with().getCropButtonColor(), PhotoUtils.alphaColor(Photo.with().getCropButtonColor(), 100));
            }
        } else {
            PickerSelectorUtil.toTextViewSelector(cancel, Photo.with().getCropButtonColor(), PhotoUtils.alphaColor(Photo.with().getCropButtonColor(), 100));
            PickerSelectorUtil.toTextViewSelector(commit, Photo.with().getCropButtonColor(), PhotoUtils.alphaColor(Photo.with().getCropButtonColor(), 100));

        }
    }

    private void initData() {
        int frameColor = Photo.with().getCropFrameColor();
        mCropView.setFrameColor(frameColor);
        mCropView.setHandleColor(frameColor);
        mCropView.setGuideColor(frameColor);
        mCropView.setCropMode(Photo.with().getCropMode());
        mCropView.setOverlayColor(Photo.with().getCropOverlayColor());
        loadImage();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.crop_back_rl || id == R.id.crop_cancel) {
            onBackPressed();
        } else if (id == R.id.crop_commit) {
            cropImage();
        }
    }

    private void loadImage() {
        showProgress();
        mCropView.startLoad(uri, mLoadCallback);
    }

    private void showProgress() {
        progress.setVisibility(View.VISIBLE);
        progress.startAnimation();
    }

    private void dismissProgress() {
        progress.setVisibility(View.GONE);
        progress.stopAnimation();
    }

    private void cropImage() {
        showProgress();
        mCropView.startCrop(createSaveUri(), mCropCallback, mSaveCallback);
    }

    private final LoadCallback mLoadCallback = new LoadCallback() {
        @Override
        public void onSuccess() {
            dismissProgress();
            PickerLog.log("图片加载成功");
        }

        @Override
        public void onError() {
            dismissProgress();
            PickerLog.log("图片加载失败");
        }
    };

    private final CropCallback mCropCallback = new CropCallback() {
        @Override
        public void onSuccess(Bitmap cropped) {
            PickerLog.log("CropCallback_onSuccess");
        }

        @Override
        public void onError() {
            PickerLog.log("CropCallback_onError");
        }
    };

    public static final int CROP_FINISH = 11;
    public static final int CROP_SUCCESS = 12;
    public static final String CROP_TYPE = "crop_type";
    private final SaveCallback mSaveCallback = new SaveCallback() {
        @Override
        public void onSuccess(Uri outputUri) {
            dismissProgress();
            PickerLog.log("mSaveCallback_onSuccess:" + outputUri.getPath());
            Intent intent = new Intent();
            intent.putExtra(CROP_TYPE, CROP_SUCCESS);
            intent.setData(outputUri);
            setResult(RESULT_OK, intent);
            finish();
        }

        @Override
        public void onError() {
            PickerLog.log("mSaveCallback_onError");
            dismissProgress();
            Toast.makeText(CropImageActivity.this, "裁剪失败", Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 创建保存路径
     *
     * @return
     */
    public Uri createSaveUri() {
        File file = PickerFileUtil.createCropFile(this);
        PickerLog.log("createSaveUri:" + file.getPath());
        return Uri.fromFile(file);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(CROP_TYPE, CROP_FINISH);
        setResult(RESULT_OK, intent);
        finish();
    }
}
