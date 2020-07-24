package com.sunny.other.suanfa;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxin17 on 2020-04-24
 * 119. 杨辉三角 II
 * 给定一个非负索引 k，其中 k ≤ 33，返回杨辉三角的第 k 行。
 */
public class SuanFaSolution119 {

    public static void main(String[] args) {
        System.out.println("out :" + getRow(4));
    }

    public static List<Integer> getRow(int rowIndex) {

        int[] list = new int[rowIndex+1];

        list[0] = 1;

        for (int row = 1; row <= rowIndex; ) {
            list[row] = 1;
            for (int i = row; i > 1; i--) {
                list[i-1] = list[i-1]+list[i-2];
            }
            row++;
        }

        List<Integer> out = new ArrayList<>(list.length);
        for (int i:list){
            out.add(i);
        }
        return out;
    }


    public static List<Integer> getRow3(int rowIndex) {
        List<Integer> list = new ArrayList<>();
        list.add(1);

        for (int row = 1; row <= rowIndex; ) {
            list.add(1);
            for (int i = row; i > 1; i--) {
                list.set(i - 1, list.get(i - 1) + list.get(i - 2));
            }
            row++;
        }
        return list;
    }

    public static List<Integer> getRow2(int rowIndex) {
        List<Integer> list = new ArrayList<>();
        list.add(1);

        for (int row = 1; row <= rowIndex; row++) {
            List<Integer> tmpList = list;
            list = new ArrayList<>();

            list.add(1);

            for (int i = row; i > 1; i--) {
                list.set(i, tmpList.get(row - i) + tmpList.get(row - i - 1));
            }

        }

        return list;
    }

//    [[1],
//    [1,1],
//    [1,2,1],
//    [1,3,3,1],
//    [1,4,6,4,1],
//    [1,5,10,10,5,1]]
}
