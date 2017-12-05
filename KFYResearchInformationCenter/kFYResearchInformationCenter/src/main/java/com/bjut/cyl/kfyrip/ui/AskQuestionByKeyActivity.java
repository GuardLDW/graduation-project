package com.bjut.cyl.kfyrip.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import com.bjut.cyl.kfyrip.adapter.QuestionByKeyListAdapter;
import com.bjut.cyl.kfyrip.bean.getQuestionList;
import com.bjut.cyl.kfyrip.bean.getQuestionList.Data;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AskQuestionByKeyActivity extends DialogShowOffAct implements
		OnClickListener, OnItemClickListener {
	public static AskQuestionByKeyActivity instance = null;
	private SharedPreferences pref;
	private EditText titleEdit, contentEdit;
	private Button submitBtn, nextBtn;
	private String keywords = "" ,newId;
	private ImageButton ivTitleBtnLeft;
	private PullToRefreshListView qnaListView;
	private QuestionByKeyListAdapter mAdapter;
	private List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> mDataOk;
	private int offset = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ask_question_bykey);
		instance = this;
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		initView();
		
		qnaListView = (PullToRefreshListView) findViewById(R.id.lv_questionbykey);
		mAdapter = new QuestionByKeyListAdapter(this, mData);
		qnaListView.setAdapter(mAdapter);
		//System.out.println("***size" + mAdapter.getCount());
		qnaListView.setOnItemClickListener(this);
		// if (mData.size() == 0) {
		// getQuestionListByKeywords("0", "10");
		// }

		qnaListView.setMode(Mode.BOTH);
		qnaListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			// Pulling Down
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				mData.clear();
				offset = 0;
				getQuestionListByKeywords("0", keywords,
						pref.getString("username", ""));
				mAdapter.notifyDataSetChanged();
				new FinishRefresh().execute();
			}

			// Pulling Up
			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				offset = offset + 10;
				// new Thread(runnable2).start();
				getQuestionListByKeywords(String.valueOf(offset), keywords,
						pref.getString("username", ""));
				mAdapter.notifyDataSetChanged();
				new FinishRefresh().execute();

			}
		});
	}

	private void initView() {
		TextView title = (TextView) findViewById(R.id.ivTitleName);
		title.setText("提问");
		titleEdit = (EditText) findViewById(R.id.edit_title);
		titleEdit.setHint("请输入主题");
		titleEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				mData.clear();
				mAdapter.notifyDataSetChanged();
				keywords = titleEdit.getText().toString();
				keywords = keywords.trim();
				getQuestionListByKeywords("0", keywords,
						pref.getString("username", ""));
			}

		});
		ivTitleBtnLeft = (ImageButton) this.findViewById(R.id.ivTitleBtnLeft);
		ivTitleBtnLeft.setOnClickListener(this);
		ivTitleBtnLeft.setBackgroundResource(R.drawable.back_btn_selector);

		nextBtn = (Button) findViewById(R.id.btn_next_step);
		nextBtn.setOnClickListener(this);
		nextBtn.setVisibility(View.VISIBLE);
	}

	
	public void getQuestionListByKeywords(String offset, String keywords,
			String user_id) {
		// showProgressDialog();
		RequestParams params = new RequestParams();
		params.addBodyParameter("offset", offset);
		params.addBodyParameter("keywords", keywords);
		params.addBodyParameter("user_id", user_id);

		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, ConfigUtil.MY_SERVICE_URL
				+ "getQuestionListByKeywords.php", params,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(
							final ResponseInfo<String> responseInfo) {
						//System.out.println(responseInfo.result);

						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// closeProgressDialog();
								ObjectMapper objectMapper = new ObjectMapper();
								String json = responseInfo.result;
								JSONObject jsonObject = null;
								try {
									jsonObject = new JSONObject(json);
									int code = jsonObject.getInt("code");
									if (code == 215) {
										getQuestionList list = objectMapper
												.readValue(json,
														getQuestionList.class);

										if (list != null) {
											mDataOk = getData(list);// 填充数据
											//System.out.println("mdataok"+ mDataOk);

											if (mDataOk.size() >= 0) {
												mData.addAll(mDataOk);
												mDataOk.clear();
												//System.out.println(mData);
												mAdapter.notifyDataSetChanged();
											}

										}
									} else if (code == 310) {
										Toast.makeText(getApplicationContext(),
												"没有更多数据！", Toast.LENGTH_SHORT)
												.show();
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
				map.put("comment_time", result.getLast_modify_time());
				map.put("click_num", result.getView_num());
				map.put("comment_num", result.getAnswer_num());
				map.put("id", result.getId());
				list.add(map);
			} else {

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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_next_step:
			if (keywords.equals("")) {
				Toast.makeText(getApplicationContext(),
						"主题不能为空！", Toast.LENGTH_SHORT)
						.show();
			}else {
				Intent intent = new Intent();
				intent.putExtra("title", keywords);
				intent.setClass(this, AskQuestionActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}
			break;
		case R.id.ivTitleBtnLeft:
			this.finish();
			break;
		default:
			break;
		}
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
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String messageId = (String) mData.get(position - 1).get("id");
		Intent intent = new Intent();
		intent.putExtra("id", messageId);
		intent.setClass(this, QnADetailsActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);

	}
}
