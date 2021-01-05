package com.material.selection.adapter;

import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.material.selection.R;
import com.material.selection.internal.entiy.Item;
import com.material.selection.internal.entiy.SelectionSpec;
import com.material.selection.internal.listener.SelectionListener;

import java.util.List;

/**
 * Description:文件夹列表
 * Author: jfz
 * Date: 2020-12-25 14:52
 */
public class AlbumAdapter extends BaseAdapter {

    private List<List<Item>> albums;
    private String allName;
    private String allNameId = "123412375974125896395147852369";
    private int selectIndex = 0;
    private String folderId;

    public AlbumAdapter(ListView listView, SelectionListener listener) {
        TypedArray typedArray = listView.getContext().getTheme().obtainStyledAttributes(new int[]{R.attr.selection_folderlist_allName});
        allName = typedArray.getString(0);
        typedArray.recycle();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    AlbumAdapter.this.selectIndex = position;
                    if (albums.get(position).size() > 0) {
                        folderId = albums.get(position).get(0).bucketId;
                    }
                    listener.selectAlbum(getItem(position));
                    listener.onAlbumAnim();
                    notifyDataSetChanged();
                }
            }
        });
    }

    public String getFolderName() {
        if (selectIndex == 0) return allName;
        if (albums != null && albums.size() > 0 && selectIndex < albums.size() && albums.get(selectIndex).size() > 0) {
            return albums.get(selectIndex).get(0).bucketDisplayName;
        } else return "";
    }

    public int getSelectIndex() {
        if (this.selectIndex == 0) return this.selectIndex;
        for (int i = 0; i < albums.size(); i++) {
            if (albums.get(i).size() > 0) {
                String fId = albums.get(i).get(0).bucketId;
                if (fId.equals(folderId)) {
                    this.selectIndex = i;
                    return i;
                }
            }
        }
        return this.selectIndex;
    }

    public void bindAlbums(List<List<Item>> albums) {
        this.albums = albums;
        notifyDataSetChanged();
    }

    public List<List<Item>> getAlbums(){
        return this.albums;
    }

    @Override
    public int getCount() {
        return albums == null ? 0 : albums.size();
    }

    @Override
    public List<Item> getItem(int position) {
        return albums.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AlbumViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.selection_item_folder, null);
            holder = new AlbumViewHolder(convertView);
        } else holder = (AlbumViewHolder) convertView.getTag();
        holder.folder_selectIv.setVisibility(position == selectIndex ? View.VISIBLE : View.GONE);
        List<Item> items = albums.get(position);
        if (items.size() > 0) {
            holder.folder_dirName.setText(position == 0 ? allName : items.get(0).bucketDisplayName);
            holder.folder_imgNums.setText(String.valueOf(items.size()));
            SelectionSpec.getInstance().imageEngine.loadFolderImage(holder.folder_thumb.getContext(), holder.folder_thumb, items.get(0).getContentUri());
        } else {
        }
        return convertView;
    }


    private static class AlbumViewHolder {
        public ImageView folder_thumb;
        public ImageView folder_selectIv;
        public TextView folder_dirName;
        public TextView folder_imgNums;

        public AlbumViewHolder(View view) {
            folder_thumb = view.findViewById(R.id.folder_thumb);
            folder_dirName = view.findViewById(R.id.folder_dirName);
            folder_imgNums = view.findViewById(R.id.folder_imgNums);
            folder_selectIv = view.findViewById(R.id.folder_selectIv);
            view.setTag(this);
        }


    }
}