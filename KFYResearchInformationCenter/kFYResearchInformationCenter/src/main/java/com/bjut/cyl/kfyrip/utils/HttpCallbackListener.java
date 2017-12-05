package com.bjut.cyl.kfyrip.utils;

public interface HttpCallbackListener {
	void onFinish(String response);
	void onError(Exception e);
}
