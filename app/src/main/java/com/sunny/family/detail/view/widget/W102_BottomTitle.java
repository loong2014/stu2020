package com.sunny.family.detail.view.widget;

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
 * |==================================================|
 * |.....title........................................|
 * |..subtitle/shortTitle.............................|
 * |==================================================|
 * 非焦点态展示shortTitle, rbText.
 * 焦点态展示title, subtitle, rbText.
 * shortTitle和title展示的内容是一样的, 但因为布局不同所以用两个textView实现.
 */
public class W102_BottomTitle extends BaseWidget {

    @Nullable
    private TextView mTitle;
    @Nullable
    private TextView mSubtitle;
    @Nullable
    private TextView mRbText;
    @Nullable
    private TextView mShortTitle;

    private W102_BottomTitle(@NonNull View root) {
        super(root);
        bindView(root);
    }

    @Override
    public void bindData(@Nullable Poster poster) {
        super.bindData(poster);
        if (poster == null || SunStrUtils.INSTANCE.isBlank(poster.getName()))
            return;
        if (mTitle != null) {
            mTitle.setText(poster.getName());
        }
        if (mSubtitle != null) {
            mSubtitle.setText(poster.getSubName());
        }

        if (mShortTitle != null) {
            mShortTitle.setText(poster.getName());
        }

    }

    @Override
    public void onFocusChanged(boolean hasFocus) {
        mRoot.setBackgroundColor(
                hasFocus ? ResUtils.INSTANCE.getColor(R.color.white) : ResUtils.INSTANCE.getColor(R.color.black_05));
        if (mTitle != null) {
            mTitle.setVisibility(hasFocus ? View.VISIBLE : View.GONE);
            mTitle.setSelected(hasFocus);
        }
        if (mSubtitle != null) {
            mSubtitle.setVisibility(hasFocus ? View.VISIBLE : View.GONE);
            mSubtitle.setSelected(hasFocus);
        }
        if (mShortTitle != null) {
            mShortTitle.setVisibility(hasFocus ? View.GONE : View.VISIBLE);
            mShortTitle.setSelected(hasFocus);
        }
    }

    private void bindView(@NonNull View root) {
        mTitle = (TextView) root.findViewById(R.id.title);
        mTitle.setText("吸血鬼日记第二季");
        mSubtitle = (TextView) root.findViewById(R.id.subtitle);
        mSubtitle.setText("你真的了解吸血鬼吗？");
        mShortTitle = (TextView) root.findViewById(R.id.short_title);
        mShortTitle.setText("吸血鬼日记第一季");
    }

    public static W102_BottomTitle create(@NonNull ViewGroup parent) {
        View root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.widget_102_bottom_title, parent, false);
        return new W102_BottomTitle(root);
    }

}
