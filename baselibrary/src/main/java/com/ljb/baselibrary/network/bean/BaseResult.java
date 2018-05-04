package com.ljb.baselibrary.network.bean;

/**
 * Author      :meloon
 * Date        :2018/5/3
 * Description :
 */

public class BaseResult<T> {
    private String msg;
    private String code;
    private Object data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isOk() {
        return code.equals("0000");
    }
}
