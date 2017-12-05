package com.bjut.cyl.kfyrip.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import com.bjut.cyl.kfyrip.adapter.MyQuestionListAdapter;
import com.bjut.cyl.kfyrip.bean.getMyQuestionList;
import com.bjut.cyl.kfyrip.bean.getMyQuestionList.Result;
import com.bjut.cyl.kfyrip.utils.ConfigUtil;
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
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MyQuestionActivity extends DialogShowOffAct implements OnItemClickListener, OnClickListener {
	private PullToRefreshListView newsListView;
	private MyQuestionListAdapter mAdapter;
	private ImageButton ivTitleBtnLeft;
	private List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> mDataOk;
	private int offset = 0;
	private SharedPreferences pref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my_question);
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		initViews();

		newsListView = (PullToRefreshListView) findViewById(R.id.lv_my_question);
		mAdapter = new MyQuestionListAdapter(this, mData);
		newsListView.setAdapter(mAdapter);
		//System.out.println("***size" + mAdapter.getCount());
		newsListView.setOnItemClickListener(this);
		if (mData.size() == 0) {
			getMyQuestionList("0", pref.getString("username", ""));
		}

		newsListView.setMode(Mode.BOTH);
		newsListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			// Pulling Down
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				mData.clear();
				offset = 0;
				getMyQuestionList("0", pref.getString("username", ""));
				mAdapter.notifyDataSetChanged();
				new FinishRefresh().execute();
			}

			// Pulling Up
			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				offset = offset + 10;
				// new Thread(runnable2).start();
				getMyQuestionList(String.valueOf(offset), pref.getString("username", ""));
				mAdapter.notifyDataSetChanged();
				new FinishRefresh().execute();

			}
		});
	}
	private void initViews() {
		TextView title = (TextView) findViewById(R.id.ivTitleName);
		title.setText("我的问题");
		
		ivTitleBtnLeft = (ImageButton) this.findViewById(R.id.ivTitleBtnLeft);
		ivTitleBtnLeft.setOnClickListener(this);
		ivTitleBtnLeft.setBackgroundResource(R.drawable.back_btn_selector);
	}
	
	public void getMyQuestionList(String offset, String user_id) {
		showProgressDialog();
		RequestParams params = new RequestParams();
		params.addBodyParameter("offset", offset);
		params.addBodyParameter("user_id", user_id);
		

		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, ConfigUtil.MY_SERVICE_URL
				+ "getMyQuestionList.php", params, new RequestCallBack<String>() {
			
			@Override
			public void onSuccess(final ResponseInfo<String> responseInfo) {
				//System.out.println(responseInfo.result);
				
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						closeProgressDialog();
						ObjectMapper objectMapper = new ObjectMapper();
						String json = responseInfo.result;
						JSONObject jsonObject = null;
						try {
							jsonObject = new JSONObject(json);
							int code = jsonObject.getInt("code");
							if (code == 225) {
								getMyQuestionList list = objectMapper.readValue(json,
										getMyQuestionList.class);
								
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
							}else if (code == 325) {
								Toast.makeText(getApplicationContext(), "没有更多数据！",
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
	
	private List<Map<String, Object>> getData(getMyQuestionList mList) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Result> dataList = mList.getResult();

		for (Result result : dataList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("title", result.getTitle());
			map.put("summary", result.getId());
			map.put("time", result.getLast_modify_time());
			map.put("view_num", result.getView_num());
			map.put("comment_num", result.getAnswer_num());
			list.add(map);
		}

		return list;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		String messageId = (String) mData.get(position - 1).get("summary");
		Intent intent = new Intent();
		intent.putExtra("id", messageId);
		intent.setClass(this, QnADetailsActivity.class);
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
			newsListView.onRefreshComplete();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ivTitleBtnLeft:
			this.finish();
			break;

		default:
			break;
		}
		
	}
	
}
