package com.cocomeng.geneqiaogallery;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.cocomeng.library.ImageLoader;
import com.cocomeng.library.widget.GFImageView;

import java.io.File;

/**
 * Created by Sunmeng on 11/18/2016.
 * E-Mail:Sunmeng1995@outlook.com
 * Description:
 */

public class GlideImageLoader implements ImageLoader {

    private final static String DEFAULT_DBNAME = "GlideImageLoader";

    @Override
    public void displayImage(Context activity, String path, ImageView imageView) {

    }

    @Override
    public void displayImage(Context activity, String path, ImageView imageView, SimpleTarget<Bitmap> callback) {

    }

    @Override
    public void displayHeadImage(Context activity, String path, ImageView imageView) {

    }

    @Override
    public void displayHeadImage(Context activity, String path, ImageView imageView, SimpleTarget<Bitmap> target) {

    }

    @Override
    public void displayImage(Activity activity, String path, final GFImageView imageView, Drawable defaultDrawable, int width, int height) {
        Glide.with(activity)
                .load("file://" + path)
                .placeholder(defaultDrawable)
                .error(defaultDrawable)
                .override(width, height)
                .diskCacheStrategy(DiskCacheStrategy.NONE) //不缓存到SD卡
                .skipMemoryCache(true)
                //.centerCrop()
                .into(new ImageViewTarget<GlideDrawable>(imageView) {
                    @Override
                    protected void setResource(GlideDrawable resource) {
                        imageView.setImageDrawable(resource);
                    }

                    @Override
                    public void setRequest(Request request) {
                    }
                });
    }

    @Override
    public void displayImageNoDefaultImg(Context activity, String path, ImageView imageView, SimpleTarget target) {

    }

    @Override
    public void displayVideoImage(Context activity, String path, ImageView imageView) {
        DrawableTypeRequest glide;
        glide = Glide.with(activity).load(new File(path));
        glide.override(500, 500);
        glide.centerCrop();
        glide.placeholder(R.drawable.img_default)
                .into(imageView);
    }

    @Override
    public void clearMemoryCache() {
    }

    @Override
    public void clearDiskCache(Context context) {

    }
}
