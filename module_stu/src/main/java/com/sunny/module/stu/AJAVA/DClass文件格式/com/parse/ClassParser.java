package com.sunny.module.stu.AJAVA.DClass文件格式.com.parse;

import com.sunny.module.stu.AJAVA.DClass文件格式.com.tools.StreamUtils;

import java.io.IOException;
import java.io.InputStream;


public class ClassParser {
    private InputStream in;

    public ClassParser(InputStream in) {
        this.in = in;
    }

    public void parse() throws IOException {        // 魔数
        magicNumber();        // 主次版本号
        version();        // 常量池
        constantPool();        // 类或接口修饰符
        accessFlag();        // 继承关系（当前类、父类、父接口）
        inheritence();        // 字段集合
        fieldList();        // 方法集合
        methodList();        // 属性集合
        attributeList();
    }

    private void attributeList() throws IOException {
        line();
        int attrLength = StreamUtils.read2(in);
        System.out.println("共有" + attrLength + "个属性");
        for (int i = 0; i < attrLength; i++) {
            line();
            attribute();
        }
    }

    private void attribute() throws IOException {
        int nameIndex = StreamUtils.read2(in);
        int length = StreamUtils.read4(in);
        byte[] info = StreamUtils.read(in, length);
        System.out.println("nameIndex:" + nameIndex);
        System.out.println("length:" + length);
        System.out.println("info:" + info);
    }

    private void methodList() throws IOException {
        int length = StreamUtils.read2(in);
        System.out.println("共有" + length + "个方法");
        for (int i = 0; i < length; i++)
            method();
    }

    private void method() throws IOException {
        System.out.println("---------------------");
        int accessFlag = StreamUtils.read2(in);
        int nameIndex = StreamUtils.read2(in);
        int descriptorIndex = StreamUtils.read2(in);
        System.out.println("accessFlag:" + accessFlag);
        System.out.println("nameIndex:" + nameIndex);
        System.out.println("descriptorIndex:" + descriptorIndex);
        attributeList();
    }

    private void fieldList() throws IOException {
        line();
        int length = StreamUtils.read2(in);
        System.out.println("共有" + length + "个字段");
        for (int i = 0; i < length; i++) {
            System.out.println("-----------------------------");
            int accessFlag = StreamUtils.read2(in);
            int nameIndex = StreamUtils.read2(in);
            int descriptorIndex = StreamUtils.read2(in);
            System.out.println("accessFlag:" + accessFlag);
            System.out.println("nameIndex:" + nameIndex);
            System.out.println("descriptorIndex:" + descriptorIndex);
            attributeList();
        }
    }

    private void inheritence() throws IOException {
        line();
        int thisClassRef = StreamUtils.read2(in);
        int superClassRef = StreamUtils.read2(in);
        System.out.println("thisClassRef:" + thisClassRef);
        System.out.println("superClassRef:" + superClassRef);
        int interfaceLen = StreamUtils.read2(in);
        System.out.println("接口数量：" + interfaceLen);
        for (int i = 0; i < interfaceLen; i++) {
            int interfaceRef = StreamUtils.read2(in);
            System.out.println("interfaceRef:" + interfaceRef);
        }
    }

    private void accessFlag() throws IOException {
        line();
        int accessFlag = StreamUtils.read2(in);
        System.out.println("accessFlag:0x" + Integer.toHexString(accessFlag) + "(" + accessFlag + ")");
    }

    private void constantPool() throws IOException {
        new ConstPoolParser(in).constPool();
    }

    private void version() throws IOException {
        line();
        int minorVersion = StreamUtils.read2(in);
        int majorVersion = StreamUtils.read2(in);
        System.out.println("版本：" + majorVersion + "." + minorVersion);
    }

    private void magicNumber() throws IOException {
        line();
        int magic = StreamUtils.read4(in);
        System.out.println("魔数：" + Integer.toHexString(magic).toUpperCase());
    }

    private void line() {
        System.out.println("----------------------");
    }
}


