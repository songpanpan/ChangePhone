package com.spp.xposeddemo.net;

public class ErrorBean {

	public String msg="";
	public String error="";
	
	public ErrorBean(String error,String msg){
		this.error = error;
		this.msg=msg;
	}
	@Override
	public String toString() {
		// 2013-3-12 上午11:23:43
		return "ErrorBean[msg:"+msg+" error:"+error+"]";
	}
}
