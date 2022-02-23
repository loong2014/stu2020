//package com.sunny.module.ble.pax
//
//import android.bluetooth.BluetoothAdapter
//import android.bluetooth.le.*
//import android.os.ParcelUuid
//import com.sunny.module.ble.server.BleBaseServerService
//
///**
// * 主设备/中心设备，不停发送广播，等待从设备的连接
// *
// * 车机上的PaxLauncher 不停发广播，等待 FF Ctrl 的连接
// */
//class BleMasterServiceOld : BleBaseServerService() {
//
//    private var mmAdvertiser: BluetoothLeAdvertiser? = null
//
//    override fun doInit() {
//        mmAdvertiser = BluetoothAdapter.getDefaultAdapter().bluetoothLeAdvertiser
//    }
//
//    override fun doRelease() {
//        doStopScan()
//    }
//
//    override fun doStartScan() {
//        dealRcvMsg("startAdvertising")
//
////        mmAdvertiser?.startAdvertising(
////            buildAdvertiseSettings(),
////            buildAdvertiseData(),
////            advertiseCallback
////        )
//
//
//        //该示例应用使用蓝牙 LE 1M PHY 进行通告：
//        mmAdvertiser?.startAdvertisingSet(
//            buildAdvertisingSetParameters(),
//            buildAdvertiseData(),
//            null,
//            null,
//            null,
//            advertisingSetCallback
//        )
//    }
//
//    override fun doStopScan() {
//        dealRcvMsg("stopAdvertising")
//        mmAdvertiser?.stopAdvertisingSet(advertisingSetCallback)
////        mmAdvertiser?.stopAdvertising(advertiseCallback)
//    }
//
//    private fun buildAdvertisingSetParameters(): AdvertisingSetParameters {
//        return AdvertisingSetParameters.Builder()
//            /*
//            当设置为 true 时，广告集将发布符合 4.x Spec 规范的广告。
//             */
//            .setLegacyMode(true)
//            /*
//            设置广告类型是可连接的还是不可连接的。
//            旧版广告既可以连接也可以扫描。
//            非传统广告只能是可扫描的或只能连接的
//             */
//            .setConnectable(true)
//            /*
//            设置广告类型是否应可扫描。
//            旧版广告既可以连接也可以扫描。
//            非传统广告只能是可扫描的或只能连接的
//             */
//            .setScannable(true)
//            /*
//            设置广告间隔。
//            INTERVAL_HIGH
//                在低频上做广告，大约每 1000 毫秒。这是默认和首选的广告模式，因为它消耗的电量最少。
//            INTERVAL_MEDIUM
//                在中频上做广告，大约每 250 毫秒。这是广告频率和功耗之间的平衡
//            INTERVAL_LOW(默认)
//                大约每 100 毫秒执行一次高频、低延迟的广告。这具有最高的功耗，不应用于连续的背景广告。
//            INTERVAL_MIN
//                广告间隔的最小值。
//            INTERVAL_MAX
//                广告间隔的最大值。
//             */
//            .setInterval(AdvertisingSetParameters.INTERVAL_HIGH)
//
//            /*
//            设置广告的传输功率级别。
//            TX_POWER_MEDIUM（默认）
//                使用中等 TX 功率水平进行广告宣传。
//            TX_POWER_ULTRA_LOW
//                使用最低传输 (TX) 功率电平进行广告宣传。低发射功率可用于限制广告包的可见范围
//            TX_POWER_LOW
//                使用低 TX 功率电平做广告。
//            TX_POWER_HIGH
//                使用高 TX 功率电平做广告。这对应于广告包的最大可见范围。
//            TX_POWER_MIN
//                TX 功率的最小值。
//            TX_POWER_MAX
//                TX 功率的最大值。
//             */
//            .setTxPowerLevel(AdvertisingSetParameters.TX_POWER_MEDIUM)
//            .build()
//    }
//
//    private fun buildAdvertiseSettings(): AdvertiseSettings {
//        return AdvertiseSettings.Builder()
//            .build()
//    }
//
//    private fun buildAdvertiseData(): AdvertiseData {
//        return AdvertiseData.Builder()
//            .setIncludeDeviceName(false)
//            .addServiceUuid(ParcelUuid(PaxBleConfig.PAX_SERVICE_UUID))
//            .build()
//    }
//
//    private val advertisingSetCallback = object : AdvertisingSetCallback() {
//        override fun onAdvertisingSetStarted(
//            advertisingSet: AdvertisingSet?,
//            txPower: Int,
//            status: Int
//        ) {
//            dealRcvMsg("onAdvertisingSetStarted($status)")
//            /*
//            After onAdvertisingSetStarted callback is called,
//            you can modify the advertising data and scan response data
//            调用 onAdvertisingSetStarted 回调后，可以修改 广告数据和扫描响应数据
//             */
//            // 1
//            advertisingSet?.setAdvertisingData(
//                AdvertiseData.Builder()
//                    .setIncludeDeviceName(true)
//                    .setIncludeTxPowerLevel(true)
//                    .build()
//            )
//        }
//
//        override fun onAdvertisingDataSet(advertisingSet: AdvertisingSet?, status: Int) {
//            dealRcvMsg("onAdvertisingDataSet($status)")
//            /*
//             Wait for onAdvertisingDataSet callback...
//             */
//            // 2
//            advertisingSet?.setScanResponseData(
//                AdvertiseData.Builder()
//                    .addServiceUuid(ParcelUuid(PaxBleConfig.PAX_SERVICE_UUID))
//                    .build()
//            )
//        }
//
//        override fun onScanResponseDataSet(advertisingSet: AdvertisingSet?, status: Int) {
//            dealRcvMsg("onScanResponseDataSet($status)")
//            // 3
//        }
//
//        override fun onAdvertisingSetStopped(advertisingSet: AdvertisingSet?) {
//            dealRcvMsg("onAdvertisingSetStopped")
//        }
//    }
//
//    private val advertiseCallback = object : AdvertiseCallback() {
//        override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
//            super.onStartSuccess(settingsInEffect)
//            dealRcvMsg("onStartSuccess")
//        }
//
//        override fun onStartFailure(errorCode: Int) {
//            super.onStartFailure(errorCode)
//            dealRcvMsg("onStartFailure($errorCode)")
//        }
//    }
//}