package com.material.selection.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.material.selection.R;
import com.material.selection.internal.utils.AnimalHelp;
import com.material.selection.internal.entiy.Item;
import com.material.selection.internal.entiy.SelectCheckIns;
import com.material.selection.internal.entiy.SelectionSpec;
import com.material.selection.internal.listener.SelectionListener;

import java.util.List;

/**
 * Description:媒体列表
 * Author: jfz
 * Date: 2020-12-24 16:20
 */
public class SelectionListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int HEADER = 0x9;
    private static final int CAMERA = 0x10;
    private static final int ITEM = 0x11;
    public static final int FOOTER = 0x12;
    private final int itemSize;
    private List<Item> mDatas;
    private Context mContext;
    private SelectionListener listener;
    private Drawable normalDrawable, selectDrawable;
    private int headHeight;

    public SelectionListAdapter(Context context, int spanCount, RelativeLayout toolbar, SelectionListener listener) {
        this.mContext = context;
        this.listener = listener;
        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        itemSize = (screenWidth - context.getResources().getDimensionPixelSize(
                R.dimen.media_grid_spacing) * (spanCount - 1)) / spanCount;
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(new int[]{R.attr.selection_icon_normal, R.attr.selection_icon_selected, R.attr.selection_toolbarHeight});
        normalDrawable = typedArray.getDrawable(0);
        selectDrawable = typedArray.getDrawable(1);
        float toolbarHeight = typedArray.getDimension(2, 0);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
        headHeight = ((int) toolbarHeight) + layoutParams.topMargin + layoutParams.bottomMargin;
        typedArray.recycle();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == CAMERA) {
            return new CameraViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.selection_item_camera, null));
        } else if (viewType == HEADER) {
            return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.selection_item_head, null));
        } else if (viewType == FOOTER) {
            return new FooterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.selection_item_footer, null));
        }
        return new ImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.selection_item_images, null));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == HEADER) {

        } else if (viewType == FOOTER) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            int[] photoVideo = getPhotoVideo();
            StringBuilder builder = new StringBuilder();
            if (photoVideo[0] > 0) {
                builder.append(photoVideo[0]);
                builder.append(" photo");
                if (photoVideo[0] > 1)
                    builder.append("s");
            }
            if (photoVideo[1] > 0) {
                if (photoVideo[0] > 0) builder.append("、");
                builder.append(photoVideo[1]).append(" video");
                if (photoVideo[1] > 1)
                    builder.append("s");
            }
            footerViewHolder.footer.setText(builder);
        } else if (viewType == CAMERA) {
            CameraViewHolder cameraViewHolder = (CameraViewHolder) holder;
            cameraViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) listener.onCapture();
                }
            });
        } else {
            ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
            final Item data = mDatas.get(SelectionSpec.getInstance().capture ? position - 2 : position - 1);
            if (data.isGif()) {
                SelectionSpec.getInstance().imageEngine.loadGif(imageViewHolder.image.getContext(), imageViewHolder.image, data);
            } else if (data.isVideo()) {
                SelectionSpec.getInstance().imageEngine.loadVideo(imageViewHolder.image.getContext(), imageViewHolder.image, data);
            } else {
                SelectionSpec.getInstance().imageEngine.loadImage(imageViewHolder.image.getContext(), imageViewHolder.image, data);
            }
            if (SelectCheckIns.getInstance().getSelectMaps().containsKey(data.id)) {
                AnimalHelp.layerAnim(imageViewHolder.layer, true);
                imageViewHolder.checkIv.setImageDrawable(selectDrawable);
            } else {
                imageViewHolder.checkIv.setImageDrawable(normalDrawable);
                AnimalHelp.layerAnim(imageViewHolder.layer, false);
            }
            if (data.isVideo()) {
                imageViewHolder.item_video.setText(DateUtils.formatElapsedTime(data.duration / 1000));
                imageViewHolder.item_video.setVisibility(View.VISIBLE);
            } else {
                imageViewHolder.item_video.setVisibility(View.GONE);
                imageViewHolder.item_video.setText("");
            }
            imageViewHolder.item_checkroot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        if (SelectCheckIns.getInstance().getSelectMaps().containsKey(data.id)) {//选中
                            SelectCheckIns.getInstance().getSelectMaps().remove(data.id);
                            listener.changeSatate();
                            notifyPosition(position);
                        } else {
                            int max = SelectionSpec.getInstance().maxSelectable;
                            if (SelectCheckIns.getInstance().getSelectNums() >= max) {
                                Toast.makeText(v.getContext(), "You have reached max selectable", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            SelectCheckIns.getInstance().put(data);
                            listener.changeSatate();
                            notifyPosition(position);
                        }
                    }
                }
            });

            imageViewHolder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int index = SelectionSpec.getInstance().capture ? position - 2 : position - 1;
                        SelectCheckIns.getInstance().setIndex(index).setPreviewItems(mDatas);
                        listener.onPreview();
                    }
                }
            });
        }
    }

    private void notifyPosition(int position) {
        notifyItemChanged(position);
    }

    private int[] getPhotoVideo() {
        int[] count = new int[2];
        int photos = 0;
        int videos = 0;
        for (Item item : mDatas) {
            if (item.isImage() || item.isGif()) {
                photos++;
            }
            if (item.isVideo()) {
                videos++;
            }
        }
        count[0] = photos;
        count[1] = videos;
        return count;
    }

    @Override
    public int getItemCount() {
        if (mDatas == null || mDatas.size() == 0) return 0;
        //摄像机+footer, 否则只有footer
        return (SelectionSpec.getInstance().capture) ? mDatas.size() + 3 : mDatas.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return HEADER;
        if (SelectionSpec.getInstance().capture && position == 1)
            return CAMERA;
        return (position == getItemCount() - 1) ? FOOTER : ITEM;
    }

    public void setData(List<Item> data) {
        this.mDatas = data;
        SelectCheckIns.getInstance().checkBadId(mContext);
        notifyDataSetChanged();
    }

    private class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public View layer;
        public RelativeLayout item_checkroot;
        public ImageView checkIv;
        public TextView item_video;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setLayoutParams(new RecyclerView.LayoutParams(itemSize, itemSize));
            this.image = itemView.findViewById(R.id.item_selection_iv);
            this.layer = itemView.findViewById(R.id.item_layer);
            this.item_checkroot = itemView.findViewById(R.id.item_checkroot);
            this.checkIv = itemView.findViewById(R.id.item_check);
            this.item_video = itemView.findViewById(R.id.item_video);
        }
    }

    private static class FooterViewHolder extends RecyclerView.ViewHolder {
        public TextView footer;

        public FooterViewHolder(@NonNull View itemView) {
            super(itemView);
            footer = itemView.findViewById(R.id.photo_footer);
        }
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, headHeight));
        }
    }

    private class CameraViewHolder extends RecyclerView.ViewHolder {
        CameraViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setLayoutParams(new RecyclerView.LayoutParams(itemSize, itemSize));
        }
    }
}