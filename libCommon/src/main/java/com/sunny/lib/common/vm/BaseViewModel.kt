package com.sunny.lib.common.vm

import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sunny.lib.common.mvvm.CommonActionEvent
import com.sunny.lib.common.mvvm.safeSet
import com.sunny.lib.common.utils.ResUtils

open class BaseViewModel : ViewModel() {
    val actionLiveData = MutableLiveData<CommonActionEvent>()

    open fun showLoading(cancel: Boolean = true, isTrans: Boolean = false) {
        actionLiveData.safeSet(CommonActionEvent(CommonActionEvent.SHOW_LOADING).apply {
            this.cancelled = cancel
            this.isTrans = isTrans
        })
    }

    open fun cancelLoading() {
        actionLiveData.safeSet(CommonActionEvent(CommonActionEvent.CANCEL_LOADING))
    }

    fun showLoadingView() {
        actionLiveData.safeSet(CommonActionEvent(CommonActionEvent.SHOW_LOADING_VIEW))
    }

    fun showContent() {
        actionLiveData.safeSet(CommonActionEvent(CommonActionEvent.SHOW_CONTENT))
    }

    fun showError() {
        actionLiveData.safeSet(CommonActionEvent(CommonActionEvent.SHOW_ERROR))
    }

    fun showError(message: String?, isHideButton: Boolean? = null) {
        actionLiveData.safeSet(CommonActionEvent(CommonActionEvent.SHOW_ERROR).apply {
            this.message = message
//            this.isHideButton = isHideButton
        })
    }


    fun showEmpty() {
        actionLiveData.safeSet(CommonActionEvent(CommonActionEvent.SHOW_EMPTY))
    }

    fun showToast(@StringRes msgId: Int) {
        showToast(ResUtils.getString(msgId))
    }

    fun showToast(message: String?) {
        message ?: return
        actionLiveData.safeSet(CommonActionEvent(CommonActionEvent.SHOW_TOAST).apply {
            this.message = message
        })
    }

    fun finish() {
//        actionLiveData.safeSet(CommonActionEvent(CommonActionEvent.FINISH_ACTIVITY))
    }
}