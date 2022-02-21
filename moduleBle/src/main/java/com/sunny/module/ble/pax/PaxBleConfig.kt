package com.sunny.module.ble.pax

import java.util.*

/**
 * 常用的uuid格式
 * https://www.cnblogs.com/bulazhang/p/8450172.html
 * new ParcelUuid(UUID.randomUUID())
 */
object PaxBleConfig {


    // 客户端与服务端的UUID需要一致
    val PAX_SERVICE_UUID: UUID by lazy {
        UUID.fromString("db764ac8-4b08-7f25-aafe-59d03c271111")
    }

}