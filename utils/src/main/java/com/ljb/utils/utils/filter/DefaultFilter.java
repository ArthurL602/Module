package com.ljb.utils.utils.filter;
/**
 * @author       :ljb
 * @date         :2019/3/23
 * @description :具体处理者
 */
public class DefaultFilter implements Filter{
	
	private boolean result;
	private String message;
	

	public DefaultFilter(boolean result, String message) {
		super();
		this.result = result;
		this.message = message;
	}



	@Override
	public String doFilter( FilterChain chain) {
		if (!result) {
			return chain.doFilter( chain);
		}
		return message;
	}

}
