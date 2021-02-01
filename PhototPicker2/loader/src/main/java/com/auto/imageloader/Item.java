package com.auto.imageloader;

import android.content.ContentUris;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

import androidx.annotation.Nullable;


/**
 * Description:
 * Author: jfz
 * Date: 2021-01-28 14:08
 */
public class Item implements Parcelable {
    public final long id;
    public final String mimeType;
    public final Uri uri;
    public final long size;
    public final long duration; // only for video, in ms
    public final String bucketId;
    public final String bucketDisplayName;
    public final String displayName;

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        @Nullable
        public Item createFromParcel(Parcel source) {
            return new Item(source);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    private Item(Parcel source) {
        id = source.readLong();
        mimeType = source.readString();
        bucketId = source.readString();
        bucketDisplayName = source.readString();
        displayName = source.readString();
        uri = source.readParcelable(Uri.class.getClassLoader());
        size = source.readLong();
        duration = source.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(mimeType);
        dest.writeString(displayName);
        dest.writeString(bucketId);
        dest.writeString(bucketDisplayName);
        dest.writeParcelable(uri, 0);
        dest.writeLong(size);
        dest.writeLong(duration);
    }

    private Item(long id, String mimeType, long size, long duration, String displayName, String bucketId, String bucketDisplayName) {
        this.id = id;
        this.mimeType = mimeType;
        this.displayName = displayName;
        this.bucketId = bucketId;
        this.bucketDisplayName = bucketDisplayName;
        Uri contentUri;
        if (isImage()) {
            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else {
            // ?
            contentUri = MediaStore.Files.getContentUri("external");
        }
        this.uri = ContentUris.withAppendedId(contentUri, id);
        this.size = size;
        this.duration = duration;
    }

    public static Item valueOf(long id, String mimeType, long size, long duration, String displayName, String bucketId, String bucketDisplayName) {
        return new Item(id, mimeType, size, duration, displayName, bucketId, bucketDisplayName);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Item)) {
            return false;
        }

        Item other = (Item) obj;
        return id == other.id
                && (mimeType != null && mimeType.equals(other.mimeType)
                || (mimeType == null && other.mimeType == null))
                && (uri != null && uri.equals(other.uri)
                || (uri == null && other.uri == null))
                && size == other.size
                && duration == other.duration;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + Long.valueOf(id).hashCode();
        if (mimeType != null) {
            result = 31 * result + mimeType.hashCode();
        }
        result = 31 * result + uri.hashCode();
        result = 31 * result + Long.valueOf(size).hashCode();
        result = 31 * result + Long.valueOf(duration).hashCode();
        return result;
    }

    public Uri getContentUri() {
        return uri;
    }

    public boolean isImage() {
        if (mimeType == null) return false;
        return mimeType.startsWith("image");
    }

    public long getId() {
        return id;
    }

    public String getMimeType() {
        return mimeType;
    }

    public long getSize() {
        return size;
    }

    public long getDuration() {
        return duration;
    }

    public String getBucketId() {
        return bucketId;
    }

    public String getBucketDisplayName() {
        return bucketDisplayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}