package com.sunny.other.suanfa;

/**
 * Created by zhangxin17 on 2020-04-24
 * 58. 最后一个单词的长度
 */
public class SuanFaSolution58 {


    public static void main(String[] args) {

//        String str = "hello world";
        String str = "a ";
        Object out = lengthOfLastWord(str);
        System.out.println("out :" + out);
    }

    public static int lengthOfLastWord(String s) {
        if (null == s) {
            return 0;
        }

        char[] chars = s.toCharArray();
        int lastSpaceIndex = -2;
        int lastWorldCount = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == ' ') {
                lastSpaceIndex = i;
                continue;
            }
            if (i - 1 == lastSpaceIndex) {
                lastWorldCount = 0;
            }
            lastWorldCount++;
        }

        return lastWorldCount;
    }

    public static int lengthOfLastWord2(String s) {
        if (null == s) {
            return 0;
        }

        s = s.trim();
        if (s.length() == 0) {
            return 0;
        }

        int len = s.length();
        int lastIndex = s.lastIndexOf(' ');

        System.out.println("len :" + len + " , lastIndex :" + lastIndex);

        if (lastIndex < 0) {
            return s.length();
        }

        if (lastIndex >= len - 1) {
            return 0;
        }

        String lastWorld = s.substring(lastIndex, len - 1);

        return lastWorld.length();
    }


}
