package com.sunny.module.view.xxx.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sunny.lib.common.base.BaseFragment;
import com.sunny.lib.common.city.CityAdapter;
import com.sunny.lib.common.city.CityModelKt;
import com.sunny.module.view.R;


/**
 * Created by zhangxin17 on 2020/11/19
 */
public class XxxDetailDataListFragment extends BaseFragment {
    private static final String TAG = "Zhang-Detail-Info";

    private int mPageId = -1;

    //
    private boolean hasInit = false;
    private View mRootLayout;

    //
    private RecyclerView mDataRecyclerView;
    private CityAdapter mDataAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //
        if (savedInstanceState != null) {
            mPageId = savedInstanceState.getInt("detail_data_id");
        }

        //
        Bundle bundle = getArguments();
        if (bundle != null) {
            mPageId = bundle.getInt("detail_data_id");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootLayout = inflater.inflate(R.layout.view_fragment_xxx_detail_data_list, container, false);
        return mRootLayout;
    }

    @Override
    public void onStart() {
        super.onStart();
        initView();
    }

    private void initView() {
        if (hasInit) {
            return;
        }
        hasInit = true;

        //
        mDataRecyclerView = mRootLayout.findViewById(R.id.recyclerView);
        mDataRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, 1));
        mDataRecyclerView.setHasFixedSize(true);

        mDataAdapter = new CityAdapter(mActivity, CityModelKt.buildCityData(mPageId + 1));
        mDataRecyclerView.setAdapter(mDataAdapter);
    }

}
