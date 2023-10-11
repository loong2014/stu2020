//package com.sunny.module.stu.BAndroid.F系统服务;
//
//import android.app.Activity;
//import android.view.Window;
//import android.view.WindowManager;
//
//import com.sunny.module.stu.base.StuImpl;
//
//public class Stu_DisplayManager extends StuImpl {
//    @Override
//    public void a_是什么() {
//        // DisplayManager 是 Android 系统的一个服务，它提供了一种接口，可以查询系统中可用的显示设备，如内置屏幕、HDMI 连接的电视或者投影仪等。
//    }
//
//    void 分屏显示() {
//        /*
//
//        以下是一些关于如何适配分屏模式的建议：
//
//        声明应用支持分屏模式：在你的应用的 AndroidManifest.xml 文件中，你需要在 <application> 或 <activity> 标签中添加 android:resizeableActivity="true" 属性，以声明你的应用支持分屏模式。
//
//        处理生命周期改变：当用户进入或退出分屏模式时，你的应用的生命周期会发生改变。你需要确保你的应用可以正确处理这些生命周期事件，例如保存和恢复状态。
//
//        适应不同的屏幕尺寸和布局：在分屏模式下，你的应用的可视区域会变小。你需要确保你的布局可以适应这种变化。你可以使用 Android 的布局系统（例如 ConstraintLayout）来创建灵活的布局，或者创建不同的布局资源，以适应不同的屏幕尺寸和方向。
//
//        检查和处理多窗口模式：你可以使用 Activity.isInMultiWindowMode() 方法来检查你的应用是否处于分屏模式。如果需要，你可以在你的应用进入或退出分屏模式时更改你的用户界面或行为。
//
//
//         */
//
//    }
//
//    /*
//    private var curPresentation: Presentation? = null
//    private val displayListener by lazy {
//        object : DisplayManager.DisplayListener {
//            override fun onDisplayAdded(displayId: Int) {
//                // 处理显示设备被添加的情况
//            }
//
//            override fun onDisplayChanged(displayId: Int) {
//                // 处理显示设备状态改变的情况
//            }
//
//            override fun onDisplayRemoved(displayId: Int) {
//                // 处理显示设备被移除的情况
//            }
//        }
//    }
//
//    private fun debugHideDisplay() {
//        curPresentation?.dismiss()
//
//        val displayManager = getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
//        displayManager.unregisterDisplayListener(displayListener)
//        curPresentation = null
//    }
//
//    private fun debugShowDisplay() {
//        if (curPresentation == null) {
//            val displayManager = getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
//            displayManager.displays?.getOrNull(1)?.let { secondaryDisplay ->
//                    curPresentation = object : Presentation(this, secondaryDisplay) {
//                override fun onCreate(savedInstanceState: Bundle?) {
//                    super.onCreate(savedInstanceState)
//
//                    setContentView(R.layout.activity_more_data_loading)
//                }
//            }
//            }
//            displayManager.registerDisplayListener(displayListener, null)
//        }
//        curPresentation?.show()
//    }
//    */
//
//}
