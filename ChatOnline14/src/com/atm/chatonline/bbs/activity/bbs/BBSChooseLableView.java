package com.atm.chatonline.bbs.activity.bbs;
/**
 * @author yuki
 * @function choose lables
 * date 2016-07-21
 */
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Window;

import com.atm.charonline.bbs.util.BBSHttpClient;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.example.studentsystem01.R;

public class BBSChooseLableView extends BaseActivity {
	private String subPath = "atm_hotDeptLabel.action";
	private static String cookie;
	private BBSHttpClient httpClient;
	private String contentResponse;
	private String tag = "BBSChooseLableView";
	private String dno;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bbs_choose_lable);
		Intent intent = getIntent();
		dno = intent.getStringExtra("id");
		Log.d(tag, "系别号" + dno);
		requestContent();// 开启线程向服务器获取数据
		initialViews();
		setListenerForViews();
		
		
	}
	private void requestContent() {
		// TODO Auto-generated method stub
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					httpClient = new BBSHttpClient(dno,cookie, subPath);
					contentResponse = httpClient.getResponse();
					processData(contentResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	private void setListenerForViews() {
		// TODO Auto-generated method stub
		
	}
	private void initialViews() {
		// TODO Auto-generated method stub
		
	}
	// 处理获取到的数据
		private void processData(String response) {
			JSONObject jsonObject;
			try {
				Log.d(tag, "标签内容" + response);
				jsonObject = new JSONObject(response);
				JSONArray jsonArray = jsonObject.getJSONArray("hotTag");
				int size = jsonArray.length();
				for (int i = 0; i < size; i++) {
					jsonObject = jsonArray.getJSONObject(i);
					String lab = jsonObject.toString();
					Log.d(tag, "lab=" + lab);
				}
				/*Message message = new Message();
				message.what = UPDATE_RADIOGROUP;
				handler.sendMessage(message);*/

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	@Override
	public void processMessage(Message msg) {
		// TODO Auto-generated method stub

	}

}
