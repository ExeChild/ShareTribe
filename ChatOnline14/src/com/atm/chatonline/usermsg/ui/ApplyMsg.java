package com.atm.chatonline.usermsg.ui;
/**
 *
 * 我的消息里面评论（包括回复）我的界面
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
	//private Integer type=0;//0--评论，1--@我，2--系统消息
	//private View mView;
	@SuppressWarnings({ "unchecked", "static-access" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.usermsg_applymsg_view);
		Button btn=(Button) findViewById(R.id.btn_back);
		btn.setOnClickListener(this);
		//这里还没有判断con是否存在，假设已经存在
		//获取userId
		userId=BaseActivity.getSelf().getUserID();
		//发送请求到服务端,理论上应该先查询数据缓存，如果有则先加载缓存，显示，然后再获取新的消息再刷新；如果没有缓存则显示正在刷洗进度条，然后再加载下载的数据
		
		cacheManager=CacheManager.getInstance();
		cacheManager.init(getApplicationContext());
		list=getCacheData();
		if(list==null){
			Log.i(tag, "LIST IS NULL");
			initData();
			addCacheData(list);
		}
		
		
		//如果list的大小大于0，则显示数据
		Log.i(tag, "list size="+list.size());
		if(list.size()>0){
			initAdapter();
		}
		//获取评论消息
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
		
		
		//刷新，发送是否有新消息的请求 Config.ishaveNewMessage
		plv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO 自动生成的方法存根
				
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO 自动生成的方法存根
				
			}
		});
		
		// 设置PullRefreshListView上提加载时的加载提示
		plv.getLoadingLayoutProxy(false, true).setPullLabel(
						"上拉加载...");
		plv.getLoadingLayoutProxy(false, true).setRefreshingLabel(
						"正在加载...");
		plv.getLoadingLayoutProxy(false, true).setReleaseLabel(
						"释放加载...");
				// 设置PullRefreshListView下拉加载时的加载提示
		plv.getLoadingLayoutProxy(true, false).setPullLabel(
						"下拉加载...");
		plv.getLoadingLayoutProxy(true, false).setRefreshingLabel(
				        "正在加载...");
		plv.getLoadingLayoutProxy(true, false).setReleaseLabel(
						"释放加载...");
		
		//为item添加监听
		plv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO 自动生成的方法存根
				
			}
		});
		//plv.setVisibility(View.VISIBLE);
		pro.setVisibility(View.GONE);
		
	}
	private void initData() {
		list=new ArrayList<ApplyMessage>();
		list.add(new ApplyMessage("陌天恒","创业去哪儿","2016-09-17","这是@我的内容，拉看看来看看"));
		list.add(new ApplyMessage("刘天奇","创业去哪儿","2016-09-17","这是@我的内容，拉看看来看看"));
	
	}

	/**
	 * 获取缓存
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
	 * 保存缓存
	 * @param data
	 */
	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	private void addCacheData(List<ApplyMessage> data){
		CacheData cacheData=new CacheData(CacheUtils.APPLY_MSG, data);
		cacheManager.addCache(cacheData);
	}
	
	/**
	 * 等待后台返回消息，并构造评论消息的List，包括构造接收消息的时间
	 */
	@Override
	public void processMessage(Message msg) {
		Bundle bundle =msg.getData();
		msg.what=1;//说明已经获取到服务器上的数据
		handler.sendMessage(msg);
		//获取完数据之后,包括新旧数据全部加载到list中去
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
	
	//发送获取我的评论消息的请求进程
	Runnable myMsgRunnable=new Runnable() {
		
		@Override
		public void run() {
			ApplyMsg.con.reqMyMsg(userId,0);	
		}
	};

}
