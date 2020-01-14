package com.sunny.family.home

import android.content.Intent
import android.os.Bundle
import com.sunny.family.R
import com.sunny.family.camera.CameraCustomActivity
import com.sunny.family.camera.CameraSysActivity
import com.sunny.lib.base.BaseActivity
import com.sunny.lib.jump.PageJumpUtils
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

        btn_enter_camera.setOnClickListener {
            startActivity(Intent(this, CameraCustomActivity::class.java))
        }

        btn_enter_photo_album.setOnClickListener {
            PageJumpUtils.jumpPhotoAlbumPage(null, this)
        }
    }

}