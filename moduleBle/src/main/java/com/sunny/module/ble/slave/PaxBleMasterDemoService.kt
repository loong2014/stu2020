package com.sunny.module.ble.slave

import android.bluetooth.*
import android.bluetooth.le.*
import android.os.ParcelUuid
import com.sunny.module.ble.PaxBleConfig
import com.sunny.module.ble.pax.PaxBleCommonService


// UUID 相关信息
//const val ServiceFFIDStr = "61bad4f56acce30010246260"
//const val ServiceUUIDStr = "99cce69d-8319-b4e3-85dd-737c1d497763"
//const val AuthUUIDStr = "5C81B02B-8C65-7C85-80E2-8EB24F1A4E15"
//const val CalendarUUIDStr = "5C81B02B-8C66-7C85-80E2-8EB24F1A4E15"
/**
 * 车机端/中央设备
 * 1. 服务启动后，开始搜索附近匹配的BLE设备（serviceUUID = "99cce69d-8319-b4e3-85dd-737c1d497763）
 * 2. 匹配成功后进行connect
 * 3. connect成功后，读取Auth特征中的手机端验证信息，并进行验证（authUUID = "5C81B02B-8C65-7C85-80E2-8EB24F1A4E15"）
 * 4. 验证通过后，再通过Auth特征，发送车机端验证信息，等待手机端验证
 * 5. 车机端机监听Calendar特征，等待手机端验证成功后，通过Calendar特征发送日历信息（CalendarUUID = "5C81B02B-8C66-7C85-80E2-8EB24F1A4E15"）
 * 6. 收到特征变化通知后，通过Calendar特征读取日历信息
 * 7. 整合日历信息，通知UI进行处理
 */
class PaxBleMasterDemoService : PaxBleCommonService() {

    private var mmLeScanner: BluetoothLeScanner? = null
    private var mmDevice: BluetoothDevice? = null
    private var mmConnectedGatt: BluetoothGatt? = null

    private fun getAuthGattCharacteristic(): BluetoothGattCharacteristic? {
        return mmConnectedGatt?.getService(PaxBleConfig.getServiceUUID())
            ?.getCharacteristic(PaxBleConfig.getAuthUUID())
    }

    private fun getCalendarGattCharacteristic(): BluetoothGattCharacteristic? {
        return mmConnectedGatt?.getService(PaxBleConfig.getServiceUUID())
            ?.getCharacteristic(PaxBleConfig.getCalendarUUID())
    }

    override fun doInit() {
        super.doInit()
        showUiInfo("开始扫描")

        mmLeScanner = BluetoothAdapter.getDefaultAdapter()?.bluetoothLeScanner
        mmLeScanner?.startScan(
            listOf(
                ScanFilter.Builder()
                    .setServiceUuid(ParcelUuid(PaxBleConfig.getServiceUUID()))
                    .build()
            ),
            ScanSettings.Builder()
                // 扫描模式
                .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
                // 返回类型
                .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                // 匹配模式
                .setMatchMode(ScanSettings.MATCH_MODE_STICKY)
                // 匹配数
                .setNumOfMatches(ScanSettings.MATCH_NUM_MAX_ADVERTISEMENT)
                // 是否返回旧版广告
                .setLegacy(true)
                // 设置物理层模式，仅在 legacy=false时生效
                .setPhy(ScanSettings.PHY_LE_ALL_SUPPORTED)
                // 延时返回扫描结果，0表示立即返回
                .setReportDelay(0)
                .build(),
            scanCallback
        )
    }

    override fun doRelease() {
        super.doRelease()
        showUiInfo("停止扫描")
        mmLeScanner?.stopScan(scanCallback)
        mmLeScanner = null

        doDisconnect()
    }

    private fun doDisconnect() {
        showUiInfo("断开连接 :$mmDevice")
        mmConnectedGatt?.disconnect()
        mmConnectedGatt?.close()
        mmConnectedGatt = null

        mmDevice = null
    }

    private val scanCallback = object : ScanCallback() {

        override fun onScanFailed(errorCode: Int) {
            showUiInfo("扫描失败 :$errorCode")
        }

        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            if (result == null) return
            dealScanResult(result)
        }
    }

    private fun dealScanResult(result: ScanResult) {
        if (mmDevice != null) return

        showUiInfo("发现设备 :${mmDevice?.name} , $mmDevice")

        val hasMatch =
            result.scanRecord?.serviceUuids?.also { it.isNotEmpty() }
                ?.contains(ParcelUuid(PaxBleConfig.getServiceUUID())) ?: false
        if (hasMatch) {
            showUiInfo("设备匹配成功")
            mmDevice = result.device
            mWorkHandler.post {
                doConnect()
            }
        } else {
            showUiInfo("设备匹配失败")
            doDisconnect()
        }

    }

    private fun doConnect() {
        showUiInfo("开始连接 :$mmDevice")
        try {
            mmDevice?.connectGatt(mmContext, false, gattCallback)
        } catch (e: Exception) {
            showUiInfo("连接失败")
            doDisconnect()
        }
    }

    private var gattCallback = object : BluetoothGattCallback() {

        // 连接设备状态
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            showLog("onConnectionStateChange status($status) , newState($newState)")
            when (newState) {
                // 连接成功
                BluetoothProfile.STATE_CONNECTED -> {
                    showUiInfo("设备连接成功，开始发现服务")
                    mmConnectedGatt = gatt
                    mmConnectedGatt?.discoverServices()
                }

                BluetoothProfile.STATE_DISCONNECTED -> {
                    showUiInfo("设备断开连接")
                    doDisconnect()
                }
            }
        }

        // 发现设备服务
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            showLog("onServicesDiscovered status($status)")
            if (status == BluetoothGatt.GATT_SUCCESS) {
                showUiInfo("发现服务成功")
                doStartAuth()
            } else {
                showUiInfo("发现服务失败 :$status")
                doDisconnect()
            }
        }

        //特征读取回调
        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            if (BluetoothGatt.GATT_SUCCESS != status) return

            val msg = getValue(characteristic)
            showLog("onCharacteristicRead msg :$msg")
            if (PaxBleConfig.getAuthUUID() == characteristic.uuid) {
                showUiInfo("读取手机端认证信息 :$msg") // msg=None
                if (PaxBleConfig.buildPhonePublicKey(null) == msg) {
                    showUiInfo("手机端认证成功")

                    mMainHandler.post {
                        getAuthGattCharacteristic()?.run {
                            val info = PaxBleConfig.buildVehiclePrivateKey(null)
                            showUiInfo("发送车机认证信息 :$info")
                            value = info.toByteArray()
                            mmConnectedGatt?.writeCharacteristic(this)
                        }
                    }
                }
            }
        }

        //特征写入回调
        override fun onCharacteristicWrite(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic, status: Int
        ) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                showUiInfo("发送成功")
            } else {
                showUiInfo("发送失败 :$status")
            }
        }

        // 由于远程特征通知而触发的回调。
        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic
        ) {
            val msg = getValue(characteristic)
            showUiInfo("收到数据 :$msg")
        }

        override fun onDescriptorRead(
            gatt: BluetoothGatt?,
            descriptor: BluetoothGattDescriptor?,
            status: Int
        ) {
            val msg = getValue(descriptor)
            showLog("onDescriptorRead msg :$msg")
        }
    }

    override fun doReadMsg(): String {
        showUiInfo("读取日历信息")
        getCalendarGattCharacteristic()?.run {
            mmConnectedGatt?.readCharacteristic(this)
//            descriptors?.forEachIndexed { index, bluetoothGattDescriptor ->
//                showLog("$index --- $msg")
//            }
        }
        return ""
    }

    private fun doStartAuth() {
        // 开始监听日历服务
        getCalendarGattCharacteristic()?.run {
            mmConnectedGatt?.setCharacteristicNotification(this, true)
        }

        mWorkHandler.postDelayed({
            showUiInfo("开始认证——获取手机端认证信息")
            getAuthGattCharacteristic()?.run {
                val msg = getValue(this)
                showUiInfo("当前手机认证信息 :$msg") // None
                showUiInfo("读取认证信息")
                mmConnectedGatt?.readCharacteristic(this)
            }
        }, 200)

    }

    override fun getValue(characteristic: BluetoothGattCharacteristic?): String {
        return characteristic?.value?.run {
            String(this)
        } ?: "None"
    }
}