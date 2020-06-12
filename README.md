# PhotoPicker
图片选择器

单选、多选
是否启用相机
是否裁剪
可设置图片显示列数
可设置列表页面的样式
可设置最多选择数量
可设置多选预览页面的样式
可设置裁剪页面的样式

无任何设置使用：
  
  
       Photo.with().into(this, new SelectImageListener() {
            @Override
            public void onSelectImages(ArrayList<String> images) {
               //返回结果  images
  
                //预览结果
               Photo.with().defaultPreview(MainActivity.this, images);
               
            }
        });
        
更多配置可参考MainActivity
例：
        
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


