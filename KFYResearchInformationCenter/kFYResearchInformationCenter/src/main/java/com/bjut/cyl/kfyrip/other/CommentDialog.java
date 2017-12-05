package com.bjut.cyl.kfyrip.other;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import com.bjut.cyl.kfyrip.ui.R;

/**
 * 审批对话框
 */
public abstract class CommentDialog extends AlertDialog implements View.OnClickListener {
	
	protected CommentDialog(Context context, String title) {
		super(context);
		this.title = title;
	}

	/**
	 * 布局中的其中一个组件
	 */
	private String title;
	private String msg;
	private EditText contentEdit;
	private TextView titleTv;
	private TextView cancel;
	private TextView submit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setCanceledOnTouchOutside(false);
		// 加载自定义布局
		setContentView(R.layout.dialog_comment);
		setDialogSize();
		titleTv = (TextView) findViewById(R.id.update_title);
		contentEdit = (EditText) findViewById(R.id.comment_content);
		titleTv.setText(title);
		cancel = (TextView) findViewById(R.id.cancel);
		cancel.setOnClickListener(this);
		submit = (TextView) findViewById(R.id.submit);
		submit.setOnClickListener(this);
	}

	/**
	 * 修改 框体大小
	 *
	 */
	public void setDialogSize() {
		Window win = this.getWindow();
		win.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
		win.setWindowAnimations(R.style.mystyle); // 添加动画
		WindowManager m = win.getWindowManager();
		Display d = m.getDefaultDisplay();
		DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
		int width = (int) (metrics.widthPixels * 0.8);
		int height = (int) (width * 0.5);
		WindowManager.LayoutParams params = win.getAttributes();
		params.width = width;
		//params.height = height;
		//params.height = 200;
		this.getWindow().setAttributes(params);
	}

	public void showKeyboard() {
		if(contentEdit!=null){
			//设置可获得焦点
			contentEdit.setFocusable(true);
			contentEdit.setFocusableInTouchMode(true);
			//请求获得焦点
			contentEdit.requestFocus();
			//调用系统输入法
			InputMethodManager inputManager = (InputMethodManager) contentEdit
					.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.showSoftInput(contentEdit, 0);
		}
	}

	public String getText(){
		String content = contentEdit.getText().toString();
		return content;
	}
	public abstract void clickCancelCallBack();
	public abstract void clickOkCallBack();
	
	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == cancel) {
			clickCancelCallBack();
		}
		if (v == submit) {
			clickOkCallBack();
		}
	}
	
}
