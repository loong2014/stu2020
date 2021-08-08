package com.sunny.module.stu.AJAVA.A泛型;

import java.util.ArrayList;
import java.util.List;

public class AUseDemo {

    // 泛型类
    static class GenericClass<T> {
        T data;

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }
    }


    // 泛型接口
    interface GenericInterface<T> {
        T getData();

        void setData(T data);
    }

    // 泛型接口实现类
    static class GenericInterfaceImpl<T> implements GenericInterface<T> {

        private T data;

        @Override
        public T getData() {
            return data;
        }

        @Override
        public void setData(T data) {
            this.data = data;
        }
    }

    // 普通类中定义了泛型方法
    static class NormalClass {
        public <T> List<T> genericMethod(T[] array) {

            List<T> list = new ArrayList<>();
            for (T t : array) {
                list.add(t);
            }
            return list;
        }
    }
}
