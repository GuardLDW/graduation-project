package com.bjut.cyl.kfyrip.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import com.bjut.cyl.kfyrip.adapter.CommentListAdapter;
import com.bjut.cyl.kfyrip.adapter.NewsListAdapter;
import com.bjut.cyl.kfyrip.bean.getCommentList;
import com.bjut.cyl.kfyrip.bean.getCommentList.Data;
import com.bjut.cyl.kfyrip.other.CommentDialog;
import com.bjut.cyl.kfyrip.utils.ConfigUtil;
import com.bjut.cyl.kfyrip.utils.PromptUtil;
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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class NotiCommentListActivity extends DialogShowOffAct implements
		OnClickListener {
	private PullToRefreshListView commentListView;
	private CommentListAdapter mAdapter;
	private List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> mDataOk;
	private String messageId;
	private LinearLayout writeLl;
	private ImageButton ivTitleBtnLeft;
	private int offset = 0;
	private SharedPreferences pref;
	private CommentDialog commentDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_comment_list);
		Intent intent = getIntent();
		messageId = intent.getStringExtra("id");
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		initView();

		commentListView = (PullToRefreshListView) findViewById(R.id.lv_comment);
		mAdapter = new CommentListAdapter(this, mData);
		commentListView.setAdapter(mAdapter);
		if (mData.size() == 0) {
			getCommentList(messageId, ConfigUtil.CHANNEL_ID_NOTIFICATION, "0");
		}

		commentListView.setMode(Mode.PULL_UP_TO_REFRESH);
		commentListView
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {

					// Pulling Down
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						mData.clear();
						offset = 0;
						getCommentList(messageId, ConfigUtil.CHANNEL_ID_NOTIFICATION, "0");
						mAdapter.notifyDataSetChanged();
						new FinishRefresh().execute();
					}

					// Pulling Up
					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						offset = offset + 10;
						getCommentList(messageId, ConfigUtil.CHANNEL_ID_NOTIFICATION, String.valueOf(offset));
						mAdapter.notifyDataSetChanged();
						new FinishRefresh().execute();

					}
				});
	}

	private void initView() {
		ivTitleBtnLeft = (ImageButton) findViewById(R.id.ivTitleBtnLeft);
		ivTitleBtnLeft.setOnClickListener(this);
		ivTitleBtnLeft.setBackgroundResource(R.drawable.back_btn_selector);
		writeLl = (LinearLayout) findViewById(R.id.ll_write);
		writeLl.setOnClickListener(this);
		TextView title = (TextView) findViewById(R.id.ivTitleName);
		title.setText("评论列表");
	}

	public void getCommentList(String id, String channel_id, String offset) {
		showProgressDialog();
		RequestParams params = new RequestParams();
		params.addBodyParameter("id", id);
		params.addBodyParameter("channel_id", channel_id);
		params.addBodyParameter("offset", offset);

		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, ConfigUtil.MY_SERVICE_URL
				+ "getCommentList.php", params, new RequestCallBack<String>() {

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
							if (code == 212) {
								getCommentList list = objectMapper.readValue(
										json, getCommentList.class);

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
							} else if (code == 312) {
								 Toast.makeText(getApplicationContext(), "没有更多评论！",
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

	private List<Map<String, Object>> getData(getCommentList mList) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Data> dataList = mList.getResult().getData();

		for (Data result : dataList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", result.getNickname());
			map.put("create_time", result.getCreate_time());
			map.put("comments", result.getContent());
			list.add(map);
		}

		return list;
	}

	public void publishComment(String id ,String channel_id, String creator_id, String content) {
		showProgressDialog();
		RequestParams params = new RequestParams();
		params.addBodyParameter("id", id);
		params.addBodyParameter("channel_id", channel_id);
		params.addBodyParameter("creator_id", creator_id);
		params.addBodyParameter("content", content);

		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, ConfigUtil.MY_SERVICE_URL
				+ "publishComment.php", params, new RequestCallBack<String>() {

			@Override
			public void onSuccess(final ResponseInfo<String> responseInfo) {
				// resultText.setText("upload response:" + responseInfo.result);
				//System.out.println(responseInfo.result);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						closeProgressDialog();
						String json = responseInfo.result;
						JSONObject jsonObject = null;
						try {
							jsonObject = new JSONObject(json);
							int code = jsonObject.getInt("code");
							if (code == 217) {
								Toast.makeText(getApplicationContext(), "评论成功！",
									     Toast.LENGTH_SHORT).show();
								mData.clear();
								getCommentList(messageId, ConfigUtil.CHANNEL_ID_NOTIFICATION, "0");
							}else if (code == 399){
								Toast.makeText(getApplicationContext(), "请登录！",
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
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ivTitleBtnLeft:
			this.finish();
			break;
		case R.id.ll_write:
			LayoutInflater inflater=LayoutInflater.from(NotiCommentListActivity.this);
			commentDialog= new CommentDialog(NotiCommentListActivity.this, "评论") {

				@Override
				public void clickCancelCallBack() {
					// TODO Auto-generated method stub
					commentDialog.dismiss();
				}

				@Override
				public void clickOkCallBack() {
					String content = commentDialog.getText();
					content = content.trim();
					if (!content.isEmpty()){
						publishComment(messageId,
								ConfigUtil.CHANNEL_ID_NOTIFICATION,
								pref.getString("username", ""), content);
						Intent intent = new Intent();
						intent.putExtra("id", messageId);
						intent.setClass(NotiCommentListActivity.this,
								NotiCommentListActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						startActivity(intent);
						commentDialog.dismiss();
					}else {
						PromptUtil.showToast("评论内容不能空！");
					}

				}
			};
			commentDialog.setView(new EditText(this));
			commentDialog.show();
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					commentDialog.showKeyboard();
				}
			}, 200);
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
			commentListView.onRefreshComplete();
		}
	}

}
