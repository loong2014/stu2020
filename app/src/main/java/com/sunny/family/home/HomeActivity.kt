package com.sunny.family.home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.sunny.family.R
import com.sunny.family.camera.CameraActivity
import com.sunny.family.camera.CameraSysActivity
import com.sunny.lib.base.BaseActivity
import kotlinx.android.synthetic.main.act_home.*

class HomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_home)

        addListener()
    }

    private fun addListener() {

        btn_enter_camera_sys.setOnClickListener {
            startActivity(Intent(this, CameraSysActivity::class.java))
        }

        btn_enter_photo_album_sys.setOnClickListener {

            val intent = Intent()
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                intent.action = Intent.ACTION_GET_CONTENT
            } else {
                intent.action = Intent.ACTION_OPEN_DOCUMENT
            }
            startActivity(intent)
        }

        btn_enter_camera.setOnClickListener {

            startActivity(Intent(this, CameraActivity::class.java))
        }
    }

}