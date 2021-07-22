package com.sunny.module.stu.H锁;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class HAtomicXXX {
    public static void main(String[] args) {

        //
        AtomicInteger aInt = new AtomicInteger();
        aInt.compareAndSet(100, 200);
        aInt.decrementAndGet();
        aInt.incrementAndGet();

        //
        AtomicIntegerArray aIntegerArray = new AtomicIntegerArray(8);
        aIntegerArray.compareAndSet(1, 2, 3);

        //
        AtomicBoolean aBoolean = new AtomicBoolean();
        aBoolean.compareAndSet(true, false);

        //
        AtomicLong aLong = new AtomicLong();
        aLong.compareAndSet(100, 200);

        System.out.println("aLong :" + aLong);
        //
        HMain.Sunny s1 = new HMain.Sunny();
        AtomicReference<HMain.Sunny> aReference = new AtomicReference<>();
        aReference.compareAndSet(null, s1);

    }
}
