package com.bjut.cyl.kfyrip.adapter;

import java.util.List;
import java.util.Map;

import com.bjut.cyl.kfyrip.other.TextViewMeasure;
import com.bjut.cyl.kfyrip.ui.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AnswerListAdapter extends BaseAdapter {
	private List<Map<String, Object>> mData;
	private LayoutInflater mInflater;

	public AnswerListAdapter(Context context, List<Map<String, Object>> mData2) {
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

			convertView = mInflater.inflate(R.layout.item_answer_list, null);
			holder.title1 = (TextView) convertView
					.findViewById(R.id.answerer_name);
			holder.title2 = (TextView) convertView
					.findViewById(R.id.answer_content);
			holder.title3 = (TextView) convertView
					.findViewById(R.id.answer_time);
			convertView.setTag(holder);

		} else {

			holder = (ViewHolder) convertView.getTag();
		}

		holder.title1
				.setText((String) mData.get(position).get("answerer_name"));
		holder.title2.setText((String) mData.get(position)
				.get("answer_content"));
		holder.title3.setText((String) mData.get(position).get("answer_time"));
		return convertView;
	}

}