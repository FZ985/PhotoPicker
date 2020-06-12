package com.photo.picker;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

import jiang.photo.picker.Photo;
import jiang.photo.picker.helper.PreviewResult;
import jiang.photo.picker.helper.SelectPreviewMode;
import jiang.photo.picker.listener.SelectImageListener;
import jiang.photo.picker.utils.PhotoUtils;
import jiang.photo.picker.utils.PhototPermission;
import jiang.photo.picker.widget.CropImageView;

public class MainActivity extends AppCompatActivity {
    Button config;
    Button basic;
    ScrollView configlayout;
    RadioButton t1, t2, t3, f1, f2;
    RadioButton rb1, rb2, rb3, rb4, rb5;
    RadioButton ib1, ib2, yb1, yb2, cob1, cob2, sbg1, sbg2, tbg1, tbg2, scheck2, scommit2, cropb1, cropb2, cropb3, preview1, preview2, preview3;
    RadioGroup group1;
    CheckBox cb1, camera;
    LinearLayout selectll, caropconfig, smorell;
    SeekBar span, seek_alpha, maxselect;
    TextView spantv, moretheme_tv, smore_maxtv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        basic = findViewById(R.id.basic);
        config = findViewById(R.id.config);
        configlayout = findViewById(R.id.configlayout);
        sbg1 = findViewById(R.id.sbg1);
        sbg2 = findViewById(R.id.sbg2);
        selectll = findViewById(R.id.selectll);
        smorell = findViewById(R.id.smorell);
        camera = findViewById(R.id.camera);
        cob1 = findViewById(R.id.cob1);
        cob2 = findViewById(R.id.cob2);
        yb1 = findViewById(R.id.yb1);
        yb2 = findViewById(R.id.yb2);
        ib1 = findViewById(R.id.ib1);
        ib2 = findViewById(R.id.ib2);
        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        t3 = findViewById(R.id.t3);
        f1 = findViewById(R.id.f1);
        f2 = findViewById(R.id.f2);
        rb1 = findViewById(R.id.rb1);
        rb2 = findViewById(R.id.rb2);
        rb3 = findViewById(R.id.rb3);
        rb4 = findViewById(R.id.rb4);
        rb5 = findViewById(R.id.rb5);
        cb1 = findViewById(R.id.cb1);
        tbg1 = findViewById(R.id.tbg1);
        tbg2 = findViewById(R.id.tbg2);
        span = findViewById(R.id.span);
        spantv = findViewById(R.id.spantv);
        cropb1 = findViewById(R.id.cropb1);
        cropb2 = findViewById(R.id.cropb2);
        cropb3 = findViewById(R.id.cropb3);
        caropconfig = findViewById(R.id.caropconfig);
        scheck2 = findViewById(R.id.scheck2);
        group1 = findViewById(R.id.group1);
        scommit2 = findViewById(R.id.scommit2);
        moretheme_tv = findViewById(R.id.moretheme_tv);
        seek_alpha = findViewById(R.id.seek_alpha);
        preview1 = findViewById(R.id.preview1);
        preview2 = findViewById(R.id.preview2);
        preview3 = findViewById(R.id.preview3);
        maxselect = findViewById(R.id.maxselect);
        smore_maxtv = findViewById(R.id.smore_maxtv);
        span.setMax(5);
        spantv.setText("显示列数  (" + span.getProgress() + ")");
        moretheme_tv.setText("多选预览主题色透明度——>" + seek_alpha.getProgress());
        smore_maxtv.setText("最多选择  (" + maxselect.getProgress() + ")张");
        if (!PhototPermission.hasReadWritePermission(this)) {
            PhototPermission.requestReadWrite(this);
        }
        initViewOper();
    }

    private void initViewOper() {
        maxselect.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                smore_maxtv.setText("最多选择  (" + progress + ")张");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        span.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                spantv.setText("显示列数  (" + progress + ")");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seek_alpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                moretheme_tv.setText("多选预览主题色透明度——>" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        group1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectll.setVisibility(View.VISIBLE);
                if (checkedId == R.id.rb1) {
                    cb1.setVisibility(View.VISIBLE);
                    smorell.setVisibility(View.GONE);
                }
                if (checkedId == R.id.rb2) {
                    cb1.setVisibility(View.GONE);
                    caropconfig.setVisibility(View.GONE);
                    smorell.setVisibility(View.VISIBLE);
                }
            }
        });
        cb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    caropconfig.setVisibility(View.VISIBLE);
                } else {
                    caropconfig.setVisibility(View.GONE);
                }
            }
        });
    }

    public void select(final View view) {
        Photo.with().into(this, new SelectImageListener() {
            @Override
            public void onSelectImages(ArrayList<String> images) {
                Photo.with().defaultPreview(MainActivity.this, images);
                view.setVisibility(View.GONE);
                config.setVisibility(View.VISIBLE);
                config.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        basic.setVisibility(View.GONE);
                        config.setVisibility(View.GONE);
                        configlayout.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

    public void goTo(View view) {
        Photo photo = Photo.with();
        //先重置一下
        photo.reset();

        //选择模式
        if (rb1.isChecked()) {
            photo.mode(Photo.SelectMode.SINGLE);
        } else if (rb2.isChecked()) {
            photo.mode(Photo.SelectMode.MULTIPLE);
        }
        //是否相机
        photo.camera(camera.isChecked());
        //显示列数
        photo.spanCount(span.getProgress());

        //标题栏颜色
        if (t1.isChecked()) {
            photo.toolBarColor(photo.defaultColor());
        } else if (t2.isChecked()) {
            photo.toolBarColor(Color.WHITE);
        } else if (t3.isChecked()) {
            photo.toolBarColor(PhotoUtils.randomColor());
        }

        //文件夹选中色
        if (f1.isChecked()) {
            photo.folderSelectColor(photo.defaultGreenColor());
        } else if (f2.isChecked()) {
            photo.folderSelectColor(PhotoUtils.randomColor());
        }

        //多选配置
        if (smorell.getVisibility() == View.VISIBLE) {
            //做多选择几张
            photo.maxSelectNums(maxselect.getProgress());

            //图片选中对勾颜色
            if (ib1.isChecked()) {
                photo.imageSelectColor(photo.defaultGreenColor());
            } else if (ib2.isChecked()) {
                photo.imageSelectColor(PhotoUtils.randomColor());
            }
            //预览文字颜色
            if (yb1.isChecked()) {
                photo.previewColor(photo.defaultGreenColor());
            } else if (yb2.isChecked()) {
                photo.previewColor(PhotoUtils.randomColor());
            }
            //确定按钮颜色
            if (cob1.isChecked()) {
                photo.commitColor(photo.defaultGreenColor());
            } else if (cob2.isChecked()) {
                photo.commitColor(PhotoUtils.randomColor());
            }

            //多选预览配置
            photo.selectPreviewMode(new SelectPreviewMode() {
                @Override
                public int getBackgroundColor() {
                    if (sbg2.isChecked()) {
                        return PhotoUtils.randomColor();
                    }
                    return getDefaultBackgroundColor();
                }

                @Override
                public int getThemeColor() {
                    if (tbg2.isChecked()) {
                        return PhotoUtils.randomColor();
                    }
                    return getDefaultThemeColor();
                }

                @Override
                public int getCheckColor() {
                    if (scheck2.isChecked()) {
                        return PhotoUtils.randomColor();
                    }
                    return getDefaultCheckColor();
                }

                @Override
                public int getCommitColor() {
                    if (scommit2.isChecked()) {
                        return PhotoUtils.randomColor();
                    }
                    return getDefaultCommitColor();
                }

                @Override
                public int getThemeAlpha() {
                    return seek_alpha.getProgress();
                }
            });

        }

        //裁剪配置
        if (cb1.getVisibility() == View.VISIBLE && cb1.isChecked()) {
            photo.crop(true);
            //裁剪模式
            if (cropb1.isChecked()) {
                photo.cropMode(CropImageView.CropMode.CIRCLE);
            } else if (cropb2.isChecked()) {
                photo.cropMode(CropImageView.CropMode.FREE);
            } else if (cropb3.isChecked()) {
                photo.cropMode(CropImageView.CropMode.SQUARE);
            }

            //裁剪界面样式颜色配置
            if (caropconfig.getVisibility() == View.VISIBLE) {
                if (rb3.isChecked()) {
                    photo.resetCrop();
                } else if (rb4.isChecked()) {
                    photo.cropToolbarColor(Color.WHITE)
                            .cropBottomColor(Color.WHITE)
                            .cropButtonColor(Color.DKGRAY)
                            .cropFrameColor(Color.BLACK)
                            .cropOverlayColor(Color.parseColor("#aa333333"))
                            .cropBackgroundColor(Color.parseColor("#f2f2f2"));
                } else if (rb5.isChecked()) {
                    photo.cropToolbarColor(PhotoUtils.randomColor())
                            .cropBottomColor(PhotoUtils.randomColor())
                            .cropButtonColor(PhotoUtils.randomColor())
                            .cropFrameColor(PhotoUtils.randomColor())
                            .cropOverlayColor(PhotoUtils.randomColor())
                            .cropBackgroundColor(PhotoUtils.randomColor());
                }
            }
        }

        photo.into(this, new SelectImageListener() {
            @Override
            public void onSelectImages(ArrayList<String> images) {
                if (preview1.isChecked()) {
                    Photo.with().defaultPreview(MainActivity.this, images);
                } else if (preview2.isChecked()) {
                    //预览界面定制
                    PreviewResult.get()
                            .images(images)
                            .toolbarColor(Color.WHITE)
                            .alphaToolbar(220)
                            .backgroundColor(Color.parseColor("#f2f2f2"))
                            .preview(MainActivity.this);
                } else if (preview3.isChecked()) {
                    PreviewResult.get()
                            .images(images)
                            .toolbarColor(PhotoUtils.randomColor())
                            .alphaToolbar(240)
                            .backgroundColor(PhotoUtils.randomColor())
                            .preview(MainActivity.this);
                } else {
                    Photo.with().defaultPreview(MainActivity.this, images);
                }
            }
        });
    }
}
