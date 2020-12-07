package com.sunny.family.泛型

import android.content.Context
import android.content.Intent
import com.sunny.family.sensor.SensorActivity

/**
 * Created by zhangxin17 on 2020/12/10
 */
/**
 * 泛型实化
 */
inline fun <reified T> startActivity(context: Context, block: Intent.() -> Unit) {
    val intent = Intent(context, T::class.java)
    intent.block()
    context.startActivity(intent)
}

class Test {

    fun doTest(context: Context) {
        startActivity<SensorActivity>(context) {
            putExtra("name", "zhang")
        }
    }
}
