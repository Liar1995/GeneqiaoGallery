package com.cocomeng.library.utils;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;

class BitmapInfo {
    private Context c;

    public BitmapInfo(Context context) {
        this.c = context;
    }

    public String saveBitmap(Bitmap bm, String savePath) {

        File file = new File(savePath);
        if (!file.exists()) {
            file.mkdir();
        }
        String fname = savePath + System.currentTimeMillis() + ".png";
        try {
            FileOutputStream out = new FileOutputStream(fname);
            bm.compress(Bitmap.CompressFormat.PNG, 70, out);
            System.out.println("file " + fname + "output done.");
            out.close();
            bm.recycle();
        } catch (Exception var8) {
            var8.printStackTrace();
        }
        return fname;
    }

}