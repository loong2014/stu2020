package com.sunny.family.loading

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.sunny.family.R
import com.sunny.lib.base.BaseActivity
import com.sunny.lib.router.RouterConstant
import kotlinx.android.synthetic.main.act_loading.*

/**
 * Created by zhangxin17 on 2020/11/25
 */
@Route(path = RouterConstant.PageLoading)
class LoadingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_loading)

        // 1
        btn_round.setOnClickListener {
            if (progress_bar_round.visibility == View.VISIBLE) {
                progress_bar_round.visibility = View.GONE
                btn_round.text = "开始加载"
            } else {
                progress_bar_round.visibility = View.VISIBLE
                btn_round.text = "正在加载"
            }
        }

        //
        btn_horizontal.setOnClickListener {
            var pos = progress_bar_horizontal.progress + 10
            if (pos > 100) {
                pos = 0
            }

            progress_bar_horizontal.progress = pos
            progress_bar_horizontal.secondaryProgress = pos / 2
        }
    }
}