package com.sunny.other.suanfa.bean;

import java.util.Stack;

/**
 * Created by zhangxin17 on 2020/7/27
 * Pair
 */
public class MinStack3 {

    Integer min = null;

    Stack<Tmp<Integer, Integer>> stack = new Stack<>();

    /**
     * initialize your data structure here.
     */
    public MinStack3() {
    }

    public void push(int x) {
        stack.push(new Tmp<>(x, min));
        if (min == null) {
            min = x;
        } else {
            min = Math.min(min, x);
        }
    }

    public void pop() {
        if (!stack.isEmpty()) {
            min = stack.pop().second;
        }
    }

    public int top() {
        if (stack.isEmpty()) {
            return 0;
        }
        return stack.peek().first;
    }

    public int getMin() {
        if (stack.isEmpty()) {
            return 0;
        }
        Integer i = stack.peek().second;

        if (i == null) {
            return min;
        }
        return Math.min(i, min);
    }

    class Tmp<F, S> {
        public final F first;
        public final S second;

        public Tmp(F f, S s) {
            this.first = f;
            this.second = s;
        }
    }
}
