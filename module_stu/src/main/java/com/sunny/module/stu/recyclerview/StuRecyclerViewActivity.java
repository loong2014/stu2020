package com.sunny.module.stu.recyclerview;

import com.sunny.module.stu.R;
import com.sunny.module.stu.base.StuBaseActivity;

public class StuRecyclerViewActivity extends StuBaseActivity {

    // https://blog.csdn.net/Derbe/article/details/106122206

 //    https://www.jianshu.com/p/4820cbb908ef
//    https://note.youdao.com/ynoteshare1/index.html?id=7a1334c5522ce212c2da799c0db825b1&type=note
//    通过isComputingLayout()方法判断是否在计算
//
//    通过getScrollState()是否等于RecyclerView.SCROLL_STATE_IDLE（结束滑动状态）判断是否正在滑动
    @Override
    protected int buildLayoutResID() {
        return R.layout.act_stu_rv_main;
    }

    @Override
    protected void initView() {
        initTopBarView("RecyclerView");
    }
}
