package com.bjut.cyl.kfyrip.utils;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.bjut.cyl.kfyrip.ui.MyApplication;


public class PromptUtil {

    private static Toast mToast;
    private static Handler mHandler = new Handler();
    private static Runnable r = new Runnable() {
        public void run() {
            mToast.cancel();
        }
    };

    public static void showToast(String msg) {
        Context context = MyApplication.getContext();
        if (null == context || TextUtils.isEmpty(msg)) {
            return;
        }
        mHandler.removeCallbacks(r);
        if (mToast != null)
            mToast.setText(msg);
        else
            mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        mHandler.postDelayed(r, 3000);
        mToast.show();

        //Toast.makeText( context, msg, Toast.LENGTH_SHORT ).show( );
    }

    //	public static void showRedToast(String msg){
//		if( null == context || TextUtils.isEmpty( msg ) ){
//			return;
//		}
//
//		LayoutInflater inflater = LayoutInflater.from(context);
//		View toastRoot = inflater.inflate(R.layout.red_toast, null);
//		Toast toast=new Toast(context);
//		toast.setView(toastRoot);
//		TextView tv=(TextView)toastRoot.findViewById(R.id.TextViewInfo);
//		tv.setText(msg);
//		toast.show();
//	}
    public static void showRedToastV2(String msg) {
        Context context = MyApplication.getContext();
        if (null == context || TextUtils.isEmpty(msg)) {
            return;
        }

        Toast toast = Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT);
        View view = toast.getView();
        view.setBackgroundColor(Color.RED);
        toast.setView(view);
        toast.show();

    }
}


