package com.atm.chatonline.bbs.activity.bbs;





import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioGroup;

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

public class BBSChooseDepartmentView extends BaseActivity {
	private RadioGroup radioGroup;
	private Button confirm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_department);
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				Intent intent = getIntent();
				String department = "";
				switch(arg1){
					case R.id.finance:
						department = "金融系";
						break;
					case R.id.management:
						department = "工商管理系";
						break;
					case R.id.insurance:
						department = "保险系";
						break;
					case R.id.technology:
						department = "互联网金融与信息工程系";
						break;
					case R.id.account:
						department = "会计系";
						break;
					case R.id.law:
						department = "法律系";
						break;
				}
				intent.putExtra("department", department);
				setResult(0, intent);
				
			}
		});
		confirm = (Button) findViewById(R.id.confirm);
		confirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	@Override
	public void processMessage(Message msg) {
		// TODO 自动生成的方法存根
		if(msg.what==Config.SEND_NOTIFICATION){
			sendNotifycation();
		}
	}
}
