package com.bjut.cyl.kfyrip;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class UpdateNoticeActivity extends AppCompatActivity {

    public static UpdateNoticeActivity instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = new UpdateNoticeActivity();
        super.onCreate(savedInstanceState);
    }
}
