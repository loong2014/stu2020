package com.sunny.lib.common.mvvm

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData

/**
 */

object LiveDataUtils {

    private val sMainHandler by lazy {
        Handler(Looper.getMainLooper())
    }

    fun <T> postSetValue(liveData: MutableLiveData<T>, newValue: T?) {
        sMainHandler.post(SetValueRunnable.create(liveData, newValue))
    }

    internal class SetValueRunnable<T> private constructor(
        private val liveData: MutableLiveData<T>,
        private val newValue: T?
    ) : Runnable {

        override fun run() {
            liveData.value = newValue
        }

        companion object {
            fun <T> create(liveData: MutableLiveData<T>, data: T?): SetValueRunnable<T> {
                return SetValueRunnable(liveData, data)
            }
        }
    }
}