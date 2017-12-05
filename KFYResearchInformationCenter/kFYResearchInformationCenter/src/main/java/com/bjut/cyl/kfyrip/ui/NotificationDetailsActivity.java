package com.bjut.cyl.kfyrip.ui;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import com.bjut.cyl.kfyrip.bean.getInfoDetailList;
import com.bjut.cyl.kfyrip.bean.getInfoDetailList.Result;
import com.bjut.cyl.kfyrip.bean.getInfoDetailList.Result.Info;
import com.bjut.cyl.kfyrip.bean.getInfoList;
import com.bjut.cyl.kfyrip.other.CommentDialog;
import com.bjut.cyl.kfyrip.other.WebAppInterface;
import com.bjut.cyl.kfyrip.utils.CalendarUtil;
import com.bjut.cyl.kfyrip.utils.ConfigUtil;
import com.bjut.cyl.kfyrip.utils.LogUtil;
import com.bjut.cyl.kfyrip.utils.OpenFiles;
import com.bjut.cyl.kfyrip.utils.PromptUtil;
import com.bjut.cyl.kfyrip.utils.ScreenUtils;
import com.bjut.cyl.kfyrip.utils.px2dp;
import com.bjut.cyl.kfyrip.view.PopupMenu.OnItemClickListener;
import com.bjut.cyl.kfyrip.view.PopupMenu;
import com.bjut.cyl.kfyrip.view.PopupMenu.MENUITEM;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class NotificationDetailsActivity extends DialogShowOffAct implements
        OnClickListener {
    private ImageButton ivTitleBtnLeft;
    private ImageView moreBtn;
    private TextView messageTitleTv, commentTv, signupTv;
    private TextView timeTv;
    private WebView contentWv;
    private CommentDialog commentDialog;
    private CalendarUtil calendarUtil;
    private SharedPreferences pref;
    private Button writeButton, commentButton;
    private String messageId, messageTitle, js;
    private LinearLayout writeLl, commentLl;
    private Button ivTitleBtnRight;
    private PopupMenu popupMenu;
    private int iscollect;
    private int signup;
    private static String calanderURL = "";
    private static String calanderEventURL = "";
    private static String calanderRemiderURL = "";
    private ImageView tzIv;

    //
    static {
        if (Integer.parseInt(Build.VERSION.SDK) >= 8) {
            calanderURL = "content://com.android.calendar/calendars";
            calanderEventURL = "content://com.android.calendar/events";
            calanderRemiderURL = "content://com.android.calendar/reminders";

        } else {
            calanderURL = "content://calendar/calendars";
            calanderEventURL = "content://calendar/events";
            calanderRemiderURL = "content://calendar/reminders";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_message_details);
        Intent intent = getIntent();
        messageId = intent.getStringExtra("id");
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        getInfoDetails(messageId);
        popupMenu = new PopupMenu(this);
        calendarUtil = new CalendarUtil();
        initView();

    }

    private void getData(getInfoDetailList mList) {
        int screenWidth = px2dp.px2dip(this, ScreenUtils.getScreenWidth(this));
        messageTitleTv = (TextView) findViewById(R.id.message_title);
        timeTv = (TextView) findViewById(R.id.message_time);
        commentTv = (TextView) findViewById(R.id.comment_num);

        Info info = mList.getResult().getInfo();
        String content = info.getContent();
        String newcontent = content.replaceAll("/cms", "http://wwwcms.bjut.edu.cn/cms");
        js = "var script = document.createElement('script');";
        js += "script.type = 'text/javascript';";
        js += "script.text = alert('haha');";
        /*
        js += "script.text = \"function ResizeImages() { ";
        js += "alert('haha');";

        js += "var myimg,oldwidth;";
        js += "var maxwidth = %@;";
        js += "for(i=0;i <document.images.length;i++){";
        js += "myimg = document.images[i];";
        js += "myimg.style.width = '';";
        js += "myimg.style.height = 'auto';";
        js += "if(myimg.width > maxwidth){";
        js += "oldwidth = myimg.width;";
        js += "myimg.width = maxwidth;";
        js += "}";
        js += "}";

        js += "}\";";
        */
        js += "document.getElementsByTagName('head')[0].appendChild(script);";

        LogUtil.i("out", js);
        newcontent = newcontent.replaceAll("attachid=\"d7ad18d9e3bb4adb8392fb42360f6608", "style=\"height:auto;width:" + String.valueOf(screenWidth - 30) + "px\" attachid=\"d7ad18d9e3bb4adb8392fb42360f6608");
        LogUtil.i("out", newcontent);
        messageTitleTv.setText(info.getTitle());
        messageTitle = info.getTitle();//获取标题
        timeTv.setText(info.getLast_modify_time());
        commentTv.setText(info.getComment_num());
        contentWv = (WebView) findViewById(R.id.wv_content);
        contentWv.getSettings().setJavaScriptEnabled(true);
        contentWv.addJavascriptInterface(new WebAppInterface(this), "Android");
        contentWv.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {//结束
                LogUtil.i("out", "in finish");
                super.onPageFinished(view, url);
//                contentWv.loadUrl("javascript:" + js);
//                contentWv.loadUrl("file:///android_asset/jsforandroid.html");
//                contentWv.loadUrl("javascript:ResizeImages()");
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //开始
                super.onPageStarted(view, url, favicon);
            }
        });
        contentWv.loadDataWithBaseURL(null, newcontent, "text/html",
                "utf-8", null);
        contentWv.setDownloadListener(new MyWebViewDownLoadListener());
    }

    private void initView() {
        signupTv = (TextView) findViewById(R.id.tv_signup);
        signupTv.setOnClickListener(this);
        ivTitleBtnLeft = (ImageButton) this.findViewById(R.id.ivTitleBtnLeft);
        ivTitleBtnLeft.setOnClickListener(this);
        ivTitleBtnLeft.setBackgroundResource(R.drawable.back_btn_selector);
        moreBtn = (ImageView) findViewById(R.id.more);
        moreBtn.setBackgroundResource(R.drawable.more);
        //ivTitleBtnRight.setBackgroundResource(R.drawable.collect);
        moreBtn.setOnClickListener(this);
        moreBtn.setVisibility(View.VISIBLE);
        writeLl = (LinearLayout) findViewById(R.id.ll_write);
        commentLl = (LinearLayout) findViewById(R.id.ll_comment);
        writeLl.setOnClickListener(this);
        commentLl.setOnClickListener(this);
        TextView toptitle = (TextView) findViewById(R.id.ivTitleName);
        toptitle.setText("通知详情");
        tzIv = (ImageView) findViewById(R.id.iv_tz);
        tzIv.setVisibility(View.VISIBLE);
    }

    public void getInfoDetails(String id) {
        showProgressDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("id", id);
        params.addBodyParameter("user_id", pref.getString("username", ""));
        // params.addBodyParameter("path", "/apps/测试应用/test文件夹");

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, ConfigUtil.MY_SERVICE_URL
                + "getInfoDetail.php", params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(final ResponseInfo<String> responseInfo) {
                // resultText.setText("upload response:" + responseInfo.result);
                //System.out.println(responseInfo.result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        ObjectMapper objectMapper = new ObjectMapper();
                        String json = responseInfo.result;
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(json);
                            int code = jsonObject.getInt("code");
                            if (code == 211) {
                                getInfoDetailList list = objectMapper
                                        .readValue(json,
                                                getInfoDetailList.class);
                                if (list.getResult().getInfo().getCollect() == 1) {
                                    popupMenu.changeImg(1);
                                    //ivTitleBtnRight.setBackgroundResource(R.drawable.collect_click);
                                    iscollect = 1;
                                } else if (list.getResult().getInfo()
                                        .getCollect() == 0) {
                                    popupMenu.changeImg(0);
                                    // ivTitleBtnRight.setBackgroundResource(R.drawable.collect);
                                    iscollect = 0;
                                }
                                if (list.getResult().getInfo().getSignup() != 2) {
                                    signupTv.setVisibility(View.VISIBLE);
                                    if (list.getResult().getInfo().getSignup() == 1) {
                                        signupTv.setText("取消报名");
                                        signup = 1;
                                    } else if (list.getResult().getInfo().getSignup() == 0) {
                                        signupTv.setText("我要报名");
                                        signup = 0;
                                    }
                                }
                                if (list != null) {
                                    getData(list);

                                }
                            } else if (code == 311) {
                                Toast.makeText(getApplicationContext(),
                                        "通知不存在！", Toast.LENGTH_SHORT).show();
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
                // resultText.setText(msg);
            }
        });
    }

    public void publishComment(String id, String channel_id, String creator_id,
                               String content) {
        showProgressDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("id", id);
        params.addBodyParameter("channel_id", channel_id);
        params.addBodyParameter("creator_id", creator_id);
        params.addBodyParameter("content", content);

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, ConfigUtil.MY_SERVICE_URL
                + "publishComment.php", params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(final ResponseInfo<String> responseInfo) {
                //System.out.println(responseInfo.result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        String json = responseInfo.result;
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(json);
                            int code = jsonObject.getInt("code");
                            if (code == 217) {
                                Toast.makeText(getApplicationContext(),
                                        "评论成功！", Toast.LENGTH_SHORT).show();
                            } else if (code == 399) {
                                Toast.makeText(getApplicationContext(), "请登录！",
                                        Toast.LENGTH_SHORT).show();
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
                // resultText.setText(msg);
            }
        });
    }

    public void uncollect(String user_id, String object_id, String object_type) {
        showProgressDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("user_id", user_id);
        params.addBodyParameter("object_id", object_id);
        params.addBodyParameter("object_type", object_type);

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, ConfigUtil.MY_SERVICE_URL
                + "uncollect.php", params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(final ResponseInfo<String> responseInfo) {
                //System.out.println(responseInfo.result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        String json = responseInfo.result;
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(json);
                            int code = jsonObject.getInt("code");
                            if (code == 222) {
                                Toast.makeText(getApplicationContext(),
                                        "取消收藏成功！", Toast.LENGTH_SHORT).show();
                                getInfoDetails(messageId);
                            } else if (code == 399) {
                                Toast.makeText(getApplicationContext(),
                                        "请登录！", Toast.LENGTH_SHORT).show();
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
                // resultText.setText(msg);
            }
        });
    }

    public void collect(String user_id, String object_id, String object_type) {
        showProgressDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("user_id", user_id);
        params.addBodyParameter("object_id", object_id);
        params.addBodyParameter("object_type", object_type);

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, ConfigUtil.MY_SERVICE_URL
                + "collect.php", params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(final ResponseInfo<String> responseInfo) {
                //System.out.println(responseInfo.result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        String json = responseInfo.result;
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(json);
                            int code = jsonObject.getInt("code");
                            if (code == 219) {
                                Toast.makeText(getApplicationContext(),
                                        "收藏成功！", Toast.LENGTH_SHORT).show();
                                getInfoDetails(messageId);
                            } else if (code == 399) {
                                Toast.makeText(getApplicationContext(),
                                        "请登录！", Toast.LENGTH_SHORT).show();
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
                // resultText.setText(msg);
            }
        });
    }

    public void signUp(String user_id, String object_id) {
        showProgressDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("creator_id", user_id);
        params.addBodyParameter("id", object_id);

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, ConfigUtil.MY_SERVICE_URL
                + "signup.php", params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(final ResponseInfo<String> responseInfo) {
                //System.out.println(responseInfo.result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        String json = responseInfo.result;
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(json);
                            int code = jsonObject.getInt("code");
                            if (code == 219) {
                                Toast.makeText(getApplicationContext(),
                                        "报名成功！", Toast.LENGTH_SHORT).show();
                                getInfoDetails(messageId);
                            } else if (code == 3292) {
                                Toast.makeText(getApplicationContext(),
                                        "请登录！", Toast.LENGTH_SHORT).show();
                            } else if (code == 3293) {
                                Toast.makeText(getApplicationContext(),
                                        "已报名！", Toast.LENGTH_SHORT).show();
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
                // resultText.setText(msg);
            }
        });
    }

    public void unsignUp(String user_id, String object_id) {
        showProgressDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("creator_id", user_id);
        params.addBodyParameter("id", object_id);

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, ConfigUtil.MY_SERVICE_URL
                + "unsignup.php", params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(final ResponseInfo<String> responseInfo) {
                //System.out.println(responseInfo.result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        String json = responseInfo.result;
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(json);
                            int code = jsonObject.getInt("code");
                            if (code == 230) {
                                Toast.makeText(getApplicationContext(),
                                        "取消报名成功！", Toast.LENGTH_SHORT).show();
                                getInfoDetails(messageId);
                            } else if (code == 3292) {
                                Toast.makeText(getApplicationContext(),
                                        "请登录！", Toast.LENGTH_SHORT).show();
                            } else if (code == 3293) {
                                Toast.makeText(getApplicationContext(),
                                        "已报名！", Toast.LENGTH_SHORT).show();
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
                // resultText.setText(msg);
            }
        });
    }

    private void showShare() {//一键分享
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");
        oks.setTitle("科研助手");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText(messageTitle);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://a.app.qq.com/o/simple.jsp?pkgname=com.bjut.cyl.kfyrip.ui");
        //oks.setUrl("bjut://sse.com");

        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivTitleBtnLeft:
                this.finish();
                break;
            case R.id.more:
                popupMenu.showLocation(R.id.more);// 设置弹出菜单弹出的位置
                // 设置回调监听，获取点击事件
                popupMenu.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onClick(MENUITEM item, String str) {
                        switch (item) {
                            case ITEM1:
                                if (iscollect == 1) {
                                    uncollect(pref.getString("username", ""), messageId, "1");
                                    popupMenu.changeImg(0);
                                } else if (iscollect == 0) {
                                    collect(pref.getString("username", ""), messageId, "1");
                                    popupMenu.changeImg(1);
                                }
                                break;
                            case ITEM2:
                                Calendar calendar = Calendar.getInstance();
                                Intent intent = new Intent(Intent.ACTION_EDIT);
                                intent.setType("vnd.android.cursor.item/event");
                                intent.putExtra("title", messageTitle);
                                intent.putExtra("description", "会议");
                                intent.putExtra("beginTime", calendar.getTime().getTime());
                                intent.putExtra("endTime", calendar.getTime().getTime());
                                startActivity(intent);
                                // calendarUtil.writeToCalender(NotificationDetailsActivity.this , messageTitle);
                                break;
                            case ITEM3:
                                showShare();
                                break;
                        }

                    }
                });
                break;
            case R.id.ll_comment:
                Intent intent = new Intent();
                intent.putExtra("id", messageId);
                intent.setClass(this, NotiCommentListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                break;
            case R.id.tv_signup:
                if (signup == 0) {
                    signUp(pref.getString("username", ""), messageId);
                    signupTv.setText("取消报名");
                } else if (signup == 1) {
                    unsignUp(pref.getString("username", ""), messageId);
                    signupTv.setText("我要报名");
                }

                break;
            case R.id.ll_write:
                LayoutInflater inflater = LayoutInflater.from(NotificationDetailsActivity.this);
                commentDialog = new CommentDialog(NotificationDetailsActivity.this, "评论") {

                    @Override
                    public void clickCancelCallBack() {
                        // TODO Auto-generated method stub
                        commentDialog.dismiss();
                    }

                    @Override
                    public void clickOkCallBack() {
                        String content = commentDialog.getText();
                        content = content.trim();
                        if (!content.isEmpty()){
                            publishComment(messageId,
                                    ConfigUtil.CHANNEL_ID_NOTIFICATION,
                                    pref.getString("username", ""), content);
                            Intent intent = new Intent();
                            intent.putExtra("id", messageId);
                            intent.setClass(NotificationDetailsActivity.this,
                                    NotiCommentListActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            startActivity(intent);
                            commentDialog.dismiss();
                        }else {
                            PromptUtil.showToast(getString(R.string.comment_content_cannot_be_empty));
                        }

                    }
                };
                commentDialog.setView(new EditText(this));
                commentDialog.show();
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        commentDialog.showKeyboard();
                    }
                }, 200);
                break;
            default:
                break;
        }
    }

    private class MyWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                    long contentLength) {
            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                Toast t = Toast.makeText(NotificationDetailsActivity.this, "需要SD卡。", Toast.LENGTH_LONG);
                t.setGravity(Gravity.CENTER, 0, 0);
                t.show();
                return;
            }
            DownloaderTask task = new DownloaderTask();
            task.execute(url);
        }

    }

    //内部类
    private class DownloaderTask extends AsyncTask<String, Void, String> {

        public DownloaderTask() {
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String url = params[0];
//          Log.i("tag", "url="+url);
            String fileName = url.substring(url.lastIndexOf("/") + 1);
            fileName = URLDecoder.decode(fileName);
            Log.i("tag", "fileName=" + fileName);

            File directory = Environment.getExternalStorageDirectory();
            File file = new File(directory, fileName);
            if (file.exists()) {
                Log.i("tag", "The file has already exists.");
                doPreView(file);
                return fileName;
            }
            try {
                HttpClient client = new DefaultHttpClient();
//                client.getParams().setIntParameter("http.socket.timeout",3000);//设置超时
                HttpGet get = new HttpGet(url);
                HttpResponse response = client.execute(get);
                if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                    HttpEntity entity = response.getEntity();
                    InputStream input = entity.getContent();

                    writeToSDCard(fileName, input);

                    input.close();
//                  entity.consumeContent();
                    doPreView(file);
                    return fileName;
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }


        }

        @Override
        protected void onCancelled() {
            // TODO Auto-generated method stub
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            closeProgressDialog();
            if (result == null) {
                Toast t = Toast.makeText(NotificationDetailsActivity.this, "连接错误！请稍后再试！", Toast.LENGTH_LONG);
                t.setGravity(Gravity.CENTER, 0, 0);
                t.show();
                return;
            }

            Toast t = Toast.makeText(NotificationDetailsActivity.this, "已保存到SD卡。", Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER, 0, 0);
            t.show();
            File directory = Environment.getExternalStorageDirectory();
            File file = new File(directory, result);
            Log.i("tag", "Path=" + file.getAbsolutePath());

//            Intent intent = getFileIntent(file);
//
//            startActivity(intent);

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
        }


        private ProgressDialog mDialog;

        private void showProgressDialog() {
            if (mDialog == null) {
                mDialog = new ProgressDialog(NotificationDetailsActivity.this);
                mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//设置风格为圆形进度条
                mDialog.setMessage("正在加载 ，请等待...");
                mDialog.setIndeterminate(false);//设置进度条是否为不明确
                mDialog.setCancelable(true);//设置进度条是否可以按退回键取消
                mDialog.setCanceledOnTouchOutside(false);
                mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        // TODO Auto-generated method stub
                        mDialog = null;
                    }
                });
                mDialog.show();

            }
        }

        private void closeProgressDialog() {
            if (mDialog != null) {
                mDialog.dismiss();
                mDialog = null;
            }
        }

        public Intent getFileIntent(File file) {
//       Uri uri = Uri.parse("http://m.ql18.com.cn/hpf10/1.pdf");
            Uri uri = Uri.fromFile(file);
            String type = getMIMEType(file);
            Log.i("tag", "type=" + type);
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(uri, type);
            return intent;
        }

        public void writeToSDCard(String fileName, InputStream input) {

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File directory = Environment.getExternalStorageDirectory();
                File file = new File(directory, fileName);
//          if(file.exists()){
//              Log.i("tag", "The file has already exists.");
//              return;
//          }
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    byte[] b = new byte[2048];
                    int j = 0;
                    while ((j = input.read(b)) != -1) {
                        fos.write(b, 0, j);
                    }
                    fos.flush();
                    fos.close();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                Log.i("tag", "NO SDCard.");
            }
        }



    }
    private String getMIMEType(File f) {
        String type = "";
        String fName = f.getName();
      /* 取得扩展名 */
        String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();

      /* 依扩展名的类型决定MimeType */
        if (end.equals("pdf")) {
            type = "application/pdf";//
        } else if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") ||
                end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            type = "audio/*";
        } else if (end.equals("3gp") || end.equals("mp4")) {
            type = "video/*";
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") ||
                end.equals("jpeg") || end.equals("bmp")) {
            type = "image/*";
        } else if (end.equals("apk")) {
        /* android.permission.INSTALL_PACKAGES */
            type = "application/vnd.android.package-archive";
        }
      else if(end.equals("pptx")||end.equals("ppt")){
        type = "application/vnd.ms-powerpoint";
      }else if(end.equals("docx")||end.equals("doc")){
        type = "application/ms-word";
      }else if(end.equals("xlsx")||end.equals("xls")){
        type = "application/vnd.ms-excel";
      } else {
//        /*如果无法直接打开，就跳出软件列表给用户选择 */
            type = "*/*";
        }
        return type;
    }
    /**
     * 预览下载好的word文件
     */
    private void doPreView(File file) {
        String type = getMIMEType(file);
        Intent intent = OpenFiles.getPreviewFileIntent(file,type);
        startActivity(intent);
    }


}
