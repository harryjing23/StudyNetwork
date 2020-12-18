package com.harry.studynetwork;

import android.os.Environment;
import android.util.Log;

import com.harry.studynetwork.okhttp.LoggingInterceptor;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Harry Jing
 * @date 12/6/20
 */
public class Test_OkHttp3 {
    private static final String TAG = "Test_OkHttp3";
    public static final String URL = "http://git.cashwayinfo.com/jinghoulin/BaiduFaceOfflineSdkAndroid/raw/master/.gitignore";


    public static void read() {
        File cacheFile = new File(Environment.getExternalStorageDirectory() + "/okhttp_cache");
        Cache cache = new Cache(cacheFile, 50L * 1024L * 1024L);// 50MB
        // 设置缓存策略，默认不缓存
//        OkHttpClient client = new OkHttpClient.Builder().cache(cache).build();


        OkHttpClient client = new OkHttpClient();
        // 若是POST请求，需要再post(RequestBody)
        Request request = new Request.Builder()
                .url(URL)
//                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            Log.d(TAG, "read: " + string);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void read1() {
        // 添加application拦截器
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new LoggingInterceptor()).build();
        Request request = new Request.Builder().url("http://www.publicobject.com/helloworld.txt")
                .header("User-Agent", "OkHttp Example")
                .build();// url会重定向到https的地址，并且拦截器中返回的响应chain.proceed()具有重定向的响应

        try {
            Response response = client.newCall(request).execute();
            response.body().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void read2() {
        // 添加network拦截器
        // 该network拦截器会执行两次，第一次为初始请求，第二次为重定向
        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new LoggingInterceptor())
                .build();

        Request request = new Request.Builder()
                .url("http://www.publicobject.com/helloworld.txt")
                .header("User-Agent", "OkHttp Example")
                .build();

        try {
            Response response = client.newCall(request).execute();
            response.body().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
