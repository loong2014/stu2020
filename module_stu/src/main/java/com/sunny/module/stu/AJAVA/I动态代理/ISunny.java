package com.sunny.module.stu.AJAVA.I动态代理;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ISunny {


    interface IBase {
        String getName(String tip);
    }

    static class BaseImpl implements IBase {

        @Override
        public String getName(String tip) {
            System.out.println("BaseImpl  getName :" + tip);
            return "getName BaseImpl(" + tip + ")";
        }
    }

    public static void main(String[] args) {
        IBase base = new BaseImpl();

        IBase proxyBase = (IBase) Proxy.newProxyInstance(base.getClass().getClassLoader(),
                base.getClass().getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                        Object result = null;
                        System.out.println("invoke method :" + method);
                        System.out.println("invoke args :" + args);
                        if (method.getName().equals("getName")) {
                            result = method.invoke(base, args);
                            System.out.println("do more  :" + result);
                        }

                        return "被拦截了";
                    }
                });


        String name = proxyBase.getName("haha");
        System.out.println("name  :" + name);
    }
}
