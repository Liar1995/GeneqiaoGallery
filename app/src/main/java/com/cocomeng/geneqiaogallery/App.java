package com.cocomeng.geneqiaogallery;

import android.app.Application;
import android.graphics.Color;

import com.cocomeng.library.CoreConfig;
import com.cocomeng.library.FunctionConfig;
import com.cocomeng.library.GalleryFinal;
import com.cocomeng.library.ThemeConfig;

/**
 * Created by Sunmeng on 11/18/2016.
 * E-Mail:Sunmeng1995@outlook.com
 * Description:
 */

public class App extends Application {

    private final static String DEFAULT_DBNAME = "App";

    @Override
    public void onCreate() {
        super.onCreate();
        initGalleryFinal();
    }

    /**
     * 相册加载初始化
     */
    public void initGalleryFinal() {
        //设置主题
        ThemeConfig theme = new ThemeConfig.Builder()
                .setTitleBarBgColor(Color.rgb(0x00, 0x96, 0x88))
                .setCheckSelectedColor(Color.rgb(0x00, 0x96, 0x88))
                .setCropControlColor(Color.rgb(0x00, 0x96, 0x88))
                .setFabNornalColor(Color.rgb(0x00, 0x96, 0x88))
                .setFabPressedColor(Color.rgb(0x00, 0x96, 0x88))
                .build();
        //配置功能
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setMutiSelectMaxSize(9)
                .setEnableCamera(true)//开启相机功能
                .setEnableEdit(false)
                .setEnableCrop(false)
                .setForceCrop(true)
                .setForceCropEdit(true)
                .setEnableRotate(true)
                .setCropSquare(true)
                .setEnablePreview(true)
                .build();
        //配置imageloader
        CoreConfig coreConfig = new CoreConfig.Builder(getApplicationContext(), new GlideImageLoader(), theme)
                .setFunctionConfig(functionConfig)
                .setNoAnimcation(true)
                .build();
        GalleryFinal.init(coreConfig);
    }
}
