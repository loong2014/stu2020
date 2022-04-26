package com.sunny.module.ble.app

import com.sunny.lib.common.base.BaseApplication

class BleApplication : BaseApplication() {

    override fun getLogTag(): String {
        return "PaxBle-"
    }
}