package com.bjut.cyl.kfyrip.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

/**
 * Created by lenovo on 2017/12/8.
 */

public class ScienceFragment  extends DialogShowOffFrag {

    private View layoutView;
    private TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        layoutView = inflater.inflate(R.layout.fragment_science, container, false);
        return layoutView;
    }

}

