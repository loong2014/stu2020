package com.sunny.lib.ui.mask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;


public class MaskImageView extends AppCompatImageView {


    public MaskImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void mask(int mImageSource, int mMaskSource) {
        if (mImageSource == 0 || mMaskSource == 0) {
            return;
        }

        Bitmap original = BitmapFactory.decodeResource(getResources(), mImageSource);
        Bitmap mask = BitmapFactory.decodeResource(getResources(), mMaskSource);

        Bitmap result = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas mCanvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));//叠加反复的部分，显示以下的
        mCanvas.drawBitmap(original, 0, 0, null);
        mCanvas.drawBitmap(mask, 0, 0, paint);
        paint.setXfermode(null);
        setImageBitmap(result);
    }

}