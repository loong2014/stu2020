package com.sunny.module.nfc.service

import android.content.ComponentName
import android.nfc.cardemulation.CardEmulation
import com.sunny.module.nfc.NfcBaseActivity

class NfcServiceActivity : NfcBaseActivity() {

    var mCardEmulation: CardEmulation? = null

    override fun doInit() {
        super.doInit()
        mCardEmulation = CardEmulation.getInstance(nfcAdapter)

        //检查您的服务是否为默认服务
        /*
        应用可以使用 isDefaultServiceForCategory(ComponentName, String) API 检查其 HCE 服务是否为某个特定类别的默认服务。
        如果您的服务不是默认服务，则可以请求将其设置为默认服务。请参阅 ACTION_CHANGE_DEFAULT。
         */
        val service: ComponentName = ComponentName(
            "com.sunny.module.nfc",
            "com.sunny.module.nfc.service.SunnyHostApduService"
        )
        mCardEmulation?.isDefaultServiceForCategory(service, "sunnyOther")
    }


}