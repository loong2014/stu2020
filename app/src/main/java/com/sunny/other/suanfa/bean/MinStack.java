package com.sunny.other.suanfa.bean;

import java.util.Stack;

/**
 * Created by zhangxin17 on 2020/7/27
 * stack存储的是，x和当前最小值的差值。
 */
public class MinStack {

    long min;

    Stack<Long> stack;

    public MinStack() {
        stack = new Stack<>();
    }

    public void push(int x) {
        if (stack.isEmpty()) {
            min = x;
            stack.push(0L);
        } else {
            stack.push(x - min);
            if (x < min) {
                min = x;
            }
        }
    }

    public void pop() {
        if (stack.isEmpty()) {
            return;
        }
        long pop = stack.pop();
        // 需要更新最小值
        if (pop < 0) {
            min = min - pop;
        }
    }

    public int top() {
        long top = stack.peek();
        // 当前值是最小值
        if (top < 0) {
            return (int) min;
        }
        // 当前值是出栈元素+最小值
        else {
            return (int) (min + top);
        }
    }

    public int getMin() {
        return (int) min;
    }
}
