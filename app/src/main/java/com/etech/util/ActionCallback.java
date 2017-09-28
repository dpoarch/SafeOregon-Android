package com.etech.util;

public interface ActionCallback {
	//public void onActionComplete(Object obj);
	public void onActionComplete(int statusCode, String callbackString, Object res);
}
