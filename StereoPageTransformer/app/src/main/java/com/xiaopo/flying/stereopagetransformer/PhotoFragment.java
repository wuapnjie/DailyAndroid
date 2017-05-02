package com.xiaopo.flying.stereopagetransformer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

/**
 * @author wupanjie
 */

public class PhotoFragment extends Fragment {
    public static final String ARG_PHOTO_RES = "photo_res";

    private int photoResId;
    private ImageView ivPhoto;

    public static PhotoFragment newInstance(int photoResId) {
        Bundle args = new Bundle();
        args.putInt(ARG_PHOTO_RES, photoResId);
        PhotoFragment fragment = new PhotoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null) return;
        Bundle args = getArguments();
        photoResId = args.getInt(ARG_PHOTO_RES, 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_photo, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        ivPhoto = (ImageView) rootView.findViewById(R.id.iv_photo);
        Picasso.with(getContext()).load(photoResId).placeholder(R.color.placeholder).fit().centerInside().into(ivPhoto);
    }
}
