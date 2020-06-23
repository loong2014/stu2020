package com.sunny.other.suanfa;

/**
 * Created by zhangxin17 on 2020-04-24
 * 28. 实现 strStr()
 */
public class SuanFaSolution11 {


    public static void main(String[] args) {
        String str = "aaaaa";
        String key = "bba";
        Object out = strStr(str, key);

        System.out.println("out :" + out);
    }

    private static int strStr(String haystack, String needle) {
        if (needle.isEmpty()) {
            return 0;
        }

        if (haystack.isEmpty()) {
            return -1;
        }

        int len = haystack.length();
        int keyLen = needle.length();

        int index = 0;
        int keyIndex = 0;

        while (true) {

            if (len - index < keyLen) {
                return -1;
            }

            if (haystack.charAt(index + keyIndex) != needle.charAt(keyIndex)) {
                index++;
                keyIndex = 0;
            } else {
                keyIndex++;
            }

            if (keyIndex >= keyLen) {
                return index;
            }
        }
    }

}
