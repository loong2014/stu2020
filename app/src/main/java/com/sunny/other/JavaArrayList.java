package com.sunny.other;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.ListIterator;
import java.util.function.UnaryOperator;

/**
 * Created by zhangxin17 on 2020-04-27
 * 基于sdk 29分析
 * 内存模型是数组 Object[]
 */
public class JavaArrayList {

    public static void main(String[] args) {
        JavaArrayList demo = new JavaArrayList();
//        demo.arrayListTest();
//        demo.testForEachRemaining();
        demo.bitSetDemo();

    }


    private void showList(List<String> list) {
        System.out.println("showList size :" + list.size());
        for (String str : list) {
            System.out.println("" + str);
        }
    }


    private void arrayListTest() {
        List<String> list = new ArrayList<>();

        list = new ArrayList<>(15); // 不指定大小，数组长度是0

        /**
         * 每次添加元素时，都会调用 ensureCapacityInternal 方法进行扩容判断，
         * 在ensureCapacityInternal中，如果当前数组的长度是0，则扩容为10。否则新数组的长度为当前数组长度的1.5倍。
         * 计算方式：newCapacity = oldCapacity + (oldCapacity >> 1);
         * 数组最大长度是：Integer.MAX_VALUE。
         * 然后执行数组copy：elementData = Arrays.copyOf(elementData, newCapacity);
         *
         * 新增元素后，size++
         *
         * 因此，如果明确知道数组的大小，建议在创建数组是直接指定，减少数组copy的花销
         */
        list.add("aaa");
        list.add("bbb");
        list.add("ccc");
        list.add("ddd");
        list.add("eee");
        list.add("ccc");
        list.add("fff");

//        list.set(1, "zzz"); // 根据下标更改存储的对象
        showList(list);

//        list.addAll(list); // addAll的扩容处理与add的方式一样，只是新增大小不一样

         /*
          每当对数组大小进行操作(扩容,添加，删除)时，modCount++
         */

        /* 1111 */
//        list.get(0); // 通过下标访问数组

        /* 222 */
        // 两种remove的实现方式一样，如果删除的不是最后一个元素，则需要进行数组内的copy移动
        // int numMoved = size - index - 1;
        // System.arraycopy(elementData, index+1, elementData, index, numMoved);

//        String old = list.remove(0);
//        boolean state = list.remove("bbb");

//        list.removeAll(list);
        /* 333 */
        // 根据条件删除
//        list.removeIf(new Predicate<String>() {
//            @Override
//            public boolean test(String s) {
//                return "ccc".equals(s);
//            }
//        });

        list.replaceAll(new UnaryOperator<String>() {
            @Override
            public String apply(String s) {
                if ("ccc".equals(s)) {
                    return "world";
                }
                return s;
            }
        });
        showList(list);


        /* 333 */
        // 遍历数组，elementData[i] = null;
//        list.clear();


        /* 444 */
//        int index = list.indexOf("bbb"); // 从下标0开始遍历数组进行对比，并返回对应的下标。如果没有匹配的，返回-1
//        index = list.lastIndexOf("bbb"); // 从下标size-1开始反方向遍历
//        boolean state = list.contains("bbb"); // 内部调用 indexOf(o) >= 0

//        list.containsAll(newList); // 父类 AbstractCollection 的方法，遍历newList，并调用contains方法判断

        /* 555 */
//        Iterator<String> iterator = list.listIterator(); // 初始游标cursor=0

//        its = list.listIterator(1); // 设置初始游标 cursor = index

        // Iterator 接口，四个方法：hasNext，next，remove以及forEachRemaining
        /*
          关键变量：limit === list.size
          cursor === 当前游标的位置
          lastRet === 最后对iterator进行
          expectedModCount === list.modCount // 在操作iterator的过程中，如果外部对list进行增删改的操作，则会抛出 ConcurrentModificationException
          另外，每次调用remove方法之前，必须调用next!!!否则会报 IllegalStateException
         */

        // ListIterator 继承 Iterator，新增了hasPrevious，previous，nextIndex，previousIndex，set，add
        /*
        set 调用的是list的set(index,e)方法，index==lastRet
        add 调用的是list的add(index,e)方法，index==cursor
         */
        ListIterator<String> listIterator = list.listIterator(2); // 可以指定起始cursor的下标
        System.out.println("next :" + listIterator.next());
        System.out.println("previous :" + listIterator.previous());
        listIterator.set("hello"); // 不能直接调用set方法，lastRet=-1，会报 IllegalStateException

        showList(list);


        // 迭代器，通过链表的方式进行

//        list.equals(list); // 父类 AbstractList 的方法，


    }


    private void showUserList(List<User> list) {
        System.out.println("showList size :" + list.size());

        list.forEach(user -> {
            System.out.println("" + user);
        });
    }

    private void testForEachRemaining() {
        List<User> list = new ArrayList<>();
        list.add(new User("aaa", 18));
        list.add(new User("bbb", 18));
        list.add(new User("ccc", 18));
        list.add(new User("Ddd", 18));
        list.add(new User("eee", 18));

        showUserList(list);


        /* 1111 */
        /*
        iterator.next();

        // 函数式编程，启始下标为 iterator 的最新游标cursor
        iterator.forEachRemaining(new Consumer<User>() {
            @Override
            public void accept(User user) {
                user.age = 30;
            }
        });

        // 此时cursor指向最后一个数据
        iterator.remove();
        iterator.next(); // 这里会报 NoSuchElementException
         */


        showUserList(list);
    }

    private static int wordIndex(int bitIndex) {
        return bitIndex >> 6;
    }


    /**
     * 数据结构是long数组，每次扩容是当前长度的2倍
     */
    private void bitSetDemo() {

        BitSet bits1 = new BitSet(16);
        BitSet bits2 = new BitSet(16);

        // set some bits
        for(int i=0; i<16; i++) {
            if((i%2) == 0) bits1.set(i);
            if((i%5) != 0) bits2.set(i);
        }

        System.out.println("Initial pattern in bits1: ");
        System.out.println(bits1); // {0, 2, 4, 6, 8, 10, 12, 14}

        System.out.println("\nInitial pattern in bits2: ");
        System.out.println(bits2); // {1, 2, 3, 4, 6, 7, 8, 9, 11, 12, 13, 14}


//        // AND bits
//        bits2.and(bits1); // {2, 4, 6, 8, 12, 14}
//        System.out.println("\nbits2 AND bits1: ");
//        System.out.println(bits2);

//        // OR bits
//        bits2.or(bits1); // {0, 1, 2, 3, 4, 6, 7, 8, 9, 10, 11, 12, 13, 14}
//        System.out.println("\nbits2 OR bits1: ");
//        System.out.println(bits2);

//        // XOR bits
//        bits2.xor(bits1); // {0, 1, 3, 7, 9, 10, 11, 13}
//        System.out.println("\nbits2 XOR bits1: ");
//        System.out.println(bits2);


        if (true) {
            return;
        }
        String[] list = new String[]{"aaa", "bbb", "ccc", "ddd", "eee", "fff", "ggg", "hhh", "iii", "jjj"};

        int size = list.length;

        /*

         */
        BitSet bitSet = new BitSet(size); // 默认初始数组大小是1，

        bitSet.set(1); // bbb
        bitSet.set(4); // eee
        bitSet.set(5); // fff

        // 移除
        int newSize = size - 3;
        for (int i = 0, j = 0; (i < size) && (j < newSize); i++, j++) {
//            showString(list, j, "before");
            i = bitSet.nextClearBit(i);
            list[j] = list[i];
            showString(list, j, "after");
        }

        // 无效数据置空
        for (int k = newSize; k < size; k++) {
            list[k] = null;
        }

        showString(list, -1, "result");
    }

    private void showString(String[] list, int count, String tip) {

        System.out.print(count + ":showString:" + tip + " --- ");
        for (String str : list) {
            System.out.print(" - " + str);
        }

        System.out.println("\n");
    }


    static class User {
        private String name;
        private int age;

        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @NonNull
        @Override
        public String toString() {
            return "name :" + name + " , age :" + age;
        }
    }

}
