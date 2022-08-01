package com.sunny.lib.common.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sunny.lib.common.R

class AppLoadingDialogFragment : BaseDialogFragment() {

    companion object {

        @JvmStatic
        fun newInstance(
            requestId: Int,
            cancelable: Boolean,
            isTrans: Boolean = false
        ): AppLoadingDialogFragment {
            return AppLoadingDialogFragment()
                .apply {
                    this.setRequestId(requestId)
                    this.isCancelable = cancelable

                    if (isTrans) {
                        setDimAmount(0.0f)

                    } else {
                        setDimAmount(0.3f)

                    }
                }
        }
    }


    private var dismissListener: (() -> Unit)? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            setFullscreen(true, false, true)
            setWrapWidth()
            setWrapHeight()
            setGravity(Gravity.CENTER)
            setCanceledOnTouchOutside(true)
//            isCanceledOnTouchOutside = true
            antiShakeDuration = 0
        }
    }

    fun setOnDismissListener(listener: () -> Unit) {
        dismissListener = listener
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dismissListener?.invoke()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_dialog_fragment_loading, container, false)
    }
}