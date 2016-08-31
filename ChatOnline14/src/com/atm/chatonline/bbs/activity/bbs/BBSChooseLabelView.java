package com.atm.chatonline.bbs.activity.bbs;
/**
 * @author yuki
 * @function choose lables
 * date 2016-07-21
 */
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.atm.charonline.bbs.util.BBSHttpClient;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.usercenter.view.FlowLayout;
import com.example.studentsystem01.R;



public class BBSChooseLabelView extends BaseActivity implements OnClickListener{
	private String subPath = "atm_hotDeptLabel.action";
	private static String cookie;
	private BBSHttpClient httpClient;
	private String contentResponse;
	private String tag = "BBSChooseLableView";
	private String dno;
	private List<String> hotLabel = new ArrayList<String>();
	private FlowLayout flowLayout;// 显示热门标签
	private ImageView iv_return,iv_addLable;
	private TextView tv_publish;
	private List<View> textViews = new ArrayList<View>();
	private Handler handler;
	private EditText et_lable;
	private List<String> ischeckedHotLabel = new ArrayList<String>();
	private LayoutInflater inflater;
	
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
		inflater = LayoutInflater.from(this);
		initialViews();
		
		handler = new Handler(){
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					initFlowLayout();
					break;
				}
			};
		};
		setListenerForViews();
		
	}
	
	//请求热门标签数据
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
		iv_addLable.setOnClickListener(this);
		iv_return.setOnClickListener(this);
	}
	private void initialViews() {
		// TODO Auto-generated method stub
		flowLayout = (FlowLayout) findViewById(R.id.flow_layout_view);
		iv_return = (ImageView) findViewById(R.id.iv_return);
		iv_addLable = (ImageView) findViewById(R.id.iv_addLable);
		tv_publish = (TextView) findViewById(R.id.tv_publish);
		et_lable = (EditText) findViewById(R.id.et_lable);
	}
	// 处理获取到的数据
		private void processData(String response) {
			JSONObject jsonObject;
			try {
				Log.d(tag, "标签内容" + response);
				jsonObject = new JSONObject(response);
				JSONArray jsonArray = jsonObject.getJSONArray("hotTag");
				int size = jsonArray.length();
				Log.d(tag, "size=" + size);
				for (int i = 0; i < size; i++) {
				//	jsonObject = jsonArray.getJSONObject(i);
					String lab = jsonArray.getString(i);
					Log.d(tag, "lab=" + lab);
					hotLabel.add(lab);
				}
				Message message = new Message();
				message.what = 1;
				handler.sendMessage(message);
				

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// 初始化热门标签
		public void initFlowLayout() {
			Log.i(tag, "设置热门标签");
			
			HotLabelOnClickListener m = new HotLabelOnClickListener();
	        for(int i = 0; i < hotLabel.size(); i ++){
	        	TextView view = (TextView) inflater.inflate(R.layout.textview_style,
						flowLayout, false);
	        	view.setText(hotLabel.get(i));
	            flowLayout.addView(view);		
	            view.setOnClickListener(m);
	        }
	        flowLayout.setVisibility(View.VISIBLE);
		}
	@Override
	public void processMessage(Message msg) {
		// TODO Auto-generated method stub

	}
	// 监听热门标签被点击的事件
	class HotLabelOnClickListener implements OnClickListener {
		private boolean flag = false;// 判断标签是否在已经选择的列表里面

		@SuppressLint("NewApi")
		@Override
		public void onClick(View v) {
			TextView tv = (TextView) v;
			String lable = (String) tv.getText();
			if(ischeckedHotLabel.contains(lable)){
				flag = false;
				ischeckedHotLabel.remove(lable);
				v.setBackground(getResources().getDrawable(
						R.drawable.bg_hottag));
			}else{
				ischeckedHotLabel.add(lable);
				v.setBackground(getResources().getDrawable(
						R.drawable.bg_isselected_hottag));
			}
		}
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.iv_addLable:
			if(et_lable.getText().toString() != null){
				String str = et_lable.getText().toString();
				TextView view = (TextView) inflater.inflate(R.layout.textview_style,
						flowLayout, false);
				HotLabelOnClickListener m = new HotLabelOnClickListener();
	        	view.setText(str);
	            flowLayout.addView(view);		
	            view.setOnClickListener(m);
	            view.setBackground(getResources().getDrawable(
						R.drawable.bg_isselected_hottag));
	            ischeckedHotLabel.add(str);
			}
			break;
		case R.id.iv_return:
			AlertDialog.Builder back = new AlertDialog.Builder(this);
			back.setTitle("提示框")
					.setMessage("确定返回上一个界面？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									BBSChooseLabelView.this.finish();
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
		}
	}
}
