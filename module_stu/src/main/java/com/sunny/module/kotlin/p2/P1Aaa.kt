package com.sunny.module.kotlin.p2

class P1Aaa private constructor(name: String) {
    companion object {
        fun build(name: String): P1Aaa {
            return P1Aaa(name)
        }
    }
}

class P1Bbb {
    companion object {
        fun build(name: String): P1Bbb {
            return P1Bbb(name)
        }
    }

    private constructor(name: String) {

    }
}