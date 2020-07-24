package com.sunny.other.suanfa;

import com.sunny.other.suanfa.bean.TreeNode;

/**
 * Created by zhangxin17 on 2020-04-24
 * 110. 平衡二叉树
 * 给定一个二叉树，判断它是否是高度平衡的二叉树。
 * 本题中，一棵高度平衡二叉树定义为：
 * 一个二叉树每个节点 的左右两个子树的高度差的绝对值不超过1。
 */
public class SuanFaSolution110 {

    public static void main(String[] args) {
        TreeNode root = new TreeNode(10);
        System.out.println("out :" + isBalanced(root));
    }

    public static boolean isBalanced(TreeNode root) {
        return height(root) != -1;
    }

    /**
     * 通过递归获取二叉树的高度
     */
    private static int height(TreeNode node) {
        if (node == null) {
            return 0;
        }
        int lH = height(node.left);
        if (lH == -1) {
            return -1;
        }
        int rH = height(node.right);
        if (rH == -1) {
            return -1;
        }

        if (Math.abs(lH - rH) > 1) {
            return -1;
        }
        return Math.max(lH, rH) + 1;
    }

}
