package com.bjut.cyl.kfyrip.utils;

import com.alibaba.fastjson.JSONObject;

public interface XHttpCallbackListener {
	void onSuccess(String jsonString, JSONObject jsonObject);
	void onCodeError(String jsonString, JSONObject jsonObject);
}
