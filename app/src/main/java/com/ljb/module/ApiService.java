package com.ljb.module;

import com.ljb.baselibrary.network.bean.BaseResult;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Author      :meloon
 * Date        :2018/5/4
 * Description :
 */

public interface ApiService {
    @Headers("baseUrl:aaa")
    @GET("OkHttpServlet/LoginServlet")
    Observable<BaseResult<UserInfo>> login(@Query("userName") String userName);
    @Headers("baseUrl:bb")
    @GET("wallpaper/e/584a7f69e9d4d.jpg")
    Observable<ResponseBody> a();

}
