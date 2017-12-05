package com.bjut.cyl.kfyrip.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import com.bjut.cyl.kfyrip.adapter.HotWordsListAdapter;
import com.bjut.cyl.kfyrip.adapter.NewsListAdapter;
import com.bjut.cyl.kfyrip.utils.ConfigUtil;
import com.bjut.cyl.kfyrip.utils.HttpCallbackListener;
import com.bjut.cyl.kfyrip.utils.HttpUtil;
import com.bjut.cyl.kfyrip.utils.LogUtil;
import com.bjut.cyl.kfyrip.bean.getHotWords;
import com.bjut.cyl.kfyrip.bean.getHotWords.Data;
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
import com.bjut.cyl.kfyrip.ui.R;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends DialogShowOffAct implements
		OnItemClickListener, OnClickListener {
	private PullToRefreshListView hotwordListView;
	private EditText searchEdit;
	private TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9,tv10;
	private ImageButton ivTitleBtnLeft;
	private ImageView searchButton;
	private HotWordsListAdapter mAdapter;
	private List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> mDataOk;
	private int offset = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_search);
		//pref = PreferenceManager.getDefaultSharedPreferences(this);
		initViews();
		getHotWords("0");
	}

	private void initViews() {
		TextView title = (TextView) findViewById(R.id.ivTitleName);
		title.setText("搜索");
		tv1 = (TextView) findViewById(R.id.tv_hot1);
		tv2 = (TextView) findViewById(R.id.tv_hot2);
		tv3 = (TextView) findViewById(R.id.tv_hot3);
		tv4 = (TextView) findViewById(R.id.tv_hot4);
		tv5 = (TextView) findViewById(R.id.tv_hot5);
		tv6 = (TextView) findViewById(R.id.tv_hot6);
		tv7 = (TextView) findViewById(R.id.tv_hot7);
		tv8 = (TextView) findViewById(R.id.tv_hot8);
		tv9 = (TextView) findViewById(R.id.tv_hot9);
		tv10 = (TextView) findViewById(R.id.tv_hot10);
		tv1.setOnClickListener(this);
		tv2.setOnClickListener(this);
		tv3.setOnClickListener(this);
		tv4.setOnClickListener(this);
		tv5.setOnClickListener(this);
		tv6.setOnClickListener(this);
		tv7.setOnClickListener(this);
		tv8.setOnClickListener(this);
		tv9.setOnClickListener(this);
		tv10.setOnClickListener(this);
		searchEdit = (EditText) findViewById(R.id.edit_search);
		searchEdit.setHint("请输入关键字...");
		searchButton = (ImageView) findViewById(R.id.btn_search);
		searchButton.setOnClickListener(this);
		
		ivTitleBtnLeft = (ImageButton) this.findViewById(R.id.ivTitleBtnLeft);
		ivTitleBtnLeft.setOnClickListener(this);
		ivTitleBtnLeft.setBackgroundResource(R.drawable.back_btn_selector);
	}


	public void getHotWords(String offset) {
		showProgressDialog();
		RequestParams params = new RequestParams();
		params.addBodyParameter("offset", offset);
		

		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, ConfigUtil.MY_SERVICE_URL
				+ "getHotWords.php", params, new RequestCallBack<String>() {

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
							if (code == 224) {
								getHotWords list = objectMapper.readValue(json,
										getHotWords.class);

								if (list != null) {
									getData(list);// 填充数据
									//System.out.println("mdataok" + mDataOk);

									if (mDataOk.size() >= 0) {
										mData.addAll(mDataOk);
										mDataOk.clear();
										//System.out.println(mData);
										mAdapter.notifyDataSetChanged();
									}

								}
							} else if (code == 324) {
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

	private void getData(getHotWords mList) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Data> dataList = mList.getResult().getData();
		tv1.setText(dataList.get(0).getWord());
		tv6.setText(dataList.get(1).getWord());
		tv2.setText(dataList.get(2).getWord());
		tv7.setText(dataList.get(3).getWord());
		tv3.setText(dataList.get(4).getWord());
		tv8.setText(dataList.get(5).getWord());
		tv4.setText(dataList.get(6).getWord());
		tv9.setText(dataList.get(7).getWord());
		tv5.setText(dataList.get(8).getWord());
		tv10.setText(dataList.get(9).getWord());

	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		String keywords = (String) mData.get(position - 1).get("hotword");
		Intent intent = new Intent();
		intent.putExtra("keywords", keywords);
		intent.setClass(this, SearchResultActivity.class);
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
			hotwordListView.onRefreshComplete();
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.btn_search:
			String content = searchEdit.getText().toString().trim();
			if (content.isEmpty()) {
				Toast.makeText(getApplicationContext(), "关键字不能为空！",
					     Toast.LENGTH_SHORT).show();
			}else {
				intent.putExtra("keywords", searchEdit.getText().toString());
				intent.setClass(this, SearchResultActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}
			break;
			case R.id.tv_hot1:
				intent.putExtra("keywords", tv1.getText().toString());
				intent.setClass(this, SearchResultActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				break;
			case R.id.tv_hot2:
				intent.putExtra("keywords", tv2.getText().toString());
				intent.setClass(this, SearchResultActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				break;
			case R.id.tv_hot10:
				intent.putExtra("keywords", tv10.getText().toString());
				intent.setClass(this, SearchResultActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				break;
			case R.id.tv_hot3:
				intent.putExtra("keywords", tv3.getText().toString());
				intent.setClass(this, SearchResultActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				break;
			case R.id.tv_hot4:
				intent.putExtra("keywords", tv4.getText().toString());
				intent.setClass(this, SearchResultActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				break;
			case R.id.tv_hot5:
				intent.putExtra("keywords", tv5.getText().toString());
				intent.setClass(this, SearchResultActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				break;
			case R.id.tv_hot6:
				intent.putExtra("keywords", tv6.getText().toString());
				intent.setClass(this, SearchResultActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				break;
			case R.id.tv_hot7:
				intent.putExtra("keywords", tv7.getText().toString());
				intent.setClass(this, SearchResultActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				break;
			case R.id.tv_hot8:
				intent.putExtra("keywords", tv8.getText().toString());
				intent.setClass(this, SearchResultActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				break;
			case R.id.tv_hot9:
				intent.putExtra("keywords", tv9.getText().toString());
				intent.setClass(this, SearchResultActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				break;
		case R.id.ivTitleBtnLeft:
			this.finish();
			break;
		default:
			break;
		}
	}
}
