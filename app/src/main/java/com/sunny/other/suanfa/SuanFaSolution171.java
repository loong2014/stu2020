package com.sunny.other.suanfa;

/**
 * 171. Excel表列序号
 * 给定一个Excel表格中的列名称，返回其相应的列序号。
 */
public class SuanFaSolution171 {

    public static void main(String[] args) {
        System.out.println("out :" + titleToNumber("AAA"));
    }

    /**
     * 26进制
     */
    public static int titleToNumber(String s) {
        int ans = 0;
        for (int i = 0; i < s.length(); i++) {
            int num = s.charAt(i) - 'A' + 1;
            ans = ans * 26 + num;
        }
        return ans;
    }

    /**
     * 使用math
     */
    public static int titleToNumber3(String s) {
        if (s == null) {
            return 0;
        }
        int out = 0;

        int len = s.length();
        int cIndex = len - 1;
        int pow = 0;
        while (cIndex >= 0) {

            int num = s.charAt(cIndex) - 'A' + 1;
            if (len - cIndex == 1) {
                out = out + num;
                pow = 26;
            } else {
                out = out + num * pow;
                pow = pow * 26;
            }
            cIndex--;
        }
        return out;
    }

    /**
     * 使用math——倒序
     */
    public static int titleToNumber2(String s) {
        if (s == null) {
            return 0;
        }
        int out = 0;

        int len = s.length();
        int lastIndex = len - 1;
        while (lastIndex >= 0) {
            int pow = (int) (Math.pow(26, len - lastIndex - 1));
            out = out + (s.charAt(lastIndex) - 'A' + 1) * pow;

            lastIndex--;
        }

        return out;
    }

}
