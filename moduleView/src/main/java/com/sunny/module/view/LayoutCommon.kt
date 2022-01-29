package com.sunny.module.view

import com.sunny.module.view.search.SearchFragment

object LayoutCommon {

    fun getFragmentClass(tag: String): Class<*>? {
        return when (tag) {
            "SearchFragment" -> SearchFragment::class.java
            else -> {
                null
            }
        }
    }


}