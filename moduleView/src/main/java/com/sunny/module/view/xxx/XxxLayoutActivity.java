package com.sunny.module.view.xxx;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sunny.lib.common.base.BaseActivity;
import com.sunny.lib.common.router.RouterConstant;
import com.sunny.lib.ui.SunnyViewPage;
import com.sunny.module.view.R;

import org.jetbrains.annotations.Nullable;

/**
 * Created by zhangxin17 on 2020/11/19
 */
@Route(path = RouterConstant.View.PageXxx)
public class XxxLayoutActivity extends BaseActivity {

    //
    private BottomNavigationView mBottomNavigationView;

    //
    private SunnyViewPage mViewPage;
    private XxxFragmentAdapter mFragmentAdapter;
    private int mWhichFragment = XxxLayoutConstants.FragmentDetail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_act_xxxlayout);

        initView();
    }

    private void initView() {

        //
        mViewPage = findViewById(R.id.view_page);
        initViewPage();

        //
        mBottomNavigationView = findViewById(R.id.bottom_navigation_view);
        initBottomNavigation();

    }

    private void initBottomNavigation() {

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.navigation_detail) {
                    mViewPage.setCurrentItem(XxxLayoutConstants.FragmentDetail);

                } else if (item.getItemId() == R.id.navigation_coordinator) {
                    mViewPage.setCurrentItem(XxxLayoutConstants.FragmentCoordinator);
                } else if (item.getItemId() == R.id.navigation_drawer) {
                    mViewPage.setCurrentItem(XxxLayoutConstants.FragmentDrawer);
                } else if (item.getItemId() == R.id.navigation_expandable) {
                    mViewPage.setCurrentItem(XxxLayoutConstants.FragmentExpandable);
                } else if (item.getItemId() == R.id.navigation_motion) {
                    mViewPage.setCurrentItem(XxxLayoutConstants.FragmentMotion);
                } else {
                    return false;
                }
                return true;
            }
        });

        mBottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {

            }
        });

        int[][] colorState = new int[][]{
                new int[]{-android.R.attr.state_checked}, // "-" 代表未选中
                new int[]{android.R.attr.state_checked}
        };

        int[] colors = new int[]{
                getResources().getColor(R.color.black_30),
                getResources().getColor(R.color.colorRed)
        };

        ColorStateList colorStateList = new ColorStateList(colorState, colors);

        mBottomNavigationView.setItemIconTintList(null);
        mBottomNavigationView.setItemTextColor(colorStateList);

        updateSelectedItemId(mWhichFragment);
    }

    private void updateSelectedItemId(int position) {
        int fragmentId;
        switch (position) {
            case XxxLayoutConstants.FragmentDetail:
                fragmentId = R.id.navigation_detail;
                break;

            case XxxLayoutConstants.FragmentDrawer:
                fragmentId = R.id.navigation_drawer;
                break;

            case XxxLayoutConstants.FragmentCoordinator:
                fragmentId = R.id.navigation_coordinator;
                break;

            case XxxLayoutConstants.FragmentMotion:
                fragmentId = R.id.navigation_motion;
                break;

            case XxxLayoutConstants.FragmentExpandable:
                fragmentId = R.id.navigation_expandable;
                break;

            default:
                fragmentId = R.id.navigation_detail;
                break;
        }
        mBottomNavigationView.setSelectedItemId(fragmentId);
    }

    private void initViewPage() {
        mFragmentAdapter = new XxxFragmentAdapter(getSupportFragmentManager());
        mViewPage.setAdapter(mFragmentAdapter);
        mViewPage.setOffscreenPageLimit(mFragmentAdapter.getCount());
        mViewPage.setCurrentItem(mWhichFragment);

        mViewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateSelectedItemId(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
