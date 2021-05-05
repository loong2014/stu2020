package com.sunny.module.stu.selector

import com.sunny.module.stu.adb.StuAdbFragment
import com.sunny.module.stu.base.NormalFragment
import com.sunny.module.stu.base.StuBaseFragment
import com.sunny.module.stu.fragment.StuFragmentFragment
import com.sunny.module.stu.git.StuGitFragment
import com.sunny.module.stu.handler.StuHandlerFragment
import com.sunny.module.stu.other.StuHttpFragment
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
            "stu_git" -> StuGitFragment()
            "stu_fragment" -> StuFragmentFragment()
            "stu_handler" -> StuHandlerFragment()
            "stu_adb" -> StuAdbFragment()
            "stu_http" -> StuHttpFragment()
            else -> NormalFragment()
        }
        mFragmentMap[type] = result
        return result
    }
}