package com.angcyo.bmob;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：
 * 创建人员：Robi
 * 创建时间：2018/02/22 15:51
 * 修改人员：Robi
 * 修改时间：2018/02/22 15:51
 * 修改备注：
 * Version: 1.0.0
 */
public class RBmob {
    public static void init(Application application, boolean debug) {
        String appid = "";
        String channel = "";
        try {
            ApplicationInfo applicationInfo = application.getPackageManager()
                    .getApplicationInfo(application.getPackageName(), PackageManager.GET_META_DATA);

            appid = String.valueOf(applicationInfo.metaData.get("BMOB_APP_ID"));
            channel = String.valueOf(applicationInfo.metaData.get("BMOB_CHANNEL"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (debug) {
            Log.i("RBmob", "init->appid:" + appid + " channel:" + channel);
        }
        //提供以下两种方式进行初始化操作：
        //第一：默认初始化
        Bmob.initialize(application, appid, channel);
        // 注:自v3.5.2开始，数据sdk内部缝合了统计sdk，开发者无需额外集成，传渠道参数即可，不传默认没开启数据统计功能
        //Bmob.initialize(this, "Your Application ID","bmob");

        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        //BmobConfig config =new BmobConfig.Builder(this)
        ////设置appkey
        //.setApplicationId("Your Application ID")
        ////请求超时时间（单位为秒）：默认15s
        //.setConnectTimeout(30)
        ////文件分片上传时每片的大小（单位字节），默认512*1024
        //.setUploadBlockSize(1024*1024)
        ////文件的过期时间(单位为秒)：默认1800s
        //.setFileExpiration(2500)
        //.build();
        //Bmob.initialize(config);
    }

    public static <T> BmobQuery<T> query() {
        BmobQuery<T> query = new BmobQuery<>();
        return query;
    }
}
