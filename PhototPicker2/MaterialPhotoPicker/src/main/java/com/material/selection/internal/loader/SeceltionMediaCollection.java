package com.material.selection.internal.loader;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.material.selection.internal.entiy.Item;
import com.material.selection.internal.utils.PickerUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Description:
 * Author: jfz
 * Date: 2020-12-24 14:08
 */
public class SeceltionMediaCollection implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int LOADER_ID = 2;
    private WeakReference<Context> mContext;
    private LoaderManager mLoaderManager;
    private LoaderMediaCallback mCallback;

    public void onCreate(AppCompatActivity activity) {
        mContext = new WeakReference<>(activity);
        mLoaderManager = LoaderManager.getInstance(activity);
    }

    public void load(LoaderMediaCallback callback) {
        this.mCallback = callback;
        if (mLoaderManager != null) {
            Bundle mediaStoreArgs = new Bundle();
            mediaStoreArgs.putBoolean("MediaStoreHelper.EXTRA_SHOW_GIF", true);
            mLoaderManager.initLoader(LOADER_ID, mediaStoreArgs, this);
        }
    }

    public void onDestroy() {
        if (mLoaderManager != null) {
            mLoaderManager.destroyLoader(LOADER_ID);
        }
        mCallback = null;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Context context = mContext.get();
        if (context == null) return null;
        return AlbumMediaLoader.newInstance(context);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data == null) {
            return;
        }
        List<Item> allList = new ArrayList<>();
        List<List<Item>> folderList = new ArrayList<>();
        HashMap<String, List<Item>> maps = new HashMap<>();
        while (data.moveToNext()) {
            int fileId = data.getInt(data.getColumnIndex(MediaStore.Files.FileColumns._ID));
            String displayName = data.getString(data.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME));
            String mimeType = data.getString(data.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE));
            String bucketId = data.getString(data.getColumnIndex(AlbumMediaLoader.COLUMN_BUCKET_ID));
            String bucketDisplayName = data.getString(data.getColumnIndex(AlbumMediaLoader.COLUMN_BUCKET_DISPLAY_NAME));
            long size = data.getLong(data.getColumnIndex(MediaStore.MediaColumns.SIZE));
            long duration = data.getLong(data.getColumnIndex("duration"));
            if (size < 1) {
                continue;
            }
            allList.add(Item.valueOf(fileId, mimeType, size, duration, displayName, bucketId, bucketDisplayName));
            if (maps.containsKey(bucketId)) {
                List<Item> items = maps.get(bucketId);
                items.add(Item.valueOf(fileId, mimeType, size, duration, displayName, bucketId, bucketDisplayName));
                maps.put(bucketId, items);
            } else {
                List<Item> items = new ArrayList<>();
                items.add(Item.valueOf(fileId, mimeType, size, duration, displayName, bucketId, bucketDisplayName));
                maps.put(bucketId, items);
            }
        }
        folderList.add(allList);
        Collection<List<Item>> values = maps.values();
        folderList.addAll(values);
        PickerUtils.log("folderListSize:" + folderList.size());
        if (mCallback != null) {
            mCallback.onLoadFinish(folderList);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    public interface LoaderMediaCallback {
        void onLoadFinish(List<List<Item>> datas);
    }
}