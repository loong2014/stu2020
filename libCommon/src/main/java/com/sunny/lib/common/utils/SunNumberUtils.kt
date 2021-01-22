package com.sunny.lib.common.utils

/**
 * Created by zhangxin17 on 2021/1/22
 */
object SunNumberUtils {


    /**
     * 求最大值
     */
    fun <T : Comparable<T>> max(vararg nums: T): T {
        if (nums.isEmpty()) throw RuntimeException("Params can not be empty")

        var maxNum = nums[0]
        for (num in nums) {
            if (num > maxNum) {
                maxNum = num
            }
        }
        return maxNum
    }
}