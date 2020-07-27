package com.sunny.other.suanfa;

/**
 * 168. Excel表列名称
 * 给定一个正整数，返回它在 Excel 表中相对应的列名称。
 */
public class SuanFaSolution168 {

    public static void main(String[] args) {
        System.out.println("out :" + convertToTitle(52));
    }

    public static String convertToTitle(int n) {
        StringBuilder sb = new StringBuilder();
        while (n != 0) {
            n--;
            sb.append((char) (n % 26 + 'A'));
            n /= 26;
        }
        return sb.reverse().toString();
    }

    public static String convertToTitle3(int n) {

        StringBuilder sb = new StringBuilder();
        while (n > 0) {

            int mod = n % 26;
            n = n / 26;

            if (mod == 0) {
                mod = 26;
                n = n - 1;
            }
            sb.insert(0, (char) ('A' + mod - 1));
        }

        return sb.toString();
    }

    public static String convertToTitle2(int n) {

        String out = "";
        char start = 'A' - 1;
        while (n > 26) {
            int mod = n % 26;
            n = n / 26;

            if (mod == 0) {
                mod = 26;
                n = n - 1;
            }
            out = (char) (start + mod) + out;
        }

        out = (char) (start + n) + out;
        return out;
    }
}
