package com.harry.studynetwork.retrofit;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author Harry
 * @date 2021/1/8
 */
public class RetrofitTest {
    private static final String TAG = "RetrofitTest";

    private static Retrofit sRetrofit;

    public static void init() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.addConverterFactory(JacksonConverterFactory.create());
//        builder.addCallAdapterFactory();// 添加rxjava
        builder.baseUrl("https://api.github.com/");
        builder.client(clientBuilder.build());// 配置OkHttpClient
        sRetrofit = builder.build();
    }

    public static void test() {
        GitHubService service = sRetrofit.create(GitHubService.class);
        service.listRepos("123").enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "onResponse: " + response);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }


    public interface GitHubService {

        @GET("users/{user}/repos")
        Call<String> listRepos(@Path("user") String user);
    }
}