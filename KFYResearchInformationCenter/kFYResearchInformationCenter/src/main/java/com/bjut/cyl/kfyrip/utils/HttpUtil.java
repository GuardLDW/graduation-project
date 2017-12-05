package com.bjut.cyl.kfyrip.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

public class HttpUtil {
	public static void sendHttpRequest(final String address,final String params, final HttpCallbackListener listener) {
		
		
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpURLConnection connection = null;
				try {
					URL url = new URL(address);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("POST");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					connection.setDoInput(true);
					connection.setDoOutput(true);
					
					DataOutputStream out = new DataOutputStream(connection.getOutputStream());
					out.writeBytes(params);
					
					InputStream in = connection.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					StringBuilder response = new StringBuilder();
					String line;
					while((line=reader.readLine()) != null) {
						response.append(line);
					}
					
					if(listener != null) {
						listener.onFinish(response.toString());
					}
					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					if(listener != null) {
						listener.onError(e);
					}
					
				} finally {
					if(connection != null) {
						connection.disconnect();
					}
				}
			}
		}).start();
		
		
		
	}
}
