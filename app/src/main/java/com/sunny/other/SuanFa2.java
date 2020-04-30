package com.sunny.other;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxin17 on 2020-04-24
 */
public class SuanFa2 {



    List<String> res = new ArrayList<>();

    public List<String> generateKuohao(int n) {
        dfs(n, n, "");

        System.out.println(res.size());
        System.out.println(res.toString());
        return res;
    }

    private void dfs(int left, int right, String curStr) {
        System.out.println("left:" + left + " , right:" + right + " , 111 curStr :" + curStr);

        if (left == 0 && right == 0) {
            res.add(curStr);
            System.out.println("add :" + curStr);
            return;
        }

        if (left > 0) {
            dfs(left - 1, right, curStr + "(");
        }

        if (right > left) {
            dfs(left, right - 1, curStr + ")");
        }

        System.out.println("left:" + left + " , right:" + right + " , 222 curStr :" + curStr);
    }

    public static void main(String[] args) {
        SuanFa2 testKuoHao = new SuanFa2();
        testKuoHao.generateKuohao(4);
    }
}
