package com.sunny.family.detail.view.common;


import android.view.View;

import androidx.annotation.Nullable;

import com.sunny.family.R;

/**
 * Created by ZhangBoshi
 * on 2019-12-6
 */

public final class BlockUtils {
    private BlockUtils() {
    }

    @Nullable
    public static PosterInfo getPosterInfo(@Nullable View view) {
        return view == null ? null : (PosterInfo) view.getTag(R.id.tag_poster_info);
    }

    public static void setPosterInfo(@Nullable View view, @Nullable PosterInfo posterInfo) {
        if (view == null)
            return;
        view.setTag(R.id.tag_poster_info, posterInfo);
    }

    public static int getAdapterPosition(@Nullable View view) {
        if (view == null)
            return -1;
        Integer position = (Integer) view.getTag(R.id.tag_adapter_position);
        return position == null ? -1 : position;
    }

    public static void setAdapterPosition(@Nullable View view, int position) {
        if (view == null)
            return;
        view.setTag(R.id.tag_adapter_position, position);
    }
}
