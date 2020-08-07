package com.sunny.family.home

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.sunny.family.R
import com.sunny.family.arcode.QrCodeActivity
import com.sunny.family.camera.CameraCustomActivity
import com.sunny.family.camera.CameraSysActivity
import com.sunny.family.flutter.StuFlutterActivity
import com.sunny.family.image.GaoSiActivity
import com.sunny.family.jiaozi.JzPlayerActivity
import com.sunny.family.livedata.MyLiveData
import com.sunny.lib.base.BaseActivity
import com.sunny.lib.jump.PageJumpUtils
import com.sunny.lib.jump.params.JumpPlayerParam
import com.sunny.lib.utils.SunLog
import com.sunny.player.config.PlayerConfig
import com.sunny.player.config.VideoType
import kotlinx.android.synthetic.main.act_home.*

class HomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_home)

        addListener()
        liveDataTest()
    }

    lateinit var nameViewModel: MyLiveData

    private fun liveDataTest() {

        nameViewModel = MyLiveData.getInstance(this)

        nameViewModel.observe(this, Observer { num: Int ->
            SunLog.i("MyLiveData", "new num is :" + num)
            live_data_name.text = "name is $num"
        })
    }


    private fun addListener() {

        btn_enter_camera_sys.setOnClickListener {
            startActivity(Intent(this, CameraSysActivity::class.java))
        }

        btn_enter_camera.setOnClickListener {
            startActivity(Intent(this, CameraCustomActivity::class.java))
        }

        btn_enter_photo_album.setOnClickListener {
            PageJumpUtils.jumpPhotoAlbumPage(context = this)
        }

        btn_enter_net_player.setOnClickListener {
            val jumpParams = JumpPlayerParam()
            jumpParams.videoId = PlayerConfig.video1_id
            jumpParams.videoType = VideoType.NETWORK

            PageJumpUtils.jumpPlayerPage(context = this, jumpParams = jumpParams)
        }

        btn_enter_detail.setOnClickListener {
            PageJumpUtils.jumpDetailPage(context = this)
        }

        btn_enter_city.setOnClickListener {
            PageJumpUtils.jumpCityPage(context = this)
        }

        btn_enter_city_expandable.setOnClickListener {
            PageJumpUtils.jumpExpandableCityPage(context = this)
        }

        btn_enter_image.setOnClickListener {
            PageJumpUtils.jumpImagePage(context = this)
        }

        btn_enter_sensor.setOnClickListener {
            PageJumpUtils.jumpSensorPage(context = this)
        }

        btn_enter_dialog.setOnClickListener {
            PageJumpUtils.jumpDialogPage(context = this)
        }

        btn_enter_jzplayer.setOnClickListener {
            startActivity(Intent(this, JzPlayerActivity::class.java))
        }
        btn_enter_trans.setOnClickListener {
            startActivity(Intent(this, GaoSiActivity::class.java))
        }

        btn_enter_main.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        btn_enter_qr.setOnClickListener {
            startActivity(Intent(this, QrCodeActivity::class.java))
        }

        btn_enter_flutter.setOnClickListener {
            startActivity(Intent(this, StuFlutterActivity::class.java))
        }
    }

}