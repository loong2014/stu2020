package com.sunny.module.stu.thread;

import com.sunny.module.stu.R;
import com.sunny.module.stu.base.StuBaseFragment;

public class ThreadFragment extends StuBaseFragment {

    @Override
    protected int buildLayoutResID() {
        return R.layout.fragment_stu_thread;
    }

    private void doThread() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
