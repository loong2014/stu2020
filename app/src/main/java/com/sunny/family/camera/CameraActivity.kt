package com.sunny.family.camera

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.sunny.family.R
import com.sunny.lib.base.BaseActivity
import com.sunny.lib.utils.SunLog
import kotlinx.android.synthetic.main.act_camera.*
import org.devio.takephoto.app.TakePhoto
import org.devio.takephoto.app.TakePhotoImpl
import org.devio.takephoto.compress.CompressConfig
import org.devio.takephoto.model.InvokeParam
import org.devio.takephoto.model.TContextWrap
import org.devio.takephoto.model.TResult
import org.devio.takephoto.permission.InvokeListener
import org.devio.takephoto.permission.PermissionManager
import org.devio.takephoto.permission.PermissionManager.TPermissionType
import org.devio.takephoto.permission.TakePhotoInvocationHandler
import java.io.File


class CameraActivity : BaseActivity(), TakePhoto.TakeResultListener, InvokeListener {

    companion object {
        val TAG = "CameraActivity"
    }

    var mInvokeParam: InvokeParam? = null

    val takePhoto by lazy {
        TakePhotoInvocationHandler.of(this).bind(TakePhotoImpl(this, this)) as TakePhoto
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        takePhoto.onCreate(savedInstanceState)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.act_camera)

        takePhoto.onEnableCompress(CompressConfig.Builder().setMaxSize(500 * 1024).create(), true)

        btn_take_photo.setOnClickListener {
            doTakePhoto()
        }
        btn_scan_photo.setOnClickListener{
            doScanPhoto()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        takePhoto.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        takePhoto.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionManager.handlePermissionsResult(this, type, mInvokeParam, this)
    }

    private fun doTakePhoto() {
        val file = File(externalCacheDir, System.currentTimeMillis().toString() + ".png")
        val imageUri = Uri.fromFile(file)
//        val imageUri: Uri = Uri.fromFile(FileUtils.buildCameraSaveFile())

        takePhoto.onPickFromCapture(imageUri)
    }
    private fun doScanPhoto(){

        takePhoto.onPickFromGallery()
    }


    override fun invoke(invokeParam: InvokeParam?): PermissionManager.TPermissionType {
        val type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam!!.method)
        if (TPermissionType.WAIT == type) {
            mInvokeParam = invokeParam
        }
        return type
    }

    override fun takeSuccess(result: TResult) {
        SunLog.i(TAG, "takeSuccess：" + result.image.compressPath)
    }

    override fun takeCancel() {
        SunLog.i(TAG, "takeCancel")
    }

    override fun takeFail(result: TResult, msg: String?) {
        SunLog.i(TAG, "takeFail :$msg")
    }


}