package com.bjut.cyl.kfyrip.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.bjut.cyl.kfyrip.bean.ExtraData;
import com.bjut.cyl.kfyrip.ui.MainActivity;
import com.bjut.cyl.kfyrip.ui.NewsDetailsActivity;
import com.bjut.cyl.kfyrip.ui.QnADetailsActivity;
import com.google.gson.Gson;

import cn.jpush.android.api.JPushInterface;

/**
 * 作者：haoran   on https://github.com/woaigmz 2017/6/5.
 * 邮箱：1549112908@qq.com
 * 说明：
 */

public class MyReceiver extends BroadcastReceiver {

    private ExtraData extraData;


    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regID = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d("info", "[MyReceiver] 接收Registration Id : " + regID);
            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
       // bundle.getString(JPushInterface.E);
            String extra_msg = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String extra_content = bundle.getString(JPushInterface.EXTRA_CONTENT_TYPE);
            Log.d("info", "[ACTION_REGISTRATION_ID] 接收到推送下来的通知的extra: " + extra);
            Log.d("info", "[ACTION_REGISTRATION_ID] 接收到推送下来的通知的extra_msg: " + extra_msg);
            Log.d("info", "[ACTION_REGISTRATION_ID] 接收到推送下来的通知的extra_content: " + extra_content);
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
            String extra_msg = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String extra_content = bundle.getString(JPushInterface.EXTRA_CONTENT_TYPE);
            Log.d("info", "[ACTION_MESSAGE_RECEIVED] 接收到推送下来的通知的extra: " + extra);
            Log.d("info", "[ACTION_MESSAGE_RECEIVED] 接收到推送下来的通知的extra_msg: " + extra_msg);
            Log.d("info", "[ACTION_MESSAGE_RECEIVED] 接收到推送下来的通知的extra_content: " + extra_content);
            Log.d("info", "[ACTION_MESSAGE_RECEIVED] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//                processCustomMessage(ctx, bundle);
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {

            Log.d("info", "[ACTION_NOTIFICATION_RECEIVED] 接收到推送下来的通知");

            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
            //String extra_msg = bundle.getString(JPushInterface.EXTRA_MESSAGE);
           // String extra_content = bundle.getString(JPushInterface.EXTRA_CONTENT_TYPE);
            Log.d("info", "[ACTION_NOTIFICATION_RECEIVED] 接收到推送下来的通知的extra: " + extra);
           // Log.d("info", "[ACTION_NOTIFICATION_RECEIVED] 接收到推送下来的通知的extra_msg: " + extra_msg);
          //  Log.d("info", "[ACTION_NOTIFICATION_RECEIVED] 接收到推送下来的通知的extra_content: " + extra_content);
            Log.d("info", "[ACTION_NOTIFICATION_RECEIVED] 接收到推送下来的通知的ID: " + notifactionId);
            extraData = new Gson().fromJson(extra, ExtraData.class);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
            //String extra_msg = bundle.getString(JPushInterface.EXTRA_MESSAGE);
           // String extra_content = bundle.getString(JPushInterface.EXTRA_CONTENT_TYPE);
            Log.d("info", "[ACTION_NOTIFICATION_OPENED] 接收到推送下来的通知的extra: " + extra);
          //  Log.d("info", "[ACTION_NOTIFICATION_OPENED] 接收到推送下来的通知的extra_msg: " + extra_msg);
          //  Log.d("info", "[ACTION_NOTIFICATION_OPENED] 接收到推送下来的通知的extra_content: " + extra_content);
            Log.d("info", "[ACTION_NOTIFICATION_OPENED] 用户点击打开了通知");
            JPushInterface.reportNotificationOpened(context, bundle.getString(JPushInterface.EXTRA_MSG_ID));

//                //打开自定义的Activity

            if (extraData.type.equals("answer")) {
                Intent i = new Intent(context, QnADetailsActivity.class);
                intent.putExtra("id", extraData.id);
                i.putExtras(bundle);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            } else {
                Intent i = new Intent(context, NewsDetailsActivity.class);
                intent.putExtra("id", extraData.id);
                i.putExtras(bundle);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }

        }
    }
}
