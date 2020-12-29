package com.sunny.module.view.xxx.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.sunny.lib.common.base.BaseFragment;
import com.sunny.module.view.R;


/**
 * Created by zhangxin17 on 2020/11/19
 */
public class XxxDrawerFragment extends BaseFragment {

    private View rootLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootLayout = inflater.inflate(R.layout.view_fragment_xxx_drawer, container, false);
        AppCompatTextView nameView = rootLayout.findViewById(R.id.fragment_name);
        nameView.setText("侧滑布局");
        return rootLayout;
    }
}
