package jiang.photo.picker.helper;

import android.text.TextUtils;

import java.util.ArrayList;

import jiang.photo.picker.utils.PickerFileUtil;


/**
 * Created by JFZ on 2017/6/15 11:07.
 */

public class LocalMediaFolder {

    private String id;
    private String coverPath;
    private String name;
    private long dateAdded;
    private ArrayList<FileBean> photos = new ArrayList<FileBean>();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LocalMediaFolder)) {
            return false;
        }
        LocalMediaFolder directory = (LocalMediaFolder) o;
        boolean hasId = !TextUtils.isEmpty(id);
        boolean otherHasId = !TextUtils.isEmpty(directory.id);
        if (hasId && otherHasId) {
            if (!TextUtils.equals(id, directory.id)) {
                return false;
            }
            return TextUtils.equals(name, directory.name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (TextUtils.isEmpty(id)) {
            if (TextUtils.isEmpty(name)) {
                return 0;
            }
            return name.hashCode();
        }
        int result = id.hashCode();
        if (TextUtils.isEmpty(name)) {
            return result;
        }
        result = 31 * result + name.hashCode();
        return result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(long dateAdded) {
        this.dateAdded = dateAdded;
    }

    public ArrayList<FileBean> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<FileBean> photos) {
        if (photos == null)
            return;
        for (int i = 0, j = 0, num = photos.size(); i < num; i++) {
            FileBean p = photos.get(j);
            if (p == null || !PickerFileUtil.fileIsExists(p.getPath())) {
                photos.remove(j);
            } else {
                j++;
            }
        }
        this.photos = photos;
    }

    public ArrayList<String> getPhotoPaths() {
        ArrayList<String> paths = new ArrayList<String>(photos.size());
        for (FileBean photo : photos) {
            paths.add(photo.getPath());
        }
        return paths;
    }

    public void addPhoto(int id, String path) {
        if (PickerFileUtil.fileIsExists(path)) {
            FileBean bean = new FileBean();
            bean.id = id;
            bean.path = path;
            bean.isSelect = false;
            photos.add(bean);
        }
    }

    public int getImageNum() {
        return photos == null ? 0 : photos.size();
    }


    @Override
    public String toString() {
        return "LocalMediaFolder{" +
                "id='" + id + '\'' +
                ", coverPath='" + coverPath + '\'' +
                ", name='" + name + '\'' +
                ", dateAdded=" + dateAdded +
                ", photos=" + photos +
                '}';
    }
}
