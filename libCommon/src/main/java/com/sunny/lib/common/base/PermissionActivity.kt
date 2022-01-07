package com.sunny.lib.common.base

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.sunny.lib.base.utils.ContextProvider
import java.util.*

/**
 * 页面需要用户授权
 */
abstract class PermissionActivity : BaseActivity() {

    companion object {
        const val RequestCodePermission = 0x00099
    }

    abstract fun buildPermissionList(): Array<String>

    abstract fun onGetPermission()


    private lateinit var _permissionList: Array<String>

    interface OnPermissionResponseListener {
        fun onSuccess(permissions: Array<out String>)
        fun onFail()
    }

    private lateinit var onPermissionResponseListener: OnPermissionResponseListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _permissionList = buildPermissionList()

        checkPermission()
    }

    private fun checkPermission() {
        requestPermission(object : OnPermissionResponseListener {
            override fun onSuccess(permissions: Array<out String>) {
                onGetPermission()
            }

            override fun onFail() {
                showPermissionTipDialog()
            }

        }, _permissionList)
    }

    private fun requestPermission(listener: OnPermissionResponseListener, permissions: Array<out String>) {
        onPermissionResponseListener = listener

        if (checkPermissions(permissions)) {
            onPermissionResponseListener.onSuccess(permissions)
        } else {
            val needPermissions = getDeniedPermissions(permissions)
            ActivityCompat.requestPermissions(this, needPermissions.toTypedArray(), RequestCodePermission)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == RequestCodePermission) {
            if (verifyPermissions(grantResults)) {
                onPermissionResponseListener.onSuccess(permissions)
            } else {
                onPermissionResponseListener.onFail()
                showPermissionTipDialog()
            }
        }
    }

    /**
     * 检测所有的权限是否都已授权
     */
    private fun checkPermissions(permissions: Array<out String>): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }

        permissions.forEach {
            if (ContextCompat.checkSelfPermission(ContextProvider.appContext, it) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }


    /**
     * 获取权限集中需要申请权限的列表
     */
    private fun getDeniedPermissions(permissions: Array<out String>): List<String> {
        val needRequestPermissionList: MutableList<String> = ArrayList()
        permissions.forEach {

            if ((ContextCompat.checkSelfPermission(ContextProvider.appContext, it) != PackageManager.PERMISSION_GRANTED)
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, it)) {
                needRequestPermissionList.add(it)
            }
        }

        return needRequestPermissionList
    }

    /**
     * 确认所有的权限是否都已授权
     * @param grantResults
     * @return
     */
    private fun verifyPermissions(grantResults: IntArray): Boolean {
        for (grantResult in grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    /**
     * 显示提示对话框
     */
    private fun showPermissionTipDialog() {
        AlertDialog.Builder(this)
                .setTitle("警告")
                .setCancelable(false)
                .setMessage("需要必要的权限才可以正常使用应用程序，您已拒绝获得该权限。 如果需要重新授权，您可以点击“允许”按钮进入系统设置进行授权")
                .setNegativeButton("取消") { dialog, _ ->
                    doExitActivity()
                    dialog?.dismiss()
                }
                .setPositiveButton("确定") { dialog, _ ->
                    jumpAppSettings()
                    dialog?.dismiss()
                }
                .show()
    }

    private fun jumpAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:$packageName")
        startActivity(intent)
    }
}