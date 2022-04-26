package com.sunny.module.ble.master

import android.bluetooth.*
import android.content.Context
import android.os.Handler
import com.sunny.module.ble.PaxBleConfig
import com.sunny.module.ble.PaxBleConfig.PAX_UUID_CALENDAR_AUTH
import com.sunny.module.ble.PaxBleConfig.PAX_UUID_CALENDAR_NOTIFY
import com.sunny.module.ble.PaxBleConfig.PAX_UUID_CALENDAR_READ
import com.sunny.module.ble.PaxBleConfig.PAX_UUID_CALENDAR_READABLE
import com.sunny.module.ble.master.helper.PaxBleDataNotifyHelper
import com.sunny.module.ble.master.helper.PaxBleDataReadHelper
import com.sunny.module.ble.utils.PaxByteUtils
import java.nio.ByteBuffer
import java.util.*


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
class PaxBleConnectModel(
    context: Context,
    mWorkHandler: Handler,
    mMainHandler: Handler,
    callback: PaxBleConnectCallback
) : PaxBleConnectModelBase(context, mWorkHandler, mMainHandler, callback) {

    private var mmConnectedGatt: BluetoothGatt? = null

    private val mmNotifyHelper: PaxBleDataNotifyHelper by lazy {
        PaxBleDataNotifyHelper(logTag)
    }

    private val mmReadHelper: PaxBleDataReadHelper by lazy {
        PaxBleDataReadHelper(logTag)
    }

    /**
     * 获取特征
     */
    private fun getCharacteristic(uuid: UUID): BluetoothGattCharacteristic? {
        return mmUserInfo?.curServiceUUID?.let {
            mmConnectedGatt?.getService(it)?.getCharacteristic(uuid)
        }
    }

    override fun doConnect(device: BluetoothDevice) {
        try {
            /*
            连接到此设备托管的 GATT 服务器。调用者充当 GATT 客户端。回调用于将结果传递给调用者，
            例如连接状态以及任何进一步的 GATT 客户端操作。该方法返回一个 BluetoothGatt 实例。
            您可以使用 BluetoothGatt 进行 GATT 客户端操作

            参数：autoConnect - 是直接连接到远程设备（false）还是自动连接
            如果为 true 的话，系统就会发起一个后台连接，等到系统发现了一个设备，就会自动连上，通常这个过程是非常慢的。
            为false 的话，就会直接连接，通常会比较快。
            同样，BluetoothGatt.connect()只能发起一个后台连接，不是直接连接。

            TRANSPORT_AUTO（默认）
                GATT 连接到远程双模设备的物理传输没有偏好
            TRANSPORT_BREDR
                首选 BREDR 传输用于 GATT 连接到远程双模设备
            TRANSPORT_LE
                首选 LE 传输用于 GATT 连接到远程双模设备
             */
            curDevice = device
            mmConnectedGatt =
                device.connectGatt(context, false, gattCallback, BluetoothDevice.TRANSPORT_LE)

            /*
            创建一个 RFCOMM BluetoothSocket 套接字，准备使用 uuid 的 SDP 查找启动到此远程设备的 不安全 传出连接。
            通信通道将没有经过身份验证的链接密钥，即它将受到中间人攻击。
            对于蓝牙 2.1 设备，链接密钥将被加密，因为加密是强制性的。
            对于旧设备（蓝牙 2.1 之前的设备），链接密钥不会被加密。
            如果需要加密和经过身份验证的通信通道，请使用 createRfcommSocketToServiceRecord。
             */
//            device.createInsecureRfcommSocketToServiceRecord(uuid) // BluetoothSocket

            /*
            创建一个 RFCOMM BluetoothSocket，准备使用 uuid 的 SDP 查找来启动到该远程设备的 安全 传出连接。
            这旨在与 BluetoothAdapter.listenUsingRfcommWithServiceRecord 一起用于对等蓝牙应用程序。
            使用 BluetoothSocket.connect 启动传出连接。这还将执行给定 uuid 的 SDP 查找以确定要连接到哪个通道。
            远程设备将通过身份验证，并且此套接字上的通信将被加密。
            仅当经过身份验证的套接字链接是可能的时才使用此套接字。身份验证是指对链接密钥进行身份验证，以防止中间人类型的攻击。
            例如，对于蓝牙 2.1 设备，如果任何设备没有输入和输出功能或仅具有显示数字键的功能，则无法进行安全套接字连接。
            在这种情况下，请使用 createInsecureRfcommSocketToServiceRecord。
            有关更多详细信息，请参阅蓝牙核心规范版本 2.1 + EDR 的安全模型第 5.2 节（第 3 卷）。
             */
//            device.createRfcommSocketToServiceRecord() // BluetoothSocket
            /*
            创建一个蓝牙 L2CAP 面向连接的通道 (CoC) BluetoothSocket，
            可用于启动到具有相同动态协议服务多路复用器 (PSM) 值的远程设备的安全传出连接。
            支持的蓝牙传输仅是 LE。
            这旨在与 BluetoothAdapter.listenUsingL2capChannel() 一起用于对等蓝牙应用程序。
            使用 BluetoothSocket.connect 启动传出连接。
            使用此 API 的应用程序负责从远程设备获取 PSM 值。
            远程设备将通过身份验证，并且此套接字上的通信将被加密。
            如果可以使用经过身份验证的套接字链接，请使用此套接字。
            身份验证是指对链接密钥进行身份验证，以防止中间人类型的攻击。
             */
//            device.createL2capChannel()
            /*
            创建一个蓝牙 L2CAP 面向连接的通道 (CoC) BluetoothSocket，
            可用于启动到具有相同动态协议服务多路复用器 (PSM) 值的远程设备的安全传出连接。支持的蓝牙传输仅是 LE。
            这旨在与 BluetoothAdapter.listenUsingInsecureL2capChannel() 一起用于对等蓝牙应用程序。
            使用 BluetoothSocket.connect 启动传出连接。
            使用此 API 的应用程序负责从远程设备获取 PSM 值。
            通信通道可能没有经过身份验证的链接密钥，即它可能会受到中间人攻击。
            如果可以使用经过加密和身份验证的通信通道，请使用 createL2capChannel(int)。
             */
//            device.createInsecureL2capChannel()
        } catch (e: Exception) {
            showLog("onConnectFiled by connectGatt error :$e")
            onConnectFiled()
        }
    }

    override fun doDisconnect(byUser: Boolean) {
        mmConnectedGatt?.disconnect()
        mmConnectedGatt?.close()
        mmConnectedGatt = null

        mmNotifyHelper.doClear()
        mmReadHelper.doClear()
    }

    private var isNotifySet = false
    private var isReadableSet = false
    private var isStartAuth = false

    /**
     * 开始监听特征变化以及读取鉴权信息
     */
    fun dealStartNotifyAndAuth() {
        val gatt = mmConnectedGatt ?: return

        showLog("dealStartNotifyAndAuth $isNotifySet , $isReadableSet , $isStartAuth")

        if (!isNotifySet) {
            getCharacteristic(PAX_UUID_CALENDAR_NOTIFY)?.run {
                if (gatt.setCharacteristicNotification(this, true)) {
                    getDescriptor(PaxBleConfig.PAX_UUID_NOTIFY_DESCRIPTOR)?.let { des ->
                        des.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                        gatt.writeDescriptor(des)
                    }
                }
            }
            isNotifySet = true
            return
        }

        if (!isReadableSet) {
            getCharacteristic(PAX_UUID_CALENDAR_READABLE)?.run {
                if (gatt.setCharacteristicNotification(this, true)) {
                    getDescriptor(PaxBleConfig.PAX_UUID_NOTIFY_DESCRIPTOR)?.let { des ->
                        des.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                        gatt.writeDescriptor(des)
                    }
                }
            }
            isReadableSet = true
            return
        }

        if (!isStartAuth) {
            getCharacteristic(PAX_UUID_CALENDAR_AUTH)?.run {
                mmConnectedGatt?.readCharacteristic(this)
            }
            isStartAuth = true
            return
        }
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
                    showLog("onConnectFiled by STATE_DISCONNECTED")
                    onConnectFiled()
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
                if (BluetoothGatt.GATT_SUCCESS == status) {
                    onDiscoveredResponse(gatt)
                } else {
                    onDiscoveredResponse(null)
                }
        }

        //特征读取回调
        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            if (BluetoothGatt.GATT_SUCCESS != status) return

            if (PAX_UUID_CALENDAR_AUTH == characteristic.uuid) {
                // 读取鉴权信息
                showLog("readPhoneAuthKey :${PaxByteUtils.bytesToHex(characteristic.value)}")
                if (mmUserInfo?.phoneAuthKey.contentEquals(characteristic.value)) {
                    // 车机鉴权通过
                    characteristic.value = mmUserInfo?.vehicleAuthKey
                    showLog("sendVehicleAuthKey :${PaxByteUtils.bytesToHex(characteristic.value)}")
                    mmConnectedGatt?.writeCharacteristic(characteristic)
                    onAuthResponse(true)
                } else {
                    onAuthResponse(false)
                }
            } else if (PAX_UUID_CALENDAR_READ == characteristic.uuid) {
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
            if (PAX_UUID_CALENDAR_NOTIFY == characteristic.uuid) {
                // 收到日历信息
                if (mmNotifyHelper.onRcvData(characteristic.value)) {
                    // 数据接收完毕
                    onRevCalendarData(mmNotifyHelper.rcvData)
                }

            } else if (PAX_UUID_CALENDAR_READABLE == characteristic.uuid) {
                // 收到phone的通知
                if (characteristic.value.size >= 4) {
                    mWorkHandler.post {
                        val sessionId = ByteBuffer.wrap(characteristic.value.copyOf(4)).int
                        if (sessionId == PaxBleConfig.SessionTypeCloseShare) {
                            // 断开连接
                            onPhoneDisconnect()
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

        // 描述写入成功
        override fun onDescriptorWrite(
            gatt: BluetoothGatt?,
            descriptor: BluetoothGattDescriptor?,
            status: Int
        ) {
            showLog("onDescriptorWrite :${descriptor?.uuid}")
            mMainHandler.post {
                dealStartNotifyAndAuth()
            }
        }
    }

    private fun doReadNextData() {
        val nextReadArray = mmReadHelper.getNextReadData()
        if (nextReadArray == null) {
            // 读取完毕
            onRevCalendarData(mmReadHelper.rcvData)
        } else {
            // 继续读取
            getCharacteristic(PAX_UUID_CALENDAR_READ)
                ?.run {
                    value = nextReadArray
                    mmConnectedGatt?.writeCharacteristic(this)
                }
        }
    }
}