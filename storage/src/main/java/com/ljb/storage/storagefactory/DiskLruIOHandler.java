package com.ljb.storage.storagefactory;

import android.content.Context;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Author      :ljb
 * Date        :2018/8/3
 * Description : 磁盘缓存
 */
public class DiskLruIOHandler extends AbstractDiskIOHandler {
    private DiskLruCache mDiskLruCache;

    public DiskLruIOHandler(Context context) {
        File dir = context.getExternalFilesDir("DiskLruCache");
        int MAX_SIZE = 10 * 1024 * 1024;//10MB
        try {
            mDiskLruCache = DiskLruCache.open(dir, 1, 1, MAX_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(String key, InputStream inputStream) {
        if (mDiskLruCache == null) return;
        OutputStream outputStream;
        try {
            DiskLruCache.Editor edit = mDiskLruCache.edit(key);
            if (edit != null) {
                outputStream = edit.newOutputStream(0);
                if (write(outputStream, inputStream)) {
                    edit.commit();
                } else {
                    edit.abort();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public InputStream get(String key) {
        if (mDiskLruCache == null) return null;
        try {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
            if (snapshot != null) {
                return snapshot.getInputStream(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void clear(String key) {
        if(mDiskLruCache==null) return;
        try {
            mDiskLruCache.remove(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private boolean write(OutputStream outputStream, InputStream inputStream) {
        if (outputStream == null || inputStream == null) return false;
        try {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            cloase(inputStream);
            cloase(outputStream);
        }
        return false;
    }

    private void cloase(Closeable io) {
        if (io != null) {
            try {
                io.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
