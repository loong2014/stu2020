package com.sunny.other.suanfa;

import com.sunny.other.suanfa.bean.TreeNode;

import java.util.Stack;

/**
 * Created by zhangxin17 on 2020-04-24
 * 100. 相同的树
 * 给定两个二叉树，编写一个函数来检验它们是否相同。
 * <p>
 * 如果两个树在结构上相同，并且节点具有相同的值，则认为它们是相同的
 */
public class SuanFaSolution100 {

    public static void main(String[] args) {
        TreeNode p = new TreeNode(9);
        TreeNode q = new TreeNode(9);
        System.out.println("out :" + isSameTree(p, q));
    }

    /**
     * 迭代
     */
    public static boolean isSameTree(TreeNode p, TreeNode q) {
        if (p == null && null == q) {
            return true;
        } else if (p == null || q == null) {
            return false;
        }


        Stack<TreeNode> pStack = new Stack<>();
        pStack.push(p);

        Stack<TreeNode> qStack = new Stack<>();
        qStack.push(q);

        while (!pStack.isEmpty() && !qStack.isEmpty()) {
            p = pStack.pop();
            q = qStack.pop();

            if (p.val != q.val) {
                return false;
            }

            if (p.left != null && q.left != null) {
                pStack.push(p.left);
                qStack.push(q.left);
            } else if (p.left == null && q.left == null) {
            } else {
                return false;
            }

            if (p.right != null && q.right != null) {
                pStack.push(p.right);
                qStack.push(q.right);
            } else if (p.right == null && q.right == null) {
            } else {
                return false;
            }
        }

        return true;
    }

    /**
     * 递归
     */
    public static boolean isSameTree2(TreeNode p, TreeNode q) {
        if (p == null && q == null) {
            return true;
        }
        if (p == null || q == null) {
            return false;
        }
        if (p.val != q.val) {
            return false;
        }
        return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
    }

}
