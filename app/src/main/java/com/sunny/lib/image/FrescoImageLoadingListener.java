package com.sunny.lib.image;

import android.graphics.Bitmap;
import android.net.Uri;

public interface FrescoImageLoadingListener {
    Bitmap onSuccess(Uri uri, Bitmap bitmap);

    Bitmap onFailure(Uri uri, Throwable throwable);
}
