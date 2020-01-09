package com.sunny.lib.utils

import android.util.Log

object SunLog {

    private val I: Int = 1
    private val D: Int = 2
    private val E: Int = 3

    fun i(tag: String, msg: String) {
        printLog(I, tag, msg)
    }

    fun d(tag: String, msg: String) {
        printLog(D, tag, msg)
    }

    fun e(tag: String, msg: String) {
        printLog(E, tag, msg)
    }

    private fun printLog(type: Int, tag: String, msg: String) {
        when (type) {
            I -> Log.i(tag, msg)
            D -> Log.d(tag, msg)
            E -> Log.e(tag, msg)
        }
    }
}