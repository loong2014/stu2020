package com.sunny.other.suanfa;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 225. 用队列实现栈
 * 使用队列实现栈的下列操作：
 * <p>
 * push(x) -- 元素 x 入栈
 * pop() -- 移除栈顶元素
 * top() -- 获取栈顶元素
 * empty() -- 返回栈是否为空
 * 注意:
 * <p>
 * 你只能使用队列的基本操作-- 也就是 push to back, peek/pop from front, size, 和 is empty 这些操作是合法的。
 * 你所使用的语言也许不支持队列。 你可以使用 list 或者 deque（双端队列）来模拟一个队列 , 只要是标准的队列操作即可。
 * 你可以假设所有操作都是有效的（例如, 对一个空的栈不会调用 pop 或者 top 操作）。
 * <p>
 * 队列（queue），先进先出，队尾入队，队头出队。
 * 栈（stack），先进后出，栈顶入栈，栈顶出栈。
 */
public class SuanFaSolution225 {

    public static void main(String[] args) {

        MyStack myStack = new MyStack();
        myStack.push(10);
        int p2 = myStack.pop();
        int p3 = myStack.top();
        boolean empty = myStack.empty();
    }

    static class MyStack {

        Queue<Integer> queue = new LinkedList<>();

        /**
         * Initialize your data structure here.
         */
        public MyStack() {

        }

        /**
         * Push element x onto stack.
         */
        public void push(int x) {
            queue.add(x);
            int size = queue.size();
            while (size > 1) {
                queue.add(queue.remove());
                size--;
            }
        }

        /**
         * Removes the element on top of the stack and returns that element.
         */
        public int pop() {
            return queue.remove();
        }

        /**
         * Get the top element.
         */
        public int top() {
            return queue.element();
        }

        /**
         * Returns whether the stack is empty.
         */
        public boolean empty() {
            return queue.isEmpty();
        }
    }

    static class MyStack2 {

        Queue<Integer> q1 = new LinkedList<>();
        Queue<Integer> q2 = new LinkedList<>();
        int top;


        /**
         * Initialize your data structure here.
         */
        public MyStack2() {

        }

        /**
         * Push element x onto stack.
         */
        public void push(int x) {
            q1.add(x);
            top = x;
        }

        /**
         * Removes the element on top of the stack and returns that element.
         */
        public int pop() {
            while (q1.size() > 1) {
                top = q1.remove();
                q2.add(top);
            }
            int v = q1.remove();
            Queue<Integer> tmp = q1;
            q1 = q2;
            q2 = tmp;
            return v;
        }

        /**
         * Get the top element.
         */
        public int top() {
            return top;
        }

        /**
         * Returns whether the stack is empty.
         */
        public boolean empty() {
            return q1.isEmpty();
        }
    }

}
