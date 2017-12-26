package com.bjut.cyl.kfyrip;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.bjut.cyl.kfyrip.ui.MyApplication;

public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent intent1 = new Intent(MyApplication.getContext(), PushService.class);
        MyApplication.getContext().startService(intent1);
        Toast.makeText(MyApplication.getContext(), "Hello", Toast.LENGTH_LONG).show();
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
