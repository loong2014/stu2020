package com.sunny.other;

import java.util.BitSet;

/**
 * Created by zhangxin17 on 2020-04-27
 * 基于sdk 29分析
 * 内存模型是数组 Long[]
 */
public class JavaBitSet {

    private void bitSetStu() {

        BitSet bitSet = new BitSet();

        // 结果与set的顺序无关
        bitSet.set(6);
        bitSet.set(11);
        bitSet.set(4);

        System.out.println("bitSet: " + bitSet);

        System.out.println("bitSet 的逻辑大小 :" + bitSet.length());
        System.out.println("bitSet 的实际大小 :" + bitSet.size());
        System.out.println("bitSet 中设置为true的位个数 :" + bitSet.cardinality());

        int len = bitSet.length();
        System.out.println("遍历 bitSet 的值 :");
        for (int i = 0; i < len; i++) {
            System.out.print(" -> " + i + "_" + bitSet.get(i));
        }
        System.out.println("\n");

        int startIndex = 7;
        System.out.println("从" + startIndex + "开始");
        System.out.println("返回第一个为 false 的位的索引 :" + bitSet.nextClearBit(startIndex));
        System.out.println("返回第一个为 true 的位的索引 :" + bitSet.nextSetBit(startIndex));
    }


    private void bitSetOpt() {

        BitSet bits1 = new BitSet(16);
        BitSet bits2 = new BitSet(16);

        // set some bits
        for (int i = 0; i < 16; i++) {
            if ((i % 2) == 0) bits1.set(i);
            if ((i % 5) != 0) bits2.set(i);
        }

        System.out.println("bits1: " + bits1); // {0, 2, 4, 6, 8, 10, 12, 14}
        System.out.println("bits2: " + bits2); // {1, 2, 3, 4, 6, 7, 8, 9, 11, 12, 13, 14}

        String opt;
//        opt = "and";
        opt = "or";
//        opt = "xor";

        System.out.print("bits1 " + opt + " bits2 = ");
        switch (opt) {

            // AND bits
            case "and": {
                bits2.and(bits1); // {2, 4, 6, 8, 12, 14}
                System.out.print(bits2);
                break;
            }
            // OR bits
            case "or": {
                bits2.or(bits1); // {0, 1, 2, 3, 4, 6, 7, 8, 9, 10, 11, 12, 13, 14}
                System.out.print(bits2);
                break;
            }

            // XOR bits
            case "xor": {
                bits2.xor(bits1); // {0, 1, 3, 7, 9, 10, 11, 13}
                System.out.print(bits2);
                break;
            }

            default: {
                System.out.println("error opt :" + opt);
                break;
            }
        }
    }

    public static void main(String[] args) {
        JavaBitSet demo = new JavaBitSet();
        demo.bitSetStu();
//        demo.bitSetOpt();
    }
}
