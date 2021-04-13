package com.sunny.module.stu.fragment

import com.sunny.module.stu.base.StuBaseFragment
import com.sunny.module.stu.git.StuGitFragment
import com.sunny.module.stu.handler.StuHandlerFragment
import java.util.*

object StuFragmentSelector {
    private val mFragmentMap: MutableMap<String, StuBaseFragment> = HashMap()

    @JvmStatic
    fun getFragment(type: String): StuBaseFragment {
        var result = mFragmentMap[type]
        if (result != null) {
            return result
        }

        //
        result = when (type) {
            "stu_handler" -> StuHandlerFragment()
            "stu_git" -> StuGitFragment()
            else -> NormalFragment()
        }
        mFragmentMap[type] = result
        return result
    }
}