/*
 * 应用级别的顶层函数/扩展函数
 */
package com.sunny.lib.common.expand

/**
 * 获取字符数量 汉字占2个长度，英文占1个长度
 */
fun handleText(text: String?, maxLen: Int): String {
    if (text.isNullOrBlank()) return ""

    var count = 0
    var endIndex = 0
    text.forEachIndexed { index, char ->
        count += if (char.code < 128) 1 else 2
        if (maxLen == count || (char.code >= 128 && maxLen + 1 == count)) {
            endIndex = index
            return@forEachIndexed
        }
    }
    return if (count < maxLen) text else {
        text.substring(0, endIndex) + "..."
    }
}
