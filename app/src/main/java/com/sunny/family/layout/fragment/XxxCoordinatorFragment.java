package com.sunny.family.layout.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.sunny.family.R;
import com.sunny.lib.base.BaseFragment;

/**
 * Created by zhangxin17 on 2020/11/19
 */
public class XxxCoordinatorFragment extends BaseFragment {

    private View rootLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootLayout = inflater.inflate(R.layout.fragment_home_base, container, false);
        AppCompatTextView nameView = rootLayout.findViewById(R.id.fragment_name);
        nameView.setText("协调布局");
        return rootLayout;
    }

}
