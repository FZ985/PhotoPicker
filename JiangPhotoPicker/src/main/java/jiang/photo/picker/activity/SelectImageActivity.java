package jiang.photo.picker.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jiang.photo.picker.Photo;
import jiang.photo.picker.R;
import jiang.photo.picker.adapter.FolderAdapter;
import jiang.photo.picker.adapter.ImageListAdapter;
import jiang.photo.picker.anim.AnimalHelp;
import jiang.photo.picker.helper.FileBean;
import jiang.photo.picker.helper.LocalMediaFolder;
import jiang.photo.picker.helper.MediaStoreHelper;
import jiang.photo.picker.listener.SelectImageChangeListener;
import jiang.photo.picker.utils.GridSpacingItemDecoration;
import jiang.photo.picker.utils.PhotoUtils;
import jiang.photo.picker.utils.PhototPermission;
import jiang.photo.picker.utils.PickerFileUtil;
import jiang.photo.picker.utils.PickerLog;
import jiang.photo.picker.utils.PickerSelectorUtil;
import jiang.photo.picker.utils.statusbar.PhotoBar;
import jiang.photo.picker.widget.PhotoProgressBar;

/**
 * Create by JFZ
 * date: 2020-06-05 9:52
 **/
public class SelectImageActivity extends AppCompatActivity implements SelectImageChangeListener, View.OnClickListener {
    private int spacing;//网格间距
    private LinearLayout toolbar;
    private RelativeLayout select_backrl;
    private ImageView select_back;
    private RecyclerView photo_recycle;
    private PhotoProgressBar progressBar;
    private LinearLayout photo_titlell;
    private RelativeLayout photo_bottom_rl;
    private ImageView jiantou;
    private TextView photo_folder;
    private LinearLayout photo_folder_ll;
    private ListView photo_folderlv;
    private ImageView empty;
    private TextView photo_preview;
    private TextView photo_commit;
    public static final int SELECT_CROP_REQUEST = 2767;
    private ImageListAdapter adapter;
    private FolderAdapter folderAdapter;
    private Photo.SelectMode mode = Photo.SelectMode.SINGLE;

    public static final String action_commit = "action_commit";
    public static final String action_choose_change = "action_choose_change";
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            PickerLog.log("onReceive===" + (intent == null));
            if (intent != null && !TextUtils.isEmpty(intent.getAction())) {
                String action = intent.getAction();
                PickerLog.log("=====Action:" + action);
                if (action.equals(action_commit)) {
                    onBackPressed();
                    return;
                } else if (action.equals(action_choose_change)) {
                    boolean isSelect = intent.getBooleanExtra("select", false);
                    String path = intent.getStringExtra("path") + "";
                    PickerLog.log("selectCahnge_select:" + isSelect + ",path:" + path);
                    adapter.selectChange(path, isSelect);
                    onSelectedState(path, isSelect);
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_activity_selectimage);
        spacing = PhotoUtils.dip2px(this, 4);
        mode = Photo.with().getMode();
        initView();
        initData();
        registerReceiver();
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(action_commit);
        filter.addAction(action_choose_change);
        registerReceiver(receiver, filter);
    }


    private void initData() {
        final int spanCount = Photo.with().getSpanCount();
        adapter = new ImageListAdapter(this, spanCount);
        GridLayoutManager manager = new GridLayoutManager(this, spanCount);
        adapter.setImageSelectListener(this);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = adapter.getItemViewType(position);
                if (viewType == ImageListAdapter.FOOTER) {
                    return spanCount;
                }
                return 1;
            }
        });
        photo_recycle.setLayoutManager(manager);
        boolean isWhiteColor = PhotoUtils.checkWhiteColor(Photo.with().getToolBarColor());
        if (isWhiteColor) {
            PickerSelectorUtil.toViewBackgraound(select_backrl, 0, Color.WHITE, PhotoUtils.alphaColor(Color.GRAY, 30));
            select_back.setImageResource(R.drawable.photo_back_black);
            PickerSelectorUtil.toViewBackgraound(photo_titlell,
                    PhotoUtils.dip2px(photo_titlell.getContext(), 15),
                    PhotoUtils.alphaColor(Color.GRAY, 30),
                    PhotoUtils.alphaColor(Color.GRAY, 30));
            photo_folder.setTextColor(Color.GRAY);
            PickerSelectorUtil.toViewColorFilter(jiantou, R.drawable.photo_jiantou, PhotoUtils.alphaColor(Color.GRAY, 100));
        } else {
            PickerSelectorUtil.toViewBackgraound(select_backrl, 0, Photo.with().getToolBarColor(), PhotoUtils.alphaColor(Color.WHITE, 30));
            select_back.setImageResource(R.drawable.photo_back_white);
            PickerSelectorUtil.toViewBackgraound(photo_titlell,
                    PhotoUtils.dip2px(photo_titlell.getContext(), 15),
                    PhotoUtils.alphaColor(Color.WHITE, 60),
                    PhotoUtils.alphaColor(Color.WHITE, 60));
            photo_folder.setTextColor(Color.WHITE);
            PickerSelectorUtil.toViewColorFilter(jiantou, R.drawable.photo_jiantou, Color.WHITE);
        }
        photo_recycle.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, spacing, 0, spacing / 2));
        PhotoBar.with(this).init(isWhiteColor);
        toolbar.setBackgroundColor(Photo.with().getToolBarColor());
        progressBar.setColor(isWhiteColor ? ContextCompat.getColor(this, R.color.photo_default) : Photo.with().getToolBarColor());
        progressBar.startAnimation();
        photo_recycle.setAdapter(adapter);

        findPhoto();
        initViewOper();
    }

    private void initViewOper() {
        photo_folderlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LocalMediaFolder item = folderAdapter.getItem(position);
                empty.setVisibility(View.GONE);
                if (item != null) {
                    folder_index = position;
                    adapter.bindImages(item.getPhotos());
                    folderAdapter.setCheckIndex(position);
                    photo_folder.setText(item.getName());
                    dismissFolder();
                }
            }
        });
    }

    private void initView() {
        empty = findViewById(R.id.photo_empty_iv);
        photo_preview = findViewById(R.id.photo_preview);
        photo_commit = findViewById(R.id.photo_commit);
        select_backrl = findViewById(R.id.select_backrl);
        photo_folder = findViewById(R.id.photo_folder);
        select_back = findViewById(R.id.select_back);
        select_backrl.setOnClickListener(this);
        toolbar = findViewById(R.id.photo_titlebar_ll);
        photo_recycle = findViewById(R.id.photo_recycle);
        progressBar = findViewById(R.id.photo_progress);
        photo_titlell = findViewById(R.id.photo_titlell);
        photo_bottom_rl = findViewById(R.id.photo_bottom_rl);
        jiantou = findViewById(R.id.photo_jiantou);
        photo_folder_ll = findViewById(R.id.photo_folder_ll);
        photo_folderlv = findViewById(R.id.photo_folderlv);
        photo_titlell.setOnClickListener(this);
        photo_folder_ll.setOnClickListener(this);
        photo_commit.setOnClickListener(this);
        photo_preview.setOnClickListener(this);
    }

    /**
     * find all photos
     */
    private int folder_index = 0;//文件夹的选中位置
    private List<LocalMediaFolder> dirs;//一次性缓存，方便去重

    private void findPhoto() {
        folderAdapter = new FolderAdapter(this);
        photo_folderlv.setAdapter(folderAdapter);
        Bundle mediaStoreArgs = new Bundle();
        mediaStoreArgs.putBoolean(MediaStoreHelper.EXTRA_SHOW_GIF, true);
        MediaStoreHelper.getPhotoDirs(SelectImageActivity.this, mediaStoreArgs, new MediaStoreHelper.PhotosResultCallback() {
            @Override
            public void onResultCallback(List<LocalMediaFolder> directories) {
                progressBar.setVisibility(View.GONE);
                if (directories != null && directories.size() > 0 && directories.get(0).getPhotos() != null && directories.get(0).getPhotos().size() > 0) {
                    SelectImageActivity.this.dirs = PhotoUtils.removal(dirs, directories);
                    folderAdapter.bindFolder(dirs);
                    empty.setVisibility(View.GONE);
                    if (folder_index >= dirs.size()) {
                        folder_index = 0;
                    }
                    //listview高度
                    int lvHeight = Math.min(5, dirs.size()) * PhotoUtils.dip2px(SelectImageActivity.this, 80);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, lvHeight);
                    photo_folderlv.setLayoutParams(params);
                    folderAdapter.setCheckIndex(folder_index);
                    photo_folder.setText(dirs.get(folder_index).getName());
                    adapter.bindImages(dirs.get(folder_index).getPhotos());
                } else {
                    empty.setVisibility((folderAdapter != null && folderAdapter.getCount() > 0) ? View.GONE : View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onCamera() {
        if (PhototPermission.hasCameraPermission(this)) {
            //打开相机
            startCamera();
        } else {
            PhototPermission.requestCamera(this);
        }
    }

    /**
     * start to camera、preview、crop
     */
    public final static int REQUEST_CAMERA = 67;
    private String cameraPath;

    private void startCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            File cameraFile = PickerFileUtil.createCameraFile(this);
            cameraPath = cameraFile.getAbsolutePath();
            Uri uri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                /**
                 * 7.0 调用系统相机拍照不再允许使用Uri方式，应该替换为FileProvider
                 * 并且这样可以解决MIUI系统上拍照返回size为0的情况
                 */
                uri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", cameraFile);
            } else {
                uri = Uri.fromFile(cameraFile);
            }
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        }
    }

    @Override
    public void onPreview(int index, List<FileBean> list) {
        SelectPreview.get().setList(list).setIndex(index).selectMap(adapter.getSelectMaps()).into(this);
    }

    @Override
    public void onSelectedState(String path, boolean isSelect) {
        folderAdapter.onSelectedState(path, isSelect);
        onResetSelectState();
    }

    @Override
    public void onResetSelectState() {
        if (mode == Photo.SelectMode.SINGLE) {
            photo_bottom_rl.setVisibility(View.GONE);
        } else {
            photo_bottom_rl.setVisibility(View.VISIBLE);
        }
        int selectNums = adapter.getSelectNums();
        int previewColor = Photo.with().getPreviewColor();
        int commitColor = Photo.with().getCommitColor();
        if (PhotoUtils.checkWhiteColor(previewColor)) {
            previewColor = Photo.with().defaultGreenColor();
        }
        int falseColor = Color.parseColor("#E4E4E4");
        if (PhotoUtils.checkWhiteColor(commitColor)) {
            commitColor = Photo.with().defaultGreenColor();
            photo_commit.setTextColor(falseColor);
        } else photo_commit.setTextColor(Color.WHITE);
        photo_preview.setText("预览(" + selectNums + "/" + Photo.with().getMaxSelectNums() + ")");
        if (selectNums > 0) {
            photo_preview.setClickable(true);
            photo_preview.setEnabled(true);
            photo_commit.setClickable(true);
            photo_commit.setEnabled(true);
            PickerSelectorUtil.toTextViewSelector(photo_preview, previewColor, PhotoUtils.darkColor(previewColor, 0.2f));
            PickerSelectorUtil.toViewBackgraound(photo_commit, PhotoUtils.dip2px(photo_commit.getContext(), 2f), commitColor, PhotoUtils.darkColor(previewColor, 0.2f));
        } else {
            photo_preview.setClickable(false);
            photo_preview.setEnabled(false);
            photo_commit.setClickable(false);
            photo_commit.setEnabled(false);
            photo_preview.setTextColor(falseColor);
            PickerSelectorUtil.toViewBackgraound(photo_commit, PhotoUtils.dip2px(photo_commit.getContext(), 2f), falseColor, falseColor);
        }
    }

    //图片选中
    @Override
    public void onSelected(String path) {
        PickerLog.log("onSelected:" + path);
        if (Photo.with().isCrop()) {
            Intent cropIntent = new Intent(this, CropImageActivity.class);
            cropIntent.putExtra(CropImageActivity.KEY_CROP_PATH, path);
            startActivityForResult(cropIntent, SELECT_CROP_REQUEST);
        } else {
            ArrayList<String> imgs = new ArrayList<>();
            imgs.add(path);
            Photo.with().getListener().onSelectImages(imgs);
            onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PickerLog.log("requestCode:" + requestCode + ",resultCode:" + resultCode);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(cameraPath))));
            } else if (requestCode == SELECT_CROP_REQUEST) {
                if (data != null) {
                    int crop = data.getIntExtra(CropImageActivity.CROP_TYPE, -1);
                    if (crop == CropImageActivity.CROP_SUCCESS && data.getData() != null) {
                        ArrayList<String> images = new ArrayList<>();
                        images.add(data.getData().getPath());
                        Photo.with().getListener().onSelectImages(images);
                        onBackPressed();
                    } else if (crop == CropImageActivity.CROP_FINISH) {

                    } else {
                        onBackPressed();
                    }
                } else {
                    Toast.makeText(this, "crop error", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (requestCode == REQUEST_CAMERA) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "相机权限未开启或开启失败,请手动开启!", Toast.LENGTH_SHORT).show();
                }
            } else {
                onBackPressed();
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.select_backrl) {
            onBackPressed();
        } else if (id == R.id.photo_folder_ll) {
            dismissFolder();
        } else if (id == R.id.photo_titlell) {
            if (isShowFolder()) {
                dismissFolder();
            } else {
                showFolder();
            }
        } else if (id == R.id.photo_preview) {
            List<FileBean> images = adapter.getSelectPreviewImages();
            SelectPreview.get().setList(images).setIndex(0).selectMap(adapter.getSelectMaps()).into(this);
        } else if (id == R.id.photo_commit) {
            commit(adapter.getSelectImages());
        }
    }

    private void commit(ArrayList<String> img) {
        Photo.with().getListener().onSelectImages(img);
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (isShowFolder()) {
            dismissFolder();
        } else finish();
    }

    @Override
    public void finish() {
        MediaStoreHelper.destroyLoader(this);
        super.finish();
    }

    private void dismissFolder() {
        AnimalHelp.folderAnims(photo_folder_ll, photo_folderlv, jiantou, View.GONE);
    }

    private void showFolder() {
        AnimalHelp.folderAnims(photo_folder_ll, photo_folderlv, jiantou, View.VISIBLE);
    }

    private boolean isShowFolder() {
        int visible = photo_folder_ll.getVisibility();
        return visible == View.VISIBLE;
    }

    @Override
    protected void onDestroy() {
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        super.onDestroy();
        Photo.with().reset();
        adapter = null;
        folderAdapter = null;
        dirs = null;
    }
}
