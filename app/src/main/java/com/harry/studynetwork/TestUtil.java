package com.harry.studynetwork;

import com.harry.studynetwork.util.OkHttpUtil;

/**
 * @author Harry
 * @date 2021/1/6
 */
public class TestUtil {

    public static void testGet() {
        OkHttpUtil.request().url(Test_OkHttp3.URL).addHeader("name", "value")
                .get(new OkHttpUtil.UtilCallback() {
                    @Override
                    public void succeeded() {

                    }

                    @Override
                    public void failed() {

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
