package com.ljb.baselibrary.http.okhttp;

import android.content.Context;
import android.util.Log;

import com.ljb.baselibrary.http.EngineCallBack;
import com.ljb.baselibrary.http.HttpUtils;
import com.ljb.baselibrary.http.IHttpEngine;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Author      :ljb
 * Date        :2018/4/7
 * Description : okhttp默认的引擎
 */
public class OkHttpEngine implements IHttpEngine {
    private static OkHttpClient mOkHttpClient = new OkHttpClient();

    @Override
    public void get(Context context,String url, Map<String, Object> params, final EngineCallBack callBack) {
        url = HttpUtils.joinParams(url, params);
        Log.e("TAG", "url: " + url);
        Request.Builder builder = new Request.Builder()//
                .url(url)//
                .tag(context);
        Request request = builder.build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                callBack.onSuccess(result);
            }
        });
    }


    @Override
    public void post(Context context,String url, Map<String, Object> params, final EngineCallBack callBack) {
        RequestBody requestBody = appendBody(params);
        Request request = new Request.Builder()//
                .url(url)//
                .tag(context)//
                .post(requestBody)//
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                callBack.onSuccess(result);
            }
        });
    }

    /**
     * 组装post请求参数body
     *
     * @param params
     * @return
     */
    private RequestBody appendBody(Map<String, Object> params) {
        MultipartBody.Builder builder = new MultipartBody.Builder()//
                .setType(MultipartBody.FORM);
        addParams(builder, params);

        return builder.build();
    }

    /**
     * 添加参数
     *
     * @param builder
     * @param params
     */
    private void addParams(MultipartBody.Builder builder, Map<String, Object> params) {
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.addFormDataPart(key, params.get(key) + "");
                Object value = params.get(key);
                if (value instanceof File) {
                    // 处理文件 --> Object File
                    File file = (File) value;
                    builder.addFormDataPart(key, file.getName(), RequestBody.create(MediaType.parse(guessMimeType
                            (file.getAbsolutePath())), file));
                } else if (value instanceof List) {
                    // 代表提交的是 List集合
                    try {
                        List<File> listFiles = (List<File>) value;
                        for (int i = 0; i < listFiles.size(); i++) {
                            // 获取文件
                            File file = listFiles.get(i);
                            builder.addFormDataPart(key + i, file.getName(), RequestBody.create(MediaType.parse
                                    (guessMimeType(file.getAbsolutePath())), file));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    builder.addFormDataPart(key, value + "");
                }
            }
        }
    }

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

}
