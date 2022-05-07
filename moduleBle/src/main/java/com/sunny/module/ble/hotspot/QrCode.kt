package com.sunny.module.ble.hotspot.arcode

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.sunny.lib.base.log.SunLog
import com.sunny.lib.common.utils.ResUtils
import java.util.*

/**
 * Created by zhangxin17 on 2020/7/29
 */
const val TAG = "QrCode-QrCodeMode"

interface IQrCode {

    /**
     * 生成二维码图片，不带白边
     */
    fun createQrBitmap(content: String, qrWidth: Int, qrHeight: Int): Bitmap

    /**
     * 生成二维码图片，带icon
     */
    fun createQrBitmapWithIcon(content: String, qrWidth: Int, qrHeight: Int, iconResId: Int): Bitmap

    /**
     * 去除二维码四周的白框
     */
    fun deleteWhite(matrix: BitMatrix): BitMatrix {
        val rec = matrix.enclosingRectangle
        val resW = rec[2]
        val resH = rec[3]

        val resMatrix = BitMatrix(resW, resH)
        resMatrix.clear()
        for (x in 0 until resW) {
            for (y in 0 until resH) {
                if (matrix.get(rec[0] + x, rec[1] + y)) {
                    resMatrix.set(x, y)
                }
            }
        }
        return resMatrix
    }

    /**
     * 生成指定大小的bitmap
     */
    fun createIconBitmap(iconResId: Int, width: Int, height: Int): Bitmap {
        val bitmap = ResUtils.getBitmap(iconResId)
        val w = bitmap.width
        val h = bitmap.height

        val scaleW = width.toFloat() / w
        val scaleH = height.toFloat() / h

        val matrix = Matrix()
        matrix.postScale(scaleW, scaleH)

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true)
    }

}

class QrCodeMode {

    private val qrCode: IQrCode by lazy {
        QrCodeModeA()
    }

    fun createQrBitmap(content: String, qrWidth: Int, qrHeight: Int): Bitmap {
        return qrCode.createQrBitmap(content, qrWidth, qrHeight)
    }

    fun createQrBitmapWithIcon(
        content: String,
        qrWidth: Int,
        qrHeight: Int,
        iconResId: Int
    ): Bitmap {
        return qrCode.createQrBitmapWithIcon(content, qrWidth, qrHeight, iconResId)
    }
}

open class QrCodeModeA : IQrCode {

    override fun createQrBitmap(content: String, qrWidth: Int, qrHeight: Int): Bitmap {

        // 黑白色值，白色不可更改，黑色定制色值
        val blackColor: Int = 0xff000000.toInt()
        val whiteColor: Int = 0xffffffff.toInt()

        SunLog.e(TAG, "qrSize (${qrWidth},${qrHeight})")

        // 生成二维码的配置参数
        val hints = Hashtable<EncodeHintType, Any>()
        hints[EncodeHintType.CHARACTER_SET] = "utf-8" // 字符集编码格式
        hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H // 纠错等级 L < M < Q < H，默认L
        hints[EncodeHintType.MARGIN] = 0 // 设置白边大小，默认10

        var bitMatrix = QRCodeWriter().encode(
            content, // 要编码的内容
            BarcodeFormat.QR_CODE, // 编码类型——二维码
            qrWidth, qrHeight, // 二维码的宽高
            hints // 配置参数
        )
        SunLog.e(TAG, "bitMatrixSize (${bitMatrix.width},${bitMatrix.height})")

        // 去除白边，可能会改变二维码的大小
        bitMatrix = deleteWhite(bitMatrix)

        // 生成二维码图片的宽高，二维码矩阵的大小
        val qrRelWidth = bitMatrix.width
        val qrRelHeight = bitMatrix.height
        SunLog.e(TAG, "qrRelSize (${bitMatrix.width},${bitMatrix.height})")

        // 像素点转换
        val pixels = IntArray(qrRelWidth * qrRelHeight)
        for (y in 0 until qrRelHeight) {
            for (x in 0 until qrRelWidth) {

                if (bitMatrix.get(x, y)) {
                    pixels[y * qrRelWidth + x] = blackColor
                } else {
                    pixels[y * qrRelWidth + x] = whiteColor
                }
            }
        }

        // 根据像素矩阵生成二维码bitmap
        val bitmap = Bitmap.createBitmap(qrRelWidth, qrRelHeight, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, qrRelWidth, 0, 0, qrRelWidth, qrRelHeight)

        return bitmap
    }

    /**
     * 生成时，用非透明的icon像素，替换二维码像素
     */
    override fun createQrBitmapWithIcon(
        content: String,
        qrWidth: Int,
        qrHeight: Int,
        iconResId: Int
    ): Bitmap {

        // 黑白色值，白色不可更改，黑色定制色值
        val blackColor: Int = 0xff000000.toInt()
        val whiteColor: Int = 0xffffffff.toInt()

        // 图标宽高
        val iconWidth = qrWidth * 15 / 50
        val iconHeight = qrHeight * 15 / 50
        SunLog.e(TAG, "qrSize ($qrWidth,$qrHeight) ; iconSize ($iconWidth,$iconHeight)")

        // 生成icon对应的bitmap
        val iconBitmap = createIconBitmap(iconResId, iconWidth, iconHeight)
        SunLog.e(TAG, "iconBitmapSize (${iconBitmap.width},${iconBitmap.height})")

        // 生成二维码的配置参数， icon会覆盖二维码的中间，所有选择高容错
        val hints = Hashtable<EncodeHintType, Any>()
        hints[EncodeHintType.CHARACTER_SET] = "utf-8" // 字符集编码格式

        hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H // 纠错等级 L < M < Q < H，默认L
        hints[EncodeHintType.MARGIN] = 0 // 设置白边大小，默认10

        var bitMatrix = MultiFormatWriter().encode(
            content, // 要编码的内容
            BarcodeFormat.QR_CODE, // 编码类型——二维码
            qrWidth, qrHeight, // 二维码的宽高
            hints // 配置参数
        )
        SunLog.e(TAG, "bitMatrixSize (${bitMatrix.width},${bitMatrix.height})")

        // 去除白边，可能会改变二维码的大小，与hints配置有关
        bitMatrix = deleteWhite(bitMatrix)

        // 生成二维码图片的宽高，二维码矩阵的大小
        val qrRelWidth = bitMatrix.width
        val qrRelHeight = bitMatrix.height
        SunLog.e(TAG, "qrRelSize (${bitMatrix.width},${bitMatrix.height})")

        // icon的区域——左上点，右下点
        val iconLTPointX = (qrRelWidth - iconWidth) / 2
        val iconLTPointY = (qrRelHeight - iconHeight) / 2

        val iconRBPointX = (qrRelWidth + iconWidth) / 2
        val iconRBPointY = (qrRelHeight + iconHeight) / 2
        SunLog.e(TAG, "icon area :($iconLTPointX , $iconLTPointY) ($iconRBPointX , $iconRBPointY) ")

        // 像素点转换
        val pixels = IntArray(qrRelWidth * qrRelHeight)
        for (y in 0 until qrRelHeight) {
            for (x in 0 until qrRelWidth) {

                // icon区域，使用icon的像素点
                if (x > iconLTPointX && y > iconLTPointY
                    && x < iconRBPointX && y < iconRBPointY
                ) {

                    val color = iconBitmap.getPixel(x - iconLTPointX, y - iconLTPointY)
                    // 透明则不显示
                    if (!isTransparentPoint(color)) {
                        pixels[y * qrRelWidth + x] = color
                        continue
                    }
                }
                // 其它区域
                if (bitMatrix.get(x, y)) {
                    pixels[y * qrRelWidth + x] = blackColor
                } else {
                    pixels[y * qrRelWidth + x] = whiteColor
                }
            }
        }

        // 根据像素矩阵生成二维码bitmap
        val bitmap = Bitmap.createBitmap(qrRelWidth, qrRelHeight, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, qrRelWidth, 0, 0, qrRelWidth, qrRelHeight)

        return bitmap


    }

    /**
     * 判断像素点是否透明
     */
    private fun isTransparentPoint(color: Int): Boolean {
        return Color.alpha(color) < 255
    }
}

class QrCodeModeB : QrCodeModeA() {
    /**
     * 二维码图片和icon图片叠加合成
     */
    override fun createQrBitmapWithIcon(
        content: String,
        qrWidth: Int,
        qrHeight: Int,
        iconResId: Int
    ): Bitmap {

        val qrBitmap = createQrBitmap(content, qrWidth, qrHeight)

        // 图标宽高
        val iconWidth = qrWidth * 15 / 50
        val iconHeight = qrHeight * 15 / 50
        val iconBitmap = createIconBitmap(iconResId, iconWidth, iconHeight)


        // icon的区域——左上点，右下点
        val iconLTPointX = (qrWidth - iconWidth) / 2
        val iconLTPointY = (qrHeight - iconHeight) / 2


        // 以二维码为画布
        val canvas = Canvas(qrBitmap)
        canvas.drawBitmap(iconBitmap, iconLTPointX.toFloat(), iconLTPointY.toFloat(), null)
        return qrBitmap
    }
}

