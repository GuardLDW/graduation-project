package com.bjut.cyl.kfyrip.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bjut.cyl.kfyrip.utils.ConfigUtil;
import com.bjut.cyl.kfyrip.utils.KeyBoardHelper;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

public class LoginActivity extends DialogShowOffAct implements OnClickListener {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private EditText usernameEditText, passwordEditText;
    private String username, password, userId;
    private Button loginButton, visitorButton;
    private LeftMenu leftMenu;
    public static LoginActivity instance = null;
    private RelativeLayout content;
    private RelativeLayout bottom;
    private KeyBoardHelper keyBoardHelper;
    private int bottomHeight;
    private String registrationID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//		int mode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;
//		getWindow().setSoftInputMode(mode);
        setContentView(R.layout.activity_login);
        registrationID = JPushInterface.getRegistrationID(LoginActivity.this);
        instance = this;
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        initView();
    }

    private void initView() {
        content = (RelativeLayout) findViewById(R.id.content_login);
        bottom = (RelativeLayout) findViewById(R.id.bottom);
        usernameEditText = (EditText) findViewById(R.id.editText_Account);
        passwordEditText = (EditText) findViewById(R.id.editText_password);
        String present_user = pref.getString("username", "");
        if (present_user.equals("anonymous")) {
            editor = pref.edit();
            editor.clear();
            editor.commit();
        }
        usernameEditText.setText(pref.getString("username", ""));
        passwordEditText.setText(pref.getString("password", ""));
        loginButton = (Button) findViewById(R.id.btn_login);
        loginButton.setOnClickListener(this);
        visitorButton = (Button) findViewById(R.id.visitor_login);
        visitorButton.setOnClickListener(this);
        usernameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //软键盘遮挡问题
                    keyBoardHelper = new KeyBoardHelper(LoginActivity.this);
                    keyBoardHelper.onCreate();
                    keyBoardHelper.setOnKeyBoardStatusChangeListener(onKeyBoardStatusChangeListener);
                    bottom.post(new Runnable() {
                        @Override
                        public void run() {
                            bottomHeight = bottom.getHeight();
                        }
                    });
                }
            }
        });

        passwordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //软键盘遮挡问题
                    keyBoardHelper = new KeyBoardHelper(LoginActivity.this);
                    keyBoardHelper.onCreate();
                    keyBoardHelper.setOnKeyBoardStatusChangeListener(onKeyBoardStatusChangeListener);
                    bottom.post(new Runnable() {
                        @Override
                        public void run() {
                            bottomHeight = bottom.getHeight();
                        }
                    });
                }
            }
        });

    }

    private KeyBoardHelper.OnKeyBoardStatusChangeListener onKeyBoardStatusChangeListener = new KeyBoardHelper.OnKeyBoardStatusChangeListener() {

        @Override
        public void OnKeyBoardPop(int keyBoardheight) {

            final int height = keyBoardheight;
            if (bottomHeight > height) {
                bottom.setVisibility(View.GONE);
            } else {
                int offset = bottomHeight - height - 25;
                final ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) content
                        .getLayoutParams();
                lp.topMargin = offset;
                content.setLayoutParams(lp);
            }

        }

        @Override
        public void OnKeyBoardClose(int oldKeyBoardheight) {
            if (View.VISIBLE != bottom.getVisibility()) {
                bottom.setVisibility(View.VISIBLE);
            }
            final ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) content
                    .getLayoutParams();
            if (lp.topMargin != 0) {
                lp.topMargin = 0;
                content.setLayoutParams(lp);
            }

        }
    };

    public void login(String username, String password,String registrationID) {
        showProgressDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("username", username);
        params.addBodyParameter("pwd", password);
        params.addBodyParameter("RegistrationId", registrationID);

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, ConfigUtil.MY_SERVICE_URL
                + "login.php", params, new RequestCallBack<String>() {

            @Override
            public void onStart() {
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
            }

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
                            if (code == 221) {
                                userId = jsonObject.getString("result");
                                editor = pref.edit();
                                editor.putString("user_id", userId);
                                editor.commit();
                                Intent intent = new Intent(LoginActivity.this,
                                        MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(intent);
                            } else if (code == 3212) {
                                Toast.makeText(getApplicationContext(),
                                        "密码错误！", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            finish();
            WelComeActivity.instance.finish();
            System.exit(0);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                username = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();
                editor = pref.edit();
                editor.putString("username", username);
                editor.putString("password", password);
                editor.commit();
                login(username, password,registrationID);
                break;
            case R.id.visitor_login:
                editor = pref.edit();
                editor.clear();
                editor.putString("username", "anonymous");
                editor.putString("password", "kfy");
                editor.commit();
                //System.out.println(pref.getString("username", "") + pref.getString("password", ""));
                login("anonymous", "kfy",registrationID);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        keyBoardHelper.onDestory();
    }

}
