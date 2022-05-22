package com.sunny.lib.base.utils

object SunRandomTools {

    const val UpperLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    const val LowerLetters = "abcdefghijklmnopqrstuvwxyz"
    const val Nums = "0123456789"

    const val AllCharacters = UpperLetters + LowerLetters + Nums

    fun buildRandomString(
        minLen: Int = 1,
        maxLen: Int = AllCharacters.length,
        prefix: String = "",
        suffix: String = ""
    ): String {

        val sb = StringBuilder()

        if (prefix.isNotBlank()) {
            sb.append(prefix)
        }

        val len = (minLen..maxLen).random() // [minLen,MaxLen]
        for (i in 0..len) {
            sb.append(AllCharacters.random())
        }

        if (suffix.isNotBlank()) {
            sb.append(suffix)
        }
        return sb.toString()
    }
}