package com.ljb.baselibrary.network.retrofit;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author      :meloon
 * Date        :2018/5/4
 * Description : Retrofit
 */

public class RetrofitClient {
    private Retrofit mRetrofit;
    private static RetrofitClient sRetrofitClient;
    public String baseUrl = "http://192.168.199.187:8080/";

    private RetrofitClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()//
                .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Log.e("TAG", message);
                    }
                }).setLevel(HttpLoggingInterceptor.Level.BODY))//
                .build();
        mRetrofit = new Retrofit.Builder()//
                .addConverterFactory(GsonConverterFactory.create())//
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//
                .baseUrl(baseUrl)//
                .client(okHttpClient)//
                .build();

    }

    public void retrofit(Retrofit retrofit) {
        mRetrofit = retrofit;
    }

    public static RetrofitClient getInstance() {
        if (sRetrofitClient == null) {
            synchronized (RetrofitClient.class) {
                if (sRetrofitClient == null) {
                    sRetrofitClient = new RetrofitClient();
                }
            }
        }
        return sRetrofitClient;
    }

    public <T> T create(Class<T> clz) {
        return mRetrofit.create(clz);
    }
}
