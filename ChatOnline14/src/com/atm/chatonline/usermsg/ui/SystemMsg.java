package com.atm.chatonline.usermsg.ui;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.usermsg.adapter.SystemMsgAdapter;
import com.atm.chatonline.usermsg.bean.Notification;
import com.example.studentsystem01.R;
/**
 * 我的消息里面系统通知界面
 */

public class SystemMsg extends BaseActivity {

	//private View mView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.usermsg_systemmsg_view);
		initAdapter();
		
	}
	private void initAdapter() {
		ListView listView=(ListView)findViewById(R.id.systemmsg_list_view);
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				return false;
			}
		});
		List<Notification> list=new ArrayList<Notification>();
		list.add(new Notification("标题1","这是测试内容。。。这是测试内容。。。这是测试内容。。。这是测试内容。。。这是测试内容。。。这是测试内容。。。这是测试内容。。。","2016-6-18"));
		list.add(new Notification("标题1","这是测试内容。。。这是测试内容。。。这是测试内容。。。这是测试内容。。。这是测试内容。。。这是测试内容。。。这是测试内容。。。","2016-6-18"));
		list.add(new Notification("标题1","这是测试内容。。。这是测试内容。。。这是测试内容。。。这是测试内容。。。这是测试内容。。。这是测试内容。。。这是测试内容。。。","2016-6-18"));
		SystemMsgAdapter adapter=new SystemMsgAdapter(getApplicationContext(), R.layout.usermsg_systemmsg_listview_item, list);
		listView.setAdapter(adapter);
	}
//	@Override
//	public boolean onLongClick(View v) {
//		Log.i("fffffffff", "dddddd");
//		return true;
//	}
	@Override
	public void processMessage(Message msg) {
		// TODO 自动生成的方法存根
		
	}

}
