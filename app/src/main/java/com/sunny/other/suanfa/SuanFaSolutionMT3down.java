package com.sunny.other.suanfa;

import com.sunny.other.SuanFaTools;

/**
 * 现在有一个城市销售经理，需要从公司出发，去拜访市内的商家，
 * 已知他的位置以及商家的位置，但是由于城市道路交通的原因，
 * 他只能在左右中选择一个方向，在上下中选择一个方向，现在问他有多少种方案到达商家地址。
 * <p>
 * 给定一个地图map及它的长宽n和m，其中1代表经理位置，2代表商家位置，-1代表不能经过的地区，0代表可以经过的地区，
 * 请返回方案数，保证一定存在合法路径。保证矩阵的长宽都小于等于10。
 */
public class SuanFaSolutionMT3down {

    public static void main(String[] args) {
        int[] list = new int[]{2, 10, 1, 7};


        int[][] map = new int[][]{{0, 1, 0, 0}, {1, 0, 1, 0}, {1, 1, 0, 0}, {1, 0, 0, 1}};
        System.out.println("map :" + SuanFaTools.toString(map));

        System.out.println("out :" + countPath(map, 10, 10));
    }

    public static int countPath(int[][] map, int n, int m) {
        return 0;
    }

}
