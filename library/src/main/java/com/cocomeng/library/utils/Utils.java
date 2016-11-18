/*
 * Copyright (C) 2014 pengjianbo(pengjianbosoft@gmail.com), Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.cocomeng.library.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.UUID;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/12/7 下午7:32
 */
public class Utils {

    public static String getFileName(String pathandname) {
        int start = pathandname.lastIndexOf("/");
        int end = pathandname.lastIndexOf(".");
        if (start != -1 && end != -1) {
            return pathandname.substring(start + 1, end);
        } else {
            return null;
        }
    }

    /**
     * 保存Bitmap到文件
     * @param bitmap
     * @param format
     * @param target
     */
    public static void saveBitmap(Bitmap bitmap, Bitmap.CompressFormat format, File target) {
        if (target.exists()) {
            target.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(target);
            bitmap.compress(format, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap rotateBitmap(String path, int orientation, int screenWidth, int screenHeight) {
        Bitmap bitmap = null;
        final int maxWidth = screenWidth / 2;
        final int maxHeight = screenHeight / 2;
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            int sourceWidth, sourceHeight;
            if (orientation == 90 || orientation == 270) {
                sourceWidth = options.outHeight;
                sourceHeight = options.outWidth;
            } else {
                sourceWidth = options.outWidth;
                sourceHeight = options.outHeight;
            }
            boolean compress = false;
            if (sourceWidth > maxWidth || sourceHeight > maxHeight) {
                float widthRatio = (float) sourceWidth / (float) maxWidth;
                float heightRatio = (float) sourceHeight / (float) maxHeight;

                options.inJustDecodeBounds = false;
                if (new File(path).length() > 512000) {
                    float maxRatio = Math.max(widthRatio, heightRatio);
                    options.inSampleSize = (int) maxRatio;
                    compress = true;
                }
                bitmap = BitmapFactory.decodeFile(path, options);
            } else {
                bitmap = BitmapFactory.decodeFile(path);
            }
            if (orientation > 0) {
                Matrix matrix = new Matrix();
                //matrix.postScale(sourceWidth, sourceHeight);
                matrix.postRotate(orientation);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }
            sourceWidth = bitmap.getWidth();
            sourceHeight = bitmap.getHeight();
            if ((sourceWidth > maxWidth || sourceHeight > maxHeight) && compress) {
                float widthRatio = (float) sourceWidth / (float) maxWidth;
                float heightRatio = (float) sourceHeight / (float) maxHeight;
                float maxRatio = Math.max(widthRatio, heightRatio);
                sourceWidth = (int) ((float) sourceWidth / maxRatio);
                sourceHeight = (int) ((float) sourceHeight / maxRatio);
                Bitmap bm = Bitmap.createScaledBitmap(bitmap, sourceWidth, sourceHeight, true);
                bitmap.recycle();
                return bm;
            }
        } catch (Exception e) {
        }
        return bitmap;
    }

    /***
     * 获取图片旋转信息。转正图片
     ****/
    public static String readPictureDegree(String path) {
        int degree = 0;
        SoftReference<Bitmap> bitmap = null;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            degree = orientation == ExifInterface.ORIENTATION_ROTATE_90 ? 90 : orientation == ExifInterface.ORIENTATION_ROTATE_180 ? 180 : orientation == ExifInterface.ORIENTATION_ROTATE_270 ? 270 : 0;
            if (degree == 0) {
                return path;
            } else {// 需要将图片翻转

                BitmapFactory.Options option = new BitmapFactory.Options();
                option.inJustDecodeBounds = true;
                FileInputStream stream1 = new FileInputStream(new File(path));
                BitmapFactory.decodeStream(stream1, null, option);
                int width_tmp = option.outWidth, height_tmp = option.outHeight;
                int scale = 1;
                stream1.close();
                int scalex = width_tmp / 720;
                int scaley = height_tmp / 720;
                scale = scalex > scaley ? scaley : scalex;
                if (scale < 1) {
                    scale = 1;
                }
                BitmapFactory.Options option2 = new BitmapFactory.Options();
                option2.inSampleSize = scale;
                FileInputStream stream2 = new FileInputStream(new File(path));
                bitmap = new SoftReference<Bitmap>(BitmapFactory.decodeStream(stream2, null, option2));
                stream2.close();
                Matrix matrix = new Matrix();
                matrix.postRotate(degree);
                bitmap = new SoftReference<Bitmap>(Bitmap.createBitmap(bitmap.get(), 0, 0, bitmap.get().getWidth(), bitmap.get().getHeight(), matrix, true));
                File fileDir = new File(SdCardUtil.SABEIMAGE);
                if (!fileDir.exists()) {
                    fileDir.mkdir();
                }
                path = SdCardUtil.SABEIMAGE+ UUID.randomUUID()+".jpg";
                File file = new File(path);
                bitmap.get().compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(bitmap!=null&&bitmap.get()!=null){
                bitmap.get().recycle();
                bitmap=null;
            }
        }
        return path;
    }

    /**
     * 取某个范围的任意数
     * @param min
     * @param max
     * @return
     */
    public static int getRandom(int min, int max){
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return s;
    }

    /**
     * 转换文件大小
     */
    public static String formetFileSize(Long fileS) {
        DecimalFormat df = new DecimalFormat("#0.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == null || fileS == 0) {
            return wrongSize;
        }
//        fileS = fileS * 1024;
//        if (fileS < 1024) {
//            fileSizeString = df.format((double) fileS) + "B";
//        } else if (fileS < 1048576) {
//            fileSizeString = df.format((double) fileS / 1024) + "KB";
//        } else if (fileS < 1073741824) {
        fileSizeString = df.format((double) fileS / 1024) + "M";
//        } else {
//            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
//        }
        if (df.format((double) fileS / 1024).equals(0.00)) {
            fileSizeString = "0.01M";
        }
        return fileSizeString;
    }
}
