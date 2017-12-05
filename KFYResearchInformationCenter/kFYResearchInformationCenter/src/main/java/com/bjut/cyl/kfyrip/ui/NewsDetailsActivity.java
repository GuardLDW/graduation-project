package com.bjut.cyl.kfyrip.ui;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import com.bjut.cyl.kfyrip.bean.getInfoDetailList;
import com.bjut.cyl.kfyrip.bean.getInfoDetailList.Result;
import com.bjut.cyl.kfyrip.bean.getInfoDetailList.Result.Info;
import com.bjut.cyl.kfyrip.bean.getInfoList;
import com.bjut.cyl.kfyrip.other.CommentDialog;
import com.bjut.cyl.kfyrip.utils.ConfigUtil;
import com.bjut.cyl.kfyrip.utils.LogUtil;
import com.bjut.cyl.kfyrip.utils.PromptUtil;
import com.bjut.cyl.kfyrip.utils.ScreenUtils;
import com.bjut.cyl.kfyrip.utils.px2dp;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class NewsDetailsActivity extends DialogShowOffAct implements
		OnClickListener {
	private ImageButton ivTitleBtnLeft;
	private TextView messageTitleTv,commentTv;
	private TextView timeTv;
	private WebView contentWv;
	private SharedPreferences pref;
	private LinearLayout writeLl, commentLl;
	private String messageId;
	private Button ivTitleBtnRight;
	private int iscollect;
	private CommentDialog commentDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_message_details);
		Intent intent = getIntent();
		messageId = intent.getStringExtra("id");
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		getInfoDetails(messageId);
		initView();

	}

	private void getData(getInfoDetailList mList) {
		int screenWidth = px2dp.px2dip(this, ScreenUtils.getScreenWidth(this));
		messageTitleTv = (TextView) findViewById(R.id.message_title);
		timeTv = (TextView) findViewById(R.id.message_time);
		commentTv = (TextView) findViewById(R.id.comment_num);
		Info info = mList.getResult().getInfo();
		String content = info.getContent();
		String newcontent = content.replaceAll("/cms", "http://www2nd.bjut.edu.cn/kjcxn");
		newcontent = newcontent.replaceAll("attachid","style=\"height:auto;width:" + String.valueOf(screenWidth-30) + "px\" attachid");
		LogUtil.i("out",newcontent);
		messageTitleTv.setText(info.getTitle());
		timeTv.setText(info.getLast_modify_time());
		commentTv.setText(info.getComment_num());
		contentWv = (WebView) findViewById(R.id.wv_content);
		contentWv.loadDataWithBaseURL(null, newcontent, "text/html",
				"utf-8", null);
	}

	private void initView() {
		ivTitleBtnLeft = (ImageButton) this.findViewById(R.id.ivTitleBtnLeft);
		ivTitleBtnLeft.setOnClickListener(this);
		ivTitleBtnLeft.setBackgroundResource(R.drawable.back_btn_selector);
		ivTitleBtnRight = (Button) findViewById(R.id.ivTitleBtnRight);
		ivTitleBtnRight.setBackgroundResource(R.drawable.collect);
		ivTitleBtnRight.setOnClickListener(this);
		ivTitleBtnRight.setVisibility(View.VISIBLE);
		writeLl = (LinearLayout) findViewById(R.id.ll_write);
		commentLl = (LinearLayout) findViewById(R.id.ll_comment);
		writeLl.setOnClickListener(this);
		commentLl.setOnClickListener(this);
		TextView toptitle = (TextView) findViewById(R.id.ivTitleName);
		toptitle.setText("新闻详情");

	}

	public void getInfoDetails(String id) {
		showProgressDialog();
		RequestParams params = new RequestParams();
		params.addBodyParameter("id", id);
		params.addBodyParameter("user_id", pref.getString("username", ""));
		// params.addBodyParameter("path", "/apps/测试应用/test文件夹");

		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, ConfigUtil.MY_SERVICE_URL
				+ "getInfoDetail.php", params, new RequestCallBack<String>() {

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
							if (code == 211) {
								getInfoDetailList list = objectMapper
										.readValue(json,
												getInfoDetailList.class);
								if (list.getResult().getInfo().getCollect() == 1) {
									ivTitleBtnRight.setBackgroundResource(R.drawable.collect_click);
									iscollect = 1;
								} else if (list.getResult().getInfo()
										.getCollect() == 0) {
									ivTitleBtnRight.setBackgroundResource(R.drawable.collect);
									iscollect = 0;
								};
								if (list != null) {
									getData(list);

								}
							}else if(code == 311){
								Toast.makeText(getApplicationContext(),
										"新闻不存在！", Toast.LENGTH_SHORT).show();
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
	
	public void uncollect(String user_id, String object_id, String object_type) {
		showProgressDialog();
		RequestParams params = new RequestParams();
		params.addBodyParameter("user_id", user_id);
		params.addBodyParameter("object_id", object_id);
		params.addBodyParameter("object_type", object_type);

		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, ConfigUtil.MY_SERVICE_URL
				+ "uncollect.php", params, new RequestCallBack<String>() {

			@Override
			public void onSuccess(final ResponseInfo<String> responseInfo) {
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
							if (code == 222) {
								Toast.makeText(getApplicationContext(),
										"取消收藏成功！", Toast.LENGTH_SHORT).show();
								getInfoDetails(messageId);
							}else if (code == 399) {
								Toast.makeText(getApplicationContext(),
										"请登录！", Toast.LENGTH_SHORT).show();
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

	public void collect(String user_id, String object_id, String object_type) {
		showProgressDialog();
		RequestParams params = new RequestParams();
		params.addBodyParameter("user_id", user_id);
		params.addBodyParameter("object_id", object_id);
		params.addBodyParameter("object_type", object_type);

		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, ConfigUtil.MY_SERVICE_URL
				+ "collect.php", params, new RequestCallBack<String>() {

			@Override
			public void onSuccess(final ResponseInfo<String> responseInfo) {
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
							if (code == 219) {
								Toast.makeText(getApplicationContext(),
										"收藏成功！", Toast.LENGTH_SHORT).show();
								getInfoDetails(messageId);
							}else if (code == 399) {
								Toast.makeText(getApplicationContext(),
										"请登录！", Toast.LENGTH_SHORT).show();
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
		case R.id.ivTitleBtnRight:
			if (iscollect == 1) {
				uncollect(pref.getString("username", ""), messageId, "2");
			} else if (iscollect == 0) {
				collect(pref.getString("username", ""), messageId, "2");
			}
			break;
		case R.id.ll_comment:
			Intent intent = new Intent();
			intent.putExtra("id", messageId);
			intent.setClass(this, NewsCommentListActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			break;
		case R.id.ll_write:
			LayoutInflater inflater=LayoutInflater.from(NewsDetailsActivity.this);
			commentDialog= new CommentDialog(NewsDetailsActivity.this, "评论") {

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
						intent.setClass(NewsDetailsActivity.this,
								NotiCommentListActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						startActivity(intent);
						commentDialog.dismiss();
					}else {
						PromptUtil.showToast(getString(R.string.comment_content_cannot_be_empty));
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

}
