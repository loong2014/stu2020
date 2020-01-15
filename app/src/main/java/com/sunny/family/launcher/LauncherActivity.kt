package com.sunny.family.launcher

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.sunny.family.FamilyApplication
import com.sunny.family.R
import com.sunny.lib.base.BaseActivity
import com.sunny.lib.jump.PageJumpUtils
import com.sunny.lib.utils.ContextProvider
import com.sunny.lib.utils.HandlerUtils
import java.util.*

class LauncherActivity : BaseActivity() {

    private val _permissionList = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE, // 读写内存权限
            Manifest.permission.RECORD_AUDIO, // 录音权限
            Manifest.permission.CAMERA) // 相机权限

    private val _requestCodePermission = 0x00099
    private lateinit var onPermissionResponseListener: OnPermissionResponseListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_launcher)

        checkPermission()
    }

    private fun checkPermission() {

        requestPermission(object : OnPermissionResponseListener {
            override fun onSuccess(permissions: Array<out String>) {
                jumpHomeAct()
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
            ActivityCompat.requestPermissions(this, needPermissions.toTypedArray(), _requestCodePermission)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == _requestCodePermission) {
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


    interface OnPermissionResponseListener {
        fun onSuccess(permissions: Array<out String>)
        fun onFail()
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
                    FamilyApplication.exitApp()
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

    private fun jumpHomeAct() {
        HandlerUtils.getUiHandler().postDelayed({
            PageJumpUtils.jumpHomePage(null, null)
        }, 500)
    }

}
