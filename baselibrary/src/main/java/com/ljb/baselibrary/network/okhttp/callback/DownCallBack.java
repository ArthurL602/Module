package com.ljb.baselibrary.network.okhttp.callback;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.ljb.baselibrary.network.Utils.NetUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;

import okhttp3.Response;

/**
 * Author      :meloon
 * Date        :2018/5/3
 * Description :
 */

public abstract class DownCallBack extends EngineCallBack {
    private String path;
    private int downSize;
    private final MainHandler mMainHandler;


    public DownCallBack(String path, int downSize) {
        this.path = path;
        this.downSize = downSize;
        mMainHandler = new MainHandler(this);
    }


    @Override
    public InputStream parseNetworkResponse(Response response) throws Exception {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        File file = new File(path);
        if (!file.exists()) {// 如果文件不存在
            file.createNewFile();
        }
        long totalLength = Long.valueOf(response.header("Content-Length"));//总共长度
        Message message = Message.obtain();
        message.what = MainHandler.BEFORE;
        message.arg1 = (int) totalLength;
        mMainHandler.sendMessage(message);
        try {
            long curLength = 0;// 当前下载量
            inputStream = response.body().byteStream();
            outputStream = new FileOutputStream(file);
            int len;
            byte[] buff = new byte[downSize];
            while ((len = inputStream.read(buff)) != -1) {
                outputStream.write(buff, 0, len);
                curLength += len;
                message = Message.obtain();
                message.what = MainHandler.DOWNING;
                message.arg1 = (int) totalLength;
                message.arg2 = (int) curLength;
                mMainHandler.sendMessage(message);
            }
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            NetUtils.close(inputStream);
            NetUtils.close(outputStream);
        }
        message = Message.obtain();
        message.what = MainHandler.COMPLETE;
        mMainHandler.sendMessage(message);
        return response.body().byteStream();
    }


    public abstract void before(long total);

    public abstract void downing(long total, long current);

    public abstract void complete();

    @Override
    public void onSuccess(Object response) {

    }

    public static class MainHandler extends Handler {
        WeakReference<DownCallBack> mReference;
        public static final int DOWNING = 0x000001;
        public static final int COMPLETE = 0x000002;
        public static final int BEFORE = 0x000003;

        public MainHandler(DownCallBack downCallBack) {
            super(Looper.getMainLooper());
            mReference = new WeakReference<DownCallBack>(downCallBack);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mReference.get() != null) {
                switch (msg.what) {
                    case DOWNING:
                        mReference.get().downing(msg.arg1, msg.arg2);
                        break;
                    case COMPLETE:
                        mReference.get().complete();
                        break;
                    case BEFORE:
                        mReference.get().before(msg.arg1);
                        break;
                }
            }
        }
    }
}
