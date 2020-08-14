package com.sunny.other.suanfa;

import java.util.HashMap;
import java.util.Map;

/**
 * 242. 有效的字母异位词
 * 给定两个字符串 s 和 t ，编写一个函数来判断 t 是否是 s 的字母异位词。
 * <p>
 * 字母异位词：指字母相同，但排列不同的字符串。
 */
public class SuanFaSolution242 {

    public static void main(String[] args) {
        System.out.println("out :" + isAnagram("", ""));
    }


    /**
     * error：性能太低
     */
    public static boolean isAnagram(String s, String t) {
        if (s != null && t != null && s.length() == t.length()) {
            for (int i = 0; i < s.length(); i++) {
                t = t.replaceFirst("" + s.charAt(i), "");
            }
            return t.isEmpty();
        }
        return false;
    }

    /**
     * error:s=ac,t=bb
     */
    public static boolean isAnagram3(String s, String t) {
        if (s != null && t != null && s.length() == t.length()) {
            int total = 0;
            for (int i = 0; i < s.length(); i++) {
                total += s.charAt(i);
                total -= t.charAt(i);
            }
            return total == 0;
        }
        return false;
    }

    /**
     * 通过哈希
     */
    public static boolean isAnagram2(String s, String t) {
        if (s != null && t != null && s.length() == t.length()) {
            Map<Character, Integer> map = new HashMap<>();
            for (int i = 0; i < s.length(); i++) {
                Integer count;
                char cs = s.charAt(i);
                count = map.get(cs);
                if (count == null) {
                    count = 0;
                }
                count++;
                map.put(cs, count);

                //
                char ct = t.charAt(i);
                count = map.get(ct);
                if (count == null) {
                    count = 0;
                }
                count -= 1;
                map.put(ct, count);
            }

            for (Integer count : map.values()) {
                if (count != 0) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

}
