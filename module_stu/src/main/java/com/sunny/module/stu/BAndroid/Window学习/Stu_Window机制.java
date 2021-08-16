package com.sunny.module.stu.BAndroid.Window学习;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

import com.sunny.module.stu.base.StuImpl;

/**
 *
 * Android全面解析之Window机制:
 * https://blog.csdn.net/weixin_43766753/article/details/108350589
 *
 * Android View 绘制流程之 DecorView 与 ViewRootImpl:
 * https://www.cnblogs.com/huansky/p/11911549.html
 */
public class Stu_Window机制 extends StuImpl {
    @Override
    public void a_是什么() {
        // window 机制就是为了管理屏幕上的view的显示以及触摸事件的传递问题。
        /*
        那什么是window，在Android的window机制中，每个view树都可以看成一个window。
        为什么不是每个view呢？因为view树中每个view的显示次序是固定的，
        例如我们的Activity布局，每一个控件的显示都是已经安排好的，对于window机制来说，属于“不可再分割的view”。

         */
        // 抽象类
        Window window;
        // 实现类：PhoneWindow


        // 接口
        WindowManager windowManager;
        // 实现类：WindowManagerImpl
        // 操作类：WindowManagerGlobal

        Activity activity;
    }


    void View树() {
    /*
    什么是view树？例如你在布局中给Activity设置了一个布局xml，那么最顶层的布局如LinearLayout就是view树的根，
    他包含的所有view就都是该view树的节点，所以这个view树就对应一个window。

    view树（后面使用view代称，后面我说的view都是指view树）是window机制的操作单位，每一个view对应一个window，
        view是window的存在形式，window是view的载体，
    我们平时看到的应用界面、dialog、popupWindow以及上面描述的悬浮窗，都是window的表现形式。
    注意，我们看到的不是window，而是view。**window是view的管理者，同时也是view的载体。
    他是一个抽象的概念，本身并不存在，view是window的表现形式。**这里的不存在，指的是我们在屏幕上是看不到window的.

     */
    }

}
