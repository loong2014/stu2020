package com.sunny.other.suanfa;

import com.sunny.other.suanfa.bean.TreeNode;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by zhangxin17 on 2020-04-24
 * 101. 对称二叉树
 * 给定一个二叉树，检查它是否是镜像对称的。
 */
public class SuanFaSolution101 {


    public static void main(String[] args) {
        TreeNode node = new TreeNode(9);
        System.out.println("out :" + isSymmetric(node));
    }

    public static boolean isSymmetric(TreeNode root) {
        return check(root, root);
    }

    public static boolean check(TreeNode u, TreeNode v) {
        Queue<TreeNode> q = new LinkedList<TreeNode>();
        q.offer(u);
        q.offer(v);
        while (!q.isEmpty()) {
            u = q.poll();
            v = q.poll();
            if (u == null && v == null) {
                continue;
            }
            if ((u == null || v == null) || (u.val != v.val)) {
                return false;
            }

            q.offer(u.left);
            q.offer(v.right);

            q.offer(u.right);
            q.offer(v.left);
        }
        return true;
    }


    /**
     * 队列——先进先出
     */
    public static boolean check4(TreeNode u, TreeNode v) {
        Queue<TreeNode> q = new LinkedList<TreeNode>();
        q.offer(u); // 尾部添加
        q.offer(v);
        while (!q.isEmpty()) {
            u = q.poll(); // 头部获取
            v = q.poll();
            if (u == null && v == null) {
                continue;
            }
            if ((u == null || v == null) || (u.val != v.val)) {
                return false;
            }

            q.offer(u.left);
            q.offer(v.right);

            q.offer(u.right);
            q.offer(v.left);
        }
        return true;
    }

    public static boolean check3(TreeNode u, TreeNode v) {
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(u);
        q.offer(v);
        while (!q.isEmpty()) {
            u = q.poll();
            v = q.poll();
            if (u == null && v == null) {
                continue;
            }
            if (u == null || v == null) {
                return false;
            }
            if (u.val != v.val) {
                return false;
            }
            q.offer(u.left);
            q.offer(v.right);

            q.offer(u.left);
            q.offer(v.right);
        }
        return true;
    }

    public static boolean check2(TreeNode p, TreeNode q) {
        if (p == null && q == null) {
            return true;
        }
        if (p == null || q == null) {
            return false;
        }
        return p.val == q.val && check(p.left, q.right) && check(p.right, q.left);
    }

}
