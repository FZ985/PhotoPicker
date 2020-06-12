package jiang.photo.picker.listener.crop;


import android.net.Uri;

public interface SaveCallback extends Callback{
    void onSuccess(Uri outputUri);
    void onError();
}
