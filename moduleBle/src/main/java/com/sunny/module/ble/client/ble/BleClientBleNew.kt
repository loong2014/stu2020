package com.sunny.module.ble.client.ble

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import com.sunny.module.ble.BleConfig
import com.sunny.module.ble.client.BleBaseClientService
import com.sunny.module.ble.client.ClientConnectCallback
import com.sunny.module.ble.client.ClientConnectThread
import com.sunny.module.ble.client.buildConnectThreadBle

/**
 * 低功耗蓝牙
 * https://developer.android.com/guide/topics/connectivity/bluetooth-le?hl=zh-cn
 *
 * 外围
 */
class BleClientBleNew : BleBaseClientService() {

    private var mScanning: Boolean = false

    override fun doRelease() {
        doStopScan()
        doStopConnect()
    }

    override fun doInit() {
        doStartScan()
    }

    override fun doStartScan() {
        log("doStartScan($mScanning)")
        val filters = mutableListOf<ScanFilter>()
//        filters.add(
//            ScanFilter.Builder()
//                .setServiceUuid(BleConfig.PAX_BLE_P_UUID)
//                .build()
//        )


        val setting = ScanSettings.Builder()
            /*
            扫描模式
            SCAN_MODE_OPPORTUNISTIC
                一种特殊的蓝牙 LE 扫描模式。使用此扫描模式的应用程序将被动地侦听其他扫描结果，而无需自行启动 BLE 扫描。
            SCAN_MODE_LOW_POWER(默认)
                在低功耗模式下执行蓝牙 LE 扫描。这是默认扫描模式，因为它消耗的电量最少。如果扫描应用程序不在前台，则强制执行此模式。
            SCAN_MODE_BALANCED
                在平衡功率模式下执行蓝牙 LE 扫描。扫描结果以在扫描频率和功耗之间提供良好折衷的速率返回。
            SCAN_MODE_LOW_LATENCY
                使用最高占空比进行扫描。建议仅在应用程序在前台运行时使用此模式。
             */
            .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
            /*
            返回类型
            CALLBACK_TYPE_ALL_MATCHES(默认)
                为找到的每个与过滤条件匹配的蓝牙广告触发回调。如果没有过滤器处于活动状态，则报告所有广告数据包。
            CALLBACK_TYPE_FIRST_MATCH
                仅针对收到的第一个与过滤条件匹配的广告数据包触发结果回调。
            CALLBACK_TYPE_MATCH_LOST
                当不再从先前已由第一次匹配回调报告的设备接收到广告时接收回调。
             */
            .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
            /*
            匹配模式
            MATCH_MODE_AGGRESSIVE(默认)
                在 Aggressive 模式下，即使信号强度微弱且在一段时间内只有少量的瞄准匹配，硬件也会更快地确定匹配。
            MATCH_MODE_STICKY
                对于粘性模式，在硬件报告之前需要更高的信号强度和目击阈值
             */
            .setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE)
            /*
            匹配数
            MATCH_NUM_MAX_ADVERTISEMENT(默认)
                每个过滤器匹配硬件允许的尽可能多的广告，这取决于硬件中资源的当前能力和可用性
            MATCH_NUM_FEW_ADVERTISEMENT
                每个过滤器匹配少量广告，取决于硬件中资源的当前能力和可用性
            MATCH_NUM_ONE_ADVERTISEMENT
                确定每个过滤器匹配多少广告，因为这是稀缺的硬件资源
             */
            .setNumOfMatches(ScanSettings.MATCH_NUM_MAX_ADVERTISEMENT)
            /*
            设置是否应在扫描结果中仅返回旧版广告。传统广告包括蓝牙核心规范 4.2 及以下规定的广告。默认情况下这是为了与旧应用程序兼容。
            true 如果只返回遗留广告
             */
            .setLegacy(false)
            /*
            设置此扫描期间要使用的物理层。这仅在 {@link ScanSettings.BuildersetLegacy} 设置为 false 时使用。
            {@link android.bluetooth.BluetoothAdapter。isLeCodedPhySupported} 可以通过调用 {@link android.bluetooth.BluetoothAdapterisLeCodedPhySupported} 来检查是否支持 LE Coded phy。选择不支持的 phy 将导致无法开始扫描。
            ScanSettings.PHY_LE_ALL_SUPPORTED(默认)
                使用所有支持的 PHY 进行扫描。这将检查控制器功能，并在 1Mbit 和 LE 编码 PHY（如果支持）或仅在 1Mbit PHY 上开始扫描。
            BluetoothDevice.PHY_LE_1M
                蓝牙 LE 1M PHY。用于指代 LE 1M 物理通道进行广告、扫描或连接。
            BluetoothDevice.PHY_LE_CODED
                蓝牙 LE 编码 PHY。用于指代用于广告、扫描或连接的 LE 编码物理通道。
             */
            .setPhy(ScanSettings.PHY_LE_ALL_SUPPORTED)
            /*
            设置蓝牙 LE 扫描的报告延迟时间戳。
            reportDelayMillis
            =0(默认) : 立即返回结果
            >0 : 导致扫描结果排队并在请求的延迟之后或内部缓冲区填满时交付
             */
            .setReportDelay(0)
            .build()
//
//        if (mScanning) {
//            mHandler.removeCallbacks(delayStopTask)
//        }
//        mHandler.postDelayed(delayStopTask, SCAN_PERIOD)
        mScanning = true

        mDeviceAllSet.clear()
        mDeviceSet.clear()
        bluetoothLeScanner?.startScan(filters, setting, mScanCallback)
        dealRcvMsg("startScan")
    }

    override fun doStopScan() {
        log("doStopScan($mScanning)")
        mScanning = false
        mHandler.removeCallbacks(delayStopTask)
        bluetoothLeScanner?.stopScan(mScanCallback)
        showTipInfo("stopScan")

        logScanResults()
    }

    private val delayStopTask = Runnable {
        doStopScan()
    }

    private val mScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            result?.device?.let {
                dealFoundOneDevice(it)
            }
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
        }

        override fun onScanFailed(errorCode: Int) {
            dealRcvMsg("onScanFailed :$errorCode")
        }
    }

    fun dealFoundOneDevice(device: BluetoothDevice) {
        if (mDeviceAllSet.add(device)) {
            log(
                "FoundNewDevice allCount(${mDeviceAllSet.size}) -> " +
                        "$device(${device.type}) , ${device.name} , ${device.uuids}"
            )
        }
        device.takeIf { it.name != null }?.let {
            if (mDeviceSet.add(it)) {
                logScanResults()
                BleConfig.updateCurDiscoveryDevices(mDeviceSet)
                dealRcvMsg("${device.name}(${device.type})(${device.bondState})\n>>>${device.address} , ${device.uuids}")
                device.uuids?.forEachIndexed { index, uuid ->
                    showLog("${device.name}($index) >>> $uuid")
                }
                tryAutoConnect(device)
            }
        }
    }

    override fun doStartConnect(address: String): Boolean {
        return bluetoothAdapter?.getRemoteDevice(address)?.let { dev ->
            tryAutoConnect(dev)
        } ?: false
    }


    // 正在连接的服务端
    var mDeviceConnectThread: ClientConnectThread? = null

    val connectMap: MutableMap<String, ClientConnectThread?> = mutableMapOf()

    private fun tryAutoConnect(device: BluetoothDevice): Boolean {
        log("tryAutoConnect : $device")
        if (buildClientConnect(device)) {
            bleCallback?.onConnectStateChanged(
                BleConfig.ConnectStateTypeConnected,
                "(${device.type})${device.name} --- ${device.address}"
            )
        }
        return true
    }

    private fun buildClientConnect(device: BluetoothDevice): Boolean {
        if (connectMap.containsKey(device.address)) {
            showLog("$device 正在连接服务端")
            return true
        }

        val sb = StringBuilder()
        connectMap.entries.iterator().takeIf { it.hasNext() }?.let {
            val entry = it.next()
            if (entry.value == null) {
                it.remove()
            } else {
                sb.append("\n${entry.key} --> ${entry.value}")
            }
        }

        if (connectMap.size >= 1) {
            showLog("无法连接更多设备")
            showLog("已连接设备 :${sb}")
            return false
        }

        connectMap.keys.forEach {
            if (connectMap[it] == null) {
                connectMap.remove(it)
            }
        }

        val connectThread =
            buildConnectThreadBle(this, device, object : ClientConnectCallback {

                override fun onStateChanged(oldState: String, newState: String) {
                }

                override fun onReadMsg(msg: String) {
                }

                override fun onWriteState(isOk: Boolean, seq: Int) {
                }

            })
        connectThread.start()
        mDeviceConnectThread = connectThread

        connectMap[device.address] = connectThread
        return true
    }

    override fun doStopConnect() {
        log("doStopConnect : ${connectMap.size}")
        connectMap.values.forEach {
            it?.doCancel()
        }
    }

    var msgSeq: Int = 0
    override fun doSendMsg(msg: String): Boolean {
        return true
//        return mDeviceConnectThread?.write(msg, (msgSeq++)) ?: false
    }


}