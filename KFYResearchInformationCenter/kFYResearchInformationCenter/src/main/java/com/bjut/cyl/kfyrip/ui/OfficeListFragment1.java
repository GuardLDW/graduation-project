package com.bjut.cyl.kfyrip.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bjut.cyl.kfyrip.adapter.OfficeListAdapter1;
import com.bjut.cyl.kfyrip.bean.OfficeBean;
import com.bjut.cyl.kfyrip.utils.XHttpCallbackListener;
import com.bjut.cyl.kfyrip.utils.XHttpUtil;

import java.util.ArrayList;

/**
 * Created by apple on 15/7/23.
 */
public class OfficeListFragment1 extends Fragment {
    private RecyclerView rv_office;
    private ArrayList<OfficeBean.Result.Model> mData = new ArrayList<>();
    private OfficeListAdapter1 mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_office1, container, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv_office = (RecyclerView)view.findViewById(R.id.rv_office);
        rv_office.setLayoutManager(linearLayoutManager);
        mAdapter = new OfficeListAdapter1(getContext(), mData);
        rv_office.setAdapter(mAdapter);
        requestOfiiceInfo();
        return view;
    }

    /**
     * 获取数据
     */
    private void requestOfiiceInfo() {
        XHttpUtil.sendHttpRequest("getOrgList.php", null, 222, 322, new XHttpCallbackListener() {
                @Override
            public void onSuccess(String jsonString, JSONObject jsonObject) {
                OfficeBean bean = JSON.parseObject(jsonString, OfficeBean.class);
                for (OfficeBean.Result.Model model : bean.getResult().zhsw) {
                    mData.add(model);
                }
                for (OfficeBean.Result.Model model : bean.getResult().zx) {
                    mData.add(model);
                }
                for (OfficeBean.Result.Model model : bean.getResult().jskf) {
                    mData.add(model);
                }
                for (OfficeBean.Result.Model model : bean.getResult().jg) {
                    mData.add(model);
                }
                for (OfficeBean.Result.Model model : bean.getResult().xtcx) {
                    mData.add(model);
                }
                for (OfficeBean.Result.Model model : bean.getResult().rwsk) {
                    mData.add(model);
                }
                for (OfficeBean.Result.Model model : bean.getResult().kx) {
                    mData.add(model);
                }
                for (OfficeBean.Result.Model model : bean.getResult().zhcs) {
                    mData.add(model);
                }
                for (OfficeBean.Result.Model model : bean.getResult().zscq) {
                    mData.add(model);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCodeError(String jsonString, JSONObject jsonObject) {

            }
        });
    }


}
