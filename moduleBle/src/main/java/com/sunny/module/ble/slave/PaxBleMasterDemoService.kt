package com.sunny.module.ble.slave

import android.bluetooth.*
import android.bluetooth.le.*
import android.os.ParcelUuid
import com.sunny.lib.base.log.SunLog
import com.sunny.module.ble.PaxBleConfig
import com.sunny.module.ble.pax.PaxBleCommonService
import com.sunny.module.ble.utils.NumberUtils
import java.nio.ByteBuffer


/**
 * 车机端/中央设备
 * 1. 服务启动后，开始搜索附近匹配的BLE设备（serviceUUID = "99cce69d-8319-b4e3-85dd-737c1d497763）
 * 2. 匹配成功后进行connect
 * 3. connect成功后，读取Auth特征中的手机端验证信息，并进行验证（authUUID = "5C81B02B-8C65-7C85-80E2-8EB24F1A4E15"）
 * 4. 验证通过后，再通过Auth特征，发送车机端验证信息，等待手机端验证
 * 5. 车机端机监听Calendar特征，等待手机端验证成功后，通过Calendar特征发送日历信息（CalendarUUID = "5C81B02B-8C66-7C85-80E2-8EB24F1A4E15"）
 * 6. 收到特征变化通知后，通过Calendar特征读取日历信息
 * 7. 整合日历信息，通知UI进行处理
 *
 * 42:23:F7:CD:5E:A4
 */
class PaxBleConnectCalendarInfo(
    private val byteArray: ByteArray,
    private val sessionId: Int,
    private val totalSize: Int
) {
    var curPos = 0
    var readSize = 240
    var hasReadAll = false

    var rcvData: ByteArray = byteArrayOf()

    companion object {

        fun showLog(info: String) {
            SunLog.i("PaxBle", info)
        }

        fun doInit(byteArray: ByteArray): PaxBleConnectCalendarInfo? {
            val sessionIdArray = byteArray.copyOf(4)
            val totalSizeArray = byteArray.copyOfRange(4, 8)

            val sessionId = ByteBuffer.wrap(sessionIdArray).int
            val totalSize = ByteBuffer.wrap(totalSizeArray).int
            showLog("sessionId :$sessionId , totalSize :$totalSize")
            return PaxBleConnectCalendarInfo(byteArray, sessionId, totalSize)
        }
    }

    fun getReadData(): ByteArray? {
        if (hasReadAll) return null
        val size = if (curPos + readSize > totalSize) {
            totalSize - curPos
        } else {
            readSize
        }
        SunLog.i("PaxBle", "readData $sessionId , $curPos , $size")
        return NumberUtils.intTo4Byte(sessionId) +
                NumberUtils.intTo4Byte(curPos) +
                NumberUtils.intTo2Byte(size)
    }

    //0000009F00000000001F7B226F73223A22694F532031
    fun saveData(byteArray: ByteArray): Boolean {

        val len = byteArray.size
        if (len <= 10) {
            SunLog.i("PaxBle", "saveData error")
            return false
        }

        val validData = byteArray.copyOfRange(10, len)
        curPos += validData.size
        rcvData += validData

        hasReadAll = curPos >= totalSize
        return true
    }
}

class PaxBleMasterDemoService : PaxBleCommonService() {

    private var mmLeScanner: BluetoothLeScanner? = null
    private var mmDevice: BluetoothDevice? = null
    private var mmConnectedGatt: BluetoothGatt? = null
    private var mmConnectedInfo: PaxBleConnectCalendarInfo? = null

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
                    /*
               该函数将向远程设备发送连接参数更新请求。
               CONNECTION_PRIORITY_BALANCED
                   使用蓝牙 SIG 推荐的连接参数。如果没有请求更新连接参数，这是默认值

               CONNECTION_PRIORITY_HIGH
                   请求高优先级、低延迟的连接。应用程序应该只请求高优先级连接参数以通过 LE 快速传输大量数据。传输完成后，应用程序应请求 CONNECTION_PRIORITY_BALANCED 连接参数以减少能源使用

               CONNECTION_PRIORITY_LOW_POWER
                   请求低功耗、降低数据速率的连接参数。
                */
//                    mmConnectedGatt?.requestConnectionPriority(BluetoothGatt.CONNECTION_PRIORITY_HIGH)
                    /*
                    执行写请求操作（无响应写入）时，发送的数据会被截断为 MTU 大小。此函数可用于请求更大的 MTU 大小以便能够一次发送更多数据
                     */
                mmConnectedGatt?.requestMtu(512)
                }

                BluetoothProfile.STATE_DISCONNECTED -> {
                    showUiInfo("设备断开连接")
                    doDisconnect()
                }
            }
        }

        override fun onMtuChanged(gatt: BluetoothGatt?, mtu: Int, status: Int) {
            super.onMtuChanged(gatt, mtu, status)
            showUiInfo("onMtuChanged :$mtu")
            mmConnectedGatt?.discoverServices()

        }

        // 发现设备服务
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                showUiInfo("发现服务成功")


                // 开始监听日历服务
                mmConnectedGatt?.getService(PaxBleConfig.getServiceUUID())
                    ?.getCharacteristic(PaxBleConfig.getCalendarReadableUUID())
                    ?.run {
                        val isOk = mmConnectedGatt?.setCharacteristicNotification(this, true)
                        getDescriptor(PaxBleConfig.getCalendarReadableDesUUID())?.let { des ->
                            des.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                            mmConnectedGatt?.writeDescriptor(des)
                        }
                        showUiInfo("监听日历变化 $isOk")
                    }

                mWorkHandler.postDelayed({
                    // 读取认证信息
                    mmConnectedGatt?.getService(PaxBleConfig.getServiceUUID())
                        ?.getCharacteristic(PaxBleConfig.getAuthUUID())
                        ?.run {
                            showUiInfo("读取鉴权信息 :${NumberUtils.bytesToHex(value)}")
                            mmConnectedGatt?.readCharacteristic(this)
                        }
                }, 200)
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
//            showUiInfo("onCharacteristicRead :${characteristic.uuid} >>> ${characteristic.value?.size}")
            if (BluetoothGatt.GATT_SUCCESS != status) return

            if (PaxBleConfig.getAuthUUID() == characteristic.uuid) {
                showUiInfo("读取认证信息 :${NumberUtils.bytesToHex(characteristic.value)}")
                if (PaxBleConfig.buildPhoneAuthKeyArray().contentEquals(characteristic.value)) {
                    showUiInfo("手机端认证成功")

                    val byteArray = PaxBleConfig.buildVehicleAuthKeyArray()
                    showUiInfo("发送认证信息 :${NumberUtils.bytesToHex(byteArray)}")
                    characteristic.value = byteArray
                    mmConnectedGatt?.writeCharacteristic(characteristic)

                } else {
                    showUiInfo("手机端认证失败")
                }

            } else if (PaxBleConfig.getCalendarReadUUID() == characteristic.uuid) {
//                showUiInfo("收到日历信息 :${NumberUtils.bytesToHex(characteristic.value)}")
                mWorkHandler.post {
                    if (mmConnectedInfo?.saveData(characteristic.value) == true) {
                        doReadMsg()
                    }
                }
            }
        }

        // 由于远程特征通知而触发的回调。
        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic
        ) {
            showLog("onCharacteristicChanged :${characteristic.uuid} >>> ${characteristic.value?.size}")
            if (PaxBleConfig.getCalendarReadableUUID() == characteristic.uuid) {
                showUiInfo("可以读取日历信息 :${NumberUtils.bytesToHex(characteristic.value)}")
                mWorkHandler.post {
                    mmConnectedInfo = PaxBleConnectCalendarInfo.doInit(characteristic.value)
                    doReadMsg()
                }
            }
        }

        //特征写入回调
        override fun onCharacteristicWrite(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic, status: Int
        ) {
            if (status != BluetoothGatt.GATT_SUCCESS) {
                showUiInfo("发送失败 :${characteristic.uuid} , $status")
                return
            }

            if (PaxBleConfig.getCalendarReadUUID() == characteristic.uuid) {
//                showUiInfo("写入读取信息成功 :${NumberUtils.bytesToHex(characteristic.value)}")
                mmConnectedGatt?.readCharacteristic(characteristic)

            } else {
                showUiInfo("发送成功 :${characteristic.uuid}")
            }
        }
    }

    override fun doSendMsg(msg: String) {
        // 读取认证信息
        mmConnectedGatt?.getService(PaxBleConfig.getServiceUUID())
            ?.getCharacteristic(PaxBleConfig.getAuthUUID())
            ?.run {
                showUiInfo("读取鉴权信息 :${NumberUtils.bytesToHex(value)}")
                mmConnectedGatt?.readCharacteristic(this)
            }
    }

    private fun dealReadFinish() {
        showUiInfo("数据接收完毕")
        val info = mmConnectedInfo?.rcvData

        if (info == null) {
            showUiInfo("没有数据")
        } else {
            val msg = String(info)
            showUiInfo("收到数据 :$msg")
        }
    }

    override fun doReadMsg(): String {
        //使用无效偏移请求读取或写入操作
        // 读取日历信息
        val byteArray = mmConnectedInfo?.getReadData()
        if (byteArray == null) {
            dealReadFinish()
        } else {
            mmConnectedGatt?.getService(PaxBleConfig.getServiceUUID())
                ?.getCharacteristic(PaxBleConfig.getCalendarReadUUID())
                ?.run {
//                    showUiInfo("写入读取信息 :${NumberUtils.bytesToHex(byteArray)}")
                    value = byteArray
                    mmConnectedGatt?.writeCharacteristic(this)
                }
        }
        return ""
    }
}