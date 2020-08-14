package com.sunny.other.suanfa.bean;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by zhangxin17 on 2020/7/1
 */
public class TreeNode {
    public int val;
    public TreeNode left;
    public TreeNode right;

    public int depth = 0;

    public TreeNode(int x) {
        val = x;
    }


    //        [6,2,8,0,4,7,9,null,null,3,5]

    public static TreeNode buildTreeNode(Integer[] list) {
        if (list == null) {
            return null;
        }

        TreeNode root = new TreeNode(list[0]);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);

        int index = 0;
        while (!queue.isEmpty()) {
            int len = queue.size();
            for (int j = 0; j < len; j++) {
                TreeNode node = queue.poll();
                index++;
                if (index < list.length && list[index] != null) {
                    node.left = new TreeNode(list[index]);
                    queue.add(node.left);
                }

                index++;
                if (index < list.length && list[index] != null) {
                    node.right = new TreeNode(list[index]);
                    queue.add(node.right);
                }
            }
        }

        return root;
    }
}
