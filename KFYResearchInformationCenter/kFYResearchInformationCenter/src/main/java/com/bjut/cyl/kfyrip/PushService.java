package com.bjut.cyl.kfyrip;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.bjut.cyl.kfyrip.ui.LoginActivity;
import com.bjut.cyl.kfyrip.ui.MainActivity;
import com.bjut.cyl.kfyrip.ui.MyApplication;
import com.bjut.cyl.kfyrip.ui.NotificationDetailsActivity;
import com.bjut.cyl.kfyrip.ui.R;
import com.bjut.cyl.kfyrip.utils.ConfigUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.bitmap.factory.BitmapFactory;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PushService extends Service {

    private List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> mDataOk;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String thisTime;
    private String lastTime;
    private MainActivity mainActivity;

    public PushService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {

        super.onCreate();

        mainActivity = MainActivity.getInstance();
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();
        editor.commit();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                //通知
                testPost("0", "10",ConfigUtil.CHANNEL_ID_NOTIFICATION);

            }
        }).start();

        //定时
        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        //int anHour = 60 * 60 * 1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + 30 * 1000;
        Intent i = new Intent(this, PushService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return START_STICKY_COMPATIBILITY;
        //return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void testPost(String offset, String pagesize, String channel_id) {


        //Toast.makeText(MainActivity.getInstance(), "XXX", Toast.LENGTH_SHORT).show();
        //获取当前时间thisTime
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        thisTime = dateformat.format(System.currentTimeMillis());
        //获取文件中存储的时间lastTime(第一次时存入lasttime为当前时间前一天)
        //lastTime = pref.getString("time", thisTime);
        lastTime = pref.getString("time", "2017-12-01 00:00:00");
        lastTime = "2017-12-01 00:00:00";
        RequestParams params = new RequestParams();
        params.addBodyParameter("offset", offset);
        params.addBodyParameter("pagesize", pagesize);
        params.addBodyParameter("channel_id", channel_id);

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, ConfigUtil.MY_SERVICE_URL
                + "getInfoList.php", params, new RequestCallBack<String>() {

            @Override
            public void onStart() {

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                // resultText.setText(current + "/" + total);
            }

            @Override
            public void onSuccess(final ResponseInfo<String> responseInfo) {
                // resultText.setText("upload response:" + responseInfo.result);
                //System.out.println(responseInfo.result);

                //UpdateNoticeActivity.instance
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        ObjectMapper objectMapper = new ObjectMapper();
                        String json = responseInfo.result;
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(json);
                            int code = jsonObject.getInt("code");
                            if (code == 210) {

                                mDataOk = getData(jsonObject);// 填充数据
                                //System.out.println("mdataok" + mDataOk);

                                if (mDataOk.size() >= 0) {
                                    mData.addAll(mDataOk);
                                    mDataOk.clear();
                                    //System.out.println(mData);

                                }

                                sendNotice("访问通知接口","返回210",100);
                                //判断发布的时间是否在thisTime与lastTime之间
                                //如果在，发布通知

                                int i = 1;
                                for (Map<String, Object> m : mData)
                                {
                                    for (String k : m.keySet())
                                    {
                                        System.out.println(k + " : " + m.get(k));
                                        //筛选新发布的通知，将该条通知信息作为参数
                                        String noticeTime = String.valueOf(m.get("time"));
                                        if(noticeTime.compareTo(lastTime) > 0 && noticeTime.compareTo(thisTime) < 0){

                                            sendNotice(String.valueOf(m.get("title")), String.valueOf(m.get("summary")), i);
                                            i++;
                                        }
                                        sendNotice("本次更新时间：", thisTime, 199);
                                        sendNotice("上次更新时间：", lastTime, 200);

                                    }

                                }

                                //记录时间
                                editor.putString("time", thisTime);
                                editor.commit();

                            }else if (code == 310) {

                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                });
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                sendNotice("本次更新时间：", thisTime, 199);
                sendNotice("上次更新时间：", lastTime, 200);
            }
        });
    }


    private List<Map<String, Object>> getData(JSONObject jsonObject) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            JSONArray jsonArray = jsonObject.getJSONObject("result").getJSONArray("info.list");
            for (int i = 0; i < jsonArray.length(); i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("title", jsonArray.getJSONObject(i).getString("title"));
                map.put("summary", jsonArray.getJSONObject(i).getString("id"));
                map.put("time", jsonArray.getJSONObject(i).getString("last_modify_time"));
                map.put("view_num", jsonArray.getJSONObject(i).getString("view_num"));
                map.put("comment_num", jsonArray.getJSONObject(i).getString("comment_num"));
                list.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void sendNotice(String contentTitle,String contentText, int i){

        //Intent intent = new Intent(this, NotificationDetailsActivity.class);
        Intent intent = new Intent(this, LoginActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        final Bitmap largeIcon = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_launcher)).getBitmap();
        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(largeIcon)
                //.setContentIntent(pi)
                .setAutoCancel(true)
                .build();
        manager.notify(i, notification);
    }

}
