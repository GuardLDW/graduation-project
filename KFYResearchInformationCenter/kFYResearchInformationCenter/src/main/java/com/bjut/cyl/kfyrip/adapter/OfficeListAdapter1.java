package com.bjut.cyl.kfyrip.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjut.cyl.kfyrip.bean.OfficeBean;
import com.bjut.cyl.kfyrip.ui.R;

import java.util.ArrayList;

/**
 * 作者：haoran   on https://github.com/woaigmz 2017/6/1.
 * 邮箱：1549112908@qq.com
 * 说明：
 */

public class OfficeListAdapter1 extends RecyclerView.Adapter {
    private Context context;
    private ArrayList<OfficeBean.Result.Model> mData;

    public OfficeListAdapter1(Context context, ArrayList<OfficeBean.Result.Model> mData) {
        this.context = context;
        this.mData = mData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sticky_list_office1, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;
        //分类显示和隐藏
        if (position == 0) {
            recyclerViewHolder.tvStickyHeader.setVisibility(View.VISIBLE);
            recyclerViewHolder.header1.setText(mData.get(position).office);
            recyclerViewHolder.header2.setText(mData.get(position).location);
            // 第一个item的吸顶信息肯定是展示的，并且标记tag为FIRST_STICKY_VIEW
        } else {
            // 之后的item都会和前一个item要展示的吸顶信息进行比较，不相同就展示，并且标记tag为HAS_STICKY_VIEW
            if (!TextUtils.equals(mData.get(position).office, mData.get(position - 1).office)) {
                recyclerViewHolder.tvStickyHeader.setVisibility(View.VISIBLE);
                recyclerViewHolder.header1.setText(mData.get(position).office);
                recyclerViewHolder.header2.setText(mData.get(position).location);
            } else {
                // 相同就不展示，并且标记tag为NONE_STICKY_VIEW
                recyclerViewHolder.tvStickyHeader.setVisibility(View.GONE);
            }
        }

        recyclerViewHolder.title1.setText(mData.get(position).name);
        recyclerViewHolder.title2.setText(mData.get(position).comment);
        recyclerViewHolder.title3.setText(mData.get(position).tel);
        recyclerViewHolder.title4.setText(mData.get(position).email);



    }

    @Override
    public int getItemCount() {
        return mData.size()==0?0:mData.size();
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private  TextView header1;
        private  TextView header2;
        private RelativeLayout tvStickyHeader;
        private TextView title1;
        private TextView title2;
        private TextView title3;
        private TextView title4;
        private TextView title5;
        private TextView title6;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tvStickyHeader = (RelativeLayout) itemView.findViewById(R.id.tv_sticky_header_view);
            header1 = (TextView) tvStickyHeader.findViewById(R.id.textView1);
            header2 = (TextView) tvStickyHeader.findViewById(R.id.textView2);
            title1 = (TextView) itemView.findViewById(R.id.name);
            title2 = (TextView) itemView.findViewById(R.id.work);
            title3 = (TextView) itemView.findViewById(R.id.phone_num);
            title4 = (TextView) itemView.findViewById(R.id.email);
            title5 = (TextView) itemView.findViewById(R.id.tv1);
            title6 = (TextView) itemView.findViewById(R.id.tv3);
        }
    }
}
