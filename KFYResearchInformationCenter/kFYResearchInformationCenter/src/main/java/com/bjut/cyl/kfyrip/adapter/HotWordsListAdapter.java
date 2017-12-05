package com.bjut.cyl.kfyrip.adapter;

import java.util.List;
import java.util.Map;

import com.bjut.cyl.kfyrip.ui.R;




import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HotWordsListAdapter extends BaseAdapter {
	private List<Map<String, Object>> mData;
	private LayoutInflater mInflater;

	

	public HotWordsListAdapter(Context context, List<Map<String, Object>> mData2) {
		this.mInflater = LayoutInflater.from(context);
		this.mData = mData2;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {

			holder = new ViewHolder();

			convertView = mInflater.inflate(R.layout.item_hotwords, null);
			holder.title1 = (TextView) convertView.findViewById(R.id.hotword);
			holder.title2 = (TextView) convertView.findViewById(R.id.total_num);
			
			convertView.setTag(holder);

		} else {

			holder = (ViewHolder) convertView.getTag();
		}

		holder.title1.setText((String) mData.get(position).get("hotword"));
		holder.title2.setText((String) mData.get(position).get("total_num"));

		return convertView;
	}

}