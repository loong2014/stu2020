package com.sunny.family.layout;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sunny.family.R;
import com.sunny.family.layout.fragment.XxxFragmentAdapter;
import com.sunny.family.view.SunnyViewPage;
import com.sunny.lib.base.BaseActivity;
import com.sunny.lib.router.RouterConstant;

import org.jetbrains.annotations.Nullable;

/**
 * Created by zhangxin17 on 2020/11/19
 */
@Route(path = RouterConstant.PageXxxLayout)
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
        setContentView(R.layout.act_xxxlayout);

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

                switch (item.getItemId()) {
                    case R.id.navigation_detail:
                        mViewPage.setCurrentItem(XxxLayoutConstants.FragmentDetail);
                        return true;
                    case R.id.navigation_coordinator:
                        mViewPage.setCurrentItem(XxxLayoutConstants.FragmentCoordinator);
                        return true;
                    case R.id.navigation_drawer:
                        mViewPage.setCurrentItem(XxxLayoutConstants.FragmentDrawer);
                        return true;
                    case R.id.navigation_expandable:
                        mViewPage.setCurrentItem(XxxLayoutConstants.FragmentExpandable);
                        return true;
                    case R.id.navigation_motion:
                        mViewPage.setCurrentItem(XxxLayoutConstants.FragmentMotion);
                        return true;

                    default:
                        return false;
                }
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
