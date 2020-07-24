package com.sunny.other.suanfa;

import android.util.Pair;

import com.sunny.other.suanfa.bean.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by zhangxin17 on 2020-04-24
 * 107. 二叉树的层次遍历 II
 * <p>
 * 给定一个二叉树，返回其节点值自底向上的层次遍历。 （即按从叶子节点所在层到根节点所在的层，逐层从左向右遍历）
 */
public class SuanFaSolution107 {


    public static void main(String[] args) {
        TreeNode node = new TreeNode(9);
        TreeNode node2 = new TreeNode(9);
        TreeNode node3 = new TreeNode(9);
        TreeNode node4 = new TreeNode(9);
        node.left = node2;
        node.right = node3;
        node3.left = node4;
        System.out.println("out :" + levelOrderBottom(node));
    }

    public static List<List<Integer>> levelOrderBottom(TreeNode root) {
        List<List<Integer>> out = new ArrayList<>();
        if (root == null) {
            return out;
        }
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);
        while (!q.isEmpty()){
            List<Integer> list = new ArrayList<>();
            int len =q.size();
            for (int i=0;i<len;i++){
                TreeNode node = q.poll();
                list.add(node.val);
                if (node.left!=null){
                    q.add(node.left);
                }
                if (node.right!=null){
                    q.add(node.right);
                }
            }
            out.add(0,list);
        }

        return out;
    }

    public static List<List<Integer>> levelOrderBottom3(TreeNode root) {
        List<List<Integer>> out = new ArrayList<>();

        Queue<Pair<TreeNode, Integer>> stack = new LinkedList<>();
        stack.add(new Pair<>(root, 1));
        while (!stack.isEmpty()) {
            Pair<TreeNode, Integer> cur = stack.poll();
            root = cur.first;
            int curDep = cur.second;

            while (root.left != null) {
                stack.add(new Pair<>(root, curDep));
                root = root.left;
                curDep = curDep + 1;
            }
            if (root.right == null) {

            } else {
                stack.add(new Pair<>(root.right, curDep));
            }
        }

        return out;
    }

    public static List<List<Integer>> levelOrderBottom2(TreeNode root) {
        List<List<Integer>> out = new ArrayList<>();
        find(root, 1, out);
        return out;
    }

    public static void find(TreeNode p, int level, List<List<Integer>> out) {
        if (p == null) {
            return;
        }
        if (level > out.size()) {
            out.add(0, new ArrayList<>());
        }
        out.get(out.size() - level).add(p.val);
        find(p.left, level + 1, out);
        find(p.right, level + 1, out);
    }

}
