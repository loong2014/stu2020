package com.sunny.module.view


data class LayoutDemoModel(val icon: String = "", val name: String, val jumpPath: String)


interface DemoItemClickListener {
    fun onItemClick(model: LayoutDemoModel)
}