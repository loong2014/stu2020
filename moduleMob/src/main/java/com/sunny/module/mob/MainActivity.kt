package com.sunny.module.mob

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cn.sharesdk.onekeyshare.OnekeyShare
import com.mob.MobSDK
import com.sunny.module.mob.databinding.ActivityMainBinding


/**
 * MobPush
 * https://www.mob.com/wiki/detailed?wiki=513&id=136
 */
class MainActivity : AppCompatActivity() {
    private lateinit var mActivityBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // 用户同意隐私协议
        MobSDK.submitPolicyGrantResult(true)

        mActivityBinding.btnShare.setOnClickListener {
            doShare()
        }
    }

    fun doShare() {
        val oks = OnekeyShare()
        //关闭sso授权
        oks.disableSSOWhenAuthorize()

        oks.apply {
            setTitle("分享")
            text = "没什么好说的"
            setImagePath("https://download.sdk.mob.com/2022/07/11/10/165750565813922.22.jpeg")
        }
        oks.show(this)
    }
}