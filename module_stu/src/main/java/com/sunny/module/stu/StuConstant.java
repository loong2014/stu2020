package com.sunny.module.stu;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sunny.module.stu.jump.StuJumpModel;

/**
 * 学习常量类
 */
public class StuConstant {


    interface StuThread {

    }

    public static String buildJumpValue(String jumpType) {
        StuJumpModel bean = new StuJumpModel();
        bean.setJumpType(jumpType);
        return new Gson().toJson(bean);
    }

    public static StuJumpModel buildJumpModel(String jsonStr) {
        Gson gson = new Gson();
        return gson.fromJson(jsonStr, new TypeToken<StuJumpModel>() {
        }.getType());
    }

    public static void reBuildJumpModel(StuJumpModel jumpModel) {

    }

}
