package jiang.photo.picker.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import jiang.photo.picker.R;
import jiang.photo.picker.listener.PreviewCheckChangeListener;
import jiang.photo.picker.widget.PickerTouchImageView;

/**
 * Create by JFZ
 * date: 2020-06-10 14:07
 **/
public class SelectPreviewFragment extends Fragment {
    public PreviewCheckChangeListener listener;
    private PickerTouchImageView touch;
    private String path = "";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.photo_item_preview, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            path = getArguments().getString("path", "");
        }
        touch = view.findViewById(R.id.touch);
        touch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onImageClick();
                }
            }
        });
        initData();
    }

    private void initData() {
        Glide.with(touch.getContext())
                .load(path)
                .placeholder(R.drawable.photo_default)
                .thumbnail(0.3f)
                .error(R.drawable.photo_default).into(touch);
    }

    public static SelectPreviewFragment instance(PreviewCheckChangeListener listener, String path) {
        SelectPreviewFragment fragment = new SelectPreviewFragment();
        fragment.listener = listener;
        Bundle bundle = new Bundle();
        bundle.putString("path", path);
        fragment.setArguments(bundle);
        return fragment;
    }
}
