package com.shosen.max.utils.GlideTransition;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

public class MiniCircleTransition extends BitmapTransformation {

    private int outWidth;

    private int outHeight;

    public MiniCircleTransition(int outWidth, int outHeight) {
        this.outWidth = outWidth;
        this.outHeight = outHeight;
    }

    private static final String ID = "com.shosen.max.utils.GlideUtils.MiniCircleTransition";

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return circleCrop(pool, toTransform);
    }

    private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
        if (source == null) {
            return null;
        }
        float widthScale = outWidth * 1.0f / source.getWidth();
        float heightScale = outHeight * 1.0f / source.getHeight();
        Matrix matrix = new Matrix();
        matrix.setScale(widthScale, heightScale);
        BitmapShader bitmapShader = new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        bitmapShader.setLocalMatrix(matrix);
        Paint paint = new Paint();
        paint.setShader(bitmapShader);
        paint.setAntiAlias(true);

        Bitmap result = pool.get(outWidth, outHeight, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(result);
        int radius = Math.min(outWidth, outHeight);
        canvas.drawCircle(outWidth / 2, outHeight / 2, radius, paint);
        //放到缓存当中
        pool.put(result);
        return result;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID.getBytes());
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MiniCircleTransition;
    }
}