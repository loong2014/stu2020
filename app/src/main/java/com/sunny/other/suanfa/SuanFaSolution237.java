package com.sunny.other.suanfa;

import com.sunny.other.suanfa.bean.ListNode;

/**
 * 237. 删除链表中的节点
 * <p>
 * 请编写一个函数，使其可以删除某个链表中给定的（非末尾）节点。传入函数的唯一参数为 要被删除的节点 。
 */
public class SuanFaSolution237 {

    public static void main(String[] args) {
        ListNode node = new ListNode(1);
        deleteNode(node);
    }

    public static void deleteNode(ListNode node) {
        node.val= node.next.val;
        node.next = node.next.next;
    }

}
