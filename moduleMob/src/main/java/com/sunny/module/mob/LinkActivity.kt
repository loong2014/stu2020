package com.sunny.module.mob

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mob.pushsdk.MobPushUtils
import com.sunny.module.mob.databinding.ActivityLinkBinding

class LinkActivity : AppCompatActivity() {
    private lateinit var mActivityBinding: ActivityLinkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_link)

        initData(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        initData(intent)
    }


    private fun initData(intent: Intent?) {
        if (intent == null) return

        val sb = StringBuilder()
        sb.append("\ndata :${intent.data}")
        intent.toUri(Intent.URI_INTENT_SCHEME)?.let {
            sb.append("\ntoUri :$it")
        }
        intent.data?.let { uri ->
            sb.append("\nscheme :${uri.scheme}")
            sb.append("\nhost :${uri.host}")
            sb.append("\nport :${uri.port}")
            sb.append("\nquery :${uri.query}")
            sb.append("\nport :${uri.port}")
        }
        //获取link界面传输的数据
        MobPushUtils.parseSchemePluginPushIntent(intent)?.let { jsonArray ->
            sb.append("\npushInfo :$jsonArray")
        }

        mActivityBinding.linkInfo.text = sb
    }

}