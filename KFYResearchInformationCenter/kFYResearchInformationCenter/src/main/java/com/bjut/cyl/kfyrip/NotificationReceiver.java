package com.bjut.cyl.kfyrip;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String summary = intent.getStringExtra("summary");
        Toast.makeText(context, summary, Toast.LENGTH_LONG).show();
    }
}
