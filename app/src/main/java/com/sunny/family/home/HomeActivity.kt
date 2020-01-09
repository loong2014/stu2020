package com.sunny.family.home

import android.content.Intent
import android.os.Bundle
import com.sunny.family.R
import com.sunny.family.camera.CameraActivity
import com.sunny.lib.base.BaseActivity
import kotlinx.android.synthetic.main.act_home.*

class HomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_home)

        addListener()
    }

    private fun addListener() {
        btn_enter_camera.setOnClickListener {

            startActivity(Intent(this, CameraActivity::class.java))
        }
    }
}