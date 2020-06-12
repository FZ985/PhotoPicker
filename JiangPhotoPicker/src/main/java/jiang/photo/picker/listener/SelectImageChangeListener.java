package jiang.photo.picker.listener;

import java.util.List;

import jiang.photo.picker.helper.FileBean;

/**
 * Create by JFZ
 * date: 2020-06-08 9:59
 **/
public interface SelectImageChangeListener {

    /**
     * 打开相机
     */
    void onCamera();

    /**
     * 照片预览
     *
     * @param index
     * @param list
     */
    void onPreview(int index, List<FileBean> list);

    /**
     * 选中的图片
     *
     * @param path
     */
    void onSelected(String path);

    /**
     * 多选状态，将相机胶卷 与 单独文件夹的图片进行关联
     *
     * @param path
     * @param isSelect
     */
    void onSelectedState(String path, boolean isSelect);

    /**
     * 刷新选中状态
     */
    void onResetSelectState();
}
