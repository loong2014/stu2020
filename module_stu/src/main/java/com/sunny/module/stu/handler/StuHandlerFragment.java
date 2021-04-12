package com.sunny.module.stu.handler;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sunny.module.stu.R;
import com.sunny.module.stu.base.StuBaseFragment;

public class StuHandlerFragment extends StuBaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stu_handler, container, false);
        return view;
    }
}
