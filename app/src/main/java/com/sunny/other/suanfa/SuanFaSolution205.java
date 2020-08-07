package com.sunny.other.suanfa;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 205. 同构字符串
 * 给定两个字符串 s 和 t，判断它们是否是同构的。
 * <p>
 * 如果 s 中的字符可以被替换得到 t ，那么这两个字符串是同构的。
 * <p>
 * 所有出现的字符都必须用另一个字符替换，同时保留字符的顺序。两个字符不能映射到同一个字符上，但字符可以映射自己本身。
 * <p>
 * "egg" == "add"
 */
public class SuanFaSolution205 {

    public static void main(String[] args) {
        String s = "abab";
        String t = "baba";
        System.out.println("out :" + isIsomorphic(s, t));
    }

    /**
     * 同时映射第三方
     */
    public static boolean isIsomorphic(String s, String t) {
        int n = s.length();
        int[] mapS = new int[128];
        int[] mapT = new int[128];

        for (int i = 0; i < n; i++) {
            char cs = s.charAt(i);
            char ct = t.charAt(i);

            if (mapS[cs] != mapT[ct]) {
                return false;
            } else {
                if (mapS[cs] == 0) {
                    mapS[cs] = i + 1;
                    mapT[ct] = i + 1;
                }
            }
        }
        return true;
    }

    /**
     * 互相映射
     */
    public static boolean isIsomorphic3(String s, String t) {
        return isIsomorphicHelp(s, t) && isIsomorphicHelp(t, s);
    }

    public static boolean isIsomorphicHelp(String s, String t) {
        int len = s.length();
        Map<Character, Character> map = new HashMap<>();

        for (int i = 0; i < len; i++) {
            char c1 = s.charAt(i);
            char c2 = t.charAt(i);
            if (map.containsKey(c1)) {
                if (map.get(c1) != c2) {
                    return false;
                }
            } else {
                map.put(c1, c2);
            }
        }
        return true;
    }


    /**
     * error
     */
    public static boolean isIsomorphic2(String s, String t) {

        int len = s.length();
        Set<Character> set = new HashSet<>(); // 保存s替换后的包含的字符

        for (int i = 0; i < len; i++) {

            System.out.println("i=" + i + " , s:" + s);

            char tChar = t.charAt(i);
            if (s.charAt(i) == tChar) {
                set.add(tChar);
                continue;
            }

            if (set.contains(tChar)) {
                String tmpS = s.substring(0, i);
                System.out.println("i=" + i + " , s:" + s + " , tmpS:" + tmpS);
                if (!t.startsWith(tmpS)) {
                    return false;
                }
            } else {
                s = s.replace(s.charAt(i), tChar);
                set.add(tChar);
            }
        }

        return s.equals(t);
    }

}
