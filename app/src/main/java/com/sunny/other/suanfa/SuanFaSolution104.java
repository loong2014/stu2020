package com.sunny.other.suanfa;

import android.util.Pair;

import com.sunny.other.suanfa.bean.TreeNode;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by zhangxin17 on 2020-04-24
 * 104. 二叉树的最大深度
 * 给定一个二叉树，找出其最大深度。
 * <p>
 * 二叉树的深度为根节点到最远叶子节点的最长路径上的节点数。
 * <p>
 * 说明: 叶子节点是指没有子节点的节点。
 */
public class SuanFaSolution104 {


    public static void main(String[] args) {
        TreeNode node = new TreeNode(9);
        TreeNode node2 = new TreeNode(9);
        TreeNode node3 = new TreeNode(9);
        TreeNode node4 = new TreeNode(9);
        node.left = node2;
        node.right = node3;
        node3.left = node4;
        System.out.println("out :" + maxDepth(node));
    }

    public static int maxDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }

        Queue<Pair<TreeNode, Integer>> stack = new LinkedList<>();
        stack.add(new Pair<>(root, 1));

        int depth = 0;

        Pair<TreeNode, Integer> current;
        while (!stack.isEmpty()) {
            current = stack.poll();
            root = current.first;
            int curDepth = current.second;
            if (root != null) {
                depth = Math.max(depth, curDepth);
                stack.add(new Pair<>(root.left, curDepth + 1));
                stack.add(new Pair<>(root.right, curDepth + 1));
            }
        }

        return depth;
    }

    public static int maxDepth3(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int left = maxDepth(root.left) + 1;
        int right = maxDepth(root.right) + 1;

        return Math.max(left, right);
    }

//
//    public static int maxDepth2(TreeNode p) {
//        if (p == null) {
//            return 0;
//        }
//
//        int maxDep = 0;
//        Stack<TreeNode> stack = new Stack<>();
//        stack.push(p);
//        TreeNode next;
//        while (!stack.isEmpty()) {
//
//            while (p != null) {
//                stack.push(p);
//
//                next = p.left;
//                if (next != null) {
//                    next.depth = p.depth + 1;
//                } else {
//                    maxDep = Math.max(maxDep, p.depth);
//                }
//                p = next;
//            }
//
//            p = stack.pop();
//            p = p.right;
//        }
//
//        return maxDep;
//    }

}
