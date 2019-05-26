package com.ljb.utils.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Author      :meloon
 * Date        :2018/11/1
 * Description : 关闭IO流
 */
public class Colseable {
    /**
     * 关闭IO流
     *
     */
    public static void closeIO(final Closeable... closeables) {
        if (closeables == null) return;
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
