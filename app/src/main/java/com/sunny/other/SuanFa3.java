package com.sunny.other;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxin17 on 2020-04-24
 */
public class SuanFa3 {

    public static void main(String[] args) {

        int[] arr = {8, 5, 3, 2, 4};

        List<String> l = new ArrayList<>(8);
        //冒泡
        for (int i = 0; i < arr.length; i++) {
            //外层循环，遍历次数
            for (int j = 0; j < arr.length - i - 1; j++) {
                //内层循环，升序（如果前一个值比后一个值大，则交换）
                //内层循环一次，获取一个最大值
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j + 1];
                    arr[j + 1] = arr[j];
                    arr[j] = temp;
                }
            }
        }


        for (int i = 0; i < arr.length; i++) {

            System.out.print(arr[i] + " - ");
        }


    }
}
