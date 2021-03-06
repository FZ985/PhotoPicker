package jiang.photo.picker.helper;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static android.provider.MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_ID;
import static android.provider.MediaStore.MediaColumns.DATA;
import static android.provider.MediaStore.MediaColumns.DATE_ADDED;
import static android.provider.MediaStore.MediaColumns.SIZE;

/**
 * Created by JFZ on 2017/6/15 11:04.
 */

public class MediaStoreHelper {

    public final static String EXTRA_SHOW_GIF = "SHOW_GIF";
    public final static int INDEX_ALL_PHOTOS = 0;
    public final static int LOADER_ID = 1;

    public static void destroyLoader(FragmentActivity activity) {
        LoaderManager.getInstance(new WeakReference<>(activity).get()).destroyLoader(LOADER_ID);
    }

    public static void getPhotoDirs(FragmentActivity activity, Bundle args, PhotosResultCallback resultCallback) {
        LoaderManager.getInstance(new WeakReference<>(activity).get()).initLoader(LOADER_ID, args,
                new PhotoDirLoaderCallbacks(activity, resultCallback));
//        activity.getSupportLoaderManager().initLoader(0, args,
//                new PhotoDirLoaderCallbacks(activity, resultCallback));
    }

    private static class PhotoDirLoaderCallbacks implements
            LoaderManager.LoaderCallbacks<Cursor> {

        private Context context;
        private PhotosResultCallback resultCallback;

        public PhotoDirLoaderCallbacks(Context context,
                                       PhotosResultCallback resultCallback) {
            this.context = context;
            this.resultCallback = resultCallback;
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new PhotoDirectoryLoader(context, args.getBoolean(
                    EXTRA_SHOW_GIF, false));
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data == null) {
                return;
            }
            List<LocalMediaFolder> directories = new ArrayList<LocalMediaFolder>();
            LocalMediaFolder photoDirectoryAll = new LocalMediaFolder();
            photoDirectoryAll.setName("相机胶卷");
            photoDirectoryAll.setId("ALL");
            while (data.moveToNext()) {

                int imageId = data.getInt(data.getColumnIndexOrThrow(BaseColumns._ID));
                String bucketId = data.getString(data
                        .getColumnIndexOrThrow(BUCKET_ID));
                String name = data.getString(data
                        .getColumnIndexOrThrow(BUCKET_DISPLAY_NAME));
                String path = data.getString(data.getColumnIndexOrThrow(DATA));
                long size = data.getInt(data.getColumnIndexOrThrow(SIZE));

//                PickerLog.e("name:"+name);
                if (size < 1) {
                    continue;
                }
                LocalMediaFolder photoDirectory = new LocalMediaFolder();
                photoDirectory.setId(bucketId);
                photoDirectory.setName(name);
                if (!directories.contains(photoDirectory)) {
                    photoDirectory.setCoverPath(path);
                    photoDirectory.addPhoto(imageId, path);
                    photoDirectory.setDateAdded(data.getLong(data
                            .getColumnIndexOrThrow(DATE_ADDED)));
                    directories.add(photoDirectory);
                } else {
                    directories.get(directories.indexOf(photoDirectory))
                            .addPhoto(imageId, path);
                }
                photoDirectoryAll.addPhoto(imageId, path);
            }
            if (photoDirectoryAll.getPhotoPaths().size() > 0) {
                photoDirectoryAll.setCoverPath(photoDirectoryAll
                        .getPhotoPaths().get(0));
            }
            directories.add(INDEX_ALL_PHOTOS, photoDirectoryAll);
            if (resultCallback != null) {
                resultCallback.onResultCallback(directories);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }

    public interface PhotosResultCallback {
        void onResultCallback(List<LocalMediaFolder> directories);
    }


}
