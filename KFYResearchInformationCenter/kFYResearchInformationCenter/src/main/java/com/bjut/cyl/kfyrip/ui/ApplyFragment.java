package com.bjut.cyl.kfyrip.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bjut.cyl.kfyrip.adapter.NewsListAdapter;
import com.bjut.cyl.kfyrip.bean.getBMList;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by apple on 15/8/13.
 */
public class ApplyFragment extends DialogShowOffFrag implements AdapterView.OnItemClickListener {
    private View layoutView;
    private PullToRefreshListView applyListView;
    private NewsListAdapter mAdapter;
    private List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> mDataOk;
    private int offset = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LogUtil.d("trace", "ApplyFragment的OnCreateView");
        layoutView = inflater.inflate(R.layout.fragment_notification, container,
                false);
        initViews();

        applyListView = (PullToRefreshListView) layoutView
                .findViewById(R.id.lv_infomation);
        mAdapter = new NewsListAdapter(getActivity(), mData);
        applyListView.setAdapter(mAdapter);
        //System.out.println("***size" + mAdapter.getCount());
        applyListView.setOnItemClickListener(this);
        if (mData.size() == 0) {
            getBMList("0");
        }

        applyListView.setMode(PullToRefreshBase.Mode.BOTH);
        applyListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            // Pulling Down
            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                mData.clear();
                offset = 0;
                getBMList("0");
                mAdapter.notifyDataSetChanged();
                new FinishRefresh().execute();
            }

            // Pulling Up
            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                offset = offset + 10;
                // new Thread(runnable2).start();
                getBMList(String.valueOf(offset));
                mAdapter.notifyDataSetChanged();
                new FinishRefresh().execute();

            }
        });

        return layoutView;
    }

    private void initViews() {
        TextView title = (TextView) getActivity().findViewById(R.id.ivTitleName);
        title.setText("报名");
    }

    public void getBMList(String offset) {
        showProgressDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("offset", offset);

        // params.addBodyParameter("path", "/apps/测试应用/test文件夹");

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, ConfigUtil.MY_SERVICE_URL
                + "getBMList.php", params, new RequestCallBack<String>() {

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
                            if (code == 228) {
//                                getBMList list = objectMapper.readValue(json,
//                                        getBMList.class);


                                if (jsonObject != null) {
                                    mDataOk = getData(jsonObject);// 填充数据
                                    //System.out.println("mdataok" + mDataOk);

                                    if (mDataOk.size() >= 0) {
                                        mData.addAll(mDataOk);
                                        mDataOk.clear();
                                        //System.out.println(mData);
                                        mAdapter.notifyDataSetChanged();
                                    }

                                }
                            } else if (code == 328) {
                                Toast.makeText(getActivity(), "没有更多数据！",
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String messageId = (String) mData.get(position - 1).get("summary");
        Intent intent = new Intent();
        intent.putExtra("id", messageId);
        intent.setClass(getActivity(), ApplyDetailsActivity.class);
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
            applyListView.onRefreshComplete();
        }
    }
}
