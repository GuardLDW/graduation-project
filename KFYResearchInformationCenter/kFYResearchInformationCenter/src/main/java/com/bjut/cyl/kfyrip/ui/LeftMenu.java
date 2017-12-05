package com.bjut.cyl.kfyrip.ui;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LeftMenu extends Fragment implements OnClickListener {
	private SlidingMenu sm;
	private View exitView,myquestView,mycollectView,verisionView,signView,signupView;
	private TextView nicknameTv;
	private LinearLayout llrootLayout;
	private String userId;
	private View layoutView;
	public LeftMenu() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		userId = bundle.getString("arg");
		sm = ((MainActivity) getActivity()).getSlidingMenu();

	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layoutView = inflater.inflate(R.layout.main_left_fragment, container,
				false);
		exitView = (View) layoutView.findViewById(R.id.menu_exit);
		myquestView = (View) layoutView.findViewById(R.id.menu_my_question);
		mycollectView = layoutView.findViewById(R.id.menu_my_collect);
		verisionView = layoutView.findViewById(R.id.menu_about);
		signView = layoutView.findViewById(R.id.menu_sign_in);
		signupView = layoutView.findViewById(R.id.menu_my_sign_up);
		verisionView.setOnClickListener(this);
		exitView.setOnClickListener(this);
		myquestView.setOnClickListener(this);
		mycollectView.setOnClickListener(this);
		signView.setOnClickListener(this);
		signupView.setOnClickListener(this);
		initView();
		return layoutView;
	}

	private void initView() {
		nicknameTv = (TextView) layoutView.findViewById(R.id.nickNameTextView);
		nicknameTv.setText(userId);
	}
	
	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.menu_exit:
			getActivity().finish();
			break;
		case R.id.menu_my_question:
			intent.setClass(getActivity(), MyQuestionActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			break;
		case R.id.menu_my_collect:
			intent.setClass(getActivity(), MyCollectActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			break;
		case R.id.menu_my_sign_up:
			intent.setClass(getActivity(), MySignUpActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			break;
		case R.id.menu_about:
			intent.setClass(getActivity(), AboutVersionActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			break;
			case R.id.menu_sign_in:
				intent.setClass(getActivity(), MipcaActivityCapture.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				break;
		default:
			break;
		}

	}

}
