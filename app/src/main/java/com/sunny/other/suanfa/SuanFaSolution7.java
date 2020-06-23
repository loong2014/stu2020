package com.sunny.other.suanfa;

/**
 * Created by zhangxin17 on 2020-04-24
 * 最长公共前缀
 */
public class SuanFaSolution7 {


    public static void main(String[] args) {


        String str = "c";
        System.out.println("len :" + str.length());
        System.out.println("first char :" + str.charAt(0));

        String[] in = new String[]{"c", "c"};
        String out = longestCommonPrefix(in);
        System.out.println("longestCommonPrefix  " + in + " >>> " + out);
    }

    private static String longestCommonPrefix(String[] strs) {
        if (strs == null || strs.length <= 0) {
            return "";
        }

        String prefix = strs[0];

        for (int i =0;i<strs.length;i++){
            while (strs[i].indexOf(prefix)!=0){
                prefix = prefix.substring(0,prefix.length()-1);
                if (prefix.isEmpty()){
                    return "";
                }
            }
        }
        return prefix;
    }

    private static String longestCommonPrefix2(String[] strs) {
        if (strs == null || strs.length <= 0) {
            return "";
        }

        if (strs.length == 1) {
            return strs[0];
        }

        String first = strs[0];

        int index = -1;
        doExit:
        while (true) {

            index++;
            if (first.length() <= index) {
                break;
            }
            char key = first.charAt(index);

            for (String str : strs) {
                if (str == null || str.length() <= index) {
                    break doExit;
                }

                if (key != str.charAt(index)) {
                    break doExit;
                }
            }
        }

        return first.substring(0, index);
    }
}
