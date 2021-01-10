package com.sunny.module.web.http

interface SunnyHttpCallback {
    fun onFinish(response: String)
    fun onError(e: Exception)
}