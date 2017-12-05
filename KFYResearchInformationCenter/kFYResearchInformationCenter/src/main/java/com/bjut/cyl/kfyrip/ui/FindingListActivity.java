package com.bjut.cyl.kfyrip.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import com.bjut.cyl.kfyrip.adapter.NewsListAdapter;
import com.bjut.cyl.kfyrip.bean.getInfoList;
import com.bjut.cyl.kfyrip.bean.getInfoList.Data;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FindingListActivity extends DialogShowOffAct implements OnClickListener, OnItemClickListener {
private String which;
private ImageButton ivTitleBtnLeft;
private PullToRefreshListView newsListView;
private NewsListAdapter mAdapter;
private List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
private List<Map<String, Object>> mDataOk;
private int offset = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_finding_list);
		Intent intent = getIntent();
		which = intent.getStringExtra("which");
		//System.out.println(which);
		initView();
		
		newsListView = (PullToRefreshListView) findViewById(R.id.lv_finding);
		mAdapter = new NewsListAdapter(this, mData);
		newsListView.setAdapter(mAdapter);
		//System.out.println("***size" + mAdapter.getCount());
		newsListView.setOnItemClickListener(this);
		if (mData.size() == 0) {
			getTZListByType("0", which);
		}

		newsListView.setMode(Mode.BOTH);
		newsListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			// Pulling Down
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				mData.clear();
				offset = 0;
				getTZListByType("0", which);
				mAdapter.notifyDataSetChanged();
				new FinishRefresh().execute();
			}

			// Pulling Up
			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				offset = offset + 10;
				// new Thread(runnable2).start();
				getTZListByType(String.valueOf(offset), which);
				mAdapter.notifyDataSetChanged();
				new FinishRefresh().execute();

			}
		});
	}
	private void initView() {
		TextView toptitle = (TextView) findViewById(R.id.ivTitleName);
		if (which.equals("0")) {
			toptitle.setText("其他");
		}else if (which.equals("1")) {
			toptitle.setText("人文社科类");
		}else if (which.equals("2")) {
			toptitle.setText("基金");
		}else if (which.equals("3")) {
			toptitle.setText("科技部");
		}else if (which.equals("4")) {
			toptitle.setText("产业");
		}
		
		ivTitleBtnLeft = (ImageButton) this.findViewById(R.id.ivTitleBtnLeft);
		ivTitleBtnLeft.setOnClickListener(this);
		ivTitleBtnLeft.setBackgroundResource(R.drawable.back_btn_selector);
	}
	
	public void getTZListByType(String offset ,String tz_type) {
		showProgressDialog();
		RequestParams params = new RequestParams();
		params.addBodyParameter("offset", offset);
		params.addBodyParameter("tz_type", tz_type);
		
		// params.addBodyParameter("path", "/apps/测试应用/test文件夹");

		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, ConfigUtil.MY_SERVICE_URL
				+ "getTZListByType.php", params, new RequestCallBack<String>() {

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
							if (code == 226) {
								getInfoList list = objectMapper.readValue(json,
										getInfoList.class);
								
								if (list != null) {
									if (list.getResult().getData() == null) {
										Toast.makeText(getApplicationContext(), "没有更多数据！",
											     Toast.LENGTH_SHORT).show();
									}
									mDataOk = getData(list);// 填充数据
									//System.out.println("mdataok" + mDataOk);
									
									if (mDataOk.size() >= 0) {
										mData.addAll(mDataOk);
										 mDataOk.clear();
										//System.out.println(mData);
										mAdapter.notifyDataSetChanged();
									}

								}
							}else if (code == 326) {
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
	
	private List<Map<String, Object>> getData(getInfoList mList) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Data> dataList = mList.getResult().getData();

		for (Data result : dataList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("title", result.getTitle());
			map.put("summary", result.getId());
			map.put("time", result.getLast_modify_time());
			map.put("view_num", result.getView_num());
			map.put("comment_num", result.getComment_num());
			list.add(map);
		}

		return list;
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
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		String messageId = (String) mData.get(position - 1).get("summary");
		Intent intent = new Intent();
		intent.putExtra("id", messageId);
		intent.setClass(this, NotificationDetailsActivity.class);
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
}
