package com.sunny.module.view.image

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.sunny.lib.common.base.BaseActivity
import com.sunny.lib.common.router.RouterConstant
import com.sunny.module.view.R
import com.sunny.module.view.databinding.ViewHomeImageviewBinding

@Route(path = RouterConstant.View.PageImageView)
class StuImageViewActivity : BaseActivity() {

    lateinit var mBinding: ViewHomeImageviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.view_home_imageview)

        initView()
    }

    private fun initView() {

        mBinding.btnOpt.setOnClickListener {
            mBinding.apply {
                img1.setImageResource(R.drawable.mock_img)
                img2.setImageResource(R.drawable.mock_mask)
                img3.setImageResource(R.drawable.mock_img)
                img31.setImageResource(R.drawable.mock_mask)
            }

            mBinding.imageOpt.mask(R.drawable.mock_img, R.drawable.mock_mask)
        }
    }

}