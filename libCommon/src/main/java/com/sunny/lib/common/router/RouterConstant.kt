package com.sunny.lib.common.router

/**
 * Created by zhangxin17 on 2020/11/12
 * https://www.jianshu.com/p/a33029557b1f
 */
class RouterConstant {

    companion object {
        const val JumpValue = "jump_value"
    }

    open class Home {
        companion object {
            const val PageDemo = "/home/demo"
            const val PageHome = "/home/home"
        }
    }

    open class Tool {
        companion object {
            const val PageDeviceInfo = "/tool/device"
        }
    }

    open class Account {
        companion object {
            const val PageDemo = "/account/demo2"
            const val PageLogin = "/account/login2"
        }
    }

    open class View {
        companion object {
            const val PageDemo = "/view/demo"
            const val PageXxx = "/view/xxx"
            const val PageDrawer = "/view/drawer"
            const val PageCoordinator = "/view/coordinator"
            const val PageStatusBar = "/view/status"
            const val PageBarBlack = "/view/bar/black"
            const val PageCardView = "/view/cardview"
            const val PageTitleUpdate = "/view/titleupdate"
            const val PageRemoteViewsService = "/view/remoteviews/service"
            const val PageRemoteViewsClient = "/view/remoteviews/client"
        }
    }

    open class Web {
        companion object {
            const val PageDemo = "/web/demo"
            const val PageWebView = "/web/webview"
            const val PageHttp = "/web/http"
        }
    }

    open class Weather {
        companion object {
            const val PageDemo = "/weather/demo"
            const val PageSearch = "/weather/search"
            const val PageWeather = "/weather/weather"
        }
    }


    open class Stu {
        companion object {
            const val PageDemo = "/stu/demo"
            const val PageBook01 = "/stu/book01"
            const val PageThread = "/stu/thread"
            const val PageIntroduction = "/stu/introduction"
            const val PageDagger = "/stu/dagger"
        }
    }

    open class Param {
        companion object {
            const val Key = "ley"
        }
    }


}