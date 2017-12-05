package com.bjut.cyl.kfyrip.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bjut.cyl.kfyrip.ui.MainActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.bjut.cyl.kfyrip.ui.R;

public class MenuFragment extends Fragment implements OnClickListener{
	private View menu_home;  //左侧菜单的home
	private View menu_wifi; //左侧菜单的wifi功能
	private View menu_setting; //左侧菜单的设置功能
	private View menu_add; //左侧菜单的添加功能
	private View menu_version; //左侧菜单的版本信息功能
	private SlidingMenu sm;
	private TextView exit;
	
     @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	sm = ((MainActivity)getActivity()).getSlidingMenu();
    }
     
     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	 View view = inflater.inflate(R.layout.main_left_fragment, container,
 				false);
//    	 menu_home = view.findViewById(R.id.menu_home);
//    	 menu_home.setOnClickListener(this);
//    	 menu_wifi = view.findViewById(R.id.menu_wifi);
//    	 menu_wifi.setOnClickListener(this);
//    	 menu_setting = view.findViewById(R.id.menu_setting);
//    	 menu_setting.setOnClickListener(this);
//    	 menu_version = view.findViewById(R.id.menu_version);
//    	 menu_version.setOnClickListener(this);
//    	 menu_add = view.findViewById(R.id.menu_add);
//    	 menu_add.setOnClickListener(this);
//    	 exit = (TextView) view.findViewById(R.id.exit);
//    	 exit.setOnClickListener(this);
    	 
    	return view;
    }

	@Override
	public void onClick(View v) {
		Fragment newContent = null;
		switch (v.getId()) {
//		case R.id.menu_home: //home的点击事件
//			newContent = new Fragment_home();
//			menu_home.setSelected(true);
//			menu_wifi.setSelected(false);
//			menu_setting.setSelected(false);
//			menu_add.setSelected(false);
//			menu_version.setSelected(false);
//			break;
//		case R.id.menu_wifi: //朋友圈的点击事件
//			newContent = new Fragment_wifi();
//			menu_home.setSelected(false);
//			menu_wifi.setSelected(true);
//			menu_setting.setSelected(false);
//			menu_add.setSelected(false);
//			menu_version.setSelected(false);
//			break;
//		case R.id.menu_setting: //设置的点击事件
//			newContent = new Fragment_setting();
//			menu_home.setSelected(false);
//			menu_wifi.setSelected(false);
//			menu_setting.setSelected(true);
//			menu_add.setSelected(false);
//			menu_version.setSelected(false);
//		    break;
//		case R.id.menu_add:
//			newContent = new Fragment_add(sm);
//			menu_home.setSelected(false);
//			menu_wifi.setSelected(false);
//			menu_setting.setSelected(false);
//			menu_add.setSelected(true);
//			menu_version.setSelected(false);
//			break;
//		case R.id.menu_version:
//			newContent = new Fragment_version();
//			menu_home.setSelected(false);
//			menu_wifi.setSelected(false);
//			menu_setting.setSelected(false);
//			menu_add.setSelected(false);
//			menu_version.setSelected(true);
//			break;
//		case R.id.exit:
//			getActivity().finish();
//			break;
//		default:
//			break;
//		}
		
//		if (newContent != null)
//			switchFragment(newContent);
		
	}
	
	/*
	 * 切换到不同的功能内容
	 */
//	private void switchFragment(Fragment fragment) {
//		if (getActivity() == null)
//			return;	
//			MainActivity ra = (MainActivity) getActivity();
//			ra.switchContent(fragment);
//		
//	}
}}
