package com.sunny.family;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

/**
 * 1. 该类必须在包名下面，不能有二级目录
 * 2. build.gradle中必须添加:
 * 2.1. 头部添加 apply plugin: 'kotlin-kapt'
 * 2.2. dependencies中添加
 * implementation 'com.android.support:support-annotations:28.0.0'
 * kapt 'com.github.bumptech.glide:compiler:4.11.0' // 必须使用kapt而不是annotationProcessor
 * <p>
 * <p>
 * 在Glide4.11中目前依然不知道GlideApp和Glide有什么区别？
 */
@GlideModule
public class SunAppGlideModule extends AppGlideModule {


    private void glideTest() {
    }
}
