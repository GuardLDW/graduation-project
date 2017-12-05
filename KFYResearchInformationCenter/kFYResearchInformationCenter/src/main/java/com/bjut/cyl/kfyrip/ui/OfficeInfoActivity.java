package com.bjut.cyl.kfyrip.ui;


import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjut.cyl.kfyrip.view.ScllorTabView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class OfficeInfoActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private static final String TAG = "TempMainActivity";
    private ViewPager mViewPager;
    private List<Fragment> mTabs = new ArrayList<Fragment>();
    private FragmentPagerAdapter mAdapter;
    private OfficeListFragment officeListFragment;
    private OfficeListFragment1 officeListFragment1;
    private ImageButton ivTitleBtnLeft;
    private ScllorTabView scllorTabView;
    private ImageView cursor;
    private TextView tab1,tab2;
    private int bmpw = 0; // 游标宽度
    private int offset = 0;// // 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_office_info);


        setOverflowShowingAlways();
        ivTitleBtnLeft = (ImageButton) findViewById(R.id.ivTitleBtnLeft);
        ivTitleBtnLeft.setOnClickListener(this);
        ivTitleBtnLeft.setBackgroundResource(R.drawable.back_btn_selector);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        TextView title = (TextView) findViewById(R.id.ivTitleName);
        title.setText("组织机构");

        tab1 = (TextView) findViewById(R.id.tab1);
        tab2 = (TextView) findViewById(R.id.tab2);
        tab1.setOnClickListener(new MyOnClickListener(0));
        tab2.setOnClickListener(new MyOnClickListener(1));
        initDatas();

        //初始化指示器位置  
        initCursorPos();
        
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(this);

    }

    private void initCursorPos() {
        // 初始化动画
        cursor = (ImageView) findViewById(R.id.cursor);
        bmpw = BitmapFactory.decodeResource(getResources(), R.drawable.a)
                .getWidth();// 获取图片宽度

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        offset = (screenW / mTabs.size() - bmpw) / 2;// 计算偏移量

        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        cursor.setImageMatrix(matrix);// 设置动画初始位置
    }

    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            mViewPager.setCurrentItem(index);
        }
    };

    private void initDatas() {
        officeListFragment = new OfficeListFragment();
        mTabs.add(officeListFragment);

        officeListFragment1 = new OfficeListFragment1();
        mTabs.add(officeListFragment1);


        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {


            @Override
            public int getCount() {
                return mTabs.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mTabs.get(position);
            }
        };


    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {

        switch (position) {
            case 0:

                break;
            case 1:

                break;
            default:
                break;
        }

    }

    @Override
    public void onPageSelected(int arg0) {
        int one = offset * 2 + bmpw;// 页卡1 -> 页卡2 偏移量
       // int two = one * 2;// 页卡1 -> 页卡3 偏移量
        Animation animation = null;
        switch (arg0) {
            case 0:
                if (currIndex == 1) {
                    animation = new TranslateAnimation(one, 0, 0, 0);
                }
            break;
            case 1:
                if (currIndex == 0) {
                    animation = new TranslateAnimation(offset, one, 0, 0);
                }
                break;
        }
        currIndex = arg0;
        animation.setFillAfter(true);// True:图片停在动画结束位置
        animation.setDuration(300);
        cursor.startAnimation(animation);

    }

    private void setOverflowShowingAlways() {
        try {
            // true if a permanent menu key is present, false otherwise.
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(config, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivTitleBtnLeft:
                this.finish();
                break;
        }
    }
}

