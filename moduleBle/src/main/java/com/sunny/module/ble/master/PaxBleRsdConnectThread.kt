package com.sunny.module.ble.master

import android.bluetooth.*
import android.content.Context
import com.sunny.module.ble.BleConfig
import com.sunny.module.ble.PaxBleConfig

class PaxBleRsdConnectThread(context: Context) : PaxBleConnectThread(context, "rsd") {

    private var mmConnectedGatt: BluetoothGatt? = null

    // meeting 信息特征
    private var meetingGattCharacteristic: BluetoothGattCharacteristic? = null

    override fun getFFID(): String? {
        return PaxBleConfig.ServiceFFIDStr
//        return PaxUserTools.userIdConfig.getRsdFFID()
    }

    private fun getAuthGattCharacteristic(): BluetoothGattCharacteristic? {
        return mmConnectedGatt?.getService(PaxBleConfig.getServiceUUID())
            ?.getCharacteristic(PaxBleConfig.getAuthUUID())
    }

    private fun getCalendarGattCharacteristic(): BluetoothGattCharacteristic? {
        return mmConnectedGatt?.getService(PaxBleConfig.getServiceUUID())
            ?.getCharacteristic(PaxBleConfig.getCalendarReadableUUID())
    }

    override fun doConnect(device: BluetoothDevice) {
        try {
            showLog("doConnect ${device.name} >>> $device")
            /*
            连接到此设备托管的 GATT 服务器。调用者充当 GATT 客户端。回调用于将结果传递给调用者，
            例如连接状态以及任何进一步的 GATT 客户端操作。该方法返回一个 BluetoothGatt 实例。
            您可以使用 BluetoothGatt 进行 GATT 客户端操作

            TRANSPORT_AUTO
                GATT 连接到远程双模设备的物理传输没有偏好
            TRANSPORT_BREDR
                首选 BREDR 传输用于 GATT 连接到远程双模设备
            TRANSPORT_LE
                首选 LE 传输用于 GATT 连接到远程双模设备
             */
            device.connectGatt(context, false, gattCallback, BluetoothDevice.TRANSPORT_AUTO)

        } catch (e: Exception) {
            updateConnectState(false)
            doDisConnect()
        }
    }

    override fun doDisConnect() {
        mmConnectedGatt?.discoverServices()
        mmConnectedGatt?.disconnect()
        mmConnectedGatt?.close()
        mmConnectedGatt = null
    }

    private fun buildMeetingInfo(): String {
        return BleConfig.doGetLocalMeetingInfoString(context)
    }

    private var gattCallback = object : BluetoothGattCallback() {

        // 连接设备状态
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            showLog("onConnectionStateChange status($status) , newState($newState)")
            when (newState) {
                // 连接成功
                BluetoothProfile.STATE_CONNECTED -> {
                    showLog("设备连接成功，开始发现服务")
                    mmConnectedGatt = gatt
                    mmConnectedGatt?.discoverServices()
                }

                BluetoothProfile.STATE_DISCONNECTED -> {
                    showLog("设备断开连接")
                    updateConnectState(false)
                    doDisConnect()
                }
            }
        }

        // 发现设备服务
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            showLog("onServicesDiscovered status($status)")
            when (status) {
                BluetoothGatt.GATT_SUCCESS -> {
                    showLog("发现服务成功")
                    updateConnectState(true)
                    dealServicesDiscovered(gatt.getService(curServiceUUID))
                }
                else -> {
                    updateConnectState(false)
                    doDisConnect()
                }
            }
        }

        //特征读取回调
        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            val msg = characteristic.value?.run {
                String(this)
            } ?: "None"

            when (characteristic.uuid) {
                PaxBleConfig.getAuthUUID() -> {

                    showUiInfo("读取手机Auth信息 :$msg")
                    showUiInfo("开始认证")

                    if (msg == "authInfoFromSlave") {
                        showUiInfo("手机端认证成功，发送车机认证信息")
                        characteristic.value = PaxBleConfig.buildPhoneAuthKeyArray()
                        mmConnectedGatt?.writeCharacteristic(characteristic)
                    }
                }
                PaxBleConfig.getCalendarReadableUUID() -> {
                    showUiInfo("收到日历信息 :$msg")
                }
//                    mmGattServer?.sendResponse(
//                        device, requestId, BluetoothGatt.GATT_SUCCESS,
//                        offset, "authInfoFromSlave".toByteArray()
//                    )
            }
        }

        //特征写入回调
        override fun onCharacteristicWrite(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic, status: Int
        ) {
            if (BluetoothGatt.GATT_SUCCESS == status) {
                val msg = characteristic.value?.run {
                    String(this)
                } ?: "None"

                showUiInfo("特征写入成功——${PaxBleConfig.getUUIDName(characteristic.uuid)} ,$msg")
            }
        }

        // 由于远程特征通知而触发的回调。
        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic
        ) {
            val msg = characteristic.value?.run {
                String(this)
            } ?: "None"
            showUiInfo("特征变化——${PaxBleConfig.getUUIDName(characteristic.uuid)} , msg=$msg")
            doReadCalendarInfo()
        }
    }

    private fun doReadCalendarInfo() {
        showUiInfo("开始接收日历信息")


        val sb = StringBuilder()

        getCalendarGattCharacteristic()?.let {
            var len = 0
//            do {
            mmConnectedGatt?.readCharacteristic(it)
            len = it.value?.size ?: 0
            if (len > 0) {
                sb.append(String(it.value))
            }
//            } while (len > 0)
        }
        showUiInfo("日历信息 :$sb")

    }

//    /**
//     * 读取meeting信息
//     */
//    fun readMeetingMsg(): String {
//        if (!isConnected) return ""
//
//        return meetingGattCharacteristic?.let {
//            mmBluetoothGatt?.readCharacteristic(it)
//            String(it.value)
//        } ?: ""
//    }


    private fun dealServicesDiscovered(gattService: BluetoothGattService?) {
        showLog("发现指定服务 :${gattService?.uuid}")

        if (gattService == null) return

        // 开始监听日历服务
        getCalendarGattCharacteristic()?.run {
            mmConnectedGatt?.setCharacteristicNotification(this, true)
        }

        getAuthGattCharacteristic()?.run {
            showLog("开始认证——获取Phone端Auth信息")
            mmConnectedGatt?.readCharacteristic(this)
        }
    }
//
//    private fun doAuth(authGatt: BluetoothGattCharacteristic?): Boolean {
//        if (authGatt == null) return false
//        showLog("开始校验")
//
//        //
//        mmBluetoothGatt?.readCharacteristic(authGatt)
//        val phoneKey = authGatt.value?.run {
//            String(this)
//        }
//        showLog("获取手机信息 :$phoneKey")
//        if (phoneKey == null) return false
//
//        val masterKey1 = PaxBleConfig.buildPhonePublicKey(curFFID)
//
//        if (phoneKey != masterKey1) return false
//        showLog("手机校验通过，发送车机信息")
//
//
//        val masterPublicKey = PaxBleConfig.buildVehiclePrivateKey(curFFID)
//
//        authGatt.value = masterPublicKey.toByteArray()
//        mmBluetoothGatt?.writeCharacteristic(authGatt)
//
//        return true
//    }
//
//    /**
//     * 发送meeting信息给从设备
//     */
//    private fun sendMeetingMsg(msg: String) {
////        meetingGattCharacteristic?.apply {
////            value = msg.toByteArray()
////        }?.run {
////            mmBluetoothGatt?.writeCharacteristic(this)
////        }
//    }
//
//    /**
//     * 读取meeting信息
//     */
//    private fun readMeetingMsg(): String {
//        return meetingGattCharacteristic?.let {
//            mmBluetoothGatt?.readCharacteristic(it)
//            it.value.toString()
//        } ?: ""
//    }


}