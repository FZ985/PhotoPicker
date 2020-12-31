package com.material.selection.internal.listener;

import com.material.selection.internal.entiy.Item;

import java.util.List;

public interface LoaderListener {
    
    void onLoadFinish(List<List<Item>> lists);
}