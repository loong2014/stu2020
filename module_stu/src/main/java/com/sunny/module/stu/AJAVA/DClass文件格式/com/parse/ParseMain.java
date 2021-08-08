package com.sunny.module.stu.AJAVA.DClass文件格式.com.parse;

import java.io.InputStream;

public class ParseMain {

    public static void main(String[] args) throws Exception {
        InputStream in = Class.class.getResourceAsStream("/com.base/DSunnyBase.class");
        ClassParser parser = new ClassParser(in);
        parser.parse();

    }
}
