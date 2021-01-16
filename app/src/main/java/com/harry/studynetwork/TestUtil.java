package com.harry.studynetwork;

import android.util.Log;

import com.harry.studynetwork.util.OkHttpUtil;

/**
 * @author Harry
 * @date 2021/1/6
 */
public class TestUtil {
    private static final String TAG = "TestUtil";

    public static void testGet() {
        Log.d(TAG, "testGet1: " + Thread.currentThread().getId());
        OkHttpUtil.request().url(Test_OkHttp3.URL).addHeader("name", "value")
                .get(new OkHttpUtil.UtilCallback() {
                    @Override
                    public void succeeded() {
                        Log.d(TAG, "testGet2: " + Thread.currentThread().getId());
                    }

                    @Override
                    public void failed() {
                        Log.d(TAG, "testGet2: " + Thread.currentThread().getId());
                    }
                });
    }

    public static void testPost() {
        OkHttpUtil.request().url(Constant.CHANNEL_URL).addHeader(Test_HttpURLConnection.Channel.getHeader())
                .post("data", new OkHttpUtil.UtilCallback() {
                    @Override
                    public void succeeded() {

                    }

                    @Override
                    public void failed() {

                    }
                });
    }
}
