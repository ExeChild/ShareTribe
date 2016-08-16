package com.atm.chatonline.usermsg.ui;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.usermsg.adapter.ReplyAdapter;
import com.atm.chatonline.usermsg.bean.ReplyMessage;
import com.example.studentsystem01.R;
/**
 *  我的消息里面的@我的界面
 */

public class ReplyMsg extends BaseActivity {

	//private View mView;
	private List<ReplyMessage> list;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.usermsg_replymsg_view);
		initData();
		initAdapter();
		
	}
	private void initAdapter() {
		ListView listView=(ListView) findViewById(R.id.reply_msg_list_view);
		ReplyAdapter adapter=new ReplyAdapter(getApplicationContext(), R.layout.reply_msg_listview_item, list); 
		listView.setAdapter(adapter);
	}
	private void initData() {
		list=new ArrayList<ReplyMessage>();
		list.add(new ReplyMessage("陌天恒","创业去哪儿","2016-09-17","这是@我的内容，拉看看来看看"));
		list.add(new ReplyMessage("刘天奇","创业去哪儿","2016-09-17","这是@我的内容，拉看看来看看"));
	
	}
	@Override
	public void processMessage(Message msg) {
		// TODO 自动生成的方法存根
		
	}

	
}
