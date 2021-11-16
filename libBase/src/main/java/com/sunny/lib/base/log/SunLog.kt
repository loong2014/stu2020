package com.sunny.lib.base.log

import android.util.Log

object SunLog {

    private val playerTag = "PlayerTag-"

    @JvmStatic
    fun buildPlayerTag(subTag: String): String {
        return playerTag + subTag
    }

    private val I: Int = 1
    private val D: Int = 2
    private val E: Int = 3

    @JvmStatic
    fun i(tag: String, msg: String) {
        printLog(I, tag, msg)
    }

    @JvmStatic
    fun d(tag: String, msg: String) {
        printLog(D, tag, msg)
    }

    @JvmStatic
    fun e(tag: String, msg: String) {
        printLog(E, tag, msg)
    }

    private fun printLog(type: Int, tag: String, msg: String) {
        val logTag = "sunny-$tag"
        when (type) {
            I -> Log.i(logTag, msg)
            D -> Log.d(logTag, msg)
            E -> Log.e(logTag, msg)
        }
    }

    /**
     * 打印当前方法的调用栈
     */
    fun showStackTrack(tag: String) {
        val array = Thread.currentThread().stackTrace
        val sb = StringBuilder()
        sb.append("\n======>begin")
        for (ele in array) {
            sb.append("\n===>")
            sb.append(ele.toString())
        }
        sb.append("\n======>end\n")
        Log.e(tag, sb.toString())
    }
}