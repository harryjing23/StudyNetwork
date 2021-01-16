package com.harry.studynetwork.util;

import android.util.Log;


import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author Harry
 * @date 2021/1/6
 */
public class OkHttpUtil {
    private static final String TAG = "OkHttpUtil";
    private static OkHttpClient sOkHttpClient;// 不要直接访问，用getClient

    private static OkHttpClient getClient() {
        if (sOkHttpClient == null) {
            synchronized (OkHttpUtil.class) {
                if (sOkHttpClient == null) {
                    initClient();
                }
            }
        }
        return sOkHttpClient;
    }

    private static void initClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(3 * 1000, TimeUnit.SECONDS);
        builder.readTimeout(3 * 1000, TimeUnit.SECONDS);
        builder.writeTimeout(3 * 1000, TimeUnit.SECONDS);
        builder.addInterceptor(new LoggingInterceptor());
        // TODO: 2021/1/6 https, cache

        sOkHttpClient = builder.build();
    }

    public static RequestUtil request() {
        RequestUtil requestUtil = new RequestUtil();
        return requestUtil;
    }

    /**
     * 所有请求都用enqueue()异步请求。若用execute()同步请求，Android要求要在子线程中执行
     * 若用enqueue()异步请求，回调则执行在子线程中
     */
    public static class RequestUtil {
        private static final String TAG = "RequestUtil";
        public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
        private Request.Builder mRequestBuilder;

        public RequestUtil() {
            mRequestBuilder = new Request.Builder();
        }

        public RequestUtil url(String url) {
            mRequestBuilder.url(url);
            return this;
        }

        /**
         * 设置一个header
         *
         * @param name
         * @param value
         */
        public RequestUtil addHeader(String name, String value) {
            Request.Builder builder = new Request.Builder();
            builder.header(name, value);
            return this;
        }

        /**
         * 设置多个header
         *
         * @param headers
         */
        public RequestUtil addHeader(Map<String, String> headers) {
            Request.Builder builder = new Request.Builder();
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.header(entry.getKey(), entry.getValue());
            }
            return this;
        }

        public void get(UtilCallback callback) {
            OkHttpUtil.getClient().newCall(mRequestBuilder.build()).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "onFailure: " + e.getMessage());
                    if (callback != null) {
                        callback.succeeded();
                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d(TAG, "onResponse: ");
                    if (!response.isSuccessful()) {
                        Log.e(TAG, "onResponse: not isSuccessful: " + response);
                    } else {
                        ResponseBody body = response.body();
                        if (body != null) {
                            Log.d(TAG, "onResponse: " + body.string());
                        } else {
                            Log.e(TAG, "onResponse: body is null");
                        }
                    }

                    if (callback != null) {
                        callback.failed();
                    }
                }
            });
        }

        /**
         * post body为String
         *
         * @param data
         * @param callback
         */
        public void post(String data, UtilCallback callback) {
            mRequestBuilder.post(RequestBody.create(MEDIA_TYPE_MARKDOWN, data));
            OkHttpUtil.getClient().newCall(mRequestBuilder.build()).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "onFailure: " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d(TAG, "onResponse: ");
                    if (!response.isSuccessful()) {
                        Log.e(TAG, "onResponse: not isSuccessful: " + response);
                    } else {
                        ResponseBody body = response.body();
                        if (body != null) {
                            Log.d(TAG, "onResponse: " + body.string());
                        } else {
                            Log.e(TAG, "onResponse: body is null");
                        }
                    }
                }
            });
        }

        /**
         * post body为File
         *
         * @param data
         * @param callback
         */
        public void post(File data, UtilCallback callback) {
            mRequestBuilder.post(RequestBody.create(MEDIA_TYPE_MARKDOWN, data));
            OkHttpUtil.getClient().newCall(mRequestBuilder.build()).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "onFailure: " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d(TAG, "onResponse: ");
                    if (!response.isSuccessful()) {
                        Log.e(TAG, "onResponse: not isSuccessful: " + response);
                    } else {
                        ResponseBody body = response.body();
                        if (body != null) {
                            Log.d(TAG, "onResponse: " + body.string());
                        } else {
                            Log.e(TAG, "onResponse: body is null");
                        }
                    }
                }
            });
        }

        // TODO: 2021/1/6 post-> stream, form parameters, multipart request
    }

    public interface UtilCallback {
        void succeeded();

        void failed();
    }
}
