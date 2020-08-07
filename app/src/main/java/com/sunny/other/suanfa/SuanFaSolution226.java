package com.sunny.other.suanfa;

import com.sunny.other.suanfa.bean.TreeNode;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 226. 翻转二叉树
 * 翻转一棵二叉树。
 */
public class SuanFaSolution226 {

    public static void main(String[] args) {

        TreeNode root = new TreeNode(10);
        invertTree(root);
    }

    /**
     * 迭代
     */
    public static TreeNode invertTree(TreeNode root) {
        if (root == null) {
            return null;
        }

        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {

            TreeNode curNode = queue.poll();
            TreeNode tmp = curNode.left;
            curNode.left = curNode.right;
            curNode.right = tmp;

            if (curNode.left != null) queue.add(curNode.left);
            if (curNode.right != null) queue.add(curNode.right);

        }
        return root;
    }


    /**
     * 递归
     */
    public static TreeNode invertTree2(TreeNode root) {
        if (root == null) {
            return null;
        }

        TreeNode lNode = root.left;
        TreeNode rNode = root.right;

        root.left = rNode;
        root.right = lNode;

        invertTree(lNode);
        invertTree(rNode);

        return root;
    }

}
