package com.sunny.lib.image.modelloader;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.signature.ObjectKey;

import java.nio.ByteBuffer;

public class SunModelLoader implements ModelLoader<String, ByteBuffer> {

    @Override
    public LoadData<ByteBuffer> buildLoadData(String model, int width, int height, Options options) {
        return new LoadData<>(new ObjectKey(model), /*fetcher=*/ new SunDataFetcher(model));
    }

    @Override
    public boolean handles(@NonNull String model) {
        return model.startsWith("sunny:");
    }
}
