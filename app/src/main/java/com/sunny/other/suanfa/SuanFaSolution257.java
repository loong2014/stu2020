package com.sunny.other.suanfa;

import com.sunny.other.suanfa.bean.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 257. 二叉树的所有路径
 * 给定一个二叉树，返回所有从根节点到叶子节点的路径。
 * <p>
 * 说明: 叶子节点是指没有子节点的节点。
 */
public class SuanFaSolution257 {

    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        System.out.println("out :" + binaryTreePaths(root));
    }

    /**
     * 迭代
     */
    public static List<String> binaryTreePaths(TreeNode root) {
        List<String> list = new ArrayList<>();
        if (root == null) {
            return list;
        }

        Queue<TreeNode> nodeQueue = new LinkedList<>();
        Queue<String> pathQueue = new LinkedList<>();
        nodeQueue.add(root);
        pathQueue.add(String.valueOf(root.val));
        while (!nodeQueue.isEmpty()) {
            TreeNode node = nodeQueue.poll();
            String path = pathQueue.poll();

            if (node.left == null && node.right == null) {
                list.add(path);
                continue;
            }
            if (node.left != null) {
                nodeQueue.add(node.left);
                pathQueue.add(path + "->" + node.left.val);
            }

            if (node.right != null) {
                nodeQueue.add(node.right);
                pathQueue.add(path + "->" + node.right.val);
            }
        }

        return list;
    }

    /**
     * 递归
     */
    public static List<String> binaryTreePaths2(TreeNode root) {
        List<String> list = new ArrayList<>();
        if (root == null) {
            return list;
        }

        buildPath(root, list, "");
        return list;
    }

    private static void buildPath(TreeNode root, List<String> list, String path) {
        if (root.left == null && root.right == null) {
            list.add(path + root.val);
            return;
        }
        path = path + root.val + "->";
        if (root.left != null) buildPath(root.left, list, path);
        if (root.right != null) buildPath(root.right, list, path);
    }

}
