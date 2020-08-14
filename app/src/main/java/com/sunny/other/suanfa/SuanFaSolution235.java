package com.sunny.other.suanfa;

import com.sunny.other.suanfa.bean.TreeNode;

/**
 * 235. 二叉搜索树的最近公共祖先
 * 给定一个二叉搜索树, 找到该树中两个指定节点的最近公共祖先。
 * <p>
 * 百度百科中最近公共祖先的定义为：“对于有根树 T 的两个结点 p、q，最近公共祖先表示为一个结点 x，满足 x 是 p、q 的祖先且 x 的深度尽可能大（一个节点也可以是它自己的祖先）。”
 * <p>
 * 二叉搜索数：二叉排序数，左树所有节点值小与父节点，右树所有节点值大于父节点
 */
public class SuanFaSolution235 {

    public static void main(String[] args) {

        TreeNode root = TreeNode.buildTreeNode(new Integer[]{6, 2, 8, 0, 4, 7, 9, null, null, 3, 5});
        TreeNode p = new TreeNode(2);
        TreeNode q = new TreeNode(4);
        System.out.println("out :" + lowestCommonAncestor(root, p, q).val);
    }


    /**
     * 递归
     */
    public static TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (p.val > root.val && q.val > root.val) {
            return lowestCommonAncestor(root.right, p, q);
        }

        if (p.val < root.val && q.val < root.val) {
            return lowestCommonAncestor(root.left, p, q);
        }
        return root;
    }

    /**
     * 迭代优化版
     */
    public static TreeNode lowestCommonAncestor3(TreeNode root, TreeNode p, TreeNode q) {

        while (root != null) {

            if (p.val > root.val && q.val > root.val) {
                root = root.right;
            } else if (p.val < root.val && q.val < root.val) {
                root = root.left;
            } else {
                return root;
            }
        }

        return null;
    }

    /**
     * 迭代
     */
    public static TreeNode lowestCommonAncestor2(TreeNode root, TreeNode p, TreeNode q) {

        int leftVal;
        int rightVal;
        if (p.val < q.val) {
            leftVal = p.val;
            rightVal = q.val;
        } else {
            leftVal = q.val;
            rightVal = p.val;
        }

        boolean stop = false;
        while (!stop) {

            if (root.val == rightVal || root.val == leftVal) {
                break;
            }

            if (root.val > rightVal) {
                root = root.left;
                continue;
            }

            if (root.val < leftVal) {
                root = root.right;
                continue;
            }

            stop = true;
        }
        return root;
    }

}
