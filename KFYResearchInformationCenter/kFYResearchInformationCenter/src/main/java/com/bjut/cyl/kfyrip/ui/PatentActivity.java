package com.bjut.cyl.kfyrip.ui;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bjut.cyl.kfyrip.bean.Photo;
import com.bjut.cyl.kfyrip.bean.getInfoDetailList;
import com.bjut.cyl.kfyrip.utils.ConfigUtil;
import com.bjut.cyl.kfyrip.utils.XHttpCallbackListener;
import com.bjut.cyl.kfyrip.utils.XHttpUtil;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import uk.co.senab.photoview.PhotoView;

public class PatentActivity extends FragmentActivity implements View.OnClickListener {
    private ImageButton ivTitleBtnLeft;
    private PhotoView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_patent);

        ivTitleBtnLeft = (ImageButton) findViewById(R.id.ivTitleBtnLeft);
        iv = (PhotoView) findViewById(R.id.iv_patent);
        ivTitleBtnLeft.setOnClickListener(this);
        ivTitleBtnLeft.setBackgroundResource(R.drawable.back_btn_selector);
        TextView title = (TextView) findViewById(R.id.ivTitleName);
        title.setText("专利");
        //Glide.with(MyApplication.getContext()).load("http://192.168.1.101/" + "images_infor/b_ht2.png").asBitmap().centerCrop().into(iv);
        requestInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivTitleBtnLeft:
                this.finish();
                break;
        }
    }

    private void requestInfo() {
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, ConfigUtil.MY_SERVICE_URL
                +"getInforPic.php", null, new RequestCallBack<String>() {
            @Override
            public void onSuccess(final ResponseInfo<String> responseInfo) {
                //System.out.println(responseInfo.result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String json = responseInfo.result;
                            Photo photo = new Gson().fromJson(json,Photo.class);
                            String picture = photo.result.get(0).picture;
                            //System.out.println("ooooooooooo"+picture);
                            Glide.with(PatentActivity.this).load("http://resass.bjut.edu.cn/resass/" + picture).asBitmap()
                                    .into(iv);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(HttpException error, String msg) {
            }
        });
    }
}

