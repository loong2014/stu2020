package com.sunny.module.stu.面试题;

import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sunny.lib.base.utils.ContextProvider;
import com.sunny.module.stu.base.StuImpl;

/**
 * https://www.jianshu.com/p/555ffeb64e68
 */
public class Touch事件分发 extends StuImpl {
    @Override
    public void a_是什么() {
        /*
            布局中有一个 Button 响应了点击
                down 返回了true
                move 返回了false
            Q：请问 Button 还能响应点击事件么？

            A：不能
         */

        ViewGroup viewGroup = new LinearLayout(ContextProvider.appContext);
        viewGroup.dispatchTouchEvent(null);

    }
}
