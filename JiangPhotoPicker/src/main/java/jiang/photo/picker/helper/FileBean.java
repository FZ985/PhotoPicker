package jiang.photo.picker.helper;

/**
 * Created by JFZ on 2017/6/15 11:12.
 */

public class FileBean {

    public int id;

    public String path;

    public boolean isSelect;

    public FileBean(int id, String path) {
        this.id = id;
        this.path = path;
    }

    public FileBean(int id, String path, boolean isSelect) {
        this.id = id;
        this.path = path;
        this.isSelect = isSelect;
    }

    public FileBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    @Override
    public String toString() {
        return "FileBean{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", isSelect=" + isSelect +
                '}';
    }
}
