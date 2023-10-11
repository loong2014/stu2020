//package com.sunny.module.stu.BAndroid.Window学习;
//
//import android.app.Activity;
//import android.view.Window;
//import android.view.WindowManager;
//
//import com.sunny.module.stu.base.StuImpl;
//
///**
// * Android全面解析之Window机制:
// * https://blog.csdn.net/weixin_43766753/article/details/108350589
// * <p>
// * Android View 绘制流程之 DecorView 与 ViewRootImpl:
// * https://www.cnblogs.com/huansky/p/11911549.html
// */
//public class Stu_Window机制 extends StuImpl {
//    @Override
//    public void a_是什么() {
//        // window 机制就是为了管理屏幕上的view的显示以及触摸事件的传递问题。
//        /*
//        那什么是window，在Android的window机制中，每个view树都可以看成一个window。
//        为什么不是每个view呢？因为view树中每个view的显示次序是固定的，
//        例如我们的Activity布局，每一个控件的显示都是已经安排好的，对于window机制来说，属于“不可再分割的view”。
//
//         */
//        // 抽象类
//        Window window;
//        // 实现类：PhoneWindow
//
//
//        // 接口
//        WindowManager windowManager;
//        // 实现类：WindowManagerImpl
//        // 操作类：WindowManagerGlobal
//
//        Activity activity;
//    }
//
//    void addView() {
//        /*
//        1. WindowManagerImpl.addView ->
//        2. WindowManagerGlobal.addView{
//            ViewRootImpl root = new ViewRootImpl(view.getContext(), display);
//            root.setView(view, wparams, panelParentView);
//        }
//        3. ViewRootImpl.setView{
//            mWindowSession = WindowManagerGlobal.getWindowSession();
//             this.mSurfaceHolder = new TakenSurfaceHolder(); // 建立与底层Surface的交互，进行绘制操作
//            requestLayout->scheduleTraversals->doTraversal->performTraversals->(measureHierarchy,performLayout,performDraw)
//            mWindowSession.addToDisplay->addWindow
//
//            Choreographer mChoreographer;
//            mChoreographer的doFrame回调(收到ASYNC信号后会触发)中调用doTraversal
//        }
//        4. WindowManagerService.addWindow{
//            mWindowMap.put(client.asBinder(), win)
//        }
//        5. SurfaceFlinger 通过ASYNC信号
//         */
//    }
//
//    void 解析PerformTraversals() {
//        // performTraversals方法是负责测量、布局和绘制视图树的主要方法。在这个方法中，onLayout方法的调用是在布局阶段进行的。
//        /*
//        public void setView(View view, WindowManager.LayoutParams attrs, View panelParentView) {
//            this.mView = view;
//            requestLayout->scheduleTraversals->doTraversal->performTraversals
//        }
//        */
//
//        View host = this.mView;
//        if (this.mFirst) {
//            host.dispatchAttachedToWindow(this.mAttachInfo, 0);
//        }
//        if (viewVisibilityChanged) {
//            host.dispatchWindowVisibilityChanged(viewVisibility);
//        }
//        if (layoutRequested) {
//            measureHierarchy()->performMeasure()->mView.measure() //  测量
//        }
//
//        hwInitialized = layoutRequested && (!this.mStopped || this.mReportNextDraw);
//        if (hwInitialized) {
//            this.performLayout(lp, this.mWidth, this.mHeight)->mView.requestLayout() //布局
//        }
//        this.mFirst = false;
//
//
//        public final Surface mSurface = new Surface();//ViewRootImpl类内定义的常量
//
//        performDraw()->draw()->{
//            Surface surface = this.mSurface;
//            if (this.mAttachInfo.mThreadedRenderer != null && this.mAttachInfo.mThreadedRenderer.isEnabled()) {
//                mAttachInfo.mThreadedRenderer.draw(this.mView, this.mAttachInfo, this)->{
//                    Choreographer choreographer = attachInfo.mViewRootImpl.mChoreographer;
//                    updateRootDisplayList(view, callbacks)->updateViewTreeDisplayList(view)->view.updateDisplayListIfDirty()->{
////                        public View(Context context) {
////                            this.mRenderNode = RenderNode.create(this.getClass().getName(), new ViewAnimationHostBridge(this));
////                        }
//                        RenderNode renderNode = this.mRenderNode;
//                        RecordingCanvas canvas = renderNode.beginRecording(width, height); // RecordingCanvas
//                        if ((this.mPrivateFlags & 128) == 128) {
//                            // View 所在数据发生变化，需要更新
//                            this.dispatchDraw(canvas);->View的子类实现
//                        } else {
//                            this.draw(canvas);
//                        }
//                    }
//                    syncAndDrawFrame(choreographer.mFrameInfo); // 硬件绘制
//                }
//            } else {
//                drawSoftware(surface, this.mAttachInfo, xOffset, yOffset, scalingRequired, dirty, surfaceInsets)->
//                {
//                    canvas = this.mSurface.lockCanvas(dirty); //
//                    this.mView.draw(canvas); // 绘制页面，没有硬件绘制
//                }
//            }
//
//        }
//    }
//    void View_draw(){
//        this.onDraw(canvas); // 子类实现
//        this.dispatchDraw(canvas); // 子类实现
//        this.drawAutofilledHighlight(canvas);
//        this.onDrawForeground(canvas);
//    }
//
//    void 硬件加速(){
//        // 应用级
//            // <application android:hardwareAccelerated="true"
//        // actvity
//            // <activity android:hardwareAccelerated="true"
//        // window
//            // window.setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED)
//            // window.clearFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED)
//        // view
//            // view.setLayerType(View.LAYER_TYPE_HARDWARE, null)
//            // view.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
//
//        // ThreadedRenderer 相对于 HardwareRenderer 的改进之处在于它将渲染操作移动到了一个独立的线程上
//        // RenderNode 提供了一种有效的方式来记录和存储绘图命令，使得硬件渲染器可以将这些命令发送到GPU进行处理
//        public void setView(View view, WindowManager.LayoutParams attrs, View panelParentView) {
//            enableHardwareAcceleration()->{
//                this.mAttachInfo.mThreadedRenderer = ThreadedRenderer.create(this.mContext, translucent, attrs.getTitle().toString());
//            }
//        }
//    }
//
//    void View树() {
//    /*
//    什么是view树？例如你在布局中给Activity设置了一个布局xml，那么最顶层的布局如LinearLayout就是view树的根，
//    他包含的所有view就都是该view树的节点，所以这个view树就对应一个window。
//
//    view树（后面使用view代称，后面我说的view都是指view树）是window机制的操作单位，每一个view对应一个window，
//        view是window的存在形式，window是view的载体，
//    我们平时看到的应用界面、dialog、popupWindow以及上面描述的悬浮窗，都是window的表现形式。
//    注意，我们看到的不是window，而是view。**window是view的管理者，同时也是view的载体。
//    他是一个抽象的概念，本身并不存在，view是window的表现形式。**这里的不存在，指的是我们在屏幕上是看不到window的.
//
//     */
//    }
//
//}
