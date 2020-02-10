package com.sunny.family.detail.view.widget;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sunny.family.R;
import com.sunny.family.detail.view.common.Poster;
import com.sunny.lib.image.FrescoUtils;
import com.sunny.lib.utils.ResUtils;

/**
 * Created by ZhangBoshi on 2019/2/18.
 */

public class W101_Image_S1001 extends BaseWidget {
    @Nullable
    private SimpleDraweeView mFrescoView;
    @Nullable
    private String mUriCache;

    private W101_Image_S1001(@NonNull View frescoView) {
        super(frescoView);
        init(frescoView);
    }

    @Override
    public void bindData(@Nullable Poster poster) {
        if (mFrescoView != null) {
            String uri = null;
            String name = null;
            if (poster != null) {
                uri = poster.getImg();
            }
            if (uri == null)
                uri = "";

            if (!uri.equals(mUriCache)) {
                FrescoUtils.setImageURI(uri, mFrescoView,
                        ResUtils.INSTANCE.getDimensionPixelSize(R.dimen.block_s1001_width),
                        ResUtils.INSTANCE.getDimensionPixelSize(R.dimen.block_s1001_image_height));
            }
            mUriCache = uri;
        }

    }

    @Override
    public void addToController(@NonNull ViewGroup parent) {

        parent.addView(mRoot, 0);
    }

    private void init(@NonNull View root) {
        mFrescoView = root.findViewById(R.id.item_img);
    }

    public static W101_Image_S1001 create(@NonNull ViewGroup parent) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_101_image,
                parent, false);
        return new W101_Image_S1001(root);
    }
}