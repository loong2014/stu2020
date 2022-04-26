package com.sunny.module.ble

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.le.ScanRecord
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.provider.Settings
import com.sunny.module.ble.utils.PaxByteUtils
import timber.log.Timber
import java.io.OutputStream

object BleTools {

    /**
     * 是否支持蓝牙
     */
    fun isSupportBluetooth(): Boolean {
        return BluetoothAdapter.getDefaultAdapter() != null
    }

    /**
     * 是否支持低功耗蓝牙
     */
    fun isSupportBLE(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
    }

    /**
     * 蓝牙是否开启
     */
    fun isBleEnabled(): Boolean {
        return BluetoothAdapter.getDefaultAdapter()?.isEnabled == true
    }


    /**
     * 获取对配对设备
     */
    fun getBondedDevices(): Set<BluetoothDevice>? {
        val adapter = BluetoothAdapter.getDefaultAdapter()
        return adapter.bondedDevices
    }

    /**
     * 获取远程设备的蓝牙协议类型
     */
    fun getRemoteType(device: BluetoothDevice): String {
        return when (device.type) {
            BluetoothDevice.DEVICE_TYPE_CLASSIC -> "传统蓝牙"
            BluetoothDevice.DEVICE_TYPE_LE -> "低功耗蓝牙"
            BluetoothDevice.DEVICE_TYPE_DUAL -> "双模式蓝牙"
            else -> "UNKNOWN"
        }
    }

    fun deviceCheck(): String {

        fun showTip(tip: String) {
            Timber.i("PaxBle", tip)
        }

        val sb = StringBuilder()
        val adapter = BluetoothAdapter.getDefaultAdapter()
        /*
        如果支持 LE 扩展广告功能，则返回最大 LE 广告数据长度（以字节为单位），否则返回 0。
        返回： 最大 LE 广告数据长度。
         */
        val maxLen = adapter.leMaximumAdvertisingDataLength
        sb.append("\nleMaximumAdvertisingDataLength :$maxLen")

        /*
        如果支持 LE 2M PHY 功能，则返回 true。
         */
        var support = adapter.isLe2MPhySupported
        sb.append("\nisLe2MPhySupported :$support")

        /*
        如果支持 LE 编码 PHY 功能，则返回真。
         */
        support = adapter.isLeCodedPhySupported
        sb.append("\nisLeCodedPhySupported :$support")

        /*
        如果支持 LE 扩展广告功能，则返回 true。
         */
        support = adapter.isLeExtendedAdvertisingSupported
        sb.append("\nisLeExtendedAdvertisingSupported :$support")

        /*
        如果支持 LE 定期广告功能，则返回 true。
         */
        support = adapter.isLePeriodicAdvertisingSupported
        sb.append("\nisLePeriodicAdvertisingSupported :$support")

        /*
        如果芯片组支持多广告，则返回 true
         */
        support = adapter.isMultipleAdvertisementSupported
        sb.append("\nisMultipleAdvertisementSupported :$support")

        /*
        如果支持卸载过滤器，则返回 true
        返回：如果芯片组支持片上过滤，则返回 true
         */
        support = adapter.isOffloadedFilteringSupported
        sb.append("\nisOffloadedFilteringSupported :$support")

        /*
        如果支持卸载扫描批处理，则返回 true
        返回：如果芯片组支持片上扫描批处理，则返回 true
         */
        support = adapter.isOffloadedScanBatchingSupported
        sb.append("\nisOffloadedScanBatchingSupported :$support")

        return sb.toString()
    }

    fun getPairedDevice(): String {
        val sb = StringBuilder()
        sb.append("PairedDevice")
        BluetoothAdapter.getDefaultAdapter().bondedDevices?.forEachIndexed { index, bluetoothDevice ->
            sb.append("\n $index >>> $bluetoothDevice")
        }
        return sb.toString()
    }

    fun permissionCheck(context: Context): String {
        val sb = StringBuilder()

        val adapter = BluetoothAdapter.getDefaultAdapter()
        sb.append("\npermissionCheck start")

        if (adapter == null) {
            sb.append("\nnot support Bluetooth")
            return sb.toString()
        }

        if (!context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            sb.append("\nnot support BLE")
            return sb.toString()
        }

        if (!adapter.isEnabled) {
            sb.append("\nBluetooth not enabled")
            return sb.toString()
        }

        if (adapter.bluetoothLeScanner == null) {
            sb.append("\nnot support LeScanner")
            return sb.toString()
        }

        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!lm.isLocationEnabled) {
            sb.append("\nLocation not enabled")
            return sb.toString()
        }

        sb.append("\npermissionCheck pass")
        return sb.toString()
    }

    /**
     * 请求开启蓝牙
     */
    fun requestEnableBle(activity: Activity, requestCode: Int): String {
        if (isBleEnabled()) {
            return "蓝牙已开启"
        }
//        // 1 auto
//        try {
//            BluetoothAdapter.getDefaultAdapter()?.enable()
//            return "蓝牙开启成功"
//        } catch (e: Exception) {
//            return "Ble enable failed :$e"
//        }

        // 2 by user
        activity.startActivityForResult(
            Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE),
            requestCode
        )
        return "请求开启蓝牙"
    }

    /**
     * 请求开启定位
     */
    fun requestEnableLocation(activity: Activity, requestCode: Int): String {
        val lm = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (lm.isLocationEnabled) {
            return "定位已开启"
        }

        // 1 auto
//        try {
//            val um = activity.getSystemService(Context.USER_SERVICE) as UserManager
//            val userHandle = um.getUserForSerialNumber(0)
//
//            val c: Class<*> = LocationManager::class.java
//            val m = c.getDeclaredMethod(
//                "setLocationEnabledForUser",
//                Boolean::class.java,
//                UserHandle::class.java
//            )
//            m.invoke(lm, true, userHandle)
//            return "Location enable succeed"
//        } catch (e: Exception) {
//            return "Location enable failed :$e"
//        }

        // 2 by user
        activity.startActivityForResult(
            Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),
            requestCode
        )
        return "请求开启定位"
    }

    /**
     * 判断是否有访问位置的权限，没有权限，直接申请位置权限
     */
    fun requestBlePermission(activity: Activity, requestCode: Int): String {
        if ((activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            || (activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            || (activity.checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
            || (activity.checkSelfPermission(Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        ) {
            activity.requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS
                ), requestCode
            )
            return "申请BLE权限"
        }
        return "BLE权限已申请"
    }


    /**
     * 判断是否有访问位置的权限，没有权限，直接申请位置权限
     */
    fun checkBleReadContactsPermission(activity: Activity, requestCode: Int): Boolean {
        if (activity.shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
            activity.requestPermissions(
                arrayOf(
                    Manifest.permission.READ_CONTACTS
                ), requestCode
            )
            return false
        }
        return true
    }

    fun buildOneDeviceShowMsg(device: BluetoothDevice, tag: String): String {
        return "$tag -> $device(${device.type}) , ${device.name} , ${device.uuids}"
    }

    /**
     * 发送消息
     */
    fun doSendMsg(msg: String?, outputStream: OutputStream?): Boolean {
        BleConfig.bleLog("doSendMsg $outputStream -> $msg")
        if (outputStream == null || msg == null) {
            return false
        }

        return try {
            outputStream.write(msg.toByteArray())
            BleConfig.bleLog("doSendMsg succeed")
            true
        } catch (e: Exception) {
            BleConfig.bleLog("doSendMsg error :$e")
            false
        }
    }

    fun buildDevicesShowMsg(
        devices: Collection<BluetoothDevice>?,
        tag: String,
        showUUID: Boolean = false
    ): String {
        val sb = StringBuilder()
        sb.append("### >>>> $tag(${devices?.size})")
        devices?.forEachIndexed { index, device ->
            sb.append("\n$index -> $device(${device.type}) , ${device.name}")
            if (showUUID) {
                sb.append(" , ${device.uuids}")
            }
        }
        sb.append("\n### <<<< $tag")
        return sb.toString()
    }

    fun isBleDiscoverableEnable(): Boolean {
        val adapter = BluetoothAdapter.getDefaultAdapter()
        return adapter.isDiscovering
    }

    /**
     * 打开设备的可见性
     */
    fun openDiscoverable(context: Context) {
        val adapter = BluetoothAdapter.getDefaultAdapter()
        try {
            val c = adapter.javaClass
            val setDiscoverableTimeout = c.getMethod("setDiscoverableTimeout", Int::class.java)
            setDiscoverableTimeout.isAccessible = true
            setDiscoverableTimeout.invoke(adapter, 300)

            val setScanMode = c.getMethod("setScanMode", Int::class.java)
            setScanMode.isAccessible = true
            // 设备处于可检测到模式。
            setScanMode.invoke(adapter, BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE)
        } catch (e: Exception) {
            e.printStackTrace()
            log("openDiscoverable error :$e")
            openDiscoverableWithOptDialog(context)
        }
    }

    /**
     * 关闭设备可见性
     */
    fun closeDiscoverable() {
        val adapter = BluetoothAdapter.getDefaultAdapter()
        try {
            val c = BluetoothAdapter::class.java
            val setDiscoverableTimeout = c.getMethod("setDiscoverableTimeout", Int::class.java)
            setDiscoverableTimeout.isAccessible = true
            setDiscoverableTimeout.invoke(adapter, 1)

            val setScanMode = c.getMethod("setScanMode", Int::class.java)
            setScanMode.isAccessible = true
            // 设备未处于可检测到模式，但仍能收到连接。
            setScanMode.invoke(adapter, BluetoothAdapter.SCAN_MODE_CONNECTABLE)
        } catch (e: Exception) {
            e.printStackTrace()
            log("closeDiscoverable error :$e")
        }
    }

    /**
     * 打开设备的可见性
     */
    private fun openDiscoverableWithOptDialog(context: Context) {
        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
            // 默认情况下，设备处于可检测到模式的时间为 120 秒（2 分钟）
            // 最高可达 3600 秒（1 小时）
            putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
        }
        context.startActivity(intent)
    }

    /**
     * 发送信息
     */
    fun doMsgSend(bluetoothGatt: BluetoothGatt, msg: String) {
        val gattService = bluetoothGatt.getService(BleConfig.PAX_UUID_SERVICE)
        val characteristic = gattService.getCharacteristic(BleConfig.PAX_UUID_WRITE)

        val bytes: ByteArray = msg.toByteArray()
        characteristic.value = bytes
        characteristic.writeType = BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE;
        bluetoothGatt.writeCharacteristic(characteristic)
    }

    /**
     *
     */
    fun doDeviceCheck(): StringBuilder {
        val sb = StringBuilder("开始检测")
        val adapter = BluetoothAdapter.getDefaultAdapter()
        if (adapter == null) {
            sb.append("不支持蓝牙")
            return sb
        }

        //
        if (!adapter.isEnabled) {
            sb.append("蓝牙未开启")
        } else {
            sb.append("蓝牙已开启")
        }


        // 最大连接数
//
//        showCheckMsg("isLeExtendedAdvertisingSupported :${adapter.isLeExtendedAdvertisingSupported}")
//        showCheckMsg("isMultipleAdvertisementSupported :${adapter.isMultipleAdvertisementSupported}")
//
//        // 通告
//        showCheckMsg("isLe2MPhySupported :${adapter.isLe2MPhySupported}")
//        showCheckMsg("isLeCodedPhySupported :${adapter.isLeCodedPhySupported}")
//        showCheckMsg("isLePeriodicAdvertisingSupported :${adapter.isLePeriodicAdvertisingSupported}")
//        showCheckMsg("isOffloadedFilteringSupported :${adapter.isOffloadedFilteringSupported}")
//        showCheckMsg("isOffloadedScanBatchingSupported :${adapter.isOffloadedScanBatchingSupported}")
//
//        val msg = "当前设备:${adapter.name} , ${adapter.address} "
//        tv_own_device_info.text = msg
//
//        if (!BleTools.checkBlePermission(this, 100)) {
//            showCheckMsg("permission error")
//            return
//        }
//
//        if (!BleTools.isSupportBLE(this)) {
//            showCheckMsg("不支持低功耗")
//        }
//
//        if (adapter.isDiscovering) {
//            showCheckMsg("设备正在扫描")
//        }
//
//        if (adapter.cancelDiscovery()) {
//            showCheckMsg("关闭发现")
//        }
//
//        BleTools.openDiscoverable(this)
//
//        isSupportBle = true
//        showCheckMsg("可以正常使用")
        return sb
    }


    private fun log(msg: String) {
        BleConfig.bleLog(msg, "BleTools")
    }

    /**
     * 过滤设置
     */
    private val mmSettings: ScanSettings by lazy {
        ScanSettings.Builder()
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
            .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
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
            true（默认）
             */
            .setLegacy(true)
            /*
            设置此扫描期间要使用的物理层。这仅在 {@link ScanSettings.Builder.setLegacy} 设置为 false 时使用。
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
    }

    fun parseScanRecord(scanRecord: ScanRecord): String {
        val sb = StringBuilder()
        var pos = 0

        sb.append("serviceUuids :${scanRecord.serviceUuids?.size}")
        scanRecord.serviceUuids?.forEach {
            sb.append("\nserviceUuids(${pos++}) >>> $it")
        }

        sb.append("serviceData :${scanRecord.serviceData?.size}")
        scanRecord.serviceData?.forEach { (t, u) ->
            sb.append("\nserviceData(${pos++}) >>> $t >>> ${PaxByteUtils.bytesToHex(u)}")
        }

        return sb.toString()
    }
}