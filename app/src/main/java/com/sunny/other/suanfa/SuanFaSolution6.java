package com.sunny.other.suanfa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxin17 on 2020-04-24
 */
public class SuanFaSolution6 {


    /**
     * 找出两个最长相近的字段
     * @param args
     */
    public static void main(String[] args) {

        String[] in = new String[]{"flower", "flow", "flight"};
        String out = longestCommonPrefix(in);
        System.out.println("longestCommonPrefix  " + in + " >>> " + out);
    }

    private static String longestCommonPrefix(String[] strs) {
        if (strs == null || strs.length <= 0) {
            return "";
        }

        if (strs.length == 1) {
            return strs[0];
        }

        int index = -1;
        while (true) {
            if (strs.length == 1) {
                break;
            }
            index++;
            strs = logic(index, strs);
        }

        System.out.println("lastStr :" + strs[0] + " , index :" + index);

        return strs[0].substring(0, index);
    }

    private static String[] logic(int index, String[] strs) {
        System.out.println("logic  index :" + index + " , " + strs.length);

        Map<Character, List<String>> keyMap = new HashMap<>();
        List<String> maxList = new LinkedList<>();
        char tmpKey;
        List<String> tmpList;
        for (String str : strs) {
            if (str.length() > index) {
                //
                tmpKey = str.charAt(index);
                tmpList = keyMap.get(tmpKey);

                System.out.println("str :" + str + " , char :" + tmpKey);
                //
                if (tmpList == null) {
                    tmpList = new ArrayList<>();
                    keyMap.put(tmpKey, tmpList);
                }
                tmpList.add(str);

                //
                if (tmpList.size() > maxList.size()) {
                    maxList = tmpList;
                }
            }
        }

        String[] outStrs = new String[maxList.size()];
        int outIndex = 0;
        for (String str : maxList) {
            outStrs[outIndex++] = str;
        }
        return outStrs;
    }


}
