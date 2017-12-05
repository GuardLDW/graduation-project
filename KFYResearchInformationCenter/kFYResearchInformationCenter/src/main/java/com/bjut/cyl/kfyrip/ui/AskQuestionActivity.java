package com.bjut.cyl.kfyrip.ui;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import com.bjut.cyl.kfyrip.bean.getQuestionList;
import com.bjut.cyl.kfyrip.utils.ConfigUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class AskQuestionActivity extends DialogShowOffAct implements
		OnClickListener {
	public static AskQuestionActivity instance = null;
	private SharedPreferences pref;
	private EditText  contentEdit;
	private Button submitBtn;
	private String title,content,newId;
	private ImageButton ivTitleBtnLeft;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ask_question);
		instance = this;
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		Intent intent = getIntent();
		title = intent.getStringExtra("title");
		getNewId();
		initView();

	}

	private void initView() {
		TextView title = (TextView) findViewById(R.id.ivTitleName);
		title.setText("提问");
		contentEdit = (EditText) findViewById(R.id.edit_content);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			public void run() {
				InputMethodManager inputManager = (InputMethodManager) contentEdit.getContext().getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(contentEdit, 0);
			}

		}, 300);
		contentEdit.setHint("请输入内容");
		contentEdit.requestFocus();
		submitBtn = (Button) findViewById(R.id.btn_next_step);
		submitBtn.setOnClickListener(this);
		submitBtn.setText("提交");
		submitBtn.setVisibility(View.VISIBLE);
		
		ivTitleBtnLeft = (ImageButton) this.findViewById(R.id.ivTitleBtnLeft);
		ivTitleBtnLeft.setOnClickListener(this);
		ivTitleBtnLeft.setBackgroundResource(R.drawable.back_btn_selector);
	}
	public void getNewId() {
		// showProgressDialog();
		RequestParams params = new RequestParams();

		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, ConfigUtil.MY_SERVICE_URL
				+ "getQuestionList.php", params,
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
										newId = list.getResult().getData().get(0).getId();
										//System.out.println("NEW!!!!!" + newId);
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
	
	public void publishQuestion(String creator_id, String title, String content) {
		showProgressDialog();
		RequestParams params = new RequestParams();
		params.addBodyParameter("creator_id", creator_id);
		params.addBodyParameter("title", title);
		params.addBodyParameter("content", content);

		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, ConfigUtil.MY_SERVICE_URL
				+ "publishQuestion.php", params, new RequestCallBack<String>() {

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
							if (code == 213) {
								Toast.makeText(getApplicationContext(),
										"问题发表成功！", Toast.LENGTH_SHORT).show();
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
		case R.id.btn_next_step:
			content = contentEdit.getText().toString();
			content = content.trim();
			//System.out.println(title + content);
			publishQuestion(pref.getString("username", ""), title, content);
			Intent intent = new Intent();
			intent.putExtra("id", String.valueOf(Integer.valueOf(newId).intValue()+1));
			intent.setClass(this, QnADetailsActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			AskQuestionByKeyActivity.instance.finish();
			AskQuestionActivity.instance.finish();
			break;
		case R.id.ivTitleBtnLeft:
			this.finish();
			break;
		default:
			break;
		}
	}

}
