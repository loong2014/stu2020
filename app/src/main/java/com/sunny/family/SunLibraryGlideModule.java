package com.sunny.family;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.LibraryGlideModule;

@GlideModule
public class SunLibraryGlideModule extends LibraryGlideModule {
    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        super.registerComponents(context, glide, registry);
        // 默认OkHttp
//        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
//        registry.replace(GlideUrl.class, InputStream.class, new VolleyUrlLoader.Factory(context));


    }
}
