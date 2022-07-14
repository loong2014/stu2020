package com.sunny.module.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.sunny.lib.common.base.BaseActivity
import com.sunny.lib.common.router.RouterConstant
import com.sunny.lib.common.utils.SunNumberUtils
import com.sunny.lib.common.utils.showSnckbar
import com.sunny.lib.common.utils.showToast
import com.sunny.module.view.bar.SystemBarTintManager
import com.sunny.module.view.databinding.ViewActivityDemoBinding
import kotlinx.android.synthetic.main.view_activity_demo.*
import kotlinx.coroutines.*
import timber.log.Timber

/**
 * Created by zhangxin17 on 2020/12/29
 */
@Route(path = RouterConstant.View.PageDemo)
class LayoutDemoActivity : BaseActivity() {

    lateinit var mActBinding: ViewActivityDemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SystemBarTintManager.translucentStatus(this)

        mActBinding = DataBindingUtil.setContentView(this, R.layout.view_activity_demo)

        SystemBarTintManager.setViewPaddingTopStatusBar(this, toolBar)

        setSupportActionBar(toolBar)

        initView()
    }

    private fun doTest() {
        lifecycleScope.launch {
            val name = withContext(Dispatchers.IO) {
                Thread.sleep(2_000)
                "张三"
            }
            showLog("获取到名字 :$name")

            val info = withContext(Dispatchers.IO) {
                showLog("根据名字获取个人信息 :$name")
                Thread.sleep(2_000)
                "张三 - 18"
            }

            showLog("获取到个人信息 :$info")
        }
    }

    private fun doTest22() {
        runBlocking {
//        lifecycleScope.launch {
            showLog("启动倒计时")
            withTimeout(2_000) {
                repeat(5) { i ->
                    showLog("倒计时 :${5 - i}")
                    delay(1_000)
                }
            }
            showLog("倒计时结束")
        }
    }

    /**
     * 同时请求多个
     */
    private fun doTest3() {
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            e.message?.let {
                showLog("异常信息: $it")
            }
        }

        val scope = lifecycleScope
        val job1 = scope.launch(exceptionHandler) {
            var name: String
            var age: Int

            withContext(Dispatchers.IO) {
                Thread.sleep(2_000)
                name = "张三"
            }
            withContext(Dispatchers.IO) {
                Thread.sleep(3_000)
                age = 18
            }

            showLog("name($name) , age($age)")
        }

//
//
//            coroutineScope {
////                supervisorScope {
////                    launch {
////
////                        showLog("A走在路上")
////                        showLog("A要休息5s")
////                        try {
////                            repeat(5) {
////                                delay(1000)
////                                // 检查是否被取消
////                                ensureActive()
////                                showLog("已经休息了 ${it + 1} 秒")
////                            }
////                            showLog("A休息好了")
////                        } finally {
////                            withContext(NonCancellable) {
////                                showLog("A遇到异常了 :$this")
////                            }
////                        }
////
////                        showLog("A接着赶路")
////
////                    }
////                    launch {
////                        throw Exception("发生异常了")
////                    }
////                }
//
////                runCatching {
//                showLog("runCatching")
//                throw Exception("发生异常了")
////                }.onSuccess {
////                    showLog("onSuccess")
////                }.onFailure {
////                    showLog("onFailure")
////                }
//            }
//        }

//
//        val job2 = scope.launch(Dispatchers.IO) {
//            showLog("2秒后取消协程")
//            delay(2_000)
//            showLog("取消协程")
//            // job2的异常会导致job1被取消
//            supervisorScope {
//                throw Exception("发生异常了")
//            }
//        }
    }

    private fun doTest2() {

        // 协程作用域
        val scope =
            CoroutineScope(Job() + Dispatchers.Main + CoroutineName("SunnyDebug"))
        showLog("CoroutineScope :$scope")

        val job: Job = scope.launch(Dispatchers.Main) {
            showLog("A走在道路上 :$this")
            async {
                showLog("A要休息5s")
                Thread.sleep(5_000)
                // 在runBlocking中不能使用delay
//                delay(5_000)
                showLog("A休息好了")
            }
            showLog("A接着赶路")
        }
    }

    private fun doTest_2() {
        lifecycleScope.launch {

            showLog("开始获取个人信息")
//            supervisorScope {
            coroutineScope {
                val nameJob = async(Dispatchers.IO) {
                    showLog("获取名字的异步操作")
                    delay(2_000)
                    val name = "张三"
                    showLog("获取到了名字：$name")
                    name
                }

                val ageJob = async(Dispatchers.IO) {
                    showLog("获取年龄的异步操作")
                    delay(3_000)
                    val age = 18
                    showLog("获取到了年龄：$age")
                    age
                }

                nameJob.await()
                ageJob.await()

                showLog("获取到了个人信息 : $nameJob >>> $ageJob")
            }
        }
    }

    private fun showLog(log: String) {
        Timber.i("SunnyDebug-${Thread.currentThread()} >>> $log")
    }

    private fun initView() {
        initToolBar()

        initRecyclerView()

        doTest()
    }

    private fun initToolBar() {
        //
        toolBar.title = "模块：控件"
        toolBar.subtitle = "各种控件的使用实战"

        // 更改ToolBar左侧按钮的两种方式
        // 1针对toolBar
//        toolBar.setNavigationIcon(R.drawable.ic_arrow_left_black)
//        // 默认点击触发finish()，可通过下面方法更改click处理
//        toolBar.setNavigationOnClickListener {
//            showToast("重写click事件")
//        }

        // 2针对actionBar
        supportActionBar?.let {
            // 打开toolbar最左侧的默认按钮(Home按钮)，默认图标是一个返回箭头，点击后返回上一个activity
            it.setDisplayHomeAsUpEnabled(true)
            // 更改Home按钮的图片
//            it.setHomeAsUpIndicator(R.drawable.ic_gift)
        }
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        val adapter = LayoutDemoAdapter(this, buildDataList())
        recyclerView.adapter = adapter

        adapter.setItemClickListener(object : DemoItemClickListener {
            override fun onItemClick(model: LayoutDemoModel) {
                ARouter.getInstance().build(model.jumpPath).navigation()
            }
        })
    }

    private fun buildDataList(): List<LayoutDemoModel> {
        val list = mutableListOf<LayoutDemoModel>()
        list.add(LayoutDemoModel(name = "XXXLayout", jumpPath = RouterConstant.View.PageXxx))
        list.add(LayoutDemoModel(name = "DrawerLayout", jumpPath = RouterConstant.View.PageDrawer))
        list.add(
            LayoutDemoModel(
                name = "CoordinatorLayout",
                jumpPath = RouterConstant.View.PageCoordinator
            )
        )
        list.add(LayoutDemoModel(name = "状态栏", jumpPath = RouterConstant.View.PageStatusBar))
        list.add(LayoutDemoModel(name = "BlackBg", jumpPath = RouterConstant.View.PageBarBlack))
        list.add(LayoutDemoModel(name = "CardView", jumpPath = RouterConstant.View.PageCardView))
        list.add(
            LayoutDemoModel(
                name = "TitleUpdate",
                jumpPath = RouterConstant.View.PageTitleUpdate
            )
        )
        list.add(LayoutDemoModel(name = "FontChange", jumpPath = RouterConstant.View.PageFont))
        list.add(
            LayoutDemoModel(
                name = "ScreenInfo",
                jumpPath = RouterConstant.View.PageToolsScreenInfo
            )
        )

//        list.add(
//            LayoutDemoModel(
//                name = "RemoteViewsService",
//                jumpPath = RouterConstant.View.PageRemoteViewsService
//            )
//        )
//        list.add(
//            LayoutDemoModel(
//                name = "RemoteViewsClient",
//                jumpPath = RouterConstant.View.PageRemoteViewsClient
//            )
//        )
        return list
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.view_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add -> {
//                showToast("点击了ToolBar的默认Home按钮")

//                showSnackBar("你确定要点！！！")

                startActivity(Intent(getActivity(), LayoutCommonActivity::class.java))
            }
            else -> {
                showToast(item.title as String)
            }
        }
        return true
    }

    private fun showSnackBar(tip: String) {
        /**
         * 第一个参数只要是当前布局的一个view就行
         */
//        Snackbar.make(toolBar, tip, Snackbar.LENGTH_SHORT)
//                .setAction("Yes") {
//                    showToast("点击了ToolBar的默认Home按钮")
//                }
//                .show()

        toolBar.showSnckbar(tip, "Yes") {

            val a = 10
            val b = 20
            val c = 30

            val max = SunNumberUtils.max(a, b, c)
            "ddd".showToast()
            finish()
        }
    }

}