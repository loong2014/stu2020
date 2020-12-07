package com.sunny.family.通知

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.sunny.family.R
import com.sunny.family.sensor.SensorActivity
import com.sunny.family.service.SunIntentService
import com.sunny.lib.base.BaseActivity
import com.sunny.lib.router.RouterConstant
import kotlinx.android.synthetic.main.act_notification.*

/**
 * Created by zhangxin17 on 2020/12/9
 */
@Route(path = RouterConstant.PageNotification)
class NotificationActivity : BaseActivity() {

    private lateinit var manager: NotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_notification)

        initNotify()

        // 取消通知，id=要取消通知
//        manager.cancel(1)

        btn_1.setOnClickListener {

            // 点击通知都的动作
            val intent = Intent(this, SensorActivity::class.java)
            val pi = PendingIntent.getActivity(this, 0, intent, 0)

            // 创建一个通知，必须使用已经创建的通知渠道，channelId = id
            val notification = NotificationCompat.Builder(this, "normal")
                    .setContentTitle("普通消息")
                    .setContentText("消息内容只能显示一行，" +
                            "Hello Sunny!,Hello Sunny!,Hello Sunny!," +
                            "Hello Sunny!,Hello Sunny!,Hello Sunny!" +
                            "Hello Sunny!,Hello Sunny!,Hello Sunny!" +
                            "Hello Sunny!,Hello Sunny!,Hello Sunny!")
                    .setSmallIcon(R.drawable.ic_nav_score_unselected)
                    .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.icon_gift))
                    .setContentIntent(pi) // 设置点击后的处理
                    .setAutoCancel(true) // 点击后，通知消失
                    .build()

            // 发送通知，不同的消息，id必须不一样，一样的id会覆盖
            manager.notify(1, notification)
        }

        btn_2.setOnClickListener {
            val notification = NotificationCompat.Builder(this, "normal")
                    .setContentTitle("多行消息")
                    .setStyle(NotificationCompat.BigTextStyle()
                            .bigText("消息内容可以显示多行," +
                                    "Hello Sunny!,Hello Sunny!,Hello Sunny!," +
                                    "Hello Sunny!,Hello Sunny!,Hello Sunny!," +
                                    "Hello Sunny!,Hello Sunny!,Hello Sunny!," +
                                    "Hello Sunny!,Hello Sunny!,Hello Sunny!")
                    )
                    .setSmallIcon(R.drawable.ic_nav_score_unselected)
                    .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.icon_gift))
                    .build()
            manager.notify(2, notification)
        }

        btn_3.setOnClickListener {
            val notification = NotificationCompat.Builder(this, "normal")
                    .setContentTitle("图片消息")
                    .setStyle(NotificationCompat.BigPictureStyle()
                            .bigPicture(BitmapFactory.decodeResource(resources, R.drawable.top_bg))
                    )
                    .setSmallIcon(R.drawable.ic_nav_score_unselected)
                    .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.icon_gift))
                    .build()
            manager.notify(3, notification)
        }

        //
        btn_4.setOnClickListener {

            // 创建一个通知，必须使用已经创建的通知渠道，channelId = id
            val notification = NotificationCompat.Builder(this, "high")
                    .setContentTitle("重要消息")
                    .setContentText("消息内容只能显示一行，" +
                            "Hello Sunny!,Hello Sunny!,Hello Sunny!," +
                            "Hello Sunny!,Hello Sunny!,Hello Sunny!" +
                            "Hello Sunny!,Hello Sunny!,Hello Sunny!" +
                            "Hello Sunny!,Hello Sunny!,Hello Sunny!")
                    .setSmallIcon(R.drawable.ic_nav_score_unselected)
                    .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.icon_gift))
                    .setAutoCancel(true) // 点击后，通知消失
                    .build()

            // 发送通知，不同的消息，id必须不一样，一样的id会覆盖
            manager.notify(1, notification)
        }

        btn_5.setOnClickListener {
            //
            val service = ARouter.getInstance().build(RouterConstant.ServiceNotification).navigation() as NotifyService
            service.showNotify("hello ARouter service")

            //
//            val intent = Intent(this, SunIntentService::class.java)
//            startService(intent)
        }

    }



    private fun initNotify() {
        manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // id:渠道id
            // name:渠道名称
            // 重要等级:High,default,low,min，不同等级决定不同的行为。用户可更改重要等级
            val channel = NotificationChannel("normal", "Normal", NotificationManager.IMPORTANCE_DEFAULT)
            val channel2 = NotificationChannel("high", "High", NotificationManager.IMPORTANCE_HIGH)

            // 创建一个id=normal的通知渠道
            manager.createNotificationChannel(channel)
            manager.createNotificationChannel(channel2)
        }
    }
}