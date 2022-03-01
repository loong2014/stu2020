package com.sunny.module.nfc

import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.NfcEvent

class NfcClientActivity : NfcBaseActivity(), NfcAdapter.CreateNdefMessageCallback {

    override fun doInit() {
        super.doInit()
        // Register callback
        nfcAdapter?.setNdefPushMessageCallback(this, this)
    }

    override fun createNdefMessage(event: NfcEvent?): NdefMessage {
        val text = "Beam me up, Android!\n\n" +
                "Beam Time: " + System.currentTimeMillis()
        return NdefMessage(
            arrayOf(
                NdefRecord.createMime(
                    "application/vnd.com.example.android.beam",
                    text.toByteArray()
                )
            )
            /**
             * The Android Application Record (AAR) is commented out. When a device
             * receives a push with an AAR in it, the application specified in the AAR
             * is guaranteed to run. The AAR overrides the tag dispatch system.
             * You can add it back in to guarantee that this
             * activity starts when receiving a beamed message. For now, this code
             * uses the tag dispatch system.
             */
            /**
             * The Android Application Record (AAR) is commented out. When a device
             * receives a push with an AAR in it, the application specified in the AAR
             * is guaranteed to run. The AAR overrides the tag dispatch system.
             * You can add it back in to guarantee that this
             * activity starts when receiving a beamed message. For now, this code
             * uses the tag dispatch system.
             *///,NdefRecord.createApplicationRecord("com.example.android.beam")
        )
    }
}