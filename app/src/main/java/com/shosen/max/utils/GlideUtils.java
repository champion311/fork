package com.shosen.max.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.shosen.max.R;

import java.security.MessageDigest;

public class GlideUtils {
    //默认图片加载
    public static RequestOptions defaultReqOptions = new RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.default_avater)
            .error(R.drawable.default_avater)
            .priority(Priority.HIGH);
    //图片选择器加载形式
    public static RequestOptions imagePicker = new RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.imagepicker_default_image)
            .error(R.drawable.imagepicker_default_image)
            .priority(Priority.NORMAL).diskCacheStrategy(DiskCacheStrategy.ALL);
    //图片预览加载形式
    public static RequestOptions imagePickerPriView = new RequestOptions()
            .fitCenter()
            .placeholder(R.drawable.imagepicker_default_image)
            .diskCacheStrategy(DiskCacheStrategy.ALL);

    public static DrawableTransitionOptions
            defaultTransOptions = new DrawableTransitionOptions().crossFade();

    public static void loadImage(Activity activity, String url, RequestOptions options,
                                 DrawableTransitionOptions transitionOptions,
                                 ImageView target) {
        Glide.with(activity).load(url).apply(options).transition(transitionOptions).into(target);
    }

    public static void loadImage(Activity activity, String url, ImageView target) {
        Glide.with(activity).load(url).apply(defaultReqOptions).transition(defaultTransOptions).into(target);
    }

    public static void loadImage(Fragment fragment, String url, ImageView target) {
        Glide.with(fragment).load(url).apply(defaultReqOptions).into(target);
    }

    public static void loadImage(Context mContext, String url, ImageView target) {
        Glide.with(mContext).load(url).apply(defaultReqOptions).into(target);
    }

    public static void loadImage(Activity activity, Uri uri, RequestOptions options,
                                 DrawableTransitionOptions transitionOptions,
                                 ImageView target) {
        Glide.with(activity).load(uri).apply(options).transition(transitionOptions).into(target);
    }

    public static void loadImage(Context mContext, String url, RequestOptions options,
                                 DrawableTransitionOptions transitionOptions,
                                 ImageView target) {
        Glide.with(mContext).load(url).apply(options).transition(transitionOptions).into(target);
    }

    public static void loadHeadImage(Context mContext, String url, ImageView target) {
        RequestOptions options = new RequestOptions();
        options.error(R.drawable.default_avater)
                .placeholder(R.drawable.default_avater).
                centerCrop().circleCrop();
        Glide.with(mContext).load(url).apply(options).transition(defaultTransOptions).into(target);
    }


    public static class CircleTransition extends BitmapTransformation {

        private static final String ID = "com.shosen.max.utils.GlideUtils.CircleTransition";


        @Override
        public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
            messageDigest.update(ID.getBytes());
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) {
                return null;
            }
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);
            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }
            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }
    }


}



