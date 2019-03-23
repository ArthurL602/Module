package com.ljb.baselibrary.utils.filter;
/**
 * @author       :ljb
 * @date         :2019/3/23
 * @description : 抽象处理者
 */
public interface Filter {
	String  doFilter(FilterChain chain);
}
