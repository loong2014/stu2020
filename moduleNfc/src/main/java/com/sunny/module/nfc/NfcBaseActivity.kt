package com.sunny.module.nfc

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.MifareUltralight
import android.os.Bundle
import com.sunny.lib.common.base.BaseActivity
import kotlinx.android.synthetic.main.nfc_activity_stu.*
import timber.log.Timber

open class NfcBaseActivity : BaseActivity() {

    fun buildTitle(): String {
        return _tag
    }

    private val _tag: String by lazy {
        javaClass.simpleName
    }
    var isSupport = false
    var nfcAdapter: NfcAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nfc_activity_stu)
        title = buildTitle()
        doCheck()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        // onResume gets called after this to handle the intent
        setIntent(intent)
    }

    private fun doCheck() {
        if (isSupport) return

        // Check for available NFC Adapter
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter == null) {
            log("Not Support NFC")
            return
        }

        isSupport = true
        doInit()
    }

    open fun doInit() {

    }

    override fun onResume() {
        super.onResume()
        // Check to see that the Activity started due to an Android Beam
        intent?.also {
            doProcessIntent(it)
        }
    }

    open fun doProcessIntent(intent: Intent) {
        //
        intent.action?.also {
            log("doProcessIntent $it")
            tv_action.text = it
        }
//        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
//
//        }
//        if (NfcAdapter.ACTION_TECH_DISCOVERED == intent.action) {

        // 从 Intent extra 获取 NDEF 消息
        var sb = StringBuilder("EXTRA_NDEF_MESSAGES")
        intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)?.also { rawMessages ->
            val messages: List<NdefMessage> = rawMessages.map { it as NdefMessage }
            sb.append("\nmessages :${messages.size}")

            messages.forEachIndexed { indexM, ndefMessage ->
                sb.append("\nndefMessage($indexM)#${ndefMessage}")
                ndefMessage.records.forEachIndexed { indexR, ndefRecord ->
                    ndefRecord?.run {
                        sb.append(
                            "\n>>>ndefRecord($indexR)#MimeType(${toMimeType()})" +
                                    " , id(${String(id)})" +
                                    " , payload${String(payload)}" +
                                    " , toUri(${toUri()})"
                        )
                    }
                }
            }

        }
        showResult(sb)

        // 从 Intent 中获取 Tag 对象，该对象包含负载并允许您枚举标签的技术
        sb = StringBuilder("EXTRA_TAG")
        (intent.getParcelableExtra(NfcAdapter.EXTRA_TAG) as Tag).also { tag ->
            sb.append("\ntechList :${tag.techList.size}")

            tag.techList.forEachIndexed { indexT, s ->
                sb.append("\ntag($indexT)#${s}")
            }
        }
        showTag(sb)

        //
        MifareUltralight.get(intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)).type

    }

    fun showTag(sb: StringBuilder) {
        val result = sb.toString()
        tv_tag.text = result
        log(result)
    }

    fun showResult(sb: StringBuilder) {
        val result = sb.toString()
        tv_result.text = result
        log(result)
    }

    fun showTip(tip: String) {
        tv_tip.text = tip
        log(tip)
    }

    fun log(log: String) {
        Timber.i("$_tag>>>$log")
    }

}