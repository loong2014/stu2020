package com.sunny.module.stu.C性能优化.A启动优化;

// 设置启动窗口的主题
class A视觉优化 {

    void 透明主题() {
    /*
        <style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
            <item name="android:windowFullscreen">true</item>
            <item name="android:windowIsTranslucent">true</item>
        </style>
    */
    }

    void 图片主题() {
    /*
        <style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">
            <item name="android:windowBackground">@drawable/lunch</item>  //闪屏页图片
            <item name="android:windowFullscreen">true</item>
            <item name="android:windowDrawsSystemBarBackgrounds">false</item><!--显示虚拟按键，并腾出空间-->
        </style>
     */
    }

}
