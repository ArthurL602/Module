package com.ljb.baselibrary.network.okhttp;

import android.util.Log;

import com.ljb.baselibrary.network.Utils.AndroidPlatform;
import com.ljb.baselibrary.network.Utils.NetUtils;
import com.ljb.baselibrary.network.exception.ApiException;
import com.ljb.baselibrary.network.okhttp.callback.DownCallBack;
import com.ljb.baselibrary.network.okhttp.callback.EngineCallBack;
import com.ljb.baselibrary.network.okhttp.callback.UploadCallBack;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Author      :ljb
 * Date        :2018/4/7
 * Description : okhttp默认的引擎
 * : 备注： 监听上传文件和 下文文件时 ，需要用个新的okhttpclient 或者 注释掉代码里的okhttpclient请求打印log
 */
class OkHttpEngine implements IHttpEngine {
    private static OkHttpClient mOkHttpClient;
    private AndroidPlatform mAndroidPlatform;

    public OkHttpEngine() {
        mAndroidPlatform = AndroidPlatform.get();
    }


    static {
        mOkHttpClient = new OkHttpClient.Builder()//

                .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Log.e("TAG", message);
                    }
                }).setLevel(HttpLoggingInterceptor.Level.BODY))//
                .build();
    }

    public void setClient(OkHttpClient client) {
        mOkHttpClient = client;
    }

    /**
     * get请求
     *
     * @param tag
     * @param url
     * @param params
     * @param cache
     * @param cacheControl
     * @param callBack
     * @param <T>
     */
    @Override
    public <T> void get(final Object tag, String url, Map<String, Object> params, boolean cache, CacheControl
            cacheControl, final EngineCallBack<T> callBack) {
        // 添加请求参数
        url = NetUtils.joinParams(url, params);
        final Request request = createRequest(tag, url, cache, cacheControl);
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mAndroidPlatform.defaultCallbackExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onError(e);
                    }
                });
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                if (callBack instanceof DownCallBack) { // 监听下载文件在子线程
                    parseResponse(response, callBack);
                } else {
                    mAndroidPlatform.defaultCallbackExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            parseResponse(response, callBack);
                        }
                    });
                }
            }
        });
    }

    /**
     * 解析 Response
     *
     * @param response
     * @param callBack
     * @param <T>
     */
    private <T> void parseResponse(final Response response, final EngineCallBack<T> callBack) {
        if (response.isSuccessful()) {
            try {
                callBack.onSuccess(callBack.parseNetworkResponse(response));
            } catch (final Exception e) {
                mAndroidPlatform.defaultCallbackExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onError(e);
                    }
                });
            }

        } else {
            mAndroidPlatform.defaultCallbackExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    callBack.onError(new ApiException("网络请求异常: code: " + response.code()));
                }
            });
        }
    }


    @Override
    public <T> void postForm(Object tag, String url, Map<String, Object> params, final EngineCallBack<T> callBack) {
        RequestBody requestBody = createFormBody(params);
        Request.Builder builder = new Request.Builder()//
                .url(url)//
                .post(requestBody);//
        if (tag != null) {
            builder.tag(tag);
        }
        Request request = builder.build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mAndroidPlatform.defaultCallbackExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onError(e);
                    }
                });

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (callBack instanceof DownCallBack) {
                    parseResponse(response, callBack);
                } else {
                    mAndroidPlatform.defaultCallbackExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            parseResponse(response, callBack);
                        }
                    });
                }
            }
        });
    }

    /**
     * 提交其他类型的body （eg json xml）
     *
     * @param tag
     * @param url
     * @param requestBody
     * @param callBack
     * @param <T>
     */
    @Override
    public <T> void postOther(Object tag, String url, RequestBody requestBody, final EngineCallBack<T> callBack) {
        Request.Builder builder = new Request.Builder()//
                .url(url)//
                .post(requestBody);//
        if (tag != null) {
            builder.tag(tag);
        }
        Request request = builder.build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mAndroidPlatform.defaultCallbackExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onError(e);
                    }
                });

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (callBack instanceof DownCallBack) {
                    parseResponse(response, callBack);
                } else {
                    mAndroidPlatform.defaultCallbackExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            parseResponse(response, callBack);
                        }
                    });
                }
            }
        });
    }


    @Override
    public <T> void postFile(Object tag, String url, Map<String, Object> params, final UploadCallBack callBack) {
        RequestBody multipartBody = createMultipartBody(params);
        ExMultipartBody body = new ExMultipartBody(multipartBody, callBack);
        Request.Builder builder = new Request.Builder()//
                .url(url)//
                .post(body);//
        if (tag != null) {
            builder.tag(tag);
        }
        Request request = builder.build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                mAndroidPlatform.defaultCallbackExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onError(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                mAndroidPlatform.defaultCallbackExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            try {
                                callBack.onSuccess(response.body().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                                callBack.onError(e);
                            }
                        } else {
                            callBack.onError(new ApiException("网络请求异常: code: " + response.code()));
                        }

                    }
                });
            }
        });

    }

    /**
     * 构建一个Request
     *
     * @param tag
     * @param url
     * @param cache
     * @param cacheControl
     * @return
     */
    private Request createRequest(Object tag, String url, boolean cache, CacheControl cacheControl) {
        // 构建Request
        Request.Builder builder = new Request.Builder()//
                .url(url);//
        if (tag != null) {
            builder.tag(tag);
        }
        Request request = null;
        if (!cache) { // 不需要缓存,都不存储
            CacheControl control = new CacheControl.Builder()//
                    .noStore()//
                    .build();
            request = builder//
                    .cacheControl(control)//
                    .build();
        } else { // 需要缓存
            if (cacheControl == null) { // 用户设置要缓存，然后又没有给缓存策略，则策列设置 max  = 0
                request = builder//
                        .cacheControl(new CacheControl.Builder()//
                                .maxAge(0, TimeUnit.SECONDS)//
                                .build())//
                        .build();
            } else {
                request = builder//
                        .cacheControl(cacheControl)//
                        .build();
            }
        }
        return request;
    }


    /**
     * 创建form表单body
     *
     * @param params
     * @return
     */
    private RequestBody createFormBody(Map<String, Object> params) {
        if (params != null) {
            FormBody.Builder builder = new FormBody.Builder();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if (entry.getValue() instanceof String) {
                    builder.add(entry.getKey(), (String) entry.getValue());
                }
            }
            return builder.build();
        }
        return null;
    }

    /**
     * 构建复杂请求体
     *
     * @param params
     * @return
     */
    private RequestBody createMultipartBody(Map<String, Object> params) {
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
