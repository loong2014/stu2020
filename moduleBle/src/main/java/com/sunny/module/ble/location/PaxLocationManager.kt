package com.sunny.module.ble.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sunny.lib.base.log.SunLog
import java.util.*

object PaxLocationManager {

    lateinit var appContext: Context

    private val _zipCodeTip: MutableLiveData<String> = MutableLiveData<String>("")

    fun getZipCodeTip(): LiveData<String> = _zipCodeTip

    val locationLV: MutableLiveData<Location?> = MutableLiveData()
    val addressLV: MutableLiveData<Address?> = MutableLiveData()
    val providersLV: MutableLiveData<List<String>> = MutableLiveData()

    fun doInit(context: Context) {
        appContext = context.applicationContext
    }

    fun doRelease(context: Context) {
    }

    fun doCheck(context: Activity, requestCode: Int) {
        if (doPermissionCheck(context)) {
            showLog("权限检测通过")
        } else {
            requestPermission(context, requestCode)
        }

        //
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers = lm.getProviders(true)

        providersLV.postValue(providers)
        showLog("可用方式(${providers.size})")
        providers.forEachIndexed { index, s ->
            showLog("$index >>> $s")
        }
    }

    fun doUpdateByGPS(context: Context) {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showLog("GPS可用")
        } else {
            showLog("跳转设置页面")
            openGPSSettings(context)
            return
        }

        //
        tryGetLocationInfo(lm, LocationManager.GPS_PROVIDER)
    }

    fun doUpdateByNetwork(context: Context) {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            showLog("Network可用")
        } else {
            showLog("跳转设置页面")
            openGPSSettings(context)
            return
        }
        //
        tryGetLocationInfo(lm, LocationManager.NETWORK_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    private fun tryGetLocationInfo(lm: LocationManager, provider: String) {
        showLog("tryGetLocationInfo($provider)")
        val location = lm.getLastKnownLocation(provider)
        locationLV.postValue(location)
        if (location == null) {
            showLog("requestLocationUpdates($provider)")
            lm.requestLocationUpdates(provider, 3000, 1F, locationListener)
        } else {
            updateLocation(location)
        }
    }

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location?) {
            showLog("onLocationChanged :$location")
            updateLocation(location)
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        }

        override fun onProviderEnabled(provider: String?) {
        }

        override fun onProviderDisabled(provider: String?) {
        }

    }

    private fun updateLocation(location: Location?) {
        showLog("updateLocation :$location")
        addressLV.postValue(null)

        if (location == null) return

        val addressList = Geocoder(appContext, Locale.getDefault()).getFromLocation(
            location.latitude,
            location.longitude,
            1
        )
        showLog("getFromLocation(${addressList?.size})")
        addressList?.forEachIndexed { index, address ->
            showLog("$index >>> ${address.phone} , ${address.postalCode}")
            if (index == 0) {
                addressLV.postValue(address)
                _zipCodeTip.postValue("${location.latitude} , ${location.longitude}  >>>  ${address.postalCode}")
            }
        }
    }

    private fun openGPSSettings(context: Context) {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    private fun doPermissionCheck(context: Context): Boolean {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
        return true
    }

    /**
     * 判断是否有访问位置的权限，没有权限，直接申请位置权限
     */
    private fun requestPermission(activity: Activity, requestCode: Int): Boolean {
        activity.requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), requestCode
        )
        return true
    }

    private fun showLog(log: String) {
        SunLog.i("PaxLocation", log)
    }
}