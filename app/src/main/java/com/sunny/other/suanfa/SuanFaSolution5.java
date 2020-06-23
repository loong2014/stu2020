package com.sunny.other.suanfa;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangxin17 on 2020-04-24
 */
public class SuanFaSolution5 {


    public static void main(String[] args) {

        String in = "IV";
        int out = romanToInt(in);
        System.out.println("romanToInt  " + in + " >>> " + out);
    }

    private static int romanToInt(String s) {
        Map<Character, Integer> keyMap = new HashMap<>();

        keyMap.put('I', 1);
        keyMap.put('V', 5);
        keyMap.put('X', 10);
        keyMap.put('L', 50);
        keyMap.put('C', 100);
        keyMap.put('D', 500);
        keyMap.put('M', 1000);

        char[] chars = s.toCharArray();

        int out = 0;
        int len = chars.length;
        int tmp = 0;
        for (int i = 0; i < len; i++) {

            if (tmp != 0) {
                out = out + keyMap.get(chars[i]) - tmp;
                tmp = 0;
                continue;
            }

            if (i + 1 < len && keyMap.get(chars[i]) < keyMap.get(chars[i + 1])) {
                tmp = keyMap.get(chars[i]);
                continue;
            }

            out = out + keyMap.get(chars[i]);
        }
        return out;
    }


}
