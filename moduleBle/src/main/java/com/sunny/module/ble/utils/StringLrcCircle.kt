package com.sunny.module.ble.utils


class StringLrcCircle(private val maxSize: Int) {

    private var tail: Int = 0

    private val array: Array<String?> = arrayOfNulls(maxSize)

    fun add(msg: String) {
        array[tail] = msg
        tail = (tail + 1) % maxSize
    }

    fun clear() {
        for (i in 0 until maxSize) {
            array[i] = null
        }
    }

    fun toList(): List<String> {
        val list = mutableListOf<String>()
        var head = tail
        for (i in 1..maxSize) {
            array[head]?.let {
                list.add(it)
            }
            head = (head + 1) % maxSize
        }
        return list
    }

}