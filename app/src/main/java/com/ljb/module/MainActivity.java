package com.ljb.module;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.ljb.baselibrary.db.DaoSupportFactory;
import com.ljb.baselibrary.db.DbSupport;
import com.ljb.baselibrary.network.okhttp.ExMultipartBody;
import com.ljb.baselibrary.network.okhttp.HttpUtils;
import com.ljb.baselibrary.network.okhttp.callback.DownCallBack;
import com.ljb.baselibrary.network.okhttp.callback.JsonCallBack;
import com.ljb.baselibrary.network.okhttp.callback.UploadCallBack;
import com.ljb.baselibrary.network.okhttp.intercepter.CacheInterceptor;
import com.ljb.baselibrary.network.okhttp.intercepter.NetWorkCacheInterceptor;
import com.ljb.baselibrary.network.retrofit.RxHelper;
import com.ljb.baselibrary.network.retrofit.RxHttpUtils;
import com.ljb.module.db.DefineUpgrade;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;

public class MainActivity extends AppCompatActivity {
    //    private String url = "http://192.168.199.187:8080/OkHttpServlet/LoginServlet";
    //    private String url = "http://sqdd.myapp.com/myapp/qqteam/tim/down/tim.apk";
    private String url = "http://pic1.win4000.com/wallpaper/e/584a7f69e9d4d.jpg";
    private String url2 = "https://api.saiwuquan.com/api/upload";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView tv = (TextView) findViewById(R.id.tv_test);
        DefineUpgrade defineUpgrade = new DefineUpgrade();
        String rootPath = "/data/data/" + getPackageName() + "/databases/";
        String dbName = "my.db";
        DbSupport dbSupport = DaoSupportFactory.getInstance().createDb(rootPath, dbName, 1, defineUpgrade);
        dbSupport.getDao(UserInfo.class);

//        f();
//        c(tv);
//        b(file);
//        a(map);
//        d();
    }

    private void f() {
        RxHttpUtils.with()//
                .addBaseUrl("bb", "http://pic1.win4000.com")//
                .create(ApiService.class)//
                .a()//
                .compose(RxHelper.<ResponseBody>transformer())//
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ResponseBody responseBody) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void d() {
        CacheControl cacheControl = new CacheControl.Builder()//
                .maxAge(10, TimeUnit.SECONDS)//
                .maxStale(60, TimeUnit.SECONDS)//
                .build();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()//
                .cache(new Cache(getCacheDir(), 1024 * 1024810))//
                .addInterceptor(new CacheInterceptor(this))//
                .addNetworkInterceptor(new NetWorkCacheInterceptor(this))//
                .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Log.e("TAG", message);
                    }
                }).setLevel(HttpLoggingInterceptor.Level.BODY))

                .build();

        HttpUtils.with(this).client(okHttpClient).isCache(true).cacheControl(cacheControl).get().url(url).addParam
                ("userName", "Ljb").execute(new JsonCallBack<UserInfo>() {
            @Override
            public void onSuccessful(UserInfo userInfo) {
                Log.e("TAG", "onSuccessful: " + userInfo.toString());
            }

            @Override
            public void onError(Throwable e) {

            }
        });


    }

    private void c(final TextView tv) {
        Map<String, Object> map = new HashMap<>();
        File file = new File("data/app/", "com.kingroot.kinguser-1.apk");
        map.put("file", file);
        HttpUtils.with(this).url(url).client(new OkHttpClient()).execute(new DownCallBack(getCacheDir()
                .getAbsolutePath() + "/a.apk", 2048) {
            @Override
            public void before(long total) {
                Log.e("TAG", "before: " + total + "  " + Thread.currentThread().getName());
            }

            @Override
            public void downing(long total, long current) {
                Log.e("TAG", "downing: " + current + "  " + Thread.currentThread().getName());
                tv.setText(current + "/" + total);
            }

            @Override
            public void complete() {
                Log.e("TAG", "complete: " + "  " + Thread.currentThread().getName());
            }

            @Override
            public void onError(Throwable e) {
                Log.e("TAG", "onError: " + "  " + Thread.currentThread().getName());
            }
        });
    }

    private void b(File file) {
        MultipartBody multipartBody = new MultipartBody.Builder()//
                .setType(MultipartBody.FORM)//
                .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse(guessMimeType(file
                        .getAbsolutePath())), file))//
                .build();
        ExMultipartBody body = new ExMultipartBody(multipartBody, new UploadCallBack() {
            @Override
            public void progress(long currentLength, long total) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSuccess(String response) {

            }
        });
        Request request = new Request.Builder()//
                .url(url2)//
                .post(body)//
                .tag(this)//
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    private void a(Map<String, Object> map) {
        HttpUtils.with(this).postFile().url(url2).addParams(map).execute(new UploadCallBack() {
            @Override
            public void progress(long currentLength, long total) {
                Log.e("TAG", "progress: " + currentLength);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSuccess(String response) {

            }
        });
    }
}
