package com.sunny.other.suanfa;

import com.sunny.other.suanfa.bean.TreeNode;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by zhangxin17 on 2020-04-24
 * 111. 二叉树的最小深度
 * 给定一个二叉树，找出其最小深度。
 * <p>
 * 最小深度是从根节点到最近叶子节点的最短路径上的节点数量。
 * <p>
 * 说明: 叶子节点是指没有子节点的节点。
 */
public class SuanFaSolution111 {

    public static void main(String[] args) {
        TreeNode root = new TreeNode(10);
        System.out.println("out :" + minDepth(root));
    }

    /**
     * 递归
     */
    public static int minDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }

        if (root.left ==null && root.right ==null){
            return 1;
        }

        int min_depth=Integer.MAX_VALUE;
        if (root.left!=null){
            min_depth = Math.min(minDepth(root.left),min_depth);
        }
        if (root.right!=null){
            min_depth = Math.min(minDepth(root.right),min_depth);
        }
        return min_depth+1;
    }


    /**
     * 自顶向底遍历
     */
    public static int minDepth2(TreeNode root) {
        if (root == null) {
            return 0;
        }

        Queue<TreeNode> queue = new LinkedList<>();

        queue.add(root);
        queue.add(null);
        int dep = 1;
        while (true) {

            TreeNode node;
            while ((node = queue.poll()) != null) {

                if (node.left == null && node.right == null) {
                    return dep;
                }

                if (node.left != null) {
                    queue.add(node.left);
                }
                if (node.right != null) {
                    queue.add(node.right);
                }
            }
            queue.add(null);
            dep++;
        }
    }

}
