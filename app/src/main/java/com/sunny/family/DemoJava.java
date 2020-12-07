package com.sunny.family;

import java.util.Random;

public class DemoJava {

    interface Function<Arg, Return> {
        Return apply(Arg arg);
    }

    Function<String, Integer> strLen = new Function<String, Integer>() {
        @Override
        public Integer apply(String s) {
            return s.length();
        }
    };

    interface PhotoCallback {
        void getList();
    }

    private PhotoCallback callback = new PhotoCallback() {
        @Override
        public void getList() {

        }
    };

    public static void main(String[] args) {
        DemoJava demoJava = new DemoJava();

        int len = demoJava.strLen.apply("hello");
        System.out.println("len of hello :" + len);

//
//        boolean[] danShiRed = getRandom(10, 10);
//
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

        int yi  = wan/10000;
        int wan2 = wan%10000;

        System.out.println("wan :" + wan);
        System.out.println("yi :" + yi);
        System.out.println("wan2 :" + wan2);


//                2019141期 12-08 19:00:00截止

//        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm:ss");
//         String dateString = formatter.format(time);
//
//        System.out.println("dateString :" + dateString);

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
