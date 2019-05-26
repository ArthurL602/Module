package com.ljb.netmodule.network.okhttp;

import android.os.Handler;
import android.os.Looper;


import com.ljb.netmodule.network.okhttp.callback.UploadCallBack;

import java.io.IOException;

import io.reactivex.annotations.Nullable;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * Author      :meloon
 * Date        :2018/5/4
 * Description :
 */

public class ExMultipartBody extends RequestBody {
    private RequestBody mRequestBody;
    private UploadCallBack mUploadCallBack;
    private long mCurrentLength;
    private Handler mHandler;
    private long mContentLength;
    private BufferedSink mBufferedSink;

    public ExMultipartBody(RequestBody requestBody, UploadCallBack uploadCallBack) {
        mRequestBody = requestBody;
        mUploadCallBack = uploadCallBack;
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public long contentLength() throws IOException {
        return mRequestBody.contentLength();
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return mRequestBody.contentType();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        mContentLength = contentLength();
        mBufferedSink = Okio.buffer(sink(sink));
        mRequestBody.writeTo(mBufferedSink);
        mBufferedSink.flush();
    }

    private Sink sink(BufferedSink sink) {
        return new ForwardingSink(sink) {
            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                mCurrentLength += byteCount;
                if (mUploadCallBack != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mUploadCallBack.progress(mCurrentLength, mContentLength);
                        }
                    });
                }
            }
        };
    }


}
