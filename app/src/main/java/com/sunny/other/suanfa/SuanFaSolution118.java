package com.sunny.other.suanfa;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxin17 on 2020-04-24
 * 118. 杨辉三角
 * 给定一个非负整数 numRows，生成杨辉三角的前 numRows 行。
 */
public class SuanFaSolution118 {

    public static void main(String[] args) {
        System.out.println("out :" + generate(4));
    }

    public static List<List<Integer>> generate(int numRows) {

        List<List<Integer>> outList = new ArrayList<>();
        if (numRows == 0) {
            return outList;
        }

        outList.add(new ArrayList<>());
        outList.get(0).add(1);

        for (int rowNum = 1; rowNum < numRows; rowNum++) {
            List<Integer> preRowlist = outList.get(rowNum - 1);
            List<Integer> rowlist = new ArrayList<>();

            for (int i=0;i<=rowNum;i++){
                if (i==0){
                    rowlist.add(preRowlist.get(i)+0);
                    continue;
                }
                if (i==rowNum){
                    rowlist.add(preRowlist.get(i-1)+0);
                    continue;
                }
                rowlist.add(preRowlist.get(i-1)+preRowlist.get(i));
            }
            outList.add(rowlist);
        }

        return outList;
    }


    public static List<List<Integer>> generate3(int numRows) {

        List<List<Integer>> outList = new ArrayList<>();
        if (numRows == 0) {
            return outList;
        }

        outList.add(new ArrayList<>());
        outList.get(0).add(1);

        for (int rowNum = 1; rowNum < numRows; rowNum++) {
            List<Integer> preRowlist = outList.get(rowNum - 1);
            List<Integer> rowlist = new ArrayList<>();

            rowlist.add(1);

            for (int j = 1; j < rowNum; j++) {
                rowlist.add(preRowlist.get(j - 1) + preRowlist.get(j));
            }

            rowlist.add(1);

            outList.add(rowlist);
        }

        return outList;
    }

    public static List<List<Integer>> generate2(int numRows) {

        List<List<Integer>> outList = new ArrayList<>();
        if (numRows == 0) {
            return outList;
        }

        List<Integer> tmpList = new ArrayList<>();
        tmpList.add(1);
        outList.add(tmpList);

        for (int i = 1; i < numRows; i++) {
            List<Integer> list = new ArrayList<>();
            list.add(1);
            for (int j = 1; j < tmpList.size(); j++) {
                list.add(tmpList.get(j - 1) + tmpList.get(j));
            }
            list.add(1);
            tmpList = list;
            outList.add(list);
        }

        return outList;

    }

//    [[1],
//    [1,1],
//    [1,2,1],
//    [1,3,3,1],
//    [1,4,6,4,1],
//    [1,5,10,10,5,1]]
}
