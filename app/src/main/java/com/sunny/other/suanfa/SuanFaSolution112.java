package com.sunny.other.suanfa;

import com.sunny.other.suanfa.bean.TreeNode;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by zhangxin17 on 2020-04-24
 * 112. 路径总和
 * 给定一个二叉树和一个目标和，判断该树中是否存在根节点到叶子节点的路径，这条路径上所有节点值相加等于目标和。
 * <p>
 * 说明: 叶子节点是指没有子节点的节点。
 */
public class SuanFaSolution112 {

    public static void main(String[] args) {
        TreeNode root = new TreeNode(10);
        System.out.println("out :" + hasPathSum(root, 10));
    }

    public static boolean hasPathSum(TreeNode root, int sum) {
        if (root == null) {
            return false;
        }
        if (root.left ==null && root.right ==null)
        {
            return root.val == sum;
        }
        return hasPathSum(root.left,sum-root.val) || hasPathSum(root.right,sum-root.val);
    }

    public static boolean hasPathSum3(TreeNode root, int sum) {
        if (root == null) {
            return false;
        }

        Queue<TreeNode> queueNode = new LinkedList<>();
        Queue<Integer> queueVal = new LinkedList<>();
        queueNode.offer(root);
        queueVal.offer(root.val);

        while (!queueNode.isEmpty()) {
            TreeNode node = queueNode.poll();
            int curSum = queueVal.poll();
            if (node.left == null && node.right == null) {
                if (curSum == sum) {
                    return true;
                }
                continue;
            }

            if (node.left != null) {
                queueNode.offer(node.left);
                queueVal.offer(curSum + node.left.val);
            }

            if (node.right != null) {
                queueNode.offer(node.right);
                queueVal.offer(curSum + node.right.val);
            }
        }

        return false;
    }


    /**
     * 左遍历
     */
    public static boolean hasPathSum2(TreeNode root, int sum) {

        if (root == null) {
            return false;
        }

        Stack<TreeNode> stack = new Stack<>();
        stack.add(root);

        int curSum = root.val;
        while (!stack.isEmpty()) {
            TreeNode node = stack.peek();
            if (node.left != null) {
                stack.add(node.left);
                curSum = curSum + node.left.val;
                continue;
            }
            if (node.right != null) {
                stack.add(node.right);
                curSum = curSum + node.right.val;
                continue;
            }

            if (curSum == sum) {
                return true;
            }

            stack.pop();
            curSum = curSum - node.val;

        }

        return false;
    }

}
