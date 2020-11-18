package com.sunny.family.layout;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.sunny.family.R;
import com.sunny.family.city.CityModelKt;
import com.sunny.family.city.adapter.CityAdapter;
import com.sunny.family.view.SunnyTopBar;
import com.sunny.lib.base.BaseActivity;
import com.sunny.lib.router.RouterConstant;
import com.sunny.lib.utils.SunLog;

import org.jetbrains.annotations.Nullable;

/**
 * Created by zhangxin17 on 2020/11/18
 */
@RequiresApi(api = Build.VERSION_CODES.M)
@Route(path = RouterConstant.PageSwipeRefresh)
public class SwipeRefreshLayoutActivity extends BaseActivity {
    private static final String TAG = "Zhang-Swipe_Act";

    //
    private SunnyTopBar mTopBar;

    //
    private SunnySwipeToLoadLayout mSwipeToLoadLayout;

    //
    private RecyclerView mSwipeRecyclerView;
    private CityAdapter mSwipeAdapter;

    //
    private RecyclerView mDataRecyclerView;
    private CityAdapter mDataAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_swipe_refesh_layout);

        initView();
    }

    private void initView() {
        //
        mTopBar = findViewById(R.id.top_bar);
        initTopBar();

        //
        mSwipeToLoadLayout = findViewById(R.id.swipe_to_load_layout);
        initSwipeRefresh();

        //
        mSwipeRecyclerView = findViewById(R.id.swipe_recyclerView);
        mSwipeRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                SunLog.i(TAG, "SwipeRecyclerView onScrolled  " + dx + " , " + dy);
            }
        });
        initSwipeRecyclerView();

        //
        mDataRecyclerView = findViewById(R.id.recyclerView);
        mDataRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                SunLog.i(TAG, "DataRecyclerView onScrolled  " + dx + " , " + dy);
            }
        });
        initDataRecyclerView();

        //
        mSwipeToLoadLayout.setTopView(findViewById(R.id.first_view));

        initFilter();
    }

    private void initTopBar() {
        mTopBar.setMiddleName("下拉刷新");

        //
        mTopBar.setOnBackBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doExitActivity();
            }
        });
    }

    private void initSwipeRefresh() {
        mSwipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                SunLog.i(TAG, "SwipeToLoadLayout  onRefresh");
                mSwipeToLoadLayout.setRefreshing(false);
            }
        });
    }

    private void initSwipeRecyclerView() {
        mSwipeRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        mSwipeRecyclerView.setHasFixedSize(true);

        mSwipeAdapter = new CityAdapter(CityModelKt.buildCityTipData());
        mSwipeRecyclerView.setAdapter(mSwipeAdapter);
    }

    private void initDataRecyclerView() {

        mDataRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        mDataRecyclerView.setHasFixedSize(true);

        mDataAdapter = new CityAdapter(this, CityModelKt.buildCityData());
        mDataRecyclerView.setAdapter(mDataAdapter);
    }

    private void initFilter() {

        findViewById(R.id.filter_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataAdapter.setList(CityModelKt.buildCityData(1));
                mDataRecyclerView.scrollToPosition(0);
            }
        });

        findViewById(R.id.filter_sheng).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataAdapter.setList(CityModelKt.buildCityData(2));
                mDataRecyclerView.scrollToPosition(0);
            }
        });

        findViewById(R.id.filter_other).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataAdapter.setList(CityModelKt.buildCityData(3));
                mDataRecyclerView.scrollToPosition(0);
            }
        });

    }
}
