package com.cocomeng.library.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by yangchaojiang on 2016/7/26.
 * 图片加载预处理处理
 */
public class SquareTransformation extends BitmapTransformation {


    public SquareTransformation(Context context) {
        super(context);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap source, int outWidth, int outHeight) {
        int width = source.getWidth();
        int height = source.getHeight();
        if (width>1000) {
            float scaleWidth =0.3f;
            float scaleHeight = 0.3f;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            Bitmap resizedBitmap = Bitmap.createBitmap(source, 0, 0, width, height, matrix, true);
            if (resizedBitmap != source) {
                source.recycle();
            }
            return resizedBitmap;
        }else if (width>400) {
            float scaleWidth =0.4f;
            float scaleHeight = 0.4f;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            Bitmap resizedBitmap = Bitmap.createBitmap(source, 0, 0, width, height, matrix, true);
            if (resizedBitmap != source) {
                source.recycle();
            }
            return resizedBitmap;
        }else {
            return source;
        }
    }


    /**
     * dp转px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    @Override
    public String getId() {
        return  "square()";
    }
}
