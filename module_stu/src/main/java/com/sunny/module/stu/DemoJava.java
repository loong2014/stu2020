package com.sunny.module.stu;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


class TClass<T> {

    public static int num = 0;

    public List<String> getStr() {
        List<String> list = new ArrayList<>();
        list.add("dddd");
        return list;
    }

    public List<Integer> getInt() {
        List<Integer> list = new ArrayList<>();
        list.add(10);
        return list;
    }

    public String getName() {
        return "dddd";
    }

    public Integer getAge() {
        return 121;
    }
}
public class DemoJava {


    public static void main(String[] args) {
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < danShiRed.length; i++) {
//            sb.append(i);
//            sb.append("(");
//            sb.append(danShiRed[i]);
//            sb.append(")");
//            sb.append(",");
//
//        }
//        System.out.println("out :" + sb.toString());

        double time = 413413413;

        int wan = (int) (time / 10000);

        int yi = wan / 10000;
        int wan2 = wan % 10000;

        System.out.println("wan :" + wan);
        System.out.println("yi :" + yi);
        System.out.println("wan2 :" + wan2);


//                2019141期 12-08 19:00:00截止

//        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm:ss");
//         String dateString = formatter.format(time);
//
//        System.out.println("dateString :" + dateString);
        List<String> l1 = new ArrayList<>();
        List<Integer> l2 = new ArrayList<>();

        System.out.println(l1.getClass() == l2.getClass());


        TClass tClass = new TClass<Integer>();
        List<String> list = tClass.getInt();

//        String name = tClass.getAge();
//
//        for (String str : list) {
//
//            System.out.println("str :" + str);
//        }

        System.out.println("list size :" + list.size());

        tClass.num = 1;

        TClass dd = new TClass();
        dd.num = 3;

        System.out.println("tClass num :" + tClass.num);

    }


    public static boolean[] getRandom(int bound, int count) {
        boolean[] out = new boolean[bound];
        Random rand = new Random();
        int next;
        for (int i = 0; i < count; ) {
            next = rand.nextInt(bound);
            if (!out[next]) {
                out[next] = true;
                i++;
            }
        }
        return out;
    }

}
