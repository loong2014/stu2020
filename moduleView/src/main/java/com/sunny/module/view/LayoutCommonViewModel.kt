package com.sunny.module.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.*
import timber.log.Timber

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

        val scope = CoroutineScope(Job() + Dispatchers.Main + CoroutineName("SunnyDebug"))

        val job: Job = scope.launch(Dispatchers.Main) {
            showLog("A走在道路上")
            async {
                showLog("A要休息5s")
                Thread.sleep(5_000)
                // 在runBlocking中不能使用delay
//                delay(5_000)
                showLog("A休息好了")
            }
            showLog("A接着赶路")
        }
//
//        viewModelScope.launch(Dispatchers.Default) {
//            showLog("B走在道路上")
//            showLog("B要休息5s")
//            delay(5_000)
//            showLog("B休息好了，接着赶路")
//        }
//
//        viewModelScope.launch {
//            showLog("C走在道路上")
//            showLog("C要休息5s")
//            delay(5_000)
//            showLog("C休息好了，接着赶路")
//        }
//        viewModelScope.launch(Dispatchers.Default) {
//            Timber.i("走在Default的道路上 :${Thread.currentThread()}")
//            delay(3000)
//            _showName.postValue("hello world")
////            _tabList.postValue(mutableListOf("TabAbc", "TabSearch"))
//        }
    }

    private fun showLog(log: String) {
        Timber.i("${Thread.currentThread()} >>> $log")
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