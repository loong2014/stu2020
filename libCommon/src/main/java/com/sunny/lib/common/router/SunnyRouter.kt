package com.sunny.lib.common.router

/**
 * Created by zhangxin17 on 2020/11/12
 * https://www.jianshu.com/p/a33029557b1f
 */
class RouterConstant {

    open class Home {
        companion object {
            const val PageDemo = "/home/demo"
            const val PageHome = "/home/home"
        }
    }

    open class Login {
        companion object {
            const val PageDemo = "/login/demo"
            const val PageLogin = "/login/login"
        }
    }

    open class View {
        companion object {
            const val PageDemo = "/view/demo"
            const val PageXxx = "/view/xxx"
        }
    }

    open class Web {
        companion object {
            const val PageDemo = "/web/demo"
        }
    }

    open class Param {
        companion object {
            const val Key = "ley"
        }
    }


}