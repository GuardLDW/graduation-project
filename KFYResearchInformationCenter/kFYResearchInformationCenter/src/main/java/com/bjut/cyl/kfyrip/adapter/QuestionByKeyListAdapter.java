package com.bjut.cyl.kfyrip.adapter;

import java.util.List;
import java.util.Map;

import com.bjut.cyl.kfyrip.ui.R;




import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class QuestionByKeyListAdapter extends BaseAdapter {
	private List<Map<String, Object>> mData;
	private LayoutInflater mInflater;

	

	public QuestionByKeyListAdapter(Context context, List<Map<String, Object>> mData2) {
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

			convertView = mInflater.inflate(R.layout.item_question_by_key, null);
			holder.title1 = (TextView) convertView.findViewById(R.id.question_title);
			holder.title2 = (TextView) convertView.findViewById(R.id.click_num);
			holder.title3 = (TextView) convertView.findViewById(R.id.comment_num);
			holder.title4 = (TextView) convertView.findViewById(R.id.comment_time);
			convertView.setTag(holder);

		} else {

			holder = (ViewHolder) convertView.getTag();
		}

		holder.title1.setText((String) mData.get(position).get("question_title"));
		holder.title2.setText((String) mData.get(position).get("click_num"));
		holder.title3.setText((String) mData.get(position).get("comment_num"));
		holder.title4.setText((String) mData.get(position).get("comment_time"));
		
	
		return convertView;
	}

}