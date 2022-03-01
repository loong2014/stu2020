package com.sunny.module.nfc

import android.net.Uri
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import com.sunny.lib.base.utils.ContextProvider
import java.nio.charset.Charset
import java.util.*

/**
 * 读取公交卡信息
 * https://blog.csdn.net/bbenskye/article/details/72457685
 */
object NfcTools {

    const val URI_android = "https://www.baidu.com/index.html"
    const val URI_Baidu = "https://www.baidu.com/index.html"

    /*
    <intent-filter>
        <action android:name="android.nfc.action.NDEF_DISCOVERED" />
        <category android:name="android.intent.category.DEFAULT" />
        <data android:scheme="http"
            android:host="developer.android.com"
            android:pathPrefix="/index.html" />
    </intent-filter>
     */
    /**
     * TNF_ABSOLUTE_URI
     * 您可以通过以下方式创建一条 TNF_ABSOLUTE_URI NDEF 记录：
     */
    fun buildUriRecord(): NdefRecord {
        val uriRecord = ByteArray(0).let { emptyByteArray ->
            NdefRecord(
                NdefRecord.TNF_ABSOLUTE_URI,
                "https://www.baidu.com/index.html".toByteArray(Charset.forName("US-ASCII")),
                emptyByteArray,
                emptyByteArray
            )
        }
        return uriRecord
    }

    /*
    <intent-filter>
        <action android:name="android.nfc.action.NDEF_DISCOVERED" />
        <category android:name="android.intent.category.DEFAULT" />
        <data android:mimeType="application/vnd.com.example.android.beam" />
    </intent-filter>
     */
    /**
     * TNF_MIME_MEDIA
     * 您可以通过以下方式创建一条 TNF_MIME_MEDIA NDEF 记录：
     */
    fun buildMimeMediaRecord(auto: Boolean = true): NdefRecord {
        val mimeRecord =
            if (auto) {
                // 使用 createMime 方法
                NdefRecord.createMime(
                    "application/vnd.com.example.android.beam",
                    "Beam me up, Android".toByteArray(Charset.forName("US-ASCII"))
                )
            } else
            // 手动创建
            {
                Charset.forName("US-ASCII").let { usAscii ->
                    NdefRecord(
                        NdefRecord.TNF_MIME_MEDIA,
                        "application/vnd.com.example.android.beam".toByteArray(usAscii),
                        ByteArray(0),
                        "Beam me up, Android!".toByteArray(usAscii)
                    )
                }
            }
        return mimeRecord
    }

    /*
    <intent-filter>
        <action android:name="android.nfc.action.NDEF_DISCOVERED" />
        <category android:name="android.intent.category.DEFAULT" />
        <data android:mimeType="text/plain" />
    </intent-filter>
     */
    /**
     * RTD 为 RTD_TEXT 的 TNF_WELL_KNOWN
     * 您可以通过以下方式创建一条 TNF_WELL_KNOWN NDEF 记录：
     */
    fun createRtdTextRecord(payload: String, locale: Locale, encodeInUtf8: Boolean): NdefRecord {
        val langBytes = locale.language.toByteArray(Charset.forName("US-ASCII"))
        val utfEncoding = if (encodeInUtf8) Charset.forName("UTF-8") else Charset.forName("UTF-16")
        val textBytes = payload.toByteArray(utfEncoding)
        val utfBit: Int = if (encodeInUtf8) 0 else 1 shl 7
        val status = (utfBit + langBytes.size).toChar()
        val data = ByteArray(1 + langBytes.size + textBytes.size)
        data[0] = status.toByte()
        System.arraycopy(langBytes, 0, data, 1, langBytes.size)
        System.arraycopy(textBytes, 0, data, 1 + langBytes.size, textBytes.size)
        return NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, ByteArray(0), data)
    }

    /*
    <intent-filter>
        <action android:name="android.nfc.action.NDEF_DISCOVERED" />
        <category android:name="android.intent.category.DEFAULT" />
        <data android:scheme="http"
            android:host="example.com"
            android:pathPrefix="" />
    </intent-filter>
     */
    /**
     * RTD 为 RTD_URI 的 TNF_WELL_KNOWN
     * 您可以通过以下方式创建一条 TNF_WELL_KNOWN NDEF 记录：
     */
    fun buildRtdUriRecord(type: String? = null): NdefRecord {
        val mimeRecord =
            when (type) {
                "autoString" -> {
                    NdefRecord.createUri("http://example.com")
                }
                "autoUri" -> {

                    Uri.parse("http://example.com").let { uri ->
                        NdefRecord.createUri(uri)
                    }
                }
                else -> {
                    val uriField = "example.com".toByteArray(Charset.forName("US-ASCII"))
                    //add 1 for the URI Prefix
                    val payload = ByteArray(uriField.size + 1)

                    //prefixes http://www. to the URI
                    payload[0] = 0x01

                    //appends URI to payload
                    System.arraycopy(uriField, 0, payload, 1, uriField.size)

                    NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_URI, ByteArray(0), payload)
                }
            }
        return mimeRecord
    }

    /*
    注意：TNF_EXTERNAL_TYPE 的 URN 的规范格式为 urn:nfc:ext:example.com:externalType，
    但 NFC Forum RTD 规范包含如下声明：NDEF 记录中必须省略 URN 的 urn:nfc:ext: 部分。
    因此，您只需要提供域名（示例中为 example.com）和类型（示例中为 externalType），
    并通过英文冒号将两者分隔开。分发 TNF_EXTERNAL_TYPE 时，
    Android 会将 urn:nfc:ext:example.com:externalType URN
        转换为 vnd.android.nfc://ext/example.com:externalType URI，
    也就是示例中的 Intent 过滤器声明的 URI。
     */
    /*
    <intent-filter>
        <action android:name="android.nfc.action.NDEF_DISCOVERED" />
        <category android:name="android.intent.category.DEFAULT" />
        <data android:scheme="vnd.android.nfc"
            android:host="ext"
            android:pathPrefix="/com.example:externalType"/>
    </intent-filter>
     */
    /**
     * TNF_EXTERNAL_TYPE
     * 您可以通过以下方式创建一条 TNF_EXTERNAL_TYPE NDEF 记录：
     */
    fun buildExternalRecord(auto: Boolean = true): NdefRecord {
        var payload: ByteArray = "hello nfc".toByteArray()

        val mimeRecord =
            if (auto) {
                //usually your app's package name
                val domain = "com.example"
                val type = "externalType"
                NdefRecord.createExternal(domain, type, payload)
            } else
            // 手动创建
            {
                NdefRecord(
                    NdefRecord.TNF_EXTERNAL_TYPE,
                    "com.example:externalType".toByteArray(Charset.forName("US-ASCII")),
                    ByteArray(0),
                    payload
                )
            }
        return mimeRecord
    }

    fun buildAARRecord(): NdefRecord {
        return ContextProvider.appContext.packageName.let {
            NdefRecord.createApplicationRecord(it)
        }
    }

    fun buildMessage(records: Array<NdefRecord>, withAAR: Boolean = false): NdefMessage {
        val array: Array<NdefRecord> = if (withAAR) {
            mutableListOf<NdefRecord>().apply {
                addAll(records)
                add(buildAARRecord())
            }.toTypedArray()
        } else {
            records
        }
        return NdefMessage(
            array
        )
    }

}