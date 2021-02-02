package com.auto.imageloader;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.CursorLoader;


/**
 * Description:
 * Author: jfz
 * Date: 2020-12-24 11:23
 */
public final class AlbumMediaLoader extends CursorLoader {

    private static final Uri QUERY_URI = MediaStore.Files.getContentUri("external");
    public static final String COLUMN_BUCKET_ID = "bucket_id";
    public static final String COLUMN_BUCKET_DISPLAY_NAME = "bucket_display_name";
    public static final String[] PROJECTION = {
            MediaStore.Files.FileColumns._ID,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.MIME_TYPE,
            MediaStore.MediaColumns.SIZE,
            COLUMN_BUCKET_ID,
            COLUMN_BUCKET_DISPLAY_NAME,
            "duration"};

    public static final String ORDER_BY = (beforeAndroidTen()
            ? MediaStore.Images.Media.DATE_ADDED
            : MediaStore.Images.Media.DATE_TAKEN) + " DESC";

    // === params for album ALL && && showSingleMediaType ==true
    private static final String SELECTION_ALL_FOR_SINGLE_MEDIA_TYPE =
            MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " AND " + MediaStore.MediaColumns.SIZE + ">0";

    private static String[] getSelectionArgsForSingleMediaType(int mediaType) {
        return new String[]{String.valueOf(mediaType)};
    }

    // === params for album ALL && MineType=="image/gif && showSingleMediaType ==true"
    private static final String SELECTION_ALL_FOR_GIF =
            MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " AND "
                    + MediaStore.MediaColumns.MIME_TYPE + "=?"
                    + " AND " + MediaStore.MediaColumns.SIZE + ">0";

    private static String[] getSelectionArgsForGifType() {
        return new String[]{String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE), "image/gif"};
    }
    // =========================================================

    private static final String SELECTION_ALL =
            "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " OR "
                    + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)"
                    + " AND " + MediaStore.MediaColumns.SIZE + ">0";
    private static final String[] SELECTION_ALL_ARGS = {
            String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
            String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO),
    };

    private AlbumMediaLoader(@NonNull Context context, @Nullable String selection, @Nullable String[] selectionArgs) {
        super(context, QUERY_URI, PROJECTION, selection, selectionArgs, ORDER_BY);
    }

    public static AlbumMediaLoader newInstance(Context context) {
        String selection;
        String[] selectionArgs;
        if (PickerManager.getInstance().getSelectionType().equals(PickerManager.onlyImage)) {
            selection = SELECTION_ALL_FOR_SINGLE_MEDIA_TYPE;
            selectionArgs = getSelectionArgsForSingleMediaType(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE);
        } else if (PickerManager.getInstance().getSelectionType().equals(PickerManager.onlyGif)) {
            selection = SELECTION_ALL_FOR_GIF;
            selectionArgs = getSelectionArgsForGifType();
        } else if (PickerManager.getInstance().getSelectionType().equals(PickerManager.onlyVideo)) {
            selection = SELECTION_ALL_FOR_SINGLE_MEDIA_TYPE;
            selectionArgs = getSelectionArgsForSingleMediaType(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO);
        } else {
            selection = SELECTION_ALL;
            selectionArgs = SELECTION_ALL_ARGS;
        }
        return new AlbumMediaLoader(context, selection, selectionArgs);
    }

    /**
     * @return 是否是 Android 10 （Q） 之前的版本
     */
    private static boolean beforeAndroidTen() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.Q;
    }
}