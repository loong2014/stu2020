package com.sunny.module.web.webview

object FFConfig {

    val URL_AMAZON =
        "http://www.amazon.com/gp/product/B09ZV2Q69R/ref=pv_ag_gcf?autoplay=1"

    val URL_YOUTUBE =
        "https://www.youtube.com/watch?v=F3e0zMx72t4"

    val URL_YOUTUBE_MUSIC =
        "https://www.youtube.com/watch?v=b1kbLwvqugk&list=RDCLAK5uy_kmPRjHDECIcuVwnKsx2Ng7fyNgFKWNJFs&index=1"

    val URL_SLING =
        "https://watch.sling.com/1/asset/6081df9f567e48ef94c5e9fd52473093/watch"

    // 默认版
    val PAX_UA_NORMAL =
        "Mozilla/5.0 (Linux; Android 10; DF91) " +
                "AppleWebKit/537.36 (KHTML, like Gecko) " +
                "Chrome/113.0.5672.77 " +
                "Safari/537.36 FFBrowser/101.10"

    // 桌面版
    val PAX_UA_DESKTOP =
        "Mozilla/5.0 (X11; Linux x86_64) " +
                "AppleWebKit/537.36 (KHTML, like Gecko) " +
                "Chrome/113.0.5672.77 " +
                "Safari/537.36 FFBrowser/101.10"


    fun getUA(isDesktop:Boolean):String {
        return if (isDesktop) PAX_UA_DESKTOP else PAX_UA_NORMAL
    }
}