package com.bjut.cyl.kfyrip.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bjut.cyl.kfyrip.adapter.NewsListAdapter;
import com.bjut.cyl.kfyrip.utils.ConfigUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2017/12/8.
 */

public class ScienceFragment extends DialogShowOffFrag implements View.OnClickListener,AdapterView.OnItemClickListener{

    private View layoutView;
    private TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7;
    private int currIndex = 0;// 当前页卡编号
    private List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> mDataOk;
    private int offset = 0;
    private NewsListAdapter mAdapter;
    private PullToRefreshListView newsListView;
    private String tz_type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        currIndex = 0;
        layoutView = inflater.inflate(R.layout.fragment_science, container, false);
        initViews();
        tz_type = "1";
        newsListView = (PullToRefreshListView) layoutView.findViewById(R.id.lv_science);
        mAdapter = new NewsListAdapter(getActivity(), mData);
        newsListView.setAdapter(mAdapter);
        newsListView.setOnItemClickListener(this);
        if (mData.size() == 0) {
            getTZListByType("0", tz_type);
        }
        newsListView.setMode(PullToRefreshBase.Mode.BOTH);
        newsListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                mData.clear();
                offset = 0;
                getTZListByType("0", tz_type);
                mAdapter.notifyDataSetChanged();
                new ScienceFragment.FinishRefresh().execute();
            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                offset = offset + 10;
                getTZListByType(String.valueOf(offset), tz_type);
                mAdapter.notifyDataSetChanged();
                new ScienceFragment.FinishRefresh().execute();
            }
        });

        return layoutView;
    }


    public void getTZListByType(String offset ,String tz_type) {
        showProgressDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("offset", offset);
        params.addBodyParameter("tz_type", tz_type);

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, ConfigUtil.MY_SERVICE_URL
                + "getTZListByType.php", params, new RequestCallBack<String>() {

            @Override
            public void onStart() {
            }
            @Override
            public void onLoading(long total, long current, boolean isUploading) {
            }
            @Override
            public void onSuccess(final ResponseInfo<String> responseInfo) {
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
                            if (code == 226) {
                                mDataOk = getData(jsonObject);// 填充数据
                                //System.out.println("mdataok" + mDataOk);

                                if (mDataOk.size() >= 0) {
                                    mData.addAll(mDataOk);
                                    mDataOk.clear();
                                    //System.out.println(mData);
                                    mAdapter.notifyDataSetChanged();
                                }
                            } else if (code == 326) {
                                mAdapter.notifyDataSetChanged();
                                Toast.makeText(getActivity(), "没有更多数据！",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
            @Override
            public void onFailure(HttpException error, String msg) {
            }
        });
    }


    private List<Map<String, Object>> getData(JSONObject jsonObject) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            JSONArray jsonArray = jsonObject.getJSONObject("result").getJSONArray("info.list");
            for (int i = 0; i < jsonArray.length(); i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("title", jsonArray.getJSONObject(i).getString("title"));
                map.put("summary", jsonArray.getJSONObject(i).getString("id"));
                map.put("time", jsonArray.getJSONObject(i).getString("last_modify_time"));
                map.put("view_num", jsonArray.getJSONObject(i).getString("view_num"));
                map.put("comment_num", jsonArray.getJSONObject(i).getString("comment_num"));
                list.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return list;
    }

    private void initViews() {
        TextView title = (TextView) getActivity().findViewById(R.id.ivTitleName);
        title.setText("科研信息");
        tv1 = (TextView) layoutView.findViewById(R.id.tv_1);
        tv2 = (TextView) layoutView.findViewById(R.id.tv_2);
        tv3 = (TextView) layoutView.findViewById(R.id.tv_3);
        tv4 = (TextView) layoutView.findViewById(R.id.tv_4);
        tv5 = (TextView) layoutView.findViewById(R.id.tv_5);
        tv6 = (TextView) layoutView.findViewById(R.id.tv_6);
        tv7 = (TextView) layoutView.findViewById(R.id.tv_7);
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        tv4.setOnClickListener(this);
        tv5.setOnClickListener(this);
        tv6.setOnClickListener(this);
        tv7.setOnClickListener(this);
        //默认第一个选中
        tv1.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_finding_textview_selected));
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String messageId = (String) mData.get(position - 1).get("summary");
        Intent intent = new Intent();
        intent.putExtra("id", messageId);
        intent.setClass(getActivity(), NotificationDetailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_1:
                changeStatue(tv1);
                if (currIndex != 0){
                    mData.clear();
                    currIndex = 0;
                    tz_type = "1";
                    getTZListByType("0",tz_type);
                }
                break;
            case R.id.tv_2:
                changeStatue(tv2);
                if (currIndex != 1){
                    mData.clear();
                    currIndex = 1;
                    tz_type = "2";
                    getTZListByType("0", tz_type);
                }
                break;
            case R.id.tv_3:
                changeStatue(tv3);
                if (currIndex != 2){
                    mData.clear();
                    currIndex = 2;
                    tz_type = "3";
                    getTZListByType("0", tz_type);
                }

                break;
            case R.id.tv_4:
                changeStatue(tv4);
                if (currIndex != 3){
                    mData.clear();
                    currIndex = 3;
                    tz_type = "4";
                    getTZListByType("0", tz_type);
                }

                break;
            case R.id.tv_5:
                changeStatue(tv5);
                if (currIndex != 4){
                    mData.clear();
                    currIndex = 4;
                    tz_type = "5";
                    getTZListByType("0",tz_type);
                }
                break;
            case R.id.tv_6:
                changeStatue(tv6);
                if (currIndex != 5){
                    mData.clear();
                    currIndex = 5;
                    tz_type = "0";
                    getTZListByType("0",tz_type);
                }
                break;
            case R.id.tv_7:
                changeStatue(tv7);
                if (currIndex != 6){
                    mData.clear();
                    currIndex = 6;
                    tz_type = "0";
                    getTZListByType("0",tz_type);
                }
                break;
        }
    }

    private void changeStatue(TextView tv) {
        tv1.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_finding_textview_normal));
        tv2.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_finding_textview_normal));
        tv3.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_finding_textview_normal));
        tv4.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_finding_textview_normal));
        tv5.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_finding_textview_normal));
        tv6.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_finding_textview_normal));
        tv7.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_finding_textview_normal));
        tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_finding_textview_selected));
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

