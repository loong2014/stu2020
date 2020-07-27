package com.sunny.other.suanfa;

import com.sunny.other.suanfa.bean.ListNode;

import java.util.HashSet;

/**
 * Created by zhangxin17 on 2020-04-24
 * 160. 相交链表
 * 编写一个程序，找到两个单链表相交的起始节点。
 */
public class SuanFaSolution160 {

    public static void main(String[] args) {
        ListNode node1 = new ListNode(10);
        ListNode node2 = new ListNode(10);
        System.out.println("out :" + getIntersectionNode(node1, node2));
    }

    /**
     * 双指针从尾逆向判断
     */
    public static ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        if (headA == null || headB == null) {
            return null;
        }

        // 获取A和B的长度
        ListNode pA = headA;
        int lenA = 0;
        while (pA.next != null) {
            lenA++;
            pA = pA.next;
        }

        ListNode pB = headB;
        int lenB = 0;
        while (pB.next != null) {
            lenB++;
            pB = pB.next;
        }

        // 没有交点
        if (pA != pB) {
            return null;
        }

        // 移动比较长的链表，使其剩余长度与短的一样
        pA = headA;
        pB = headB;
        if (lenA > lenB) {
            for (int i = 0; i < lenA - lenB; i++) {
                pA = pA.next;
            }
        } else {
            for (int i = 0; i < lenB - lenA; i++) {
                pB = pB.next;
            }
        }

        // 因为有交点，且剩余长度一样，一起移动时，必然有节点相等
        while (pA != pB) {
            pA = pA.next;
            pB = pB.next;
        }

        return pA;
    }

    /**
     * 双指针，链表拼接
     * 分别从头遍历a+b和b+a，如果有交集，pA和pB一定会出现相等的情况。否则遍历结束后pA和pB都为null
     */
    public static ListNode getIntersectionNode4(ListNode headA, ListNode headB) {
        if (headA == null || headB == null) {
            return null;
        }

        ListNode pA = headA;
        ListNode pB = headB;

        while (pA != pB) {
            if (pA == null) {
                pA = headB;
            } else {
                pA = pA.next;
            }

            if (pB == null) {
                pB = headA;
            } else {
                pB = pB.next;
            }
        }
        return pA;
    }


    /**
     * 利用set的特性
     */
    public static ListNode getIntersectionNode3(ListNode headA, ListNode headB) {
        if (headA == null || headB == null) return null;

        HashSet<ListNode> nodes = new HashSet<>();

        while (headA != null || headB != null) {

            if (headA != null) {

                if (!nodes.contains(headA)) {
                    nodes.add(headA);
                    headA = headA.next;
                } else {
                    return headA;
                }
            } else {

                if (!nodes.contains(headB)) {
                    nodes.add(headB);
                    headB = headB.next;
                } else {
                    return headB;
                }
            }
        }
        return null;
    }

    /**
     * 利用set的特性
     */
    public static ListNode getIntersectionNode2(ListNode headA, ListNode headB) {
        if (headA == null || headB == null) {
            return null;
        }

        HashSet<ListNode> setA = new HashSet<>();
        HashSet<ListNode> setB = new HashSet<>();

        while (headA != null || headB != null) {

            if (headA != null) {
                if (setB.contains(headA)) {
                    return headA;
                } else {
                    setA.add(headA);
                }
                headA = headA.next;
            }

            if (headB != null) {
                if (setA.contains(headB)) {
                    return headB;
                } else {
                    setB.add(headB);
                }
                headB = headB.next;
            }
        }

        return null;
    }
}
