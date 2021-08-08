package com;

import com.base.DISunny;
import com.base.DSunnyBase;
import com.bean.DUserInfo;

public class DSunny extends DSunnyBase {

    private static final String TAG = "TAGDSunny";


    private String name;

    private Integer integer;
    private int age;

    private DISunny iSunny;
    private SunnyInner inner = new SunnyInner();

    public DSunny() {
        iSunny = new DUserInfo();
    }

    private DISunny getUserInfo() throws Exception {
        age = inner.index;
        if (age < 0) {
            throw new Exception("age mast > 0");
        }
        return iSunny;
    }

    public void showInfo() throws Exception{
        DISunny userInfo = getUserInfo();
        showLog(TAG + userInfo.getTip() + age);
    }

    class SunnyInner {
        int index = 0;
    }

}
