package com.sunny.stu.module.cs

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.sunny.stu.module.cs.client.ClientActivity
import com.sunny.stu.module.cs.service.ServiceActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun clickMpc(view: View?) {
        startActivity(Intent(this, ServiceActivity::class.java))
    }

    fun clickHpc(view: View?) {
        startActivity(Intent(this, ClientActivity::class.java))
    }

}