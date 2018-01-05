package com.bjut.cyl.kfyrip;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.bjut.cyl.kfyrip.ui.NotificationDetailsActivity;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String summary = intent.getStringExtra("summary");
        Intent detailIntent = new Intent(context, NotificationDetailsActivity.class);
        detailIntent.putExtra("id", summary);
        detailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(detailIntent);

        Toast.makeText(context, summary, Toast.LENGTH_LONG).show();
    }
}
