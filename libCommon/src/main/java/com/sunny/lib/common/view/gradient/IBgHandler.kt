package com.sunny.lib.common.view.gradient

/**
 * Created by songzhukai on 2020/9/28.
 * 此组件负责桌面背景图切换业务
 * @see BgHandler
 *
 * 目前由观星使用此组件
 * 1.全局桌面背景
 * 2.单一桌面背景
 *
 */
interface IBgHandler {
    /** 绑定桌面全局背景图 **/
    fun bindGlobalImg(url: String)

    /** 更新背景图 **/
    fun handle(bgMsg: BgMsg)
}

data class BgMsg(val type: Type, val url: String = "")

enum class Type {
    Global, //全局背景图
    Single  //单一背景图
}