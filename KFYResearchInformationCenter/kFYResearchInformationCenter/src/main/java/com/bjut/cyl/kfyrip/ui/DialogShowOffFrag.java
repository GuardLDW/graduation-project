package com.bjut.cyl.kfyrip.ui;

import com.bjut.cyl.kfyrip.utils.LogUtil;

import android.app.Fragment;
import android.app.ProgressDialog;

public abstract class DialogShowOffFrag extends Fragment {
	
	protected ProgressDialog progressDialog;
	protected boolean isVisible;
	
//	@Override
//	public void setUserVisibleHint(boolean isVisibleToUser) {
//		super.setUserVisibleHint(isVisibleToUser);
//		if(getUserVisibleHint()){
//			isVisible = true;
//			onVisible();
//			
//		}else{
//			isVisible = false;
//			onInvisible();
//		}
//	}
//	
//	protected void onVisible() {
//		lazyLoad();
//	}
//	protected abstract void lazyLoad();
//	protected void onInvisible() {
//		
//	}

	protected void showProgressDialog(){
		LogUtil.d("trace","showProgressDialog()");
		if(progressDialog == null) {
			progressDialog = new ProgressDialog(getActivity());
			progressDialog.setMessage("正在加载...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}
	protected void closeProgressDialog(){
		LogUtil.d("trace","closeProgressDialog()");
		if(progressDialog != null) {
			progressDialog.dismiss();
		}
	}
	
}
