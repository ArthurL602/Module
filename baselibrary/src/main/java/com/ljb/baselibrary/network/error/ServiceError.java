package com.ljb.baselibrary.network.error;

/**
 * Author      :meloon
 * Date        :2018/5/3
 * Description : 数据返回错误
 */

public class ServiceError extends Throwable{
    public ServiceError(String message) {
        super(message);
    }
}
