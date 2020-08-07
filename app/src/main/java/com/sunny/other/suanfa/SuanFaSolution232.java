package com.sunny.other.suanfa;

import java.util.Stack;

/**
 * 232. 用栈实现队列
 * 使用栈实现队列的下列操作：
 * <p>
 * push(x) -- 将一个元素放入队列的尾部。
 * pop() -- 从队列首部移除元素。
 * peek() -- 返回队列首部的元素。
 * empty() -- 返回队列是否为空。
 */
public class SuanFaSolution232 {

    public static void main(String[] args) {
        MyQueue queue = new MyQueue();
    }

    /**
     * 栈：先进后出
     * 队列：先进先出
     */
    static class MyQueue {

        Stack<Integer> s1 = new Stack<>();
        Stack<Integer> s2 = new Stack<>();

        int front;

        /**
         * Initialize your data structure here.
         */
        public MyQueue() {
        }

        /**
         * Push element x to the back of queue.
         */
        public void push(int x) {

            if (s1.empty()) {
                front = x;
            }
            while (!s1.isEmpty()) {
                s2.push(s1.pop());
            }

            s2.push(x);

            while (!s2.isEmpty()) {
                s1.push(s2.pop());
            }
        }

        /**
         * Removes the element from in front of queue and returns that element.
         */
        public int pop() {
            int out = s1.pop();
            if (!s1.isEmpty()) {
                front = s1.peek();
            }
            return out;
        }

        /**
         * Get the front element.
         */
        public int peek() {
            return front;

        }

        /**
         * Returns whether the queue is empty.
         */
        public boolean empty() {
            return s1.isEmpty();
        }
    }


}
