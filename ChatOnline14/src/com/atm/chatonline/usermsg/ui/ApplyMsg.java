package com.atm.chatonline.usermsg.ui;
/**
 *
 * �ҵ���Ϣ�������ۣ������ظ����ҵĽ���
 */
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.usermsg.adapter.ApplyAdapter;
import com.atm.chatonline.usermsg.bean.ApplyMessage;
import com.atm.chatonline.usermsg.util.CacheData;
import com.atm.chatonline.usermsg.util.CacheManager;
import com.atm.chatonline.usermsg.util.CacheUtils;
import com.example.studentsystem01.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class ApplyMsg extends BaseActivity implements OnClickListener{

	private List<ApplyMessage> list;
	private String userId;
	private ApplyAdapter adapter;
	private PullToRefreshListView plv;
	private Handler handler;
	private CacheManager cacheManager;
	//private ApplyMessage msg;
	private int test=1;
	private String tag="Applymsg";
	//private 
	//private Integer type=0;//0--���ۣ�1--@�ң�2--ϵͳ��Ϣ
	//private View mView;
	@SuppressWarnings({ "unchecked", "static-access" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.usermsg_applymsg_view);
		Button btn=(Button) findViewById(R.id.btn_back);
		btn.setOnClickListener(this);
		//���ﻹû���ж�con�Ƿ���ڣ������Ѿ�����
		//��ȡuserId
		userId=BaseActivity.getSelf().getUserID();
		//�������󵽷����,������Ӧ���Ȳ�ѯ���ݻ��棬��������ȼ��ػ��棬��ʾ��Ȼ���ٻ�ȡ�µ���Ϣ��ˢ�£����û�л�������ʾ����ˢϴ��������Ȼ���ټ������ص�����
		
		cacheManager=CacheManager.getInstance();
		cacheManager.init(getApplicationContext());
		list=getCacheData();
		if(list==null){
			Log.i(tag, "LIST IS NULL");
			initData();
			addCacheData(list);
		}
		
		
		//���list�Ĵ�С����0������ʾ����
		Log.i(tag, "list size="+list.size());
		if(list.size()>0){
			initAdapter();
		}
		//��ȡ������Ϣ
		new Thread(myMsgRunnable).start();
		//initData();
		//initAdapter();
		
		if(handler==null){
			handler=new Handler(){

				@SuppressLint("HandlerLeak")
				@Override
				public void handleMessage(Message msg) {
					switch (msg.what) {
					case 1:
						
						break;

					default:
						break;
					}
				}
				
			};
		}
		
	}
	
	private void initAdapter() {
		ProgressBar pro=(ProgressBar) findViewById(R.id.applymsg_probar);
		plv=(PullToRefreshListView)findViewById(R.id.apply_msg_list_view);
		adapter=new ApplyAdapter(getApplicationContext(), R.layout.apply_msg_listview_item, list); 
		plv.setAdapter(adapter);
		
		
		//ˢ�£������Ƿ�������Ϣ������ Config.ishaveNewMessage
		plv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO �Զ����ɵķ������
				
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO �Զ����ɵķ������
				
			}
		});
		
		// ����PullRefreshListView�������ʱ�ļ�����ʾ
		plv.getLoadingLayoutProxy(false, true).setPullLabel(
						"��������...");
		plv.getLoadingLayoutProxy(false, true).setRefreshingLabel(
						"���ڼ���...");
		plv.getLoadingLayoutProxy(false, true).setReleaseLabel(
						"�ͷż���...");
				// ����PullRefreshListView��������ʱ�ļ�����ʾ
		plv.getLoadingLayoutProxy(true, false).setPullLabel(
						"��������...");
		plv.getLoadingLayoutProxy(true, false).setRefreshingLabel(
				        "���ڼ���...");
		plv.getLoadingLayoutProxy(true, false).setReleaseLabel(
						"�ͷż���...");
		
		//Ϊitem��Ӽ���
		plv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO �Զ����ɵķ������
				
			}
		});
		//plv.setVisibility(View.VISIBLE);
		pro.setVisibility(View.GONE);
		
	}
	private void initData() {
		list=new ArrayList<ApplyMessage>();
		list.add(new ApplyMessage("İ���","��ҵȥ�Ķ�","2016-09-17","����@�ҵ����ݣ�������������"));
		list.add(new ApplyMessage("������","��ҵȥ�Ķ�","2016-09-17","����@�ҵ����ݣ�������������"));
	
	}

	/**
	 * ��ȡ����
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<ApplyMessage> getCacheData(){
		CacheData  data =cacheManager.getCache(CacheUtils.APPLY_MSG);
		if(data==null){
			return null;
		}
		return (List<ApplyMessage>) data.getData();
	}
	
	/**
	 * ���滺��
	 * @param data
	 */
	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	private void addCacheData(List<ApplyMessage> data){
		CacheData cacheData=new CacheData(CacheUtils.APPLY_MSG, data);
		cacheManager.addCache(cacheData);
	}
	
	/**
	 * �ȴ���̨������Ϣ��������������Ϣ��List���������������Ϣ��ʱ��
	 */
	@Override
	public void processMessage(Message msg) {
		Bundle bundle =msg.getData();
		msg.what=1;//˵���Ѿ���ȡ���������ϵ�����
		handler.sendMessage(msg);
		//��ȡ������֮��,�����¾�����ȫ�����ص�list��ȥ
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			onBackPressed();
			break;

		default:
			break;
		}
		
	}
	
	//���ͻ�ȡ�ҵ�������Ϣ���������
	Runnable myMsgRunnable=new Runnable() {
		
		@Override
		public void run() {
			ApplyMsg.con.reqMyMsg(userId,0);	
		}
	};

}
