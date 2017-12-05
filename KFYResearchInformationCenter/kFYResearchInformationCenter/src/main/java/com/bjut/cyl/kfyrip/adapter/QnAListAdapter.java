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

public class QnAListAdapter extends BaseAdapter {
	private List<Map<String, Object>> mData;
	private LayoutInflater mInflater;

	

	public QnAListAdapter(Context context, List<Map<String, Object>> mData2) {
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

			convertView = mInflater.inflate(R.layout.item_qna_list, null);
			holder.title1 = (TextView) convertView.findViewById(R.id.question_title);
			holder.title2 = (TextView) convertView.findViewById(R.id.click_num);
			holder.title3 = (TextView) convertView.findViewById(R.id.comment_num);
			holder.title4 = (TextView) convertView.findViewById(R.id.comment_time);
			holder.title5 = (TextView) convertView.findViewById(R.id.answerer_name);
			holder.title6 = (TextView) convertView.findViewById(R.id.answer_content);
			holder.title7 = (TextView) convertView.findViewById(R.id.answer_time);
			holder.ll1 = (LinearLayout) convertView.findViewById(R.id.nice_answer_content);
			holder.ll2 = (LinearLayout) convertView.findViewById(R.id.parting_line);
			convertView.setTag(holder);

		} else {

			holder = (ViewHolder) convertView.getTag();
		}

		holder.title1.setText((String) mData.get(position).get("question_title"));
		holder.title2.setText((String) mData.get(position).get("click_num"));
		holder.title3.setText((String) mData.get(position).get("comment_num"));
		holder.title4.setText((String) mData.get(position).get("comment_time"));
		holder.title5.setText((String) mData.get(position).get("answerer_name"));
		holder.title6.setText((String) mData.get(position).get("answer_content"));
		holder.title7.setText((String) mData.get(position).get("answer_time"));
		
		if (mData.get(position).get("answer_content").toString().equals("")) {
			holder.ll1.setVisibility(View.GONE);
			holder.ll2.setVisibility(View.GONE);
		}else {
			holder.ll1.setVisibility(View.VISIBLE);
			holder.ll2.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

}