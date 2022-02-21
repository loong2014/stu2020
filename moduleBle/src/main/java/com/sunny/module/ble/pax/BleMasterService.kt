package com.sunny.module.ble.pax

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.*
import android.os.ParcelUuid
import com.sunny.module.ble.server.BleBaseServerService

/**
 * 主设备/中心设备，不停发送广播，等待从设备的连接
 *
 * 车机上的PaxLauncher 不停发广播，等待 FF Ctrl 的连接
 */
class BleMasterService : BleBaseServerService() {

    private var mmAdvertiser: BluetoothLeAdvertiser? = null

    private var currentAdvertisingSet: AdvertisingSet? = null

    override fun doInit() {
        mmAdvertiser = BluetoothAdapter.getDefaultAdapter().bluetoothLeAdvertiser
    }

    override fun doRelease() {
        doStopScan()
    }

    // 该示例应用使用 BLE 2M PHY 进行通告
    override fun doStartScan() {
        dealRcvMsg("startAdvertising")

        mmAdvertiser?.startAdvertisingSet(
            buildAdvertisingSetParameters(),
            buildAdvertiseData(),
            null,
            null,
            null,
            advertisingSetCallback
        )

//        // Can also stop and restart the advertising
//        currentAdvertisingSet.enableAdvertising(false, 0, 0);
//        // Wait for onAdvertisingEnabled callback...
//        currentAdvertisingSet.enableAdvertising(true, 0, 0);
//        // Wait for onAdvertisingEnabled callback...
//
//        // Or modify the parameters - i.e. lower the tx power
//        currentAdvertisingSet.enableAdvertising(false, 0, 0);
//        // Wait for onAdvertisingEnabled callback...
//        currentAdvertisingSet.setAdvertisingParameters(parameters.setTxPowerLevel
//            (AdvertisingSetParameters.TX_POWER_LOW).build());
//        // Wait for onAdvertisingParametersUpdated callback...
//        currentAdvertisingSet.enableAdvertising(true, 0, 0);
//        // Wait for onAdvertisingEnabled callback...
//
//        // When done with the advertising:
//        advertiser.stopAdvertisingSet(callback);
    }

    override fun doStopScan() {
        dealRcvMsg("stopAdvertising")
        mmAdvertiser?.stopAdvertisingSet(advertisingSetCallback)
    }

    private fun buildAdvertisingSetParameters(): AdvertisingSetParameters {
        return AdvertisingSetParameters.Builder()
            .setLegacyMode(false)
            .setInterval(AdvertisingSetParameters.INTERVAL_HIGH)
            .setTxPowerLevel(AdvertisingSetParameters.TX_POWER_MEDIUM)
            .setPrimaryPhy(BluetoothDevice.PHY_LE_1M)
            .setSecondaryPhy(BluetoothDevice.PHY_LE_2M)
            .build()
    }

    private fun buildAdvertiseData(): AdvertiseData {
        return AdvertiseData.Builder()
            .setIncludeDeviceName(false)
            .addServiceUuid(ParcelUuid(PaxBleConfig.PAX_SERVICE_UUID))
            .build()
    }

//
//    private fun buildAdvertiseData(): AdvertiseData {
////        val uuid = ParcelUuid(UUID.randomUUID())
////        dealRcvMsg("UUID1 :$uuid")
//
//        return AdvertiseData.Builder()
//            .addServiceData(
//                ParcelUuid(PaxBleConfig.PAX_SERVICE_UUID),
//                "sunny buildAdvertiseData".toByteArray()
//            )
//            .build()
//    }

    private val advertisingSetCallback = object : AdvertisingSetCallback() {
        override fun onAdvertisingSetStarted(
            advertisingSet: AdvertisingSet?,
            txPower: Int,
            status: Int
        ) {
            dealRcvMsg("onAdvertisingSetStarted($status)")
            currentAdvertisingSet = advertisingSet

//            val uuid = ParcelUuid(UUID.randomUUID())
//            dealRcvMsg("UUID2 :$uuid")

//            val addServiceData = AdvertiseData.Builder()
//                .addServiceData(
//                    ParcelUuid(PaxBleConfig.PAX_SERVICE_UUID),
//                    "sunny onAdvertisingSetStarted".toByteArray()
//                )
//                .build()
            currentAdvertisingSet?.setAdvertisingData(buildAdvertiseData())
        }

        override fun onAdvertisingDataSet(advertisingSet: AdvertisingSet?, status: Int) {
            dealRcvMsg("onAdvertisingDataSet($status)")
            /*
             Wait for onAdvertisingDataSet callback...
             */
            // 2
            advertisingSet?.setScanResponseData(
                buildAdvertiseData()
//                AdvertiseData.Builder()
//                    .addServiceUuid(ParcelUuid(PaxBleConfig.PAX_SERVICE_UUID))
//                    .build()
            )
        }

        override fun onScanResponseDataSet(advertisingSet: AdvertisingSet?, status: Int) {
            dealRcvMsg("onScanResponseDataSet($status)")
            // 3
        }

        override fun onAdvertisingSetStopped(advertisingSet: AdvertisingSet?) {
            dealRcvMsg("onAdvertisingSetStopped")
        }
    }

    private val advertiseCallback = object : AdvertiseCallback() {
        override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
            super.onStartSuccess(settingsInEffect)
            dealRcvMsg("onStartSuccess")
        }

        override fun onStartFailure(errorCode: Int) {
            super.onStartFailure(errorCode)
            dealRcvMsg("onStartFailure($errorCode)")
        }
    }
}