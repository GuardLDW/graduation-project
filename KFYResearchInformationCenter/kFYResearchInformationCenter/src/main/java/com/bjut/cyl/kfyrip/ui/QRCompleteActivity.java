package com.bjut.cyl.kfyrip.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjut.cyl.kfyrip.bean.getCollectListTz;
import com.bjut.cyl.kfyrip.utils.ConfigUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;


public class QRCompleteActivity extends DialogShowOffAct implements View.OnClickListener {
    private ImageButton ivTitleBtnLeft;
    private final static int SCANNIN_GREQUEST_CODE = 1;
    private TextView mTextView;
    private String messageId, already;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_qr);
        Intent intent = getIntent();
        messageId = intent.getStringExtra("id");

        already = intent.getStringExtra("already");
        pref = PreferenceManager.getDefaultSharedPreferences(this);


        initView();
    }

    private void initView() {
        TextView title = (TextView) findViewById(R.id.ivTitleName);
        title.setText("扫码");
        ivTitleBtnLeft = (ImageButton) this.findViewById(R.id.ivTitleBtnLeft);
        ivTitleBtnLeft.setOnClickListener(this);
        ivTitleBtnLeft.setBackgroundResource(R.drawable.back_btn_selector);
        Button mButton = (Button) findViewById(R.id.button1);
        mButton.setOnClickListener(this);
        mTextView = (TextView) findViewById(R.id.result);
        if ("true".equals(already)) {
            mTextView.setText("您已签到，欢迎参加本次会议！");
        } else if ("false".equals(already)) {
            mTextView.setText("签到成功，欢迎参加本次会议！");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivTitleBtnLeft:
                this.finish();
                break;
            case R.id.button1:
                Intent intent = new Intent();
                intent.putExtra("id", messageId);
                intent.setClass(this, NotificationDetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                break;
        }
    }
}
