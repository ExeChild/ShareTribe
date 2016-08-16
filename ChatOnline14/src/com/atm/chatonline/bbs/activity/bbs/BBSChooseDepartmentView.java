package com.atm.chatonline.bbs.activity.bbs;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.atm.charonline.bbs.util.BBSHttpClient;
import com.atm.chatonline.bbs.commom.UriAPI;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.chat.util.Config;
import com.example.studentsystem01.R;

/**
 * @类 com.atm.chatonline.activity.bbs ---BBSChooseDepartmentView
 * @作用 该类用于显示选择系别界面
 * @作者 钟YD
 * @时间 2015-8-24
 * 
 */

public class BBSChooseDepartmentView extends BaseActivity implements
		OnClickListener {
	private RadioGroup radioGroup;
	private TextView next;
	private ImageView iv_return;
	private String subPath = "atm_deptList.action";
	private BBSHttpClient connect;
	private String contentResponse;
	private String selectedDepartment, selectedDno;
	private Handler handler;
	private static final int UPDATE_RADIOGROUP = 1;
	private Map<String, String> departInfo = new HashMap<String, String>();
	private static String cookie;
	private Context context;
	private String tag = "BBSChooseDepartmentView";

	// private static final int RESULT_DEPARTMENT = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_department);
		requestContent();// 开启线程向服务器获取数据
		initialViews();
		setListenerForViews();
		getCookie();
		context = getApplicationContext();
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				String department;
				switch (msg.what) {
				case UPDATE_RADIOGROUP:
					int size = departInfo.size();
					int i = 1;
					for (String key : departInfo.keySet()) {
						RadioButton button = new RadioButton(context);
						department = departInfo.get(key);
						Log.d("BBSChooseDepartmentView", department);
						button.setText(department);
						button.setTextColor(Color.BLACK);
						radioGroup.addView(button);
					}
					break;
				}
			}
		};
	}

	// 获取cookie
	private void getCookie() {
		// TODO Auto-generated method stub
		SharedPreferences pref = getSharedPreferences("data",
				Context.MODE_PRIVATE);
		cookie = pref.getString("cookie", "");
	}

	private void requestContent() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					connect = new BBSHttpClient(cookie, subPath);
					contentResponse = connect.getResponse();
					processData(contentResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	// 处理获取到的数据
	private void processData(String response) {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(response);
			JSONArray jsonArray = jsonObject.getJSONArray("department");
			int size = jsonArray.length();
			for (int i = 0; i < size; i++) {
				jsonObject = jsonArray.getJSONObject(i);
				String dno = jsonObject.getString("dno");
				String department = jsonObject.getString("dname");
				departInfo.put(dno, department);
			}
			Message message = new Message();
			message.what = UPDATE_RADIOGROUP;
			handler.sendMessage(message);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setListenerForViews() {
		// TODO Auto-generated method stub
		radioGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup arg0, int arg1) {
						// TODO Auto-generated method stub
						int radioButtonId = arg0.getCheckedRadioButtonId();
						// 根据ID获取RadioButton的实例
						RadioButton rb = (RadioButton) findViewById(radioButtonId);
						selectedDepartment = (String) rb.getText();
						for (String getKey : departInfo.keySet()) {
							if (departInfo.get(getKey).equals(selectedDepartment)) {
								selectedDno = getKey;
							}
						}
						Log.d(tag, "选中的系别" + selectedDepartment);
						Log.d(tag, "系别号" + selectedDno);
					}
				});

		next.setOnClickListener(this);
		iv_return.setOnClickListener(this);
	}

	private void initialViews() {
		// TODO Auto-generated method stub
		iv_return = (ImageView) findViewById(R.id.iv_return);
		next = (TextView) findViewById(R.id.next);
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
	}

	@Override
	public void processMessage(Message msg) {
		// TODO 自动生成的方法存根
		if (msg.what == Config.SEND_NOTIFICATION) {
			sendNotifycation();
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		case R.id.iv_return:
			AlertDialog.Builder back = new AlertDialog.Builder(this);
			back.setTitle("提示框")
					.setMessage("确定返回上一个界面？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									BBSChooseDepartmentView.this.finish();
								}

							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									// TODO Auto-generated method stub
								}
							});

			back.create().show();
			break;
		case R.id.next:
			Intent intent = new Intent(BBSChooseDepartmentView.this,
					BBSChooseLableView.class);
			intent.putExtra("id", selectedDno);
			startActivity(intent);
			break;
		}
	}
}
