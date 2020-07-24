package com.sunny.other.suanfa;

import com.sunny.other.suanfa.bean.TreeNode;

/**
 * Created by zhangxin17 on 2020-04-24
 * 108. 将有序数组转换为二叉搜索树
 */
public class SuanFaSolution {


    public static void main(String[] args) {
        TreeNode node = new TreeNode(9);
        System.out.println("out :" + isSymmetric(node));
    }

    public static boolean isSymmetric(TreeNode root) {
        return check(root, root);
    }

    public static boolean check(TreeNode p, TreeNode q) {
        if (p == null && q == null) {
            return true;
        }
        if (p == null || q == null) {
            return false;
        }
        return p.val == q.val && check(p.left, q.right) && check(p.right, q.left);
    }

}
