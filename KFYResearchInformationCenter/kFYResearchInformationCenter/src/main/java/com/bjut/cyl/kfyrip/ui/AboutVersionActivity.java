package com.bjut.cyl.kfyrip.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;


public class AboutVersionActivity extends Activity implements View.OnClickListener {
    private ImageButton ivTitleBtnLeft;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_version);
        initView();
    }

    private void initView() {
        TextView title = (TextView) findViewById(R.id.ivTitleName);
        title.setText("关于");
        ivTitleBtnLeft = (ImageButton) this.findViewById(R.id.ivTitleBtnLeft);
        ivTitleBtnLeft.setOnClickListener(this);
        ivTitleBtnLeft.setBackgroundResource(R.drawable.back_btn_selector);
        TextView textView = (TextView) findViewById(R.id.tv_call);
        textView.setOnClickListener(this);
        TextView versionTv = (TextView) findViewById(R.id.tv_version);
        versionTv.setText(getVersionName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivTitleBtnLeft:
                this.finish();
                break;
            case R.id.tv_call:
                Intent phoneIntent = new Intent(
                        "android.intent.action.CALL", Uri.parse("tel:"
                        + "13552191022"));
                startActivity(phoneIntent);
                break;
        }
    }

    private String getVersionName(){
        //获取版本信息
        String versionName = "";
        String pkName = this.getPackageName();
        try {
            versionName = getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }
}
