package com.sunny.module.view.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object CommonAdapter {

    @BindingAdapter("sunnyImg")
    @JvmStatic
    fun sunnyImg(v: ImageView, url: String?) {
        url?.let {
            Glide.with(v).load(url).into(v)
        }
    }
}