package com.bjut.cyl.kfyrip.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bjut.cyl.kfyrip.adapter.CollectListAdapter;
import com.bjut.cyl.kfyrip.bean.getMyBMList;
import com.bjut.cyl.kfyrip.utils.ConfigUtil;
import com.bjut.cyl.kfyrip.utils.LogUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by apple on 15/8/25.
 */
public class MySignUpActivity extends DialogShowOffAct implements AdapterView.OnItemClickListener,View.OnClickListener{
    private PullToRefreshListView newsListView;
    private CollectListAdapter mAdapter;
    private ImageButton ivTitleBtnLeft;
    private List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> mDataOk;
    private int offset = 0;
    private SharedPreferences pref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        LogUtil.d("trace", "MySignUp的OnCreateView");
        setContentView(R.layout.activity_my_question);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        initViews();

        newsListView = (PullToRefreshListView)findViewById(R.id.lv_my_question);
        mAdapter = new CollectListAdapter(this, mData);
        newsListView.setAdapter(mAdapter);
        //System.out.println("***size" + mAdapter.getCount());
        newsListView.setOnItemClickListener(this);
        if (mData.size() == 0) {
            getCollectList("1" , pref.getString("username", ""));
        }

        newsListView.setMode(PullToRefreshBase.Mode.BOTH);
    }

    private void initViews() {
        ivTitleBtnLeft = (ImageButton) this.findViewById(R.id.ivTitleBtnLeft);
        ivTitleBtnLeft.setOnClickListener(this);
        ivTitleBtnLeft.setBackgroundResource(R.drawable.back_btn_selector);
        TextView toptitle = (TextView) findViewById(R.id.ivTitleName);
        toptitle.setText("我的报名");
    }



    public void getCollectList(String object_type, String user_id) {
        showProgressDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("object_type", object_type);
        params.addBodyParameter("user_id", user_id);

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, ConfigUtil.MY_SERVICE_URL
                + "getMyBMList.php", params, new RequestCallBack<String>() {

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
                            if (code == 231) {
                                getMyBMList list = objectMapper.readValue(json,
                                        getMyBMList.class);

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

    private List<Map<String, Object>> getData(getMyBMList mList) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<getMyBMList.Data> dataList = mList.getResult().getData();

        for (getMyBMList.Data result : dataList) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", result.getTitle());
            map.put("id", result.getId());
            list.add(map);
        }

        return list;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String messageId = (String) mData.get(position - 1).get("id");
        Intent intent = new Intent();
        intent.putExtra("id", messageId);
        intent.setClass(this, NotificationDetailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivTitleBtnLeft:
                this.finish();
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
            newsListView.onRefreshComplete();
        }
    }
}


