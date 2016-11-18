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

package com.cocomeng.library.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.cocomeng.library.GalleryFinal;
import com.cocomeng.library.R;
import com.cocomeng.library.model.PhotoInfo;
import com.cocomeng.library.widget.GFImageView;

import java.util.List;
import java.util.Map;

import cn.finalteam.toolsfinal.adapter.ViewHolderAdapter;


/**
 * Desction:
 * Author:pengjianbo
 * Date:15/10/10 下午4:59
 */
public class PhotoListAdapter extends ViewHolderAdapter<PhotoListAdapter.PhotoViewHolder, PhotoInfo> {

    private Map<String, PhotoInfo> mSelectList;
    private int mScreenWidth;
    private int mRowWidth;

    private Activity mActivity;

    public PhotoListAdapter(Activity activity, List<PhotoInfo> list, Map<String, PhotoInfo> selectList, int screenWidth) {
        super(activity, list);
        this.mSelectList = selectList;
        this.mScreenWidth = screenWidth;
        this.mRowWidth = mScreenWidth / 3;
        this.mActivity = activity;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = inflate(R.layout.gf_adapter_photo_list_item, parent);
        setHeight(view);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        PhotoInfo photoInfo = getDatas().get(position);

        String path = "";
        if (photoInfo != null) {
            path = photoInfo.getPhotoPath();
        }

        //设置显示默认占位图
        holder.mIvThumb.setImageResource(R.drawable.img_default);
        Drawable defaultDrawable = mActivity.getResources().getDrawable(R.drawable.img_default);
        //视频占位图
        Drawable defaultVideoDrawable = mActivity.getResources().getDrawable(R.drawable.group_dhk_video);
        //判断是视频还是图片
        if (photoInfo.isPic()) {
            Log.d("Sunmeng", "地址：" + path);
            holder.iv_video_zhezhao.setVisibility(View.GONE);
            GalleryFinal.getCoreConfig().getImageLoader().displayImage(mActivity, path, holder.mIvThumb, defaultDrawable, mRowWidth, mRowWidth);
        } else {
            holder.iv_video_zhezhao.setVisibility(View.VISIBLE);
            holder.mIvCheck.setVisibility(View.GONE);
            //holder.mIvThumb.setImageDrawable(defaultVideoDrawable);
           // photoInfo.setImageView(holder.mIvThumb);
            GalleryFinal.getCoreConfig().getImageLoader().displayImage(mActivity, photoInfo.getPhotoPath(), holder.mIvThumb, defaultVideoDrawable, mRowWidth, mRowWidth);
           // LoadVideoImage load=new LoadVideoImage(mActivity);
           // load.execute(photoInfo);
            //GalleryFinal.getCoreConfig().getImageLoader().displayImage(mActivity, path, holder.mIvThumb, defaultVideoDrawable, mRowWidth, mRowWidth);
//            holder.mIvThumb.setImageBitmap(ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.MICRO_KIND));
            //MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
        }

        holder.mView.setAnimation(null);
        if (GalleryFinal.getCoreConfig().getAnimation() > 0) {
            holder.mView.setAnimation(AnimationUtils.loadAnimation(mActivity, GalleryFinal.getCoreConfig().getAnimation()));
        }
        holder.mIvCheck.setImageResource(GalleryFinal.getGalleryTheme().getIconCheck());
        if (GalleryFinal.getFunctionConfig().isMutiSelect()) {
            if (photoInfo.isPic()) {
                holder.mIvCheck.setVisibility(View.VISIBLE);
            }
            if (mSelectList.get(photoInfo.getPhotoPath()) != null) {
                holder.mIvCheck.setBackgroundColor(GalleryFinal.getGalleryTheme().getCheckSelectedColor());
            } else {
                holder.mIvCheck.setBackgroundColor(GalleryFinal.getGalleryTheme().getCheckNornalColor());
            }
        } else {
            holder.mIvCheck.setVisibility(View.GONE);
        }
    }

    private void setHeight(final View convertView) {
        int height = mScreenWidth / 3 - 8;
        convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
    }

    public static class PhotoViewHolder extends ViewHolderAdapter.ViewHolder {

        public GFImageView mIvThumb;
        public ImageView mIvCheck;
        public ImageView iv_video_zhezhao;

        View mView;

        public PhotoViewHolder(View view) {
            super(view);
            mView = view;
            mIvThumb = (GFImageView) view.findViewById(R.id.iv_thumb);
            mIvCheck = (ImageView) view.findViewById(R.id.iv_check);
            iv_video_zhezhao = (ImageView) view.findViewById(R.id.iv_video_zhezhao);
        }
    }
}
