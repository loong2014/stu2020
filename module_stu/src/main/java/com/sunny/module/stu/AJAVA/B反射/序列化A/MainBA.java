package com.sunny.module.stu.AJAVA.B反射.序列化A;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MainBA {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        BASunny sunny = new BASunny("张三", 18, 30);

        //序列化
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("BASunny.obj"));
        oos.writeObject("测试序列化");
        oos.writeObject(618);

        BASunny test = new BASunny("张三", 18, 30);
        oos.writeObject(test);

        //反序列化
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("BASunny.obj"));

        System.out.println((String) ois.readObject());
        System.out.println((Integer) ois.readObject());
        System.out.println((BASunny) ois.readObject());

    }
}
