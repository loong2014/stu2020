package com.sunny.module.stu.AJAVA.J集合框架;

import android.util.ArraySet;

import com.sunny.module.stu.base.StuImpl;

import java.util.AbstractList;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * https://blog.csdn.net/feiyanaffection/article/details/81394745
 */
public class Stu集合框架 extends StuImpl {

    @Override
    public void s_数据结构() {
        // Collection  Map

        Collection collection;
        List list;
        Map map;
    }
     /*
        Collection 接口的接口 对象的集合（单列集合）
        ├——-List 接口：元素按进入先后有序保存，可重复
        │—————-├ LinkedList 接口实现类， 链表， 插入删除， 没有同步， 线程不安全
        │—————-├ ArrayList 接口实现类， 数组， 随机访问， 没有同步， 线程不安全
        │—————-└ Vector 接口实现类 数组， 同步， 线程安全
        │ ———————-└ Stack 是Vector类的实现类
        └——-Set 接口： 仅接收一次，不可重复，并做内部排序
        ├—————-└HashSet 使用hash表（数组）存储元素
        │————————└ LinkedHashSet 链表维护元素的插入次序
        └ —————-TreeSet 底层实现为二叉树，元素排好序

        Map 接口 键值对的集合 （双列集合）
        ├———Hashtable 接口实现类， 同步， 线程安全
        ├———HashMap 接口实现类 ，没有同步， 线程不安全-
        │—————–├ LinkedHashMap 双向链表和哈希表实现
        │—————–└ WeakHashMap
        ├ ——–TreeMap 红黑树对所有的key进行排序
        └———IdentifyHashMap
     */

    private void stu_Collection(){
        //
        List list;

        Set set;

        ArraySet arraySet;

        AbstractSet abstractSet;

        HashSet hashSet;

        //
        AbstractList abstractList;

        ArrayList arrayList;
    }

}
