package com.bjut.cyl.kfyrip.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import com.bjut.cyl.kfyrip.adapter.QnAListAdapter;
import com.bjut.cyl.kfyrip.utils.ConfigUtil;
import com.bjut.cyl.kfyrip.utils.LogUtil;
import com.bjut.cyl.kfyrip.bean.getQuestionList;
import com.bjut.cyl.kfyrip.bean.getQuestionList.Data;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class QnAFragment extends DialogShowOffFrag implements
		OnItemClickListener, OnClickListener {
	private View layoutView;
	private PullToRefreshListView qnaListView;
	private QnAListAdapter mAdapter;
	private List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> mDataOk;
	private int offset = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LogUtil.d("trace", "NotificationFragment的OnCreateView");
		layoutView = inflater.inflate(R.layout.fragment_notification, container,
				false);
		initViews();

		qnaListView = (PullToRefreshListView) layoutView
				.findViewById(R.id.lv_infomation);
		mAdapter = new QnAListAdapter(getActivity(), mData);
		qnaListView.setAdapter(mAdapter);
		//System.out.println("***size" + mAdapter.getCount());
		qnaListView.setOnItemClickListener(this);
		if (mData.size() == 0) {
			testPost("0", "10");
		}

		qnaListView.setMode(Mode.BOTH);
		qnaListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			// Pulling Down
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				mData.clear();
				offset = 0;
				testPost("0", "10");
				mAdapter.notifyDataSetChanged();
				new FinishRefresh().execute();
			}

			// Pulling Up
			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				offset = offset + 10;
				// new Thread(runnable2).start();
				testPost(String.valueOf(offset), "10");
				mAdapter.notifyDataSetChanged();
				new FinishRefresh().execute();

			}
		});

		return layoutView;
	}

	private void initViews() {
		TextView title = (TextView) getActivity().findViewById(R.id.ivTitleName);
		title.setText("信息中心");
		Button button = (Button) getActivity().findViewById(R.id.ivTitleBtnRight);
		button.setVisibility(View.VISIBLE);
		button.setOnClickListener(this);
	}

	public void testPost(String offset, String pagesize) {
		showProgressDialog();
		RequestParams params = new RequestParams();
		params.addBodyParameter("offset", offset);
		params.addBodyParameter("pagesize", pagesize);
		// params.addBodyParameter("path", "/apps/测试应用/test文件夹");

		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, ConfigUtil.MY_SERVICE_URL
				+ "getQuestionList.php", params, new RequestCallBack<String>() {

			@Override
			public void onStart() {
				// resultText.setText("conn...");
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				// resultText.setText(current + "/" + total);
			}

			@Override
			public void onSuccess(final ResponseInfo<String> responseInfo) {
				// resultText.setText("upload response:" + responseInfo.result);
				//System.out.println(responseInfo.result);
				
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						closeProgressDialog();
						ObjectMapper objectMapper = new ObjectMapper();
						String json = responseInfo.result;
						JSONObject jsonObject = null;
						try {
							jsonObject = new JSONObject(json);
							int code = jsonObject.getInt("code");
							if (code == 215) {
								getQuestionList list = objectMapper.readValue(json,
										getQuestionList.class);
								
								if (list != null) {
									mDataOk = getData(list);// 填充数据
									//System.out.println("mdataok" + mDataOk);
									
									if (mDataOk.size() >= 0) {
										mData.addAll(mDataOk);
										 mDataOk.clear();
										//System.out.println(mData);
										mAdapter.notifyDataSetChanged();
									}

								}
							}else if (code == 310) {
								Toast.makeText(getActivity(), "没有更多数据！",
									     Toast.LENGTH_SHORT).show();
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				// resultText.setText(msg);
			}
		});
	}

	private List<Map<String, Object>> getData(getQuestionList mList) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Data> dataList = mList.getResult().getData();

		for (Data result : dataList) {
			if (result.getAnswer() != null) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("question_title", result.getTitle());
				map.put("answerer_name", result.getAnswer().getAnswer_nickname());
				map.put("comment_time", result.getLast_modify_time());
				map.put("answer_content", result.getAnswer().getAnswer_content());
				map.put("answer_time", result.getAnswer().getAnswer_time());
				map.put("click_num", result.getView_num());
				map.put("comment_num", result.getAnswer_num());
				map.put("id", result.getId());
				list.add(map);
			}else {


				Map<String, Object> map = new HashMap<String, Object>();
				map.put("question_title", result.getTitle());
				map.put("answerer_name", "");
				map.put("comment_time", result.getLast_modify_time());
				map.put("answer_content", "");
				map.put("answer_time", "");
				map.put("click_num", result.getView_num());
				map.put("comment_num", result.getAnswer_num());
				map.put("id", result.getId());
				list.add(map);
			}
			
		}

		return list;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		String messageId = (String) mData.get(position - 1).get("id");
		Intent intent = new Intent();
		intent.putExtra("id", messageId);
		intent.setClass(getActivity(), QnADetailsActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);

	}

	private class FinishRefresh extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			qnaListView.onRefreshComplete();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ivTitleBtnRight:
			Intent intent = new Intent();
			intent.setClass(getActivity(), AskQuestionByKeyActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			break;

		default:
			break;
		}
		
	}
}
