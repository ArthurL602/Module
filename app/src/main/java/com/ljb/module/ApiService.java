package com.ljb.module;

import com.ljb.baselibrary.network.bean.BaseResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Author      :meloon
 * Date        :2018/5/4
 * Description :
 */

public interface ApiService {
    @GET("OkHttpServlet/LoginServlet")
    Observable<BaseResult<UserInfo>> login(@Query("userName") String userName);

}
