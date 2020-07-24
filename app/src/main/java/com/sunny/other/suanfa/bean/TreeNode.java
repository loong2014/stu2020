package com.sunny.other.suanfa.bean;

/**
 * Created by zhangxin17 on 2020/7/1
 */
public class TreeNode {
    public int val;
    public TreeNode left;
    public TreeNode right;

    public int depth = 0;

    public TreeNode(int x) {
        val = x;
    }
}
