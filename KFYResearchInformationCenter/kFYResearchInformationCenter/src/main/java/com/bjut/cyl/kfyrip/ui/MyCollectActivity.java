package com.bjut.cyl.kfyrip.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bjut.cyl.kfyrip.fragment.Fragment_home;
import com.bjut.cyl.kfyrip.fragment.MenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.bjut.cyl.kfyrip.ui.R;

public class MyCollectActivity extends Activity implements
		OnClickListener {
	private ImageButton ivTitleBtnLeft;
	private Button ivTitleBtnRight;
	private SlidingMenu sm;
	private FragmentManager fm;
	private MyNotiFragment myNotiFragment;
	private MyNewsFragment myNewsFragment;
	private MyQnAFragment myQnAFragment;
	private Button  btnNews, btnQna, btnNoti;
	private LinearLayout buttonLayout;
	private SharedPreferences pref;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_mycollect);
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		
		initView();
	}

	private void initView() {
		TextView title = (TextView) findViewById(R.id.ivTitleName);
		title.setText("我的收藏");

		ivTitleBtnLeft = (ImageButton) this.findViewById(R.id.ivTitleBtnLeft);
		ivTitleBtnLeft.setOnClickListener(this);
		ivTitleBtnLeft.setBackgroundResource(R.drawable.back_btn_selector);
		fm = getFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();

		myNewsFragment = new MyNewsFragment();
		myNotiFragment = new MyNotiFragment();
		myQnAFragment = new MyQnAFragment();

		transaction.add(R.id.content_mine, myNotiFragment);
		transaction.show(myNotiFragment);
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		transaction.commit();


		btnNews = (Button) findViewById(R.id.btn_mid);
		btnQna = (Button) findViewById(R.id.btn_right);
		btnNoti = (Button) findViewById(R.id.btn_left);
		btnNews.setText("新闻");
		btnQna.setText("问题");
		btnNoti.setText("通知");
		btnNews.setOnClickListener(this);
		btnQna.setOnClickListener(this);
		btnNoti.setOnClickListener(this);

		btnNoti.setBackgroundResource(R.drawable.collect_btn_choose_left);
		btnNoti.setTextColor(getResources().getColor(R.color.white));
	}

	@Override
	public void onClick(View v) {
		FragmentTransaction transaction = fm.beginTransaction();
		switch (v.getId()) {
		case R.id.ivTitleBtnLeft:
			this.finish();
			break;
		
		case R.id.btn_mid:
			if (!myNewsFragment.isVisible()) {
				// myResearchFragment = new MyResearchFragment();
				transaction.replace(R.id.content_mine, myNewsFragment);
				transaction
						.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
				setColorTop(2);
			}
			break;
		case R.id.btn_right:
			if (!myQnAFragment.isVisible()) {
				// myResearchFragment = new MyResearchFragment();
				transaction.replace(R.id.content_mine, myQnAFragment);
				transaction
						.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
				setColorTop(3);
			}
			break;
		case R.id.btn_left:
			if (!myNotiFragment.isVisible()) {
				// infomationCenterFragment = new InfomationCenterFragment();
				transaction.replace(R.id.content_mine, myNotiFragment);
				transaction
						.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
				setColorTop(1);
			}
			break;
		default:
			break;
		}
		transaction.commit();
	}


	
	private void setColorTop(int num){
		switch (num) {
		case 1:
			btnNoti.setBackgroundResource(R.drawable.collect_btn_choose_left);
			btnNoti.setTextColor(getResources().getColor(R.color.white));
			btnNews.setBackgroundResource(R.drawable.collect_btn_selector_mid);
			btnNews.setTextColor(this.getResources().getColor(R.color.black));
			btnQna.setBackgroundResource(R.drawable.collect_btn_selector_right);
			btnQna.setTextColor(this.getResources().getColor(R.color.black));
			break;
		case 2:
			btnNoti.setBackgroundResource(R.drawable.collect_btn_selector_left);
			btnNoti.setTextColor(this.getResources().getColor(R.color.black));
			btnNews.setBackgroundResource(R.drawable.collect_btn_choose_mid);
			btnNews.setTextColor(this.getResources().getColor(R.color.white));
			btnQna.setBackgroundResource(R.drawable.collect_btn_selector_right);
			btnQna.setTextColor(this.getResources().getColor(R.color.black));
			break;
		case 3:
			btnNoti.setBackgroundResource(R.drawable.collect_btn_selector_left);
			btnNoti.setTextColor(this.getResources().getColor(R.color.black));
			btnNews.setBackgroundResource(R.drawable.collect_btn_selector_mid);
			btnNews.setTextColor(this.getResources().getColor(R.color.black));
			btnQna.setBackgroundResource(R.drawable.collect_btn_choose_right);
			btnQna.setTextColor(this.getResources().getColor(R.color.white));
			break;
		default:
			break;
		}
	}
}
