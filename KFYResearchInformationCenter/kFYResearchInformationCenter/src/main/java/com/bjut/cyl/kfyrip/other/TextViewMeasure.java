package com.bjut.cyl.kfyrip.other;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.widget.RelativeLayout;

public class TextViewMeasure extends android.support.v7.widget.AppCompatTextView {
private Context context;
	public TextViewMeasure(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		Layout layout = getLayout();
		if (layout != null) {
			int height = (int) Math.ceil(getMaxLineHeight(this.getText()
					.toString()))
					+ getCompoundPaddingTop()
					+ getCompoundPaddingBottom();
			int width = getMeasuredWidth();
			setMeasuredDimension(width, height);
		}
	}

	private float getMaxLineHeight(String str) {
		float height = 0.0f;
		float screenW = ((Activity) context).getWindowManager()
				.getDefaultDisplay().getWidth();
		float paddingLeft = ((RelativeLayout) this.getParent()).getPaddingLeft();
		float paddingReft = ((RelativeLayout) this.getParent()).getPaddingRight();
		// 这里具体this.getPaint()要注意使用，要看你的TextView在什么位置，这个是拿TextView父控件的Padding的，为了更准确的算出换行
		int line = (int) Math.ceil((this.getPaint().measureText(str) / (screenW
				- paddingLeft - paddingReft)));
		height = (this.getPaint().getFontMetrics().descent - this.getPaint()
				.getFontMetrics().ascent) * line;
		return height;
	}
}
