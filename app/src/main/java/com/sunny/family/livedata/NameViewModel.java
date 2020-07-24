package com.sunny.family.livedata;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

/**
 * Created by zhangxin17 on 2020/7/13
 */
public class NameViewModel extends ViewModel {
    private MutableLiveData<String> mCurrentName;

    private MutableLiveData<List<String>> mNameListData;
    private MediatorLiveData<String> familyNameData;

    public MutableLiveData<String> getCurrentName() {
        if (mCurrentName == null) {
            mCurrentName = new MutableLiveData<>();
        }
        return mCurrentName;
    }

    public MutableLiveData<List<String>> getNameListData() {
        if (mNameListData == null) {
            mNameListData = new MutableLiveData<>();
        }
        return mNameListData;
    }
}
