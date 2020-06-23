package com.sunny.other.suanfa;

/**
 * Created by zhangxin17 on 2020-04-24
 */
public class SuanFaSolution4 {


    public static void main(String[] args) {

        int key = 1234321;

        boolean yes = isPalindrome(key);
        System.out.println(key + " is palindrome :" + yes);
    }

    /**
     * 计算一半
     */
    public static boolean isPalindrome(int x) {
        if (x < 0) {
            return false;
        }
        if (x ==0){
            return true;
        }
        if (x%10 ==0){
            return false;
        }

        int y = 0;
        while (x > y) {
            y = y * 10 + x % 10;
            x = x / 10;
        }

        return x == y || x == y / 10;
    }


    public static boolean isPalindrome2(int x) {
        if (x < 0) {
            return false;
        }
        int oldX = x;
        int y = 0;
        while (x > 0) {
            y = y * 10 + x % 10;
            x = x / 10;
            System.out.println("x :" + x + " , y :" + y);
        }

        return oldX == y;
    }
}
