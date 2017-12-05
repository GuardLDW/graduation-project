package com.bjut.cyl.kfyrip.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bjut.cyl.kfyrip.adapter.OfficeListAdapter;
import com.bjut.cyl.kfyrip.bean.OfficeBean;
import com.bjut.cyl.kfyrip.utils.XHttpCallbackListener;
import com.bjut.cyl.kfyrip.utils.XHttpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by apple on 15/7/23.
 */
public class OfficeListFragment extends Fragment {
    private ListView listView;
    private OfficeListAdapter mAdapter;
    private List<OfficeBean.Result.Model> mData = new ArrayList<>();
    private int office;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_office, container, false);
        listView = (ListView) view.findViewById(R.id.lv_office);
        mAdapter = new OfficeListAdapter(getActivity(), mData);
        listView.setAdapter(mAdapter);
        requestOfiiceInfo();
        return view;
    }

    /**
     * 获取组织机构信息
     */
    private void requestOfiiceInfo() {
        XHttpUtil.sendHttpRequest("getOrgList.php", null, 222, 322, new XHttpCallbackListener() {
            @Override
            public void onSuccess(String jsonString, JSONObject jsonObject) {
                OfficeBean bean = JSON.parseObject(jsonString, OfficeBean.class);
                mData.addAll(bean.getResult().yld);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCodeError(String jsonString, JSONObject jsonObject) {

            }
        });
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "聂祚仁");
        map.put("work", "副校长兼科学技术发展院院长");
        map.put("phone_num", "");
        map.put("room", "");
        map.put("email", "");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("name", "石照耀");
        map.put("work", "科学技术发展院常务副院长兼协同创新和专项办公室主任（正处级）");
        map.put("phone_num", "67396202");
        map.put("room", "知新园409");
        map.put("email", "kjcshi@bjut.edu.cn");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("name", "林绍福");
        map.put("work", "科学技术发展院副院长兼技术开发和成果转化办公室主任（正处级）");
        map.put("phone_num", "67391304");
        map.put("room", "知新园401");
        map.put("email", "linshaofu@bjut.edu.cn");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("name", "肖念");
        map.put("work", "科学技术发展院副院长兼人文社会科学处处长（正处级）");
        map.put("phone_num", "67392749");
        map.put("room", "知新园419");
        map.put("email", "xiaon@bjut.edu.cn");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("name", "卓力");
        map.put("work", "科学技术发展院纵向、基地和国际合作办公室主任（副处级）");
        map.put("phone_num", "67392749");
        map.put("room", "知新园419");
        map.put("email", "zhuoli@bjut.edu.cn");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("name", "刘伟");
        map.put("work", "科学技术发展院先进技术办公室主任（副处级）");
        map.put("phone_num", "67392582");
        map.put("room", "知新园119");
        map.put("email", "liuw@bjut.edu.cn");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("name", "杨登才");
        map.put("work", "科学技术发展院综合事务和成果办公室主任（副处级）");
        map.put("phone_num", "67391484");
        map.put("room", "知新园415");
        map.put("email", "dengcaiyang@bjut.edu.cn");
        list.add(map);
        return list;
    }
}
