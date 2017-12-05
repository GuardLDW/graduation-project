package com.bjut.cyl.kfyrip.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bjut.cyl.kfyrip.swipemenulistview.SwipeMenu;
import com.bjut.cyl.kfyrip.swipemenulistview.SwipeMenuCreator;
import com.bjut.cyl.kfyrip.swipemenulistview.SwipeMenuItem;
import com.bjut.cyl.kfyrip.swipemenulistview.SwipeMenuListView;
import com.bjut.cyl.kfyrip.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.bjut.cyl.kfyrip.swipemenulistview.SwipeMenuListView.OpenOrCloseListener;
import com.bjut.cyl.kfyrip.ui.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

@SuppressLint("ValidFragment")
public class Fragment_add extends Fragment {
	private View view;
	private SlidingMenu menu;
	private Context mContext;
	private Handler handler = new Handler();
	private Runnable myRunnable;
	private List<String> list;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_add, null);
		mContext = getActivity();
		InitView(view);
		return view;
	}

	private void InitView(View view) {
		list = new ArrayList<String>();
		for (int i = 0; i < 20; i++) {
			list.add("第"+i+"测试");
		}
		SwipeMenuListView listView = (SwipeMenuListView) view
				.findViewById(R.id.id_swipelistview);
		MyAdapter adapter = new MyAdapter();
		listView.setAdapter(adapter);

		// step 1. create a MenuCreator
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				SwipeMenuItem openItem = new SwipeMenuItem(
						mContext.getApplicationContext());
				openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
						0xCE)));
				openItem.setWidth(dp2px(90));
				openItem.setTitle("Revise");
				openItem.setTitleSize(18);
				openItem.setTitleColor(Color.WHITE);
				menu.addMenuItem(openItem);

				SwipeMenuItem deleteItem = new SwipeMenuItem(
						mContext.getApplicationContext());
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						0x3F, 0x25)));
				deleteItem.setWidth(dp2px(90));
				deleteItem.setTitle("Delete");
				deleteItem.setTitleColor(Color.WHITE);
				menu.addMenuItem(deleteItem);
			}
		};
		// set creator
		listView.setMenuCreator(creator);

		// step 2. listener item click event
		listView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public void onMenuItemClick(int position, SwipeMenu menu, int index) {
				switch (index) {
				case 0:
					break;
				case 1:
					list.remove(index);
					Toast.makeText(mContext, "第"+index+"条被删除", Toast.LENGTH_SHORT).show();
					break;
				}
			}
		});

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(mContext, position + " long click", Toast.LENGTH_SHORT).show();
				return false;
			}
		});

		listView.setOnOpenOrCloseListener(new OpenOrCloseListener() {

			@Override
			public void isOpen(boolean isOpen) {
				if (!isOpen) {// 如果是打开的
					menu.setMode(SlidingMenu.LEFT);
					menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
					handler.removeCallbacks(myRunnable);
				} else { // 都是关闭的
					menu.setMode(SlidingMenu.LEFT_RIGHT);
					menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
					resetMenu();
				}

			}

		});

	}

	private void resetMenu() {
		handler.postDelayed(myRunnable = new Runnable() {
			@Override
			public void run() {
				menu.setMode(SlidingMenu.LEFT);
				menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			}
		}, 300);
	}

	public Fragment_add(SlidingMenu menu) {
		super();
		this.menu = menu;
	}
	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public String getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(mContext.getApplicationContext(),
						R.layout.item_list_add, null);
				new ViewHolder(convertView);
			}
			ViewHolder holder = (ViewHolder) convertView.getTag();
			String txt = getItem(position);
			holder.tv_name.setText(txt);
			return convertView;
		}

		class ViewHolder {
			TextView tv_name;

			public ViewHolder(View view) {
				tv_name = (TextView) view.findViewById(R.id.tv_name);
				view.setTag(this);
			}
		}
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}

}
