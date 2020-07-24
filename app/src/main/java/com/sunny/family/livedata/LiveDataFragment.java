package com.sunny.family.livedata;

import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.sunny.family.R;
import com.sunny.lib.utils.ContextProvider;
import com.sunny.lib.utils.SunLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by zhangxin17 on 2020/7/13
 */
public class LiveDataFragment extends Fragment {

    private static final String TAG = "LiveDataFragment";

    private NameViewModel mNameViewModel;

    private TextView mTvName;

    public static LiveDataFragment getInstance() {
        return new LiveDataFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNameViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance((Application) ContextProvider.appContext))
                .get(NameViewModel.class);

        mNameViewModel.getCurrentName().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                SunLog.i(TAG, "onChanged :" + s);
                mTvName.setText(s);
            }
        });

        mNameViewModel.getNameListData().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                for (String item : strings) {
                    SunLog.i(TAG, "name :" + item);
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_livedata, container, false);
        mTvName = view.findViewById(R.id.tv_name);

        view.findViewById(R.id.btn_change_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = new Random().nextInt(100);
                mNameViewModel.getCurrentName().setValue("name :" + num);

                getDataInstance().updateValue(num);
            }
        });

        view.findViewById(R.id.btn_update_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> nameList = new ArrayList<>();
                int num;

                for (int i = 0; i < 5; i++) {
                    num = new Random().nextInt(100);
                    nameList.add("name-" + num);
                }
                mNameViewModel.getNameListData().setValue(nameList);
            }
        });
        return view;
    }

    private MyLiveData getDataInstance() {
        return MyLiveData.getInstance(getContext());
    }

}
