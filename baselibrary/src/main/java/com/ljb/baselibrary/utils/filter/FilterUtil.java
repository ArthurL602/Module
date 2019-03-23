package com.ljb.baselibrary.utils.filter;

/**
 * @author :ljb
 * @date :2019/3/23
 * @description : 过滤工具类
 */
public class FilterUtil {
    private FilterChain chain;
    private FilterUtil() {
        chain=new FilterChain();
    }

    public static FilterUtil init(){
        return new FilterUtil();
    }

    public FilterUtil addFilter(Filter fileter){
        chain.addFilter(fileter);
        return this;
    }

    public String filter(){
        return chain.doFilter(chain);
    }
}
