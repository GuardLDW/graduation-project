package com.bjut.cyl.kfyrip.ui;

import com.bjut.cyl.kfyrip.utils.LogUtil;
import com.lidroid.xutils.util.LogUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class WelComeActivity extends Activity {
	private ImageView welcomView;
	private static final long SPLASH_DELAY_MILLIS = 3000;
	Animation mAnimation = null;
	private SharedPreferences pref;
	private Editor editor;
	private String firsttime = "";
	public static WelComeActivity instance = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		LogUtil.d("trace", "welcom!!!!!!!!!!!!!!!");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		instance = this;
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		editor = pref.edit();
		editor.commit();
		firsttime = pref.getString("firsttime", "");
		if (firsttime.equals("")) {
			setContentView(R.layout.activity_welcom);
			editor = pref.edit();
			editor.putString("firsttime", "yes");
			editor.commit();
			mAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha);
			welcomView = (ImageView) findViewById(R.id.img_welcome);
			welcomView.startAnimation(mAnimation);
			new Handler().postDelayed(new Runnable() {
				public void run() {
					Intent intent = new Intent();
					intent.setClass(WelComeActivity.this, LoginActivity.class);
					startActivity(intent);
				}
			}, SPLASH_DELAY_MILLIS);
		}else {
			//System.out.println("okokkokokook"+pref.getString("firsttime", ""));
			Intent intent = new Intent();
			intent.setClass(WelComeActivity.this, LoginActivity.class);
			startActivity(intent);
		}
	}
}
// mHandler.sendMessageDelayed(msg, delayMillis)(GOTO_MAIN_ACTIVITY, 5000);

// private static final int GOTO_MAIN_ACTIVITY = 0;
// private Handler mHandler = new Handler(){
// public void handleMessage(android.os.Message msg) {
//
// switch (msg.what) {
// case GOTO_MAIN_ACTIVITY:
// Intent intent = new Intent();
// intent.setClass(WelComeActivity.this, LoginActivity.class);
// startActivity(intent);
// finish();
// break;
//
// default:
// break;
// }
// };
// };}