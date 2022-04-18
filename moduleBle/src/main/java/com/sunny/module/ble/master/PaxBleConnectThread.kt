package com.sunny.module.ble.master

import android.bluetooth.*
import android.content.Context
import android.os.Handler
import com.sunny.module.ble.PaxBleConfig
import com.sunny.module.ble.SEAT_RSD_NAME
import com.sunny.module.ble.utils.PaxByteUtils
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
 */
internal class PaxBleConnectThread(
    context: Context,
    mWorkHandler: Handler,
    mMainHandler: Handler,
    seatName: String
) : PaxBleConnectThreadBase(context, mWorkHandler, mMainHandler, seatName) {

    private var mmConnectedGatt: BluetoothGatt? = null

    private val mmNotifyHelper: PaxBleCalendarNotifyHelper by lazy {
        PaxBleCalendarNotifyHelper(SEAT_RSD_NAME)
    }

    private val mmReadHelper: PaxBleCalendarReadHelper by lazy {
        PaxBleCalendarReadHelper(SEAT_RSD_NAME)
    }

    override fun doConnect(device: BluetoothDevice) {
        try {
            /*
            连接到此设备托管的 GATT 服务器。调用者充当 GATT 客户端。回调用于将结果传递给调用者，
            例如连接状态以及任何进一步的 GATT 客户端操作。该方法返回一个 BluetoothGatt 实例。
            您可以使用 BluetoothGatt 进行 GATT 客户端操作

            TRANSPORT_AUTO（默认）
                GATT 连接到远程双模设备的物理传输没有偏好
            TRANSPORT_BREDR
                首选 BREDR 传输用于 GATT 连接到远程双模设备
            TRANSPORT_LE
                首选 LE 传输用于 GATT 连接到远程双模设备
             */
            device.connectGatt(context, false, gattCallback)
        } catch (e: Exception) {
            onConnectFiled()
        }
    }

    override fun doDisconnect(byUser: Boolean) {
        mmConnectedGatt?.disconnect()
        mmConnectedGatt?.close()
        mmConnectedGatt = null

        onDisconnect(byUser)
    }

    private var gattCallback = object : BluetoothGattCallback() {

        // 连接设备状态
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    // 1、连接成功，设置MTU
                    showLog("connected")
                    mmConnectedGatt = gatt

                    /*
                    该函数将向远程设备发送连接参数更新请求。
                    CONNECTION_PRIORITY_BALANCED(默认)
                      使用蓝牙 SIG 推荐的连接参数。如果没有请求更新连接参数，这是默认值

                    CONNECTION_PRIORITY_HIGH
                      请求高优先级、低延迟的连接。应用程序应该只请求高优先级连接参数以通过 LE 快速传输大量数据。
                      传输完成后，应用程序应请求 CONNECTION_PRIORITY_BALANCED 连接参数以减少能源使用

                    CONNECTION_PRIORITY_LOW_POWER
                      请求低功耗、降低数据速率的连接参数。
                     */
//                    mmConnectedGatt?.requestConnectionPriority(BluetoothGatt.CONNECTION_PRIORITY_BALANCED)

                    /*
                    执行写请求操作（无响应写入）时，发送的数据会被截断为 MTU 大小。
                    此函数可用于请求更大的 MTU 大小以便能够一次发送更多数据
                     */
                    mmConnectedGatt?.requestMtu(PaxBleConfig.PAX_MTU)
                }

                BluetoothProfile.STATE_DISCONNECTED -> {
                    showLog("doDisconnect by STATE_DISCONNECTED")
                    doDisconnect()
                }
            }
        }

        override fun onMtuChanged(gatt: BluetoothGatt?, mtu: Int, status: Int) {
            // 2、MTU设置成功，开始发现服务
            showLog("onMtuChanged($status) :$mtu")
            mmReadHelper.updateReadSize(mtu)
            mmConnectedGatt?.discoverServices()
        }

        // 发现设备服务
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            if (BluetoothGatt.GATT_SUCCESS != status) {
                showLog("doDisconnect by no ServicesDiscovered")
                doDisconnect()
                return
            }

            showLog("servicesDiscovered")

            // 4、设置特征监听
            doSetNotify(mmConnectedGatt)

            // 5、读取鉴权信息
            mWorkHandler.postDelayed({
                getAuthCharacteristic(mmConnectedGatt)?.run {
                    mmConnectedGatt?.readCharacteristic(this)
                }
            }, 200)
        }

        //特征读取回调
        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            if (BluetoothGatt.GATT_SUCCESS != status) return

            if (PaxBleConfig.PAX_UUID_CALENDAR_AUTH == characteristic.uuid) {
                // 读取鉴权信息
                showLog("readAuthInfo :${PaxByteUtils.bytesToHex(characteristic.value)}")
                if (PaxBleConfig.buildPhoneAuthKeyArray(curFFID)
                        .contentEquals(characteristic.value)
                ) {
                    // 车机鉴权通过
                    characteristic.value = PaxBleConfig.buildVehicleAuthKeyArray(curFFID)
                    showLog("sendAuthInfo :${PaxByteUtils.bytesToHex(characteristic.value)}")
                    mmConnectedGatt?.writeCharacteristic(characteristic)

                    onConnectSucceed()
                } else {
                    onAuthFiled()
                }
            } else if (PaxBleConfig.PAX_UUID_CALENDAR_READ == characteristic.uuid) {
                // 读取日历信息
                mWorkHandler.post {
                    // 保持读取的数据
                    if (mmReadHelper.saveReadData(characteristic.value)) {
                        // 继续读取数据
                        doReadNextData()
                    }
                }
            }
        }

        //特征写入回调
        override fun onCharacteristicWrite(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            if (BluetoothGatt.GATT_SUCCESS != status) {
                showLog("Write Failed :${PaxByteUtils.bytesToHex(characteristic.value)}")
                return
            }
            showLog("Write Succeed :${PaxByteUtils.bytesToHex(characteristic.value)}")

            if (PaxBleConfig.PAX_UUID_CALENDAR_READ == characteristic.uuid) {
                // 读取日历信息数据写入成功，开始读取日历信息
                mmConnectedGatt?.readCharacteristic(characteristic)
            }
        }

        // 由于远程特征通知而触发的回调。
        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic
        ) {
            if (PaxBleConfig.PAX_UUID_CALENDAR_NOTIFY == characteristic.uuid) {
                // 收到日历信息
                if (mmNotifyHelper.onRcvData(characteristic.value)) {
                    dealRcvData(mmNotifyHelper.rcvData)
                }

            } else if (PaxBleConfig.PAX_UUID_CALENDAR_READABLE == characteristic.uuid) {
                // 收到phone的通知
                if (characteristic.value.size >= 4) {
                    mWorkHandler.post {
                        val sessionId = ByteBuffer.wrap(characteristic.value.copyOf(4)).int
                        if (sessionId == PaxBleConfig.SessionTypeCloseShare) {
                            // 断开连接
                            showLog("doDisconnect by close share")
                            doDisconnect(true)
                        } else if (sessionId > 0) {
                            // 日历发生变化，准备读取

                            showLog("has data need read :${PaxByteUtils.bytesToHex(characteristic.value)}")
                            // 初始化本次日历信息
                            mmReadHelper.doInit(characteristic.value)
                            // 开始读取日历信息
                            doReadNextData()
                        }
                    }
                }
            }
        }
    }

    private fun doReadNextData() {
        val nextReadArray = mmReadHelper.getNextReadData()
        if (nextReadArray == null) {
            // 读取完毕
            dealRcvData(mmReadHelper.rcvData)
        } else {
            // 继续读取
            getReadCharacteristic(mmConnectedGatt)
                ?.run {
                    value = nextReadArray
                    mmConnectedGatt?.writeCharacteristic(this)
                }
        }
    }
}