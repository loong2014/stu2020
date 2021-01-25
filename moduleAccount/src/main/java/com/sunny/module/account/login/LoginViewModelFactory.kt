package com.sunny.module.account.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Created by zhangxin17 on 2021/1/25
 */
class LoginViewModelFactory(private val count: Int) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(count = count) as T
    }
}