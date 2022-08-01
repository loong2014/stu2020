package com.sunny.lib.common.mvvm

/**
 */
class CommonActionEvent(val action: Int) {
    companion object {
        const val SHOW_LOADING_VIEW = 0
        const val SHOW_CONTENT = 1
        const val SHOW_EMPTY = 2
        const val SHOW_ERROR = 3
        const val SHOW_TOAST = 4
        const val SHOW_LOADING = 5
        const val CANCEL_LOADING = 6
    }

    var cancelled: Boolean? = null
    var isTrans: Boolean? = null
    var message: String? = null
}