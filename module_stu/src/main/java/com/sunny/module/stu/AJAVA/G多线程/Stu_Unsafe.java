package com.sunny.module.stu.AJAVA.G多线程;

import com.sunny.module.stu.base.StuImpl;

public class Stu_Unsafe extends StuImpl {

    @Override
    public void a_是什么() {
        super.a_是什么();
    }

    @Override
    public void d_怎么用() {
        //

        // xref: /libcore/ojluni/src/main/java/sun/misc/Unsafe.java

        // getUnsafe
        /*
57    public static Unsafe getUnsafe() {
62        ClassLoader calling = VMStack.getCallingClassLoader();
63        if ((calling != null) && (calling != Unsafe.class.getClassLoader())) {
64            throw new SecurityException("Unsafe access denied");
65        }
67        return THE_ONE;
68    }
         */

        // objectFieldOffset
        /*
78    public long objectFieldOffset(Field field) {
79        if (Modifier.isStatic(field.getModifiers())) {
80            throw new IllegalArgumentException("valid for instance fields only");
81        }
82        return field.getOffset();
83    }
         */

        // park
        /*
354    public void park(boolean absolute, long time) {
355        if (absolute) {
356            Thread.currentThread().parkUntil$(time);
357        } else {
358            Thread.currentThread().parkFor$(time);
359        }
360    }
         */

        // xref: /libcore/ojluni/src/main/java/java/lang/Thread.java
        // parkUntil$
        /*
2180    public final void parkUntil$(long time) {
2181        synchronized(lock) {
2196        final long currentTime = System.currentTimeMillis();
2197        if (time <= currentTime) {
2198            parkState = ParkState.UNPARKED;
2199        } else {
2200            long delayMillis = time - currentTime;
2201            // Long.MAX_VALUE / NANOS_PER_MILLI (0x8637BD05SF6) is the largest
2202            // long value that won't overflow to negative value when
2203            // multiplyed by NANOS_PER_MILLI (10^6).
2204            long maxValue = (Long.MAX_VALUE / NANOS_PER_MILLI);
2205            if (delayMillis > maxValue) {
2206                delayMillis = maxValue;
2207            }
2208            parkFor$(delayMillis * NANOS_PER_MILLI);
2209        }
2210        }
2211    }
         */

        // parkFor$
        /*
149    private final Object lock = new Object();

2124    public final void parkFor$(long nanos) {
2125        synchronized(lock) {
2126        switch (parkState) {
2127            case ParkState.PREEMPTIVELY_UNPARKED: {
2128                parkState = ParkState.UNPARKED;
2129                break;
2130            }
2131            case ParkState.UNPARKED: {
2132                long millis = nanos / NANOS_PER_MILLI;
2133                nanos %= NANOS_PER_MILLI;
2134
2135                parkState = ParkState.PARKED;
2136                try {
2137                    lock.wait(millis, (int) nanos);
2138                } catch (InterruptedException ex) {
2139                    interrupt();
2140                } finally {
2146                    if (parkState == ParkState.PARKED) {
2147                        parkState = ParkState.UNPARKED;
2148                    }
2149                }
2150                break;
2151            }
2152            default : {
2153                throw new AssertionError("Attempt to repark");
2154            }
2155        }
2156        }
2157    }
         */
    }
}
