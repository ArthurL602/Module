package com.ljb.baselibrary.network.exception;

/**
 * Author      :meloon
 * Date        :2018/5/3
 * Description : 数据返回错误
 */

public class ApiException extends Throwable{
    public ApiException(String message) {
        super(message);
    }
}
