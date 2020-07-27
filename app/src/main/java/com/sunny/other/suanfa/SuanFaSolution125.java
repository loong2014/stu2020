package com.sunny.other.suanfa;

/**
 * Created by zhangxin17 on 2020-04-24
 * 125. 验证回文串
 * 给定一个字符串，验证它是否是回文串，只考虑字母和数字字符，可以忽略字母的大小写。
 * 说明：本题中，我们将空字符串定义为有效的回文串。
 */
public class SuanFaSolution125 {

    public static void main(String[] args) {
        System.out.println("out :" + isPalindrome("A man, a plan, a canal: Panama"));
    }

    public static boolean isPalindrome(String s) {
        if (s == null) {
            return false;
        }

        s = s.toLowerCase();
        //
        char[] chars = new char[s.length()];
        int cIndex = 0;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if ((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9')) {
                chars[cIndex] = c;
                cIndex++;
            }
        }

        //
        int len = cIndex;
        int lIndex = 0;
        int rIndex = len - 1;
        while (rIndex >= lIndex) {
            if (chars[lIndex] != chars[rIndex]) {
                return false;
            }

            lIndex++;
            rIndex--;
        }

        return true;
    }
}
