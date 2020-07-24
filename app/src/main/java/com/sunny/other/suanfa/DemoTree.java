package com.sunny.other.suanfa;

import com.sunny.other.suanfa.bean.TreeNode;

import java.util.Stack;

/**
 * Created by zhangxin17 on 2020/7/1
 */
public class DemoTree {

    public static void main(String[] args) {

    }

    /**
     * 递归遍历——前序，中序，后序
     */
    public static void recursiveOrder(TreeNode p) {
        if (p == null) {
            return;
        }

        // 1 前序
        visit(p);
        recursiveOrder(p.left);

        // 2 中序
        visit(p);
        recursiveOrder(p.right);

        // 3 后序
        visit(p);
    }

    /**
     * 非递归——前序列
     */
    public static void iterativePreOrder(TreeNode p) {
        if (p == null) {
            return;
        }

        Stack<TreeNode> stack = new Stack<>();
        stack.push(p);
        while (!stack.isEmpty()) {
            p = stack.pop();
            visit(p);

            if (p.left != null) {
                stack.push(p.left);
            }
            if (p.right != null) {
                stack.push(p.right);
            }

        }
    }

    public static void iterativePreOrder2(TreeNode p) {
        if (p == null) {
            return;
        }

        Stack<TreeNode> stack = new Stack<>();
        while (!stack.isEmpty() || p != null) {
            while (p != null) {
                visit(p);
                stack.push(p);
                p = p.left;
            }

            p = stack.pop();
            p = p.right;
        }
    }


    private static void visit(TreeNode node) {

    }
}
