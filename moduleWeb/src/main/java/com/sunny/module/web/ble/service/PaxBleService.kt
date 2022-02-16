package com.sunny.module.web.ble.service

import com.sunny.module.web.ble.PaxBleServiceBase

/**
 * 低功耗蓝牙
 * https://developer.android.com/guide/topics/connectivity/bluetooth-le?hl=zh-cn
 *
 * macbook : ac:bc:32:97:bc:38
 * Redmi K40 : 9C:5A:81:2F:19:39
 * FF 91 Driver : 22:22:ed:16:c2:52
 */
class PaxBleService : PaxBleServiceBase() {
    private var mScanning: Boolean = false


}