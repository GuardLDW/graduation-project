package com.bjut.cyl.kfyrip.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bjut.cyl.kfyrip.bean.OfficeBean;
import com.bjut.cyl.kfyrip.ui.R;

import java.util.List;
import java.util.Map;

public class OfficeListAdapter extends BaseAdapter {
    private List<OfficeBean.Result.Model> mData;
    private LayoutInflater mInflater;


    public OfficeListAdapter(Context context, List<OfficeBean.Result.Model> mData2) {
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

            convertView = mInflater.inflate(R.layout.item_office, null);
            holder.title1 = (TextView) convertView.findViewById(R.id.name);
            holder.title2 = (TextView) convertView.findViewById(R.id.work);
            holder.title3 = (TextView) convertView.findViewById(R.id.phone_num);
            holder.title4 = (TextView) convertView.findViewById(R.id.room);
            holder.title5 = (TextView) convertView.findViewById(R.id.email);

            holder.title6 = (TextView) convertView.findViewById(R.id.tv1);
            holder.title7 = (TextView) convertView.findViewById(R.id.tv2);
            holder.title8 = (TextView) convertView.findViewById(R.id.tv3);
            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        holder.title1.setText(mData.get(position).name);
        holder.title2.setText(mData.get(position).comment);
        holder.title3.setText(mData.get(position).tel);
        holder.title4.setText(mData.get(position).location);
        holder.title5.setText(mData.get(position).email);
        if (mData.get(position).tel.equals("")) {
            holder.title3.setVisibility(View.GONE);
            holder.title4.setVisibility(View.GONE);
            holder.title5.setVisibility(View.GONE);
            holder.title6.setVisibility(View.GONE);
            holder.title7.setVisibility(View.GONE);
            holder.title8.setVisibility(View.GONE);
        } else {
            holder.title3.setVisibility(View.VISIBLE);
            holder.title4.setVisibility(View.VISIBLE);
            holder.title5.setVisibility(View.VISIBLE);
            holder.title6.setVisibility(View.VISIBLE);
            holder.title7.setVisibility(View.VISIBLE);
            holder.title8.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

}