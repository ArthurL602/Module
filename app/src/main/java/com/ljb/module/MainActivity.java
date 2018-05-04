package com.ljb.module;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.ljb.baselibrary.network.callback.DownCallBack;
import com.ljb.baselibrary.network.callback.UploadCallBack;
import com.ljb.baselibrary.network.okhttp.ExMultipartBody;
import com.ljb.baselibrary.network.okhttp.HttpUtils;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    //    private String url = "http://192.168.199.187:8080/OkHttpServlet/LoginServlet";
    private String url = "http://sqdd.myapp.com/myapp/qqteam/tim/down/tim.apk";
    //    private String url = "http://pic1.win4000.com/wallpaper/e/584a7f69e9d4d.jpg";
    private String url2 = "https://api.saiwuquan.com/api/upload";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView tv = (TextView) findViewById(R.id.tv_test);
        Map<String, Object> map = new HashMap<>();
        File file = new File("data/app/", "com.kingroot.kinguser-1.apk");
        map.put("file", file);
        HttpUtils.with(this).url(url).client(new OkHttpClient()).execute(new DownCallBack(getCacheDir().getAbsolutePath() + "/a.apk", 2048) {
            @Override
            public void before(long total) {
                Log.e("TAG", "before: " + total + "  "+Thread.currentThread().getName());
            }

            @Override
            public void downing(long total, long current) {
                Log.e("TAG", "downing: "+current+ "  "+Thread.currentThread().getName());
                tv.setText(current+"/"+total);
            }

            @Override
            public void complete() {
                Log.e("TAG", "complete: "+ "  "+Thread.currentThread().getName());
            }

            @Override
            public void onError(Throwable e) {
                Log.e("TAG", "onError: "+ "  "+Thread.currentThread().getName());
            }
        });
//        b(file);
//        a(map);
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
