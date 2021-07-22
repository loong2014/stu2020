package com.sunny.module.stu.A泛型;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

class Test {

    public void setList(List<Object> list) {

    }
}

public class MainA {

    public static void main(String[] args) {

        Test test = new Test();

        List strList = new ArrayList<Object>();
//        = new ArrayList<>();
        List intList = new ArrayList<>();
        intList.add(1);
        intList.add("ddd");

        strList = intList;

        System.out.println("strList num :" + strList.getClass());
        System.out.println("intList num :" + intList.getClass());


//        test.setList(strList);
        test.setList(intList);
        //java 中泛型的原始类型 指的是忽略类型参数的泛型类，比如Bet<String> 的原始类型是Bet


        //
        ArrayList<String> arrayList = new ArrayList<String>();

        // 会提示类型检查错误
        ArrayList<String> arrayList1 = new ArrayList(); //第一种 情况
        arrayList1.add("aaa");
//        arrayList1.add(22); // error ，有类型检查

        ArrayList arrayList2 = new ArrayList<String>();//第二种 情况
        arrayList2.add("aaa");
        arrayList2.add(22);

        // 通过反射添加
        try {
            ArrayList<Integer> arrayList3 = new ArrayList<Integer>();
            arrayList3.add(1);//这样调用add方法只能存储整形，因为泛型类型的实例为Integer
            arrayList3.getClass().getMethod("add", Object.class).invoke(arrayList3, "asd");
            for (int i = 0; i < arrayList3.size(); i++) {
                System.out.println(arrayList3.get(i));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }



    }
}
