package com.bjut.cyl.kfyrip.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bjut.cyl.kfyrip.ScienceFragment;
import com.bjut.cyl.kfyrip.fragment.MenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import cn.sharesdk.framework.ShareSDK;

public class MainActivity extends SlidingFragmentActivity implements
        OnClickListener {
    public static MainActivity instance = null;
    private ImageButton ivTitleBtnLeft;
    private Button ivTitleBtnRight;
    private Fragment mContent;
    private SlidingMenu sm;
    private FragmentManager fm;
    private MenuFragment menuFragment;

    private FindingsFragment findingsFragment;
    private MyResearchFragment myResearchFragment;
    private ApplyFragment applyFragment;

    private ScienceFragment scienceFragment;

    private InfomationCenterFragment infomationCenterFragment;
    private NewsFragment newsFragment;
    private QnAFragment qnAFragment;

    private LeftMenu leftMenu;
    private Button btnNews, btnQna, btnNoti;
    private ImageView btnInfo, btnFind, btnMy, btnBm, btnScience;
    private LinearLayout llInfo, llFind, llMy, llBm, llscience;
    private LinearLayout buttonLayout, searchLl;
    private TextView xxzxTv, fxTv, lcTv, tzTv, xwTv, wdTv, bmTv, scienceTv;
    private SharedPreferences pref;

    public static MainActivity getInstance() {
        if (instance == null) {
            instance = new MainActivity();
        }
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
       //
        //Log.d("info", "[MyReceiver] 接收Registration Id : " + id);
        ShareSDK.initSDK(this);//初始化分享功能
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        /*
		 * initSlidingMenu(); initView();
		 */
        if (findViewById(R.id.main_left_fragment) == null) {
            setBehindContentView(R.layout.main_left_layout);
            getSlidingMenu().setSlidingEnabled(true);
            getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            View v = new View(this);
            setBehindContentView(v);
            getSlidingMenu().setSlidingEnabled(false);
            getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
        sm = getSlidingMenu();

        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setFadeEnabled(false);
        sm.setBehindScrollScale(0.25f);
        sm.setFadeDegree(0.25f);
        sm.setBehindCanvasTransformer(new SlidingMenu.CanvasTransformer() {
            @Override
            public void transformCanvas(Canvas canvas, float percentOpen) {
                float scale = (float) (percentOpen * 0.25 + 0.75);
                canvas.scale(scale, scale, -canvas.getWidth() / 2,
                        canvas.getHeight() / 2);
            }
        });
        sm.setBackgroundColor(getResources().getColor(R.color.tools_box_bg));
        sm.setAboveCanvasTransformer(new SlidingMenu.CanvasTransformer() {
            @Override
            public void transformCanvas(Canvas canvas, float percentOpen) {
                float scale = (float) (1 - percentOpen * 0.25);
                canvas.scale(scale, scale, 0, canvas.getHeight() / 2);
            }
        });
        // if (savedInstanceState != null) {
        // mContent = getSupportFragmentManager().getFragment(
        // savedInstanceState, "mContent");
        // }
        // if (mContent == null) {
        // mContent = new Fragment_home();
        // }
        //
        // getSupportFragmentManager().beginTransaction()
        // .replace(R.id.content_frame, mContent).commit();

        // set the Behind View Fragment
        // getSupportFragmentManager().beginTransaction()
        // .replace(R.id.main_left_fragment, new MenuFragment()).commit();
//		fm = getFragmentManager();
//		FragmentTransaction transaction = fm.beginTransaction();
//		menuFragment = new MenuFragment();
//		transaction.add(R.id.main_left_fragment, menuFragment);
//		
//		transaction
//				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//		transaction.commit();


        initView();
    }

    private void initView() {
        ivTitleBtnLeft = (ImageButton) this.findViewById(R.id.ivTitleBtnLeft);
        ivTitleBtnLeft.setOnClickListener(this);
        ivTitleBtnRight = (Button) findViewById(R.id.ivTitleBtnRight);
        fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        //leftMenu = new LeftMenu(pref.getString("user_id", ""));
        LeftMenu leftMenu = new LeftMenu();
        Bundle bundle = new Bundle();
        bundle.putString("arg", pref.getString("user_id", ""));
        leftMenu.setArguments(bundle);
        transaction.add(R.id.main_left_fragment, leftMenu);
        transaction.show(leftMenu);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        //初始化fragment
        infomationCenterFragment = new InfomationCenterFragment();
        findingsFragment = new FindingsFragment();
        myResearchFragment = new MyResearchFragment();
        newsFragment = new NewsFragment();
        qnAFragment = new QnAFragment();
        applyFragment = new ApplyFragment();
        scienceFragment = new ScienceFragment();
        transaction.add(R.id.content, infomationCenterFragment);
        transaction.show(infomationCenterFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
        //初始化按钮
        btnInfo = (ImageView) findViewById(R.id.btn_infomation_center);
        btnFind = (ImageView) findViewById(R.id.btn_finding);
        btnMy = (ImageView) findViewById(R.id.btn_my_research);
        btnNews = (Button) findViewById(R.id.btn_news);
        btnQna = (Button) findViewById(R.id.btn_questnanswer);
        btnNoti = (Button) findViewById(R.id.btn_notification);
        btnBm = (ImageView) findViewById(R.id.btn_apply);
        btnScience = (ImageView) findViewById(R.id.btn_science);

        btnNews.setOnClickListener(this);
        btnQna.setOnClickListener(this);
        btnNoti.setOnClickListener(this);


        buttonLayout = (LinearLayout) findViewById(R.id.ll_button);
        buttonLayout.setVisibility(View.VISIBLE);

        llInfo = (LinearLayout) findViewById(R.id.ll_information_center);
        llFind = (LinearLayout) findViewById(R.id.ll_finding);
        llMy = (LinearLayout) findViewById(R.id.ll_my_research);
        llBm = (LinearLayout) findViewById(R.id.ll_apply);
        llscience = (LinearLayout) findViewById(R.id.ll_science);
        llInfo.setOnClickListener(this);
        llFind.setOnClickListener(this);
        llMy.setOnClickListener(this);
        llBm.setOnClickListener(this);
        llscience.setOnClickListener(this);

        xxzxTv = (TextView) findViewById(R.id.xxzx_tv);
        fxTv = (TextView) findViewById(R.id.fx_tv);
        lcTv = (TextView) findViewById(R.id.lc_tv);
        tzTv = (TextView) findViewById(R.id.tz_tv);
        xwTv = (TextView) findViewById(R.id.xw_tv);
        wdTv = (TextView) findViewById(R.id.wd_tv);
        bmTv = (TextView) findViewById(R.id.apply_tv);
        scienceTv = (TextView) findViewById(R.id.science_tv);

        btnInfo.setBackgroundResource(R.drawable.tab_xxzx_click);
        xxzxTv.setTextColor(this.getResources().getColor(R.color.button_click));
        btnNoti.setBackgroundResource(R.drawable.xxzx_tz_click);
        tzTv.setTextColor(this.getResources().getColor(R.color.button_click));

        searchLl = (LinearLayout) findViewById(R.id.ll_search);
        searchLl.setOnClickListener(this);
    }

	/*
	 * //初始化左侧菜单 private void initSlidingMenu() { mContent = new
	 * Fragment_home(); getSupportFragmentManager() .beginTransaction()
	 * .replace(R.id.content_frame, mContent) .commit();
	 * setBehindContentView(R.layout.main_left_layout);//设置左边的菜单布局
	 * FragmentTransaction mFragementTransaction = getSupportFragmentManager()
	 * .beginTransaction(); Fragment mFrag = new LeftSlidingMenuFragment();
	 * mFragementTransaction.replace(R.id.main_left_fragment, mFrag);
	 * mFragementTransaction.commit(); mSlidingMenu = getSlidingMenu();
	 * mSlidingMenu.setMode(SlidingMenu.LEFT);// 设置是左滑还是右滑，还是左右都可以滑，我这里只做了左滑
	 * mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);// 设置菜单宽度
	 * mSlidingMenu.setFadeDegree(0.35f);// 设置淡入淡出的比例
	 * mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//设置手势模式
	 * mSlidingMenu.setShadowDrawable(R.drawable.shadow);// 设置左菜单阴影图片
	 * mSlidingMenu.setFadeEnabled(true);// 设置滑动时菜单的是否淡入淡出
	 * mSlidingMenu.setBehindScrollScale(0.333f);// 设置滑动时拖拽效果
	 * 
	 * }
	 */

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = fm.beginTransaction();
        switch (v.getId()) {
            case R.id.ivTitleBtnLeft:
                // 点击标题左边按钮弹出左侧菜单
                sm.showMenu(true);
                break;
            case R.id.ll_search:
                Intent intent = new Intent();
                intent.setClass(this, SearchActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                break;
            case R.id.ll_information_center:
                if (!infomationCenterFragment.isVisible()) {
                    searchLl.setVisibility(View.VISIBLE);
                    // infomationCenterFragment = new InfomationCenterFragment();
                    transaction.replace(R.id.content, infomationCenterFragment);
                    transaction
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    buttonLayout.setVisibility(View.VISIBLE);
                    setColor(1);
                    setColorTop(1);
                }
                ivTitleBtnRight.setVisibility(View.GONE);

                break;
            case R.id.ll_finding:
                if (!findingsFragment.isVisible()) {
                    searchLl.setVisibility(View.GONE);
                    // findingsFragment = new FindingsFragment();
                    transaction.replace(R.id.content, findingsFragment);
                    transaction
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    buttonLayout.setVisibility(View.GONE);
                    setColor(2);
                }
                ivTitleBtnRight.setVisibility(View.GONE);
                break;
            case R.id.ll_apply:
                if (!applyFragment.isVisible()) {
                    searchLl.setVisibility(View.GONE);
                    transaction.replace(R.id.content, applyFragment);
                    transaction
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    buttonLayout.setVisibility(View.GONE);
                    setColor(4);
                }
                ivTitleBtnRight.setVisibility(View.GONE);
                break;
            case R.id.ll_my_research:
                searchLl.setVisibility(View.GONE);
                if (!myResearchFragment.isVisible()) {
                    // myResearchFragment = new MyResearchFragment();
                    transaction.replace(R.id.content, myResearchFragment);
                    transaction
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    buttonLayout.setVisibility(View.GONE);
                    setColor(3);
                }
                ivTitleBtnRight.setVisibility(View.GONE);
                break;
            case R.id.ll_science://底部的科研信息
                searchLl.setVisibility(View.GONE);//隐藏搜索
                if (!scienceFragment.isVisible()) {
                    // myResearchFragment = new MyResearchFragment();
                    transaction.replace(R.id.content, scienceFragment);
                    transaction
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    buttonLayout.setVisibility(View.GONE);
                    setColor(5);
                }
                ivTitleBtnRight.setVisibility(View.GONE);
                break;
            case R.id.btn_news:
                if (!newsFragment.isVisible()) {
                    // myResearchFragment = new MyResearchFragment();
                    transaction.replace(R.id.content, newsFragment);
                    transaction
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    setColorTop(2);
                }
                ivTitleBtnRight.setVisibility(View.GONE);
                break;
            case R.id.btn_questnanswer:
                if (!qnAFragment.isVisible()) {
                    // myResearchFragment = new MyResearchFragment();
                    transaction.replace(R.id.content, qnAFragment);
                    transaction
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    setColorTop(3);
                }
                break;
            case R.id.btn_notification:
                if (!infomationCenterFragment.isVisible()) {
                    // infomationCenterFragment = new InfomationCenterFragment();
                    transaction.replace(R.id.content, infomationCenterFragment);
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    buttonLayout.setVisibility(View.VISIBLE);
                    setColorTop(1);
                }
                ivTitleBtnRight.setVisibility(View.GONE);
                break;
            default:
                break;
        }
        transaction.commit();
    }

    /**
     * 左侧菜单点击切换首页的内容
     */
    public void switchContent(Fragment fragment) {
        mContent = fragment;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, fragment).commit();
        getSlidingMenu().showContent();
    }

    // @Override
    // public void onSaveInstanceState(Bundle outState) {
    // super.onSaveInstanceState(outState);
    // getSupportFragmentManager().putFragment(outState, "mContent", mContent);
    // }

    long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {

            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {

                finish();
                LoginActivity.instance.finish();
                WelComeActivity.instance.finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setColor(int num) {//底部tab颜色切换
        switch (num) {
            case 1:
                btnInfo.setBackgroundResource(R.drawable.tab_xxzx_click);
                xxzxTv.setTextColor(this.getResources().getColor(R.color.button_click));
                btnFind.setBackgroundResource(R.drawable.tab_xm);
                fxTv.setTextColor(this.getResources().getColor(R.color.bottom_text));
                btnMy.setBackgroundResource(R.drawable.tab_bszn);
                lcTv.setTextColor(this.getResources().getColor(R.color.bottom_text));
                btnBm.setBackgroundResource(R.drawable.tab_bm);
                bmTv.setTextColor(this.getResources().getColor(R.color.bottom_text));
                btnScience.setBackgroundResource(R.drawable.tab_science);
                scienceTv.setTextColor(this.getResources().getColor(R.color.bottom_text));
                break;
            case 2:
                btnInfo.setBackgroundResource(R.drawable.tab_xxzx);
                xxzxTv.setTextColor(this.getResources().getColor(R.color.bottom_text));
                btnFind.setBackgroundResource(R.drawable.tab_xm_click);
                fxTv.setTextColor(this.getResources().getColor(R.color.button_click));
                btnMy.setBackgroundResource(R.drawable.tab_bszn);
                lcTv.setTextColor(this.getResources().getColor(R.color.bottom_text));
                btnBm.setBackgroundResource(R.drawable.tab_bm);
                bmTv.setTextColor(this.getResources().getColor(R.color.bottom_text));
                btnScience.setBackgroundResource(R.drawable.tab_science);
                scienceTv.setTextColor(this.getResources().getColor(R.color.bottom_text));
                break;
            case 3:
                btnInfo.setBackgroundResource(R.drawable.tab_xxzx);
                xxzxTv.setTextColor(this.getResources().getColor(R.color.bottom_text));
                btnFind.setBackgroundResource(R.drawable.tab_xm);
                fxTv.setTextColor(this.getResources().getColor(R.color.bottom_text));
                btnMy.setBackgroundResource(R.drawable.tab_bszn_click);
                lcTv.setTextColor(this.getResources().getColor(R.color.button_click));
                btnBm.setBackgroundResource(R.drawable.tab_bm);
                bmTv.setTextColor(this.getResources().getColor(R.color.bottom_text));
                btnScience.setBackgroundResource(R.drawable.tab_science);
                scienceTv.setTextColor(this.getResources().getColor(R.color.bottom_text));
                break;
            case 4:
                btnInfo.setBackgroundResource(R.drawable.tab_xxzx);
                xxzxTv.setTextColor(this.getResources().getColor(R.color.bottom_text));
                btnFind.setBackgroundResource(R.drawable.tab_xm);
                fxTv.setTextColor(this.getResources().getColor(R.color.bottom_text));
                btnMy.setBackgroundResource(R.drawable.tab_bszn);
                lcTv.setTextColor(this.getResources().getColor(R.color.bottom_text));
                btnBm.setBackgroundResource(R.drawable.tab_bm_click);
                bmTv.setTextColor(this.getResources().getColor(R.color.button_click));
                btnScience.setBackgroundResource(R.drawable.tab_science);
                scienceTv.setTextColor(this.getResources().getColor(R.color.bottom_text));
                break;
            case 5://科研信息
                btnInfo.setBackgroundResource(R.drawable.tab_xxzx);
                xxzxTv.setTextColor(this.getResources().getColor(R.color.bottom_text));
                btnFind.setBackgroundResource(R.drawable.tab_xm);
                fxTv.setTextColor(this.getResources().getColor(R.color.bottom_text));
                btnMy.setBackgroundResource(R.drawable.tab_bszn);
                lcTv.setTextColor(this.getResources().getColor(R.color.bottom_text));
                btnBm.setBackgroundResource(R.drawable.tab_bm);
                bmTv.setTextColor(this.getResources().getColor(R.color.bottom_text));
                btnScience.setBackgroundResource(R.drawable.tab_science_click);
                scienceTv.setTextColor(this.getResources().getColor(R.color.button_click));
                break;
            default:
                break;
        }
    }

    private void setColorTop(int num) {//顶部颜色切换
        switch (num) {
            case 1:
                btnNoti.setBackgroundResource(R.drawable.xxzx_tz_click);
                tzTv.setTextColor(this.getResources().getColor(R.color.button_click));
                btnNews.setBackgroundResource(R.drawable.xxzx_xw);
                xwTv.setTextColor(this.getResources().getColor(R.color.bottom_text));
                btnQna.setBackgroundResource(R.drawable.xxzx_wd);
                wdTv.setTextColor(this.getResources().getColor(R.color.bottom_text));
                break;
            case 2:
                btnNoti.setBackgroundResource(R.drawable.xxzx_tz);
                tzTv.setTextColor(this.getResources().getColor(R.color.bottom_text));
                btnNews.setBackgroundResource(R.drawable.xxzx_xw_click);
                xwTv.setTextColor(this.getResources().getColor(R.color.button_click));
                btnQna.setBackgroundResource(R.drawable.xxzx_wd);
                wdTv.setTextColor(this.getResources().getColor(R.color.bottom_text));
                break;
            case 3:
                btnNoti.setBackgroundResource(R.drawable.xxzx_tz);
                tzTv.setTextColor(this.getResources().getColor(R.color.bottom_text));
                btnNews.setBackgroundResource(R.drawable.xxzx_xw);
                xwTv.setTextColor(this.getResources().getColor(R.color.bottom_text));
                btnQna.setBackgroundResource(R.drawable.xxzx_wd_click);
                wdTv.setTextColor(this.getResources().getColor(R.color.button_click));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK(this);//结束分享功能
    }
}
