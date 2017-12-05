package com.bjut.cyl.kfyrip.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import com.bjut.cyl.kfyrip.adapter.AnswerListAdapter;
import com.bjut.cyl.kfyrip.bean.getAnswerList.Data;
import com.bjut.cyl.kfyrip.bean.getQuestionDetail;
import com.bjut.cyl.kfyrip.bean.getQuestionDetail.Datanormal;
import com.bjut.cyl.kfyrip.bean.getQuestionDetail.Datavip;
import com.bjut.cyl.kfyrip.bean.getQuestionDetail.Result.Question;
import com.bjut.cyl.kfyrip.bean.getAnswerList;
import com.bjut.cyl.kfyrip.other.CommentDialog;
import com.bjut.cyl.kfyrip.utils.ConfigUtil;
import com.bjut.cyl.kfyrip.utils.PromptUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class QnADetailsActivity extends DialogShowOffAct implements
        OnClickListener, OnItemClickListener {
    private ImageButton ivTitleBtnLeft;
    private TextView questionTitleTv, commenttimeTv, answertimeTv, viewNumTv, commentNumTv, contentTv, nicknameTv;
    private WebView contentWv;
    private SharedPreferences pref;
    private LinearLayout writeLl, vipLl, normalLl;
    private String messageId;
    private AnswerListAdapter mAdapter, mAdapter1;
    private ListView vipListView, normalListView;
    private List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> mDataOk;
    private List<Map<String, Object>> mDataNormal = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> mDataOkNormal;
    private int offset = 0;
    private Button ivTitleBtnRight;
    private int iscollect;
    private PullToRefreshScrollView pullToRefreshScrollView;
    private CommentDialog commentDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_question_details1);
        Intent intent = getIntent();
        messageId = intent.getStringExtra("id");
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        initView();

        vipListView = (ListView) findViewById(R.id.lv_answer_vip);
        normalListView = (ListView) findViewById(R.id.lv_answer_normal);
        vipListView.setFocusable(false);
        normalListView.setFocusable(false);
        mAdapter = new AnswerListAdapter(this, mData);
        mAdapter1 = new AnswerListAdapter(this, mDataNormal);
        vipListView.setAdapter(mAdapter);
        normalListView.setAdapter(mAdapter1);

        //System.out.println("***size" + mAdapter.getCount());
        vipListView.setOnItemClickListener(this);
        if (mData.size() == 0) {
            getQuestionDetail(messageId);
        }
        pullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.scrollview);
        pullToRefreshScrollView.setMode(Mode.PULL_UP_TO_REFRESH);
        pullToRefreshScrollView.setOnRefreshListener(new OnRefreshListener2<ScrollView>() {

            // Pulling Down
            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ScrollView> refreshView) {
                mData.clear();
                offset = 0;
                getAnswerList(messageId, "10");
                mAdapter1.notifyDataSetChanged();
                new FinishRefresh().execute();
            }

            // Pulling Up
            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ScrollView> refreshView) {
                offset = offset + 10;
                // new Thread(runnable2).start();
                getAnswerList(messageId, String.valueOf(offset));
                mAdapter1.notifyDataSetChanged();
                new FinishRefresh().execute();

            }
        });
    }

    private void getData(getQuestionDetail mList) {
        questionTitleTv = (TextView) findViewById(R.id.question_title);
        commenttimeTv = (TextView) findViewById(R.id.comment_time);
        nicknameTv = (TextView) findViewById(R.id.answerer_name);
        contentTv = (TextView) findViewById(R.id.answer_content);
        answertimeTv = (TextView) findViewById(R.id.answer_time);

        Question question = mList.getResult().getQuestion();
        questionTitleTv.setText(question.getTitle());
        commenttimeTv.setText(question.getLast_modify_time());
        nicknameTv.setText(question.getUser_nickname());
        contentTv.setText(question.getContent());
        answertimeTv.setText(question.getLast_modify_time());
    }

    private List<Map<String, Object>> getDataListVip(getQuestionDetail mList) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<Datavip> dataList = mList.getResult().getDatavip();
        //System.out.println(dataList);
        if (dataList != null) {
            for (Datavip result : dataList) {

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("answerer_name", result.getUser_nickname());
                map.put("answer_content", result.getContent());
                map.put("answer_time", result.getLast_modify_time());
                map.put("id", result.getId());
                list.add(map);

            }
        }

        return list;
    }

    private List<Map<String, Object>> getDataListNormal(getQuestionDetail mList) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<Datanormal> dataList = mList.getResult().getDatanormal();

        for (Datanormal result : dataList) {
            if (result != null) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("answerer_name", result.getUser_nickname());
                map.put("answer_content", result.getContent());
                map.put("answer_time", result.getLast_modify_time());
                map.put("id", result.getId());
                list.add(map);
            }
        }
        return list;
    }

    private List<Map<String, Object>> getDataList1(getAnswerList mList) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<Data> dataList = mList.getResult().getData();

        for (Data result : dataList) {
            if (result != null) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("answerer_name", result.getUser_nickname());
                map.put("answer_content", result.getContent());
                map.put("answer_time", result.getLast_modify_time());
            map.put("id", result.getId());
            list.add(map);}
        }
        return list;
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
        writeLl.setOnClickListener(this);
        vipLl = (LinearLayout) findViewById(R.id.ll_vip_answer);
        normalLl = (LinearLayout) findViewById(R.id.ll_normal_answer);
        TextView toptitle = (TextView) findViewById(R.id.ivTitleName);
        toptitle.setText("问答详情");

    }

    public void getQuestionDetail(String id) {
        showProgressDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("id", id);
        params.addBodyParameter("user_id", pref.getString("username", ""));
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, ConfigUtil.MY_SERVICE_URL
                        + "getQuestionDetail.php", params,
                new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(
                            final ResponseInfo<String> responseInfo) {
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
                                    if (code == 216) {
                                        getQuestionDetail list = objectMapper
                                                .readValue(json,
                                                        getQuestionDetail.class);
                                        if (list.getResult().getQuestion()
                                                .getCollect() == 1) {
                                            ivTitleBtnRight.setBackgroundResource(R.drawable.collect_click);
                                            iscollect = 1;
                                        } else if (list.getResult()
                                                .getQuestion().getCollect() == 0) {
                                            ivTitleBtnRight.setBackgroundResource(R.drawable.collect);
                                            iscollect = 0;
                                        }
                                        ;
                                        if (list != null) {

                                            if (list.getResult().getDatavip() != null) {
                                                mDataOk = getDataListVip(list);// 填充数据
                                                //System.out.println("mdataok"+ mDataOk);
                                                if (mDataOk.size() >= 0) {
                                                    mData.addAll(mDataOk);
                                                    mDataOk.clear();
                                                    //System.out.println(mData);
                                                    mAdapter.notifyDataSetChanged();
                                                    //setListViewHeightBasedOnChildren(vipListView,mAdapter);
                                                }
                                            }else {
                                                vipLl.setVisibility(View.GONE);
                                            }
                                            if (list.getResult().getDatanormal() != null) {
                                                mDataOkNormal = getDataListNormal(list);// 填充数据
                                               // System.out.println("mdataokno" + mDataOkNormal);
                                                if (mDataOkNormal.size() >= 0) {
                                                    mDataNormal.addAll(mDataOkNormal);
                                                    mDataOkNormal.clear();
                                                    //System.out.println(mDataNormal);
                                                    mAdapter1.notifyDataSetChanged();
                                                    //setListViewHeightBasedOnChildren(normalListView,mAdapter1);
                                                }
                                            }else {
                                                normalLl.setVisibility(View.GONE);
                                            }

                                            if (offset == 0) {
                                                getData(list);
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

    public void getAnswerList(String id, String off) {
        showProgressDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("id", id);
        params.addBodyParameter("user_id", pref.getString("username", ""));
        params.addBodyParameter("offset", off);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, ConfigUtil.MY_SERVICE_URL
                        + "getAnswerList.php", params,
                new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(
                            final ResponseInfo<String> responseInfo) {
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
                                    if (code == 218) {
                                        getAnswerList list = objectMapper
                                                .readValue(json,
                                                        getAnswerList.class);

                                        if (list != null) {

                                            mDataOkNormal = getDataList1(list);// 填充数据
                                            //System.out.println("mdataok"+ mDataOkNormal);

                                            if (mDataOkNormal.size() >= 0) {
                                                mDataNormal.addAll(mDataOkNormal);
                                                mDataOkNormal.clear();
                                                //System.out.println(mData);
                                                mAdapter1.notifyDataSetChanged();
                                                //setListViewHeightBasedOnChildren(vipListView,mAdapter);
                                            }
                                        }
                                    } else if (code == 318) {
                                        Toast.makeText(getApplicationContext(),
                                                "没有更多答案！", Toast.LENGTH_SHORT).show();
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

    public void publishAnswer(String id, String creator_id, String content) {
        showProgressDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("id", id);
        params.addBodyParameter("creator_id", creator_id);
        params.addBodyParameter("content", content);

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, ConfigUtil.MY_SERVICE_URL
                    + "publishAnswer.php", params, new RequestCallBack<String>() {

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
                            if (code == 214) {
                                mData.clear();
                                mDataNormal.clear();
                                getQuestionDetail(messageId);
                                Toast.makeText(getApplicationContext(),
                                        "评论成功！", Toast.LENGTH_SHORT).show();
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
                                mData.clear();
                                mDataNormal.clear();
                                getQuestionDetail(messageId);
                            } else if (code == 399) {
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
                                mData.clear();
                                mDataNormal.clear();
                                getQuestionDetail(messageId);
                            } else if (code == 399) {
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
                    uncollect(pref.getString("username", ""), messageId, "3");
                } else if (iscollect == 0) {
                    collect(pref.getString("username", ""), messageId, "3");
                }
                break;

            case R.id.ll_write:
                LayoutInflater inflater = LayoutInflater.from(QnADetailsActivity.this);
                commentDialog = new CommentDialog(QnADetailsActivity.this, "评论") {

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
                            publishAnswer(messageId, pref.getString("username", ""), content);
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

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub

    }

    private class FinishRefresh extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            pullToRefreshScrollView.onRefreshComplete();
        }
    }

}
