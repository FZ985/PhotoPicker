package com.material.selection.internal.listener;

import com.material.selection.internal.entiy.Item;

import java.util.List;

public interface SelectionListener {

    void onCapture();

    void selectAlbum(List<Item> albums);

    void onAlbumAnim();

    void changeSatate();

    void onPreview();

}