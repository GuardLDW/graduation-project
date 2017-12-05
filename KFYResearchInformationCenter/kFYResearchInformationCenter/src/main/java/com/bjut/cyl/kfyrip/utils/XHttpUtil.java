package com.bjut.cyl.kfyrip.utils;

import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * Created by Che on 16/6/8.
 */
public class XHttpUtil {

    public static void sendHttpRequest(String httpUrl, RequestParams params, final int successCode, final int errorCode, final XHttpCallbackListener listener) {
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, ConfigUtil.MY_SERVICE_URL
                + httpUrl, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(final ResponseInfo<String> responseInfo) {
                //System.out.println(responseInfo.result);
                String jsonString = responseInfo.result;
                com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(jsonString);
                int code = jsonObject.getInteger("code");
                if (code == successCode) {
                    if (listener != null) {
                        listener.onSuccess(jsonString, jsonObject);
                    }
                } else if (code == errorCode) {
                    if (listener != null) {
                        listener.onCodeError(jsonString, jsonObject);
                    }
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                PromptUtil.showToast("请检查网络连接");
            }
        });
    }

    public static void postHttpRequest(String httpUrl, RequestParams params, final int successCode, final int errorCode, final XHttpCallbackListener listener) {
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.GET,
                httpUrl, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(final ResponseInfo<String> responseInfo) {
                //System.out.println(responseInfo.result);
                String jsonString = responseInfo.result;
                JSONObject jsonObject = JSON.parseObject(jsonString);
                int code = jsonObject.getInteger("code");
                if (code == successCode) {
                    if (listener != null) {
                        listener.onSuccess(jsonString, jsonObject);
                    }
                } else if (code == errorCode) {
                    if (listener != null) {
                        listener.onCodeError(jsonString, jsonObject);
                    }
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                PromptUtil.showToast("请检查网络连接");
            }
        });
    }
}
