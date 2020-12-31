package com.material.selection.internal.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.material.selection.R;
import com.material.selection.internal.entiy.Item;
import com.material.selection.internal.entiy.SelectionSpec;
import com.material.selection.widget.PickerTouchImageView;

/**
 * Description:
 * Author: jfz
 * Date: 2020-12-30 14:10
 */
public class PreviewFragment extends Fragment {

    private PickerTouchImageView image;
    private ImageView video;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.selection_item_preview, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Item item = getArguments().getParcelable("item");
        image = view.findViewById(R.id.preview_touch);
        video = view.findViewById(R.id.preview_video);
        if (item != null) {
            if (item.isVideo()) {
                video.setVisibility(View.VISIBLE);
                video.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(item.uri, "video/*");
                        try {
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(getContext(), "No App found supporting video preview", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else video.setVisibility(View.GONE);

            if (item.isGif()) {
                SelectionSpec.getInstance().imageEngine.loadPreviewGif(getActivity(), image, item.getContentUri());
            } else {
                SelectionSpec.getInstance().imageEngine.loadPreviewImage(getActivity(), image, item.getContentUri());
            }
        }
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null && getActivity() instanceof PreviewActivity) {
                    ((PreviewActivity) getActivity()).onClick(v);
                }
            }
        });
    }

    public static PreviewFragment instance(Item item) {
        PreviewFragment fragment = new PreviewFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("item", item);
        fragment.setArguments(bundle);
        return fragment;
    }
}