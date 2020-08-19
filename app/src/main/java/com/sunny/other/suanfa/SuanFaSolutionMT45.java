package com.sunny.other.suanfa;

/**
 * 给你一个01字符串，定义答案=该串中最长的连续1的长度，
 * 现在你有至多K次机会，每次机会可以将串中的某个0改成1，现在问最大的可能答案
 * <p>
 * 输入第一行两个整数N,K，表示字符串长度和机会次数
 * <p>
 * 第二行输入N个整数，表示该字符串的元素
 * <p>
 * ( 1 <= N <= 300000
 * , 0 <= K <= N )
 */
public class SuanFaSolutionMT45 {

    public static void main(String[] args) {
        int[] list = new int[]{2, 10, 1, 7};

        String str = "1001010101";
        System.out.println("out :" + maxNum(str, str.length(), 2));
    }


    public static int maxNum(String str, int len, int k) {

        int[] nums = new int[len];
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            nums[i] = c - '0';
        }

        int max = 0;
        for (int left1 = 0; left1 < len; left1++) {
            if (nums[left1] == 1) {
                int tmpMax = 0;
                int tmpK = k;
                for (int i = left1; i < len; i++) {
                    if (nums[i] == 1) {
                        tmpMax++;
                    } else if (tmpK > 0) {
                        tmpK--;
                        tmpMax++;
                    } else {
                        break;
                    }
                }
                max = Math.max(max, tmpMax);
            }
        }

        return max;
    }
}
