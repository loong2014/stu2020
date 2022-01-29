package com.sunny.module.view

import androidx.lifecycle.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LayoutCommonViewModel : ViewModel() {
    private val _tabList: MutableLiveData<List<String>> by lazy {
        MutableLiveData<List<String>>().also {
            loadUsers()
        }
    }
    private val _showName = MutableLiveData<String>()


    val tipStr: LiveData<String> = liveData {
        emit("Loading")
        delay(4_000)
        val tip = "hello liveData"
        emit(tip)
    }

    fun showName(): LiveData<String> {
        return _showName
    }

    fun getTabList(): LiveData<List<String>> {
        return _tabList
    }

    private fun loadUsers() {
        // Do an asynchronous operation to fetch users.
        viewModelScope.launch {

            delay(2000)
            _showName.postValue("hello world")
//            _tabList.postValue(mutableListOf("TabAbc", "TabSearch"))
        }
    }

    fun getTabNames(): String {
        val sb = StringBuilder()
        _tabList.value?.forEach {
            sb.append(it)
            sb.append("  ")
        }

        return sb.toString()
    }
}