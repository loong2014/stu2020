package com.sunny.other.suanfa;

import com.sunny.other.SuanFaTools;

/**
 * Created by zhangxin17 on 2020-04-24
 * 在4x4的棋盘上摆满了黑白棋子，黑白两色的位置和数目随机
 * 其中左上角坐标为(1,1),右下角坐标为(4,4),现在依次有一些翻转操作，
 * 要对一些给定支点坐标为中心的上下左右四个棋子的颜色进行翻转，请计算出翻转后的棋盘颜色。
 * <p>
 * 给定两个数组A和f,分别为初始棋盘和翻转位置。其中翻转位置共有3个。请返回翻转后的棋盘。
 */
public class SuanFaSolutionMT2 {

    public static void main(String[] args) {
        int[] list = new int[]{2, 10, 1, 7};


        int[][] A = new int[][]{{0, 1, 0, 0}, {1, 0, 1, 0}, {1, 1, 0, 0}, {1, 0, 0, 1}};
//        int[][] f = new int[][]{{2, 3}, {4, 2}, {2, 3}};
        int[][] f = new int[][]{{2, 3}};
        System.out.println("A :" + SuanFaTools.toString(A));
        System.out.println("f :" + SuanFaTools.toString(f));

        int[][] outYes = flipChess2(A, f);
        System.out.println("outYes :" + SuanFaTools.toString(outYes));

        A = new int[][]{{0, 1, 0, 0}, {1, 0, 1, 0}, {1, 1, 0, 0}, {1, 0, 0, 1}};
        int[][] out = flipChess(A, f);
        System.out.println("out :" + SuanFaTools.toString(out));
    }

    /**
     * 简化版
     */
    public static int[][] flipChess(int[][] A, int[][] f) {
        // write code here

        for (int[] ints : f) {
            int row = ints[0] - 1;
            int col = ints[1] - 1;

            if (row - 1 >= 0) {
                A[row - 1][col] = (A[row - 1][col] == 0) ? 1 : 0;
            }

            if (row + 1 <= 3) {
                A[row + 1][col] = (A[row + 1][col]) == 0 ? 1 : 0;
            }

            if (col - 1 >= 0) {
                A[row][col - 1] = (A[row][col - 1]) == 0 ? 1 : 0;
            }

            if (col + 1 <= 3) {
                A[row][col + 1] = (A[row][col + 1]) == 0 ? 1 : 0;
            }
        }
        return A;
    }


    /**
     * 二维数组
     */
    public static int[][] flipChess2(int[][] A, int[][] f) {
        // write code here

        int x, y;
        for (int[] ints : f) {
            x = ints[0] - 1;
            y = ints[1] - 1;

            // 四角
            if (x == 0 && y == 0) {
                A[x + 1][y] = (A[x + 1][y] + 1) % 2;
                A[x][y + 1] = (A[x][y + 1] + 1) % 2;
            } else if (x == 0 && y == A.length - 1) {
                A[x + 1][y] = (A[x + 1][y] + 1) % 2;
                A[x][y - 1] = (A[x][y - 1] + 1) % 2;
            } else if (x == A.length - 1 && y == 0) {
                A[x - 1][y] = (A[x - 1][y] + 1) % 2;
                A[x][y + 1] = (A[x][y + 1] + 1) % 2;
            } else if (x == A.length - 1 && y == A.length - 1) {
                A[x - 1][y] = (A[x - 1][y] + 1) % 2;
                A[x][y - 1] = (A[x][y - 1] + 1) % 2;
            }

            // 四边
            else if (x == 0) {
                A[x + 1][y] = (A[x + 1][y] + 1) % 2;
                A[x][y + 1] = (A[x][y + 1] + 1) % 2;
                A[x][y - 1] = (A[x][y - 1] + 1) % 2;
            } else if (x == A.length - 1) {
                A[x - 1][y] = (A[x - 1][y] + 1) % 2;
                A[x][y + 1] = (A[x][y + 1] + 1) % 2;
                A[x][y - 1] = (A[x][y - 1] + 1) % 2;
            } else if (y == 0) {
                A[x][y + 1] = (A[x][y + 1] + 1) % 2;
                A[x - 1][y] = (A[x - 1][y] + 1) % 2;
                A[x + 1][y] = (A[x + 1][y] + 1) % 2;
            } else if (y == A.length - 1) {
                A[x][y - 1] = (A[x][y - 1] + 1) % 2;
                A[x - 1][y] = (A[x - 1][y] + 1) % 2;
                A[x + 1][y] = (A[x + 1][y] + 1) % 2;
            }
            // 区域内
            else {
                A[x][y - 1] = (A[x][y - 1] + 1) % 2;
                A[x][y + 1] = (A[x][y + 1] + 1) % 2;
                A[x + 1][y] = (A[x + 1][y] + 1) % 2;
                A[x - 1][y] = (A[x - 1][y] + 1) % 2;
            }
        }
        return A;
    }

}
