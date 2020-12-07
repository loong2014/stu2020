package com.sunny.family.搜索框

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.sunny.family.R
import com.sunny.lib.base.BaseActivity
import com.sunny.lib.router.RouterConstant
import com.sunny.lib.utils.SunLog
import kotlinx.android.synthetic.main.act_search.*

/**
 * Created by zhangxin17 on 2020/12/24
 * https://www.cnblogs.com/yueshangzuo/p/8685810.html
 */
@Route(path = RouterConstant.PageSearchView)
class SearchViewActivity : BaseActivity() {

    companion object {
        const val TAG = "Sunny-SearchView"
    }

    @JvmField
    @Autowired(name = RouterConstant.Param.Key)
    var queryKey: String? = null

    lateinit var mSearchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_search)

        ARouter.getInstance().inject(this)

        SunLog.i(TAG, "param from route , queryKey :$queryKey")

        initView()
    }

    private fun initView() {

        //
        search_view1.isIconified = false

        //
        search_view3.onActionViewExpanded()

        //
        mSearchView = search_view
        mSearchView.setIconifiedByDefault(false)
        initSearchView()
    }

    private fun initSearchView() {
        //搜索框输入和提交事件监听
        mSearchView.queryHint = "搜索大神"
        val searchAutoComplete: SearchView.SearchAutoComplete = search_view.findViewById(R.id.search_src_text)
        searchAutoComplete.highlightColor = resources.getColor(R.color.black)
        searchAutoComplete.setTextColor(resources.getColor(android.R.color.background_light));
        searchAutoComplete.textSize = resources.getDimension(R.dimen.sp_14)

        mSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            // 软键盘点击搜素后触发
            override fun onQueryTextSubmit(query: String?): Boolean {
                trySearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                doTextChange(newText)
                return true
            }
        })

        // 监听拦截搜索框右侧X按钮的点击事件
        mSearchView.findViewById<View>(R.id.search_close_btn).setOnClickListener {
            search_view.isIconified = true // 清空搜索框
            trySearch(null)
        }
//
//
        // 搜索框左侧搜索图标点击事件监听
        val searchIconView: View = mSearchView.findViewById(R.id.search_mag_icon);
        searchIconView.isClickable = true
        searchIconView.setOnClickListener {
            val query = mSearchView.query?.toString()
            trySearch(query)
        }

        //
        search_cancel.setOnClickListener {
            search_view.isIconified = true
            trySearch(null)
        }

//        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextChange(String queryText) {
//
//
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextSubmit(String queryText) {
//                //更新新的数组信息
//                receive_putsite_list = receiveList;
//
//                //重新刷新listview组件
//                myAdapter.notifyDataSetChanged();
//
//                return true;
//            }
//        });
    }

    private fun trySearch(key: String?) {

    }

    private fun doTextChange(key: String?) {
        if (key.isNullOrBlank()) {
            SunLog.d(TAG, "doTextChange  isBlank")
            return
        }

    }

    private fun doClearText() {

    }

}