package com.sunny.family.aop;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by zhangxin17 on 2020/8/25
 * <p>
 * Join Points:
 * 简称JPoints，是AspectJ的核心思想之一，它就像一把刀，把程序的整个执行过程切成了一段段不同的部分。
 * 例如，构造方法调用、调用方法、方法执行、异常等等，这些都是Join Points，
 * 实际上，也就是你想把新的代码插在程序的哪个地方，是插在构造方法中，还是插在某个方法调用前，
 * 或者是插在某个方法中，这个地方就是Join Points，当然，不是所有地方都能给你插的，只有能插的地方，才叫Join Points。
 * <p>
 * Pointcuts:
 * Join Points和Pointcuts的区别实际上很难说，我也不敢说我理解的一定对，但这些都是概念上的内容，并不影响我们去使用。
 * Pointcuts，在我理解，实际上就是在Join Points中通过一定条件选择出我们所需要的Join Points，
 * 所以说，Pointcuts，也就是带条件的Join Points，作为我们需要的代码切入点。
 * <p>
 * Advice:
 * Advice其实是最好理解的，也就是我们具体插入的代码，以及如何插入这些代码。
 * 我们最开始举的那个例子，里面就是使用的最简单的Advice——Before。类似的还有After、Around，我们后面来讲讲他们的区别。
 * <p>
 * <p>
 * 以下是AspectJ中的三个通配符：
 * 1)* 表示匹配任意数量的字符，除了句号(.)
 * 2).. 表示比配包含句号在内的任意数量的字符。
 * 3)+ 表示比配任意给定的类型的子类和子接口。
 * <p>
 * <p>
 * Call与Execution的区别：
 * execution是在被切入的方法中，call是在调用被切入的方法前或者后。
 * <p>
 * Call（Before）
 * Pointcut{
 * Pointcut Method
 * }
 * Call（After）
 * <p>
 * Pointcut{
 * execution（Before）
 * Pointcut Method
 * execution（After）
 * }
 */

/**
 * 通过@ Aspect声明一个切面类
 */
@Aspect
public class AspectTest {
    private static final String TAG = "AOP-AspectTest";

    /**
     * @ Before : Advice，也就是具体的插入点
     * <p>
     * execution : 处理Join Point的类型，例如call、execution
     * execution表达式: execution(<修饰符模式>? <返回类型模式> <方法名模式>(<参数模式>) <异常模式>?)
     * <p>
     * (* android.app.Activity.on**(..)) : 这个是最重要的表达式，
     * 第一个『*』表示返回值，『*』表示返回值为任意类型，
     * 后面这个就是典型的包名路径，其中可以包含『*』来进行通配，几个『*』没区别。同时，这里可以通过『&&、||、!』来进行条件组合。
     * ()代表这个方法的参数，你可以指定类型，例如android.os.Bundle，或者(..)这样来代表任意类型、任意个数的参数。
     * <p>
     */
//    @Before("execution(* android.app.Activity.on**(..))")
    @Before("execution(* com.sunny.family.aop.AspectTestActivity.on**(..))")
    public void onActivityMethodBefore(JoinPoint joinPoint) throws Throwable {
        String key = joinPoint.getSignature().toString();
        Log.i(TAG, "onActivityMethodBefore :" + key);
    }

    @After("execution(* com.sunny.family.aop.AspectTestActivity.on**(..))")
    public void onActivityMethodAfter(JoinPoint joinPoint) throws Throwable {
        String key = joinPoint.getSignature().toString();
        Log.i(TAG, "onActivityMethodAfter :" + key);
    }
//
////    @Around("execution(* com.sunny.family.aop.AspectTestActivity.testBtn1(..))")
//    @Before("call(* com.sunny.family.aop.AspectTestActivity.testAop(..))")
//    public void onActivityMethodAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
//        String key = proceedingJoinPoint.getSignature().toString();
//        Object[] args = proceedingJoinPoint.getArgs();
//        Object obj = args[0];
//        if (obj instanceof View) {
//            Log.i(TAG, "onActivityMethodAround  params :" + obj);
//        }
//        Log.i(TAG, "onActivityMethodAround First :" + key + " , args :" + args.length);
//
//        proceedingJoinPoint.proceed();
//        Log.i(TAG, "onActivityMethodAround Second :" + key);
//    }
//
//    /*     自定义切入文件   begin     */
//    @Pointcut("execution(@com.sunny.family.aop.AspectDebugTool * *(..))")
//    public void AspectDebugToolMethod() {
//
//    }
//
//    @Before("AspectDebugToolMethod()")
//    public void onAspectDebugToolMethodBefore(JoinPoint joinPoint) throws Throwable {
//        String key = joinPoint.getSignature().toString();
//        Log.i(TAG, "onAspectDebugToolMethodBefore :" + key);
//    }
//
//    /*     自定义切入文件   end     */

    /* 精确定位 */
//
//    /**
//     * 只在testBtn2方法中执行
//     */
//    @Pointcut("withincode(* com.sunny.family.aop.AspectTestActivity.testBtn2(..))")
//    public void invokeAOP2() {
//    }
//
//    /**
//     * 调用testAop时执行
//     */
//    @Pointcut("call(* com.sunny.family.aop.AspectTestActivity.testAop(..))")
//    public void invokeAOP() {
//    }
//
//    /**
//     * 同时满足上面量条件时，即在testBtn2方法中调用testAop时，才切入
//     */
//    @Pointcut("invokeAOP() && invokeAOP2()")
//    public void invokeAOPOnlyInAOP2(JoinPoint joinPoint) {
//    }
//
//    @Before("invokeAOPOnlyInAOP2()")
//    public void beforeInvokeAOPOnlyInAOP2(JoinPoint joinPoint) {
//        String key = joinPoint.getSignature().toString();
//        Log.d(TAG, "beforeInvokeAOPOnlyInAOP2: " + key);
//    }
}
