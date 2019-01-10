package com.shosen.max.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.shosen.max.constant.Contstants;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

public class TakePhotoUtils {

    private static String TAG = "TakePhotoUtils";

    /**
     * 调用拍照camera
     *
     * @param mContext
     */
    public static File openCamera(Activity mContext) {
        if (mContext == null) {
            return null;
        }
        File outPutFile = getOutPutFilePath(mContext);
        Uri imageUri;
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(mContext, "com.shosen.max.provider", outPutFile);
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            intentFromCapture.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
            intentFromCapture.addFlags(FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            imageUri = Uri.fromFile(outPutFile);
        }
        intentFromCapture.putExtra("imageUri", imageUri.toString());
        mContext.startActivityForResult(intentFromCapture, Contstants.CAMERA_REQUEST_CODE);
        return outPutFile;
    }

    public static File openGallery(Activity mContext) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        File galleryFile = getGalleryFilePath(mContext);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//如果大于等于7.0使用FileProvider
            Uri uriForFile = FileProvider.getUriForFile
                    (mContext, "com.shosen.max.provider", galleryFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            mContext.startActivityForResult(intent, Contstants.SELECT_PIC_NOUGAT);
        } else {
            //7.0以下不需要设置
            //intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mGalleryFile));
            mContext.startActivityForResult(intent, Contstants.SELECT_PIC_UNDER_NOUGAT);
        }
        return galleryFile;
    }

    /**
     * @param activity
     * @param inputUri
     * @return 裁剪图片的返回结果
     */
    public static File startPhotoZoom(Activity activity, Uri inputUri) {
        if (inputUri == null) {
            Log.e("error", "The uri is not exist.");
            return null;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        File mCropFile = getCropFilePath(activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri outPutUri = Uri.fromFile(mCropFile);
            intent.setDataAndType(inputUri, "image/*");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri);
            intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            Uri outPutUri = Uri.fromFile(mCropFile);
            if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                String url = FileUtils.getFilePathByUri(activity, inputUri);//这个方法是处理4.4以上图片返回的Uri对象不同的处理方法
                intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
            } else {
                intent.setDataAndType(inputUri, "image/*");
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri);
        }
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", false);
        intent.putExtra("noFaceDetection", false);//去除默认的人脸识别，否则和剪裁匡重叠
        intent.putExtra("outputFormat", "JPEG");
        //intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());// 图片格式
        activity.startActivityForResult(intent, Contstants.CROP_IMAGE_REQUEST_CODE);//
        return mCropFile;
    }

    public static File getOutPutFilePath(Context mContext) {
        File parent = mContext.getExternalFilesDir(null);
        return createFile(parent, "output", ".jpg");
    }

    public static File getGalleryFilePath(Context mContext) {
        File parent = mContext.getExternalFilesDir(null);
        return createFile(parent, "gallery", ".jpg");
    }

    public static File getCropFilePath(Context mContext) {
        File parent = mContext.getExternalFilesDir(null);
        return createFile(parent, "cropped", ".jpg");
    }

    public static File getSavedBitmapFilePath(Context mContext) {
        File parent = mContext.getExternalFilesDir(null);
        return createFile(parent, "saved_image", ".jpg");
    }

    /**
     * 根据系统时间、前缀、后缀产生一个文件
     */
    public static File createFile(File folder, String prefix, String suffix) {
        if (!folder.exists() || !folder.isDirectory()) folder.mkdirs();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        String filename = prefix + dateFormat.format(new Date(System.currentTimeMillis())) + suffix;
        return new File(folder, filename);
    }

}
