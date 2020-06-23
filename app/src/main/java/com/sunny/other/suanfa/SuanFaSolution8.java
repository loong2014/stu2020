package com.sunny.other.suanfa;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by zhangxin17 on 2020-04-24
 * 有效的括号
 */
public class SuanFaSolution8 {


    public static void main(String[] args) {
        String in = "([)]";
        Object out = isValid(in);
        System.out.println(in + " >>> " + out);


    }

    private static boolean isValid(String s) {
        if (s.isEmpty()) {
            return true;
        }
        if (s.length() % 2 != 0) {
            return false;
        }

        Map<Character, Character> map = new HashMap<>();
        map.put('(', ')');
        map.put(')', '(');
        map.put('{', '}');
        map.put('}', '{');
        map.put('[', ']');
        map.put(']', '[');

        char[] queue = new char[s.length()];

        int index = -1;
        for (int i = 0; i < s.length(); i++) {
            Character cur = s.charAt(i);
            if (index == -1) {
                index++;
                queue[index] = cur;
                continue;
            }

            Character pre = queue[index];
            if (pre == map.get(cur)) {
                index--;
                continue;
            }
            index++;
            queue[index] = cur;
        }

        return index == -1;
    }

    private static boolean isValid2(String s) {
        if (s.isEmpty()) {
            return true;
        }
        if (s.length() % 2 != 0) {
            return false;
        }

        Map<Character, Character> map = new HashMap<>();
        map.put('(', ')');
        map.put(')', '(');
        map.put('{', '}');
        map.put('}', '{');
        map.put('[', ']');
        map.put(']', '[');

        ConcurrentLinkedDeque<Character> queue = new ConcurrentLinkedDeque<>();

        for (int i = 0; i < s.length(); i++) {
            Character cur = s.charAt(i);
            Character pre = queue.peekFirst();

            if (pre == map.get(cur)) {
                queue.pollFirst();
                continue;
            }
            queue.addFirst(cur);
        }

        return queue.size() == 0;
    }
}
