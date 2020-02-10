package com.sunny.family.detail.view.widget;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sunny.family.R;
import com.sunny.family.detail.view.common.Poster;
import com.sunny.lib.utils.ResUtils;
import com.sunny.lib.utils.SunStrUtils;

/**
 * Created by ZhangBoshi on 2019/2/18.
 */

public class W103_CenterText extends BaseWidget {
    @Nullable
    private final TextView textView;
    @Nullable
    private String mUriCache;
    private boolean mISelect;
    private boolean mIsHasFocus;

    private W103_CenterText(View view) {
        super(view);
        textView = view.findViewById(R.id.center_title);
    }

    @Override
    public void bindData(@Nullable Poster poster) {
        if (textView != null && poster != null) {
            if (!SunStrUtils.INSTANCE.isBlank(poster.getName())) {
                textView.setText(poster.getName());
            } else {
                textView.setText(poster.getEpisode());
            }
        }
    }

    @Override
    public void onFocusChanged(boolean hasFocus) {
        super.onFocusChanged(hasFocus);
        mIsHasFocus = hasFocus;
        Log.e("W103_CenterText", "hasFocus=" + hasFocus);
        if (textView != null) {

            if (hasFocus) {
                textView.setTextColor(ResUtils.INSTANCE.getColor(R.color.black_80));
                textView.setBackgroundDrawable(
                        ResUtils.INSTANCE.getDrawable(R.drawable.detail_variety_set_focus));
            } else if (mISelect) {
                textView.setTextColor(ResUtils.INSTANCE.getColor(R.color.variety_tab_select));
                textView.setBackgroundDrawable(
                        ResUtils.INSTANCE.getDrawable(R.drawable.detail_variety_set_no_focus));
            } else {
                textView.setTextColor(ResUtils.INSTANCE.getColor(R.color.white_80));
                textView.setBackgroundDrawable(
                        ResUtils.INSTANCE.getDrawable(R.drawable.detail_variety_set_no_focus));

            }

        }

    }

    @Override
    public void onViewSelect(boolean isSelect) {
        mISelect = isSelect;
        Log.e("W103_CenterText", "isSelect=" + isSelect);
        if (textView != null && !mIsHasFocus) {
            textView.setTextColor(isSelect ? ResUtils.INSTANCE.getColor(R.color.variety_tab_select)
                    : ResUtils.INSTANCE.getColor(R.color.white_80));

        }

    }

    @Override
    public void addToController(@NonNull ViewGroup parent) {

        parent.addView(mRoot, 0);
    }

    public static W103_CenterText create(@NonNull ViewGroup parent) {
        View root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.widget_103_center_text, parent, false);
        return new W103_CenterText(root);
    }
}