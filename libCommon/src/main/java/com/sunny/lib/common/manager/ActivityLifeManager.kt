package com.sunny.lib.common.manager

import android.app.Activity

object ActivityLifeManager {

    private val activityList = mutableListOf<Activity>()


    fun getTopActivity(): Activity {
        return activityList.last()
    }

    fun getActivity(clz: Class<*>): Activity? {

        for (act in activityList) {
            if (act.javaClass.isAssignableFrom(clz)) {
                return act
            }
        }
        return null
    }

    fun finishAll() {
        for (act in activityList) {
            act.finish()
        }
    }

    fun onCreate(activity: Activity) {
        activityList.add(activity)
    }

    fun onDestroy(activity: Activity) {
        activityList.remove(activity)
    }

}