package jiang.photo.picker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import jiang.photo.picker.Photo;
import jiang.photo.picker.R;
import jiang.photo.picker.helper.FileBean;
import jiang.photo.picker.helper.LocalMediaFolder;


/**
 * Created by JFZ on 2017/6/26 15:40.
 */

public class FolderAdapter extends BaseAdapter {

    private Context mContext;
    private List<LocalMediaFolder> directories;

    private int checkIndex = 0;

    private int checkColor;

    public FolderAdapter(Context context) {
        this.mContext = context;
        this.checkColor = Photo.with().getFolderSelectColor();
    }

    @Override
    public int getCount() {
        return directories == null ? 0 : directories.size();
    }

    @Override
    public LocalMediaFolder getItem(int position) {
        return directories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DirectoriesViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item_folder, null);
            viewHolder = new DirectoriesViewHolder(convertView);
        } else {
            viewHolder = (DirectoriesViewHolder) convertView.getTag();
        }
        LocalMediaFolder data = directories.get(position);
        viewHolder.dirName.setText(data.getName());
        viewHolder.imgNums.setText(data.getImageNum() + "张");
        viewHolder.index.setVisibility(checkIndex == position ? View.VISIBLE : View.GONE);
        viewHolder.index.setColorFilter(checkColor);

        Glide.with(mContext).load(data.getCoverPath()).centerCrop().error(R.drawable.photo_default).into(viewHolder.img);
        return convertView;
    }

    public void setCheckIndex(int checkIndex) {
        this.checkIndex = checkIndex;
        notifyDataSetChanged();
    }

    public void onSelectedState(String path, boolean isSelect) {
        if (directories !=null){
            for (int i = 0; i < directories.size(); i++) {
                ArrayList<FileBean> photos = directories.get(i).getPhotos();
                if (photos !=null){
                    for (int j = 0; j < photos.size(); j++) {
                        if (photos.get(j).getPath().equals(path)){
                            photos.get(j).isSelect = isSelect;
                        }
                    }
                }
            }
            notifyDataSetChanged();
        }
    }

    public void bindFolder(List<LocalMediaFolder> directories) {
        this.directories = directories;
        notifyDataSetChanged();
    }

    class DirectoriesViewHolder {

        public TextView dirName;
        public TextView imgNums;
        public ImageView img;
        public ImageView index;

        public DirectoriesViewHolder(View view) {

            dirName = (TextView) view.findViewById(R.id.dirName);
            imgNums = (TextView) view.findViewById(R.id.imgNums);
            img = (ImageView) view.findViewById(R.id.img);
            index = (ImageView) view.findViewById(R.id.index);

            view.setTag(this);
        }
    }

}
