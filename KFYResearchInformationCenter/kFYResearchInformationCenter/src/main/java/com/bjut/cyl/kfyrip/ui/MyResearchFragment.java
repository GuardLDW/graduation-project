package com.bjut.cyl.kfyrip.ui;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyResearchFragment extends Fragment implements View.OnClickListener {
	private View layoutView;
	private RelativeLayout img1,img2,img3,img4;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		layoutView = inflater.inflate(R.layout.fragment_guide, container,
				false);
		initViews();
		return layoutView;
	}

	private void initViews() {
		TextView title = (TextView) getActivity().findViewById(R.id.ivTitleName);
		title.setText("办事指南");
		img1 = (RelativeLayout) layoutView.findViewById(R.id.img1);
		img1.setOnClickListener(this);
		img2 = (RelativeLayout) layoutView.findViewById(R.id.img2);
		img2.setOnClickListener(this);
		img3 = (RelativeLayout) layoutView.findViewById(R.id.img3);
		img3.setOnClickListener(this);
		img4 = (RelativeLayout) layoutView.findViewById(R.id.img4);
		img4.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()){
			case R.id.img1:
				//组织机构
				intent.setClass(getActivity(), OfficeInfoActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				break;
			case R.id.img2:
				//专利
				intent.setClass(getActivity(), PatentActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				break;
			case R.id.img3:
				//立项到款
				intent.setClass(getActivity(), LixiangActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				break;
			case R.id.img4:
				//报奖
				intent.setClass(getActivity(), BaoJiangActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				break;
		}


	}
}
