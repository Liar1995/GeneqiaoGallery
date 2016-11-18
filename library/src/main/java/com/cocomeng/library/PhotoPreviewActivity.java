package com.cocomeng.library;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cocomeng.library.adapter.PhotoPreviewAdapter;
import com.cocomeng.library.model.PhotoInfo;
import com.cocomeng.library.utils.Utils;
import com.cocomeng.library.widget.GFViewPager;

import java.io.File;
import java.util.ArrayList;


/**
 * Desction:
 * Author:pengjianbo
 * Date:2015/12/29 0029 14:43
 */
public class PhotoPreviewActivity extends PhotoBaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    static final String PHOTO_LIST = "photo_list";

    private RelativeLayout mTitleBar, footer_bar;
    private ImageView mIvBack, iv_video_img, iv_video_mask;
    private TextView mTvTitle;
    private TextView mTvIndicator;

    private GFViewPager mVpPager;
    private ArrayList<PhotoInfo> mPhotoList;
    private PhotoPreviewAdapter mPhotoPreviewAdapter;

    private ThemeConfig mThemeConfig;
    private String videoPath;
    private Button btn_file_size, btn_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mThemeConfig = GalleryFinal.getGalleryTheme();

        if (mThemeConfig == null) {
            resultFailureDelayed(getString(R.string.please_reopen_gf), true);
        } else {
            setContentView(R.layout.gf_activity_photo_preview);
            findViews();
            setListener();
            setTheme();

            mPhotoList = (ArrayList<PhotoInfo>) getIntent().getSerializableExtra(PHOTO_LIST);
            if(mPhotoList==null){
                resultFailureDelayed(getString(R.string.please_reopen_gf), true);
            }else{
                if (mPhotoList.get(0).isPic()) {
                    mPhotoPreviewAdapter = new PhotoPreviewAdapter(this, mPhotoList);
                    mVpPager.setAdapter(mPhotoPreviewAdapter);
                    footer_bar.setVisibility(View.GONE);
                    iv_video_mask.setVisibility(View.GONE);
                    iv_video_img.setVisibility(View.GONE);
                } else {
                    mVpPager.setVisibility(View.GONE);
                    footer_bar.setVisibility(View.VISIBLE);
                    mTvTitle.setVisibility(View.GONE);
                    mTvIndicator.setText(getString(R.string.video_preview));

                    iv_video_mask.setVisibility(View.VISIBLE);
                    iv_video_img.setVisibility(View.VISIBLE);
                    GalleryFinal.getCoreConfig().getImageLoader().displayVideoImage(this, mPhotoList.get(0).getPhotoPath(), iv_video_img);
                    btn_file_size.setText(getString(R.string.video_preview_size)+ Utils.formetFileSize(mPhotoList.get(0).getVideoSize()/1024));//设置显示的文件大小
                }
            }
        }
    }

    private void findViews() {
        mTitleBar = (RelativeLayout) findViewById(R.id.titlebar);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvIndicator = (TextView) findViewById(R.id.tv_indicator);

        mVpPager = (GFViewPager) findViewById(R.id.vp_pager);

        iv_video_img = (ImageView) findViewById(R.id.iv_video_img);
        iv_video_mask = (ImageView) findViewById(R.id.iv_video_mask);
        footer_bar = (RelativeLayout) findViewById(R.id.footer_bar);
        btn_file_size = (Button) findViewById(R.id.btn_file_size);
        btn_send = (Button) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);
        iv_video_mask.setOnClickListener(this);
    }

    private void setListener() {
        mVpPager.addOnPageChangeListener(this);
        mIvBack.setOnClickListener(mBackListener);
    }

    private void setTheme() {
        mIvBack.setImageResource(mThemeConfig.getIconBack());
        if (mThemeConfig.getIconBack() == R.drawable.ic_gf_back) {
            mIvBack.setColorFilter(mThemeConfig.getTitleBarIconColor());
        }

        mTitleBar.setBackgroundColor(mThemeConfig.getTitleBarBgColor());
        mTvTitle.setTextColor(mThemeConfig.getTitleBarTextColor());
        if (mThemeConfig.getPreviewBg() != null) {
            mVpPager.setBackgroundDrawable(mThemeConfig.getPreviewBg());
        }
    }

    @Override
    protected void takeResult(PhotoInfo info) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mTvIndicator.setText((position + 1) + "/" + mPhotoList.size());
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private View.OnClickListener mBackListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    @Override
    public void onClick(View v) {
        //点击视频预览
        if (v.getId() == R.id.iv_video_mask) {
            try {
                Intent intent = intent = getVideoFileIntent(mPhotoList.get(0).getPhotoPath());
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, getString(R.string.video_preview_no_toast), Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.btn_send) {//发送
            btn_send.setEnabled(false);
            resultData(mPhotoList);
        }
    }

    // android获取一个用于打开视频文件的intent

    public static Intent getVideoFileIntent(String param)

    {

        Intent intent = new Intent("android.intent.action.VIEW");

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.putExtra("oneshot", 0);

        intent.putExtra("configchange", 0);

        Uri uri = Uri.fromFile(new File(param));

        intent.setDataAndType(uri, "video/*");

        return intent;

    }
}
