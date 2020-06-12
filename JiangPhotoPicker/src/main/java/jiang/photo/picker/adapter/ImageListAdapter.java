package jiang.photo.picker.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import jiang.photo.picker.Photo;
import jiang.photo.picker.R;
import jiang.photo.picker.anim.AnimalHelp;
import jiang.photo.picker.helper.FileBean;
import jiang.photo.picker.listener.SelectImageChangeListener;
import jiang.photo.picker.utils.PhotoUtils;

/**
 * Create by JFZ
 * date: 2020-06-05 16:29
 **/
public class ImageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int CAMERA = 1;
    private final int PICTURE = 2;
    public static final int FOOTER = 3;
    private List<FileBean> imageList;

    private int SCREEN_SIZE;//item 宽高
    private SelectImageChangeListener listener;//回调
    private Photo.SelectMode mode;

    private LinkedHashMap<String, FileBean> selectMaps;

    public ImageListAdapter(Context context, int spanCount) {
        this.SCREEN_SIZE = (PhotoUtils.getScreenWidth(context)) / spanCount;
        imageList = new ArrayList<>();
        mode = Photo.with().getMode();
        selectMaps = new LinkedHashMap<>();
    }

    public LinkedHashMap<String, FileBean> getSelectMaps() {
        return selectMaps;
    }

    @Override
    public int getItemViewType(int position) {
        if (Photo.with().isCamera()) {
            if (position == 0) {
                return CAMERA;
            } else {
                return (position == getItemCount() - 1) ? FOOTER : PICTURE;
            }
        } else {
            return (position == getItemCount() - 1) ? FOOTER : PICTURE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == CAMERA) {
            return new CameraViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.photo_item_camera, null));
        } else if (viewType == FOOTER) {
            return new FooterViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.photo_item_footer, null));
        }
        return new PictureViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.photo_item_images, null));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        int viewType = getItemViewType(position);
        if (viewType == PICTURE) {
            //图片
            final PictureViewHolder pictureViewHolder = (PictureViewHolder) viewHolder;
            final FileBean data = imageList.get(Photo.with().isCamera() ? position - 1 : position);
            if (mode == Photo.SelectMode.SINGLE) {
                pictureViewHolder.checkRoot.setVisibility(View.GONE);
                pictureViewHolder.layer.setVisibility(View.GONE);
                pictureViewHolder.check.setVisibility(View.GONE);
                pictureViewHolder.checkRoot.setClickable(false);
            } else {
                pictureViewHolder.checkRoot.setClickable(true);
                pictureViewHolder.check.setVisibility(View.VISIBLE);
                pictureViewHolder.checkRoot.setVisibility(View.VISIBLE);
                if (data.isSelect) {
                    pictureViewHolder.check.setImageResource(R.drawable.photo_icon_select);
                    pictureViewHolder.check.setColorFilter(Photo.with().getImageSelectColor());
                    AnimalHelp.layerAnim(pictureViewHolder.layer, true);
                    putImage(data);
                } else {
                    pictureViewHolder.check.setImageResource(R.drawable.photo_icon_normal);
                    pictureViewHolder.check.setColorFilter(Color.parseColor("#FFFFFF"));
                    AnimalHelp.layerAnim(pictureViewHolder.layer, false);
                    removeImage(data);
                }
            }
            pictureViewHolder.checkRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //选中点击
                    if (listener != null) {
                        if (data.isSelect) {
                            data.isSelect = false;
                            removeImage(data);
                            listener.onSelectedState(data.getPath(), data.isSelect);
                            notifyPosition(position);
                        } else {
                            int max = Photo.with().getMaxSelectNums();
                            if (getSelectNums() >= max) {
                                Toast.makeText(v.getContext(), "你最多只能选择" + max + "张照片", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (PhotoUtils.checkFile(data.getPath())) {
                                data.isSelect = true;
                                putImage(data);
                                listener.onSelectedState(data.getPath(), data.isSelect);
                                notifyPosition(position);
                            } else {
                                Toast.makeText(v.getContext(), "图片已损坏", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });
            //图片点击
            pictureViewHolder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mode == Photo.SelectMode.SINGLE) {
                        if (listener != null) {
                            listener.onSelected(data.getPath());
                        }
                    } else if (mode == Photo.SelectMode.MULTIPLE) {
                        if (listener != null) {
                            int index = Photo.with().isCamera() ? position - 1 : position;
                            listener.onPreview(index, imageList);
                        }
                    }
                }
            });

            Glide.with(pictureViewHolder.image.getContext())
                    .load(data.getPath())
                    .placeholder(R.drawable.photo_default)
                    .error(R.drawable.photo_default)
                    .centerCrop()
                    .into(pictureViewHolder.image);
        } else if (viewType == CAMERA) {
            CameraViewHolder cameraViewHolder = (CameraViewHolder) viewHolder;
            cameraViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onCamera();
                    }
                }
            });
        } else if (viewType == FOOTER) {
            //页脚
            FooterViewHolder footerViewHolder = (FooterViewHolder) viewHolder;
            footerViewHolder.footer.setText(getItemSize() + "张照片");
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

    public int getSelectNums() {
        return (selectMaps != null) ? selectMaps.size() : 0;
    }

    public ArrayList<String> getSelectImages() {
        ArrayList<String> imgs = new ArrayList<>();
        if (selectMaps != null) {
            Collection<FileBean> values = selectMaps.values();
            if (values != null) {
                for (FileBean obj : values) {
                    imgs.add(obj.getPath());
                }
            }
        }
        return imgs;
    }

    public List<FileBean> getSelectPreviewImages() {
        ArrayList<FileBean> imgs = new ArrayList<>();
        if (selectMaps != null) {
            Collection<FileBean> values = selectMaps.values();
            if (values != null) {
                for (FileBean obj : values) {
                    imgs.add(obj);
                }
            }
        }
        return imgs;
    }

    @Override
    public int getItemCount() {
        if (imageList == null || imageList.size() == 0) return 0;
        //摄像机+footer, 否则只有footer
        return (Photo.with().isCamera()) ? imageList.size() + 2 : imageList.size() + 1;
    }

    public int getItemSize() {
        return imageList.size();
    }

    private void notifyPosition(int position) {
        notifyItemChanged(position);
    }

    public void bindImages(List<FileBean> data) {
        resetSelectMaps();
        if (data != null && data.size() > 0) {
            imageList.clear();
            imageList.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void selectChange(String path, boolean isSelect) {
        if (imageList == null) return;
        for (int i = 0; i < imageList.size(); i++) {
            int index = i + 1;
            if (imageList.get(i).getPath().equals(path)) {
                imageList.get(i).isSelect = isSelect;
                if (isSelect) {
                    putImage(imageList.get(i));
                } else {
                    removeImage(imageList.get(i));
                }
                notifyPosition(Photo.with().isCamera() ? index : i);
            }
        }
    }

    //刷新一下选中的图片， 防止已删除的照片还在map里
    private void resetSelectMaps() {
        if (selectMaps != null) {
            Collection<FileBean> values = selectMaps.values();
            if (values != null) {
                for (FileBean obj : values) {
                    File file = new File(obj.getPath());
                    if (!file.exists()) {
                        removeImage(obj);
                    }
                }
            }
        }
        if (listener != null) {
            listener.onResetSelectState();
        }
    }


    public void setImageSelectListener(SelectImageChangeListener listener) {
        this.listener = listener;
    }

    private class PictureViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private View layer;
        private ImageView check;
        private RelativeLayout checkRoot;

        PictureViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setLayoutParams(new RecyclerView.LayoutParams(SCREEN_SIZE, SCREEN_SIZE));
            image = itemView.findViewById(R.id.item_image);
            layer = itemView.findViewById(R.id.item_layer);
            check = itemView.findViewById(R.id.item_check);
            checkRoot = itemView.findViewById(R.id.item_checkroot);
        }
    }

    private class CameraViewHolder extends RecyclerView.ViewHolder {
        CameraViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setLayoutParams(new RecyclerView.LayoutParams(SCREEN_SIZE, SCREEN_SIZE));
        }
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {
        public TextView footer;

        public FooterViewHolder(@NonNull View itemView) {
            super(itemView);
            footer = itemView.findViewById(R.id.photo_footer);
        }
    }
}
