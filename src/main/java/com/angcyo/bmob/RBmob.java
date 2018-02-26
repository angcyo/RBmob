package com.angcyo.bmob;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.angcyo.uiview.net.P;
import com.angcyo.uiview.net.RSubscriber;
import com.angcyo.uiview.utils.RUtils;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import rx.Subscription;

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

    /**
     * 保存新的数据, 保存成功返回数据 objectId, 如果失败, 返回空字符串
     */
    public static <T extends BmobObject> Subscription save(T data, final OnSingleResult<String> onResult) {
        return data.saveObservable().subscribe(new RSubscriber<String>() {
            @Override
            public void onSucceed(String bean) {
                super.onSucceed(bean);
                onResult.onResult(bean); //ObjectId 字符串类型
            }

            @Override
            public void onError(int code, String msg) {
                //super.onError(code, msg);
                onResult.onResult("");
            }
        });
    }

    /**
     * 根据条件, 如果找到了已经存在的数据, 则更新数据, 否在添加数据
     */
    public static <T extends BmobObject> Subscription update(Class<T> cls, final T data, String where, final OnSingleResult<String> onResult) {
        return query(cls, where, new OnResult<T>() {
            @Override
            public void onResult(List<T> resultList) {
                if (RUtils.isListEmpty(resultList)) {
                    save(data, onResult);
                } else {
                    final String objectId = resultList.get(0).getObjectId();
                    data.update(objectId, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                onResult.onResult(objectId);
                            } else {
                                onResult.onResult("");
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * 根据相等的条件, 查询数据
     *
     * @param where "key:value" 的格式
     */
    public static <T extends BmobObject> Subscription query(Class<T> cls, String where, final OnResult<T> onResult) {
        final BmobQuery<T> query = new BmobQuery<>();
        query.setLimit(500);//最大返回500条, 请查看文档的分页查询
        P.foreach(new P.OnValue() {
            @Override
            public void onValue(String key, String value) {
                query.addWhereEqualTo(key, value);
            }
        }, where);

        return query.findObjectsObservable(cls).subscribe(new RSubscriber<List<T>>() {
            @Override
            public void onSucceed(List<T> bean) {
                super.onSucceed(bean);
                onResult.onResult(bean);
            }

            @Override
            public void onError(int code, String msg) {
                //super.onError(code, msg);
                onResult.onResult(null);
            }
        });
    }

    /**
     * 删除满足查询条件的所有数据
     */
    public static <T extends BmobObject> Subscription delete(Class<T> cls, String where, final OnResult<T> onResult) {
        return query(cls, where, new OnResult<T>() {
            @Override
            public void onResult(final List<T> resultList) {
                if (RUtils.isListEmpty(resultList)) {
                    //删除失败, 参数为null
                    onResult.onResult(null);
                } else {
                    for (T item : resultList) {
                        item.delete(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    //删除成功, 返回被删除的数据
                                    onResult.onResult(resultList);
                                } else {
                                    onResult.onResult(null);
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    interface OnResult<T> {
        void onResult(List<T> resultList);
    }

    interface OnSingleResult<T> {
        void onResult(T result);
    }

    public static class RFindListener<T> extends FindListener<T> {

        @Override
        public void done(List<T> resultList, BmobException exception) {

        }
    }
}
