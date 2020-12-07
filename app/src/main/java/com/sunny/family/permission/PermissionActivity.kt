package com.sunny.family.permission

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.sunny.family.FamilyApplication
import com.sunny.family.R
import com.sunny.lib.base.BaseActivity
import com.sunny.lib.jump.PageJumpUtils
import com.sunny.lib.router.RouterConstant
import com.sunny.lib.utils.ContextProvider
import com.sunny.lib.utils.HandlerUtils
import kotlinx.android.synthetic.main.act_permission.*
import java.util.*

@Route(path = RouterConstant.PagePermission)
class PermissionActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_permission)

        btn_call_phone.setOnClickListener {
            if (checkPermission(Manifest.permission.CALL_PHONE, 1)) {
                tryCallPhone()
            }
        }

        btn_read_contacts.setOnClickListener {
            if (checkPermission(Manifest.permission.READ_CONTACTS, 2)) {
                showToast("通讯录读取权限获取成功")
            }
        }
    }

    /**
     * 需要申请：android.permission.CALL_PHONE
     * 在6.0以下的设备上，只要在manifest中添加即可： <uses-permission android:name="android.permission.CALL_PHONE"/>
     * 但在6.0及更高版本上则无法正常运行
     */
    private fun tryCallPhone() {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:10086")
        startActivity(intent)
    }

    /**
     * 判断应用是否具有该权限，如果没有，向用户申请该权限，申请结果在onRequestPermissionsResult中回调
     */
    private fun checkPermission(permission: String, requestCode: Int): Boolean {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
            return false
        }
        return true
    }

    /**
     * 权限申请结果
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            1 -> {
                if (verifyPermissions(grantResults)) {
                    tryCallPhone()
                } else {
                    showPermissionTipDialog()
                }
            }

            2 -> {
                if (verifyPermissions(grantResults)) {
                    showToast("获取成功")
                } else {
                    showToast("获取失败")
                }
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
        HandlerUtils.uiHandler.postDelayed({
            PageJumpUtils.jumpHomePage()
            doExitActivity()
        }, 500)
    }

}
