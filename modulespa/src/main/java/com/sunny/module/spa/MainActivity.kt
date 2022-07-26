package com.sunny.module.spa

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import androidx.lifecycle.lifecycleScope
import com.sunny.lib.common.base.BaseActivity
import kotlinx.coroutines.delay
import timber.log.Timber

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startService(Intent(this, SpaModeService::class.java))

        getDialogPermission()
    }

    private fun getDialogPermission() {
        if (!Settings.canDrawOverlays(this)) {
            startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION))
        } else {
            tryRequestPermissions()
        }
    }

    private fun tryRequestPermissions() {
        if (checkSelfPermission(Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED) {
            Timber.i("requestPermissions")
            requestPermissions(arrayOf(Manifest.permission.SYSTEM_ALERT_WINDOW), 100)
        } else {
            dealStart()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Timber.i("onRequestPermissionsResult(${permissions[0]} , ${grantResults[0]})")
        dealStart()
    }

    private fun dealStart() {
        lifecycleScope.launchWhenCreated {
            delay(3_000)
            sendBroadcast(Intent(SpaModeService.INNER_ON))
            finish()
        }
    }
}