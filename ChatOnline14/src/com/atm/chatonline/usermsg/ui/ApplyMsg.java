package com.atm.chatonline.usermsg.ui;
/**
 *
 * �ҵ���Ϣ�������ۣ������ظ����ҵĽ���
 */
import java.util.List;

import org.afinal.simplecache.ACache;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
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
import android.widget.TextView;

import com.atm.charonline.bbs.util.ExtendsIntent;
import com.atm.charonline.bbs.util.LogUtil;
import com.atm.chatonline.bbs.activity.bbs.BBSPostDetailView;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.chat.util.FileUtil;
import com.atm.chatonline.usermsg.adapter.ApplyAdapter;
import com.atm.chatonline.usermsg.bean.ApplyMessage;
import com.atm.chatonline.usermsg.bean.ApplyMessageData;
import com.atm.chatonline.usermsg.util.CacheData;
import com.atm.chatonline.usermsg.util.CacheManager;
import com.atm.chatonline.usermsg.util.CacheUtils;
import com.atm.chatonline.usermsg.util.MyMsgReceivePhoto;
import com.example.studentsystem01.R;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class ApplyMsg extends BaseActivity implements OnClickListener{

	private List<ApplyMessage> list;
	private String userId;
	private ApplyAdapter adapter;
	private PullToRefreshListView plv;
	private ProgressBar pro;
	//private Handler handler=new Handler();
	private CacheManager cacheManager;
	private ACache mCache;
	private boolean hasCache=false;
	private String tag="Applymsg";
	private TextView applymsg_hint;
	private Thread myMsgThread;
	private ApplyMessageData data;//������������Ϣ���б�
	//private Integer type=0;//0--���ۣ�1--@�ң�2--ϵͳ��Ϣ
	
	//���ͻ�ȡ�ҵ�������Ϣ���������,Ϊ��֮�������ΪActivity���ٺ����߳�û�б����٣����԰�����߳�д��ǰ��
	Runnable myMsgRunnable=new Runnable() {
			
			@Override
			public void run() {
				Log.i(tag, "��ȡ������Ϣ");
				ApplyMsg.con.reqMyMsg(userId,1);	
			}
	};
	

	@SuppressWarnings({ "unchecked", "static-access" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.usermsg_applymsg_view);
		initUI();
		initData();
	}
	
	private void initData() {
		
		mCache=ACache.get(getApplicationContext());
		//���ﻹû���ж�con�Ƿ���ڣ������Ѿ�����
		//��ȡuserId
		userId=BaseActivity.getSelf().getUserID();
		//�������󵽷����,������Ӧ���Ȳ�ѯ���ݻ��棬��������ȼ��ػ��棬��ʾ��Ȼ���ٻ�ȡ�µ���Ϣ��ˢ�£����û�л�������ʾ����ˢϴ��������Ȼ���ټ������ص�����
		initCache();
		//��ȡ������Ϣ
		myMsgThread=new Thread(myMsgRunnable);//�����Activity������֮ǰ����û�з��أ���Ҫ��ֹ�ý���
		myMsgThread.start();
		
		
		
	}

	/**
	 * ��ȡ����������ü����ȳ�ʼ������
	 */
	private void initUI() {
		Button btn=(Button) findViewById(R.id.btn_back);
		pro=(ProgressBar) findViewById(R.id.applymsg_probar);
		applymsg_hint=(TextView) findViewById(R.id.applymsg_hint);
		btn.setOnClickListener(this);

	}

	/**
	 * ��ȡ����
	 */
	@SuppressWarnings({ "deprecation", "static-access" })
	private void initCache() {
//		cacheManager=CacheManager.getInstance();
//		cacheManager.init(getApplicationContext());
		data=getCacheData();
		
		if(data!=null){
			//���list�Ĵ�С����0������ʾ����
			list=data.getApplyMessage();
			LogUtil.i("list size="+list.size());
			for(ApplyMessage applyMeesage:list){
				applyMeesage.getContent().setHeadImage(mCache.getAsBitmap(applyMeesage.getContent().getUserId())
						);
			}
			hasCache=true;
			initAdapter();
		}
		
		
	}

	private void initAdapter() {
		
		plv=(PullToRefreshListView)findViewById(R.id.apply_msg_list_view);
		adapter=new ApplyAdapter(getApplicationContext(), R.layout.apply_msg_listview_item, list); 
		plv.setAdapter(adapter);
		
		//ˢ�£������Ƿ�������Ϣ������ Config.ishaveNewMessage
		plv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				
				if(myMsgThread==null){
					myMsgThread=new Thread(myMsgRunnable);
					myMsgThread.start();
				}else{
					myMsgThread.start();
				}
				
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				if(myMsgThread==null){
					myMsgThread=new Thread(myMsgRunnable);
					myMsgThread.start();
				}else{
					myMsgThread.start();
				}
				
				
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
				ExtendsIntent intent = new ExtendsIntent(ApplyMsg.this,
						BBSPostDetailView.class, list.get(position - 1)
								.getContent().getEssayId(), null, null, 1);
				startActivity(intent);
			}
		});
		
		pro.setVisibility(View.GONE);
		
	}
	
	/**
	 * ��ȡ����
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ApplyMessageData getCacheData(){
		return (ApplyMessageData) mCache.getAsObject(CacheUtils.APPLY_MSG);
//		CacheData  data =cacheManager.getCache(CacheUtils.APPLY_MSG);
//		if(data==null){
//			return null;
//		}
//		return (List<ApplyMessage>) data.getData();
	}
	
	/**
	 * ���滺��
	 * @param data
	 */
	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	private void addCacheData(ApplyMessageData data){
		mCache.put(CacheUtils.APPLY_MSG, data);
//		CacheData cacheData=new CacheData(CacheUtils.APPLY_MSG, data);
//		cacheManager.addCache(cacheData);
	}
	
	/**
	 * �ȴ���̨������Ϣ��������������Ϣ��List���������������Ϣ��ʱ��
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void processMessage(Message msg) {
		Bundle bundle =msg.getData();
		String json=bundle.getString("MyMessage");
		JSONObject jsonObject=null;
		try {
			jsonObject = new JSONObject(json);
			JSONArray jsonArr=(JSONArray) jsonObject.get("message");
			
			if(jsonArr.length()>0){//������Ϣ
				
				//����Ѿ��л��棬�������Ϣ��ӵ����棬������ֱ��ֱ��ת��Ϊdata
				if(data==null){
					data=new Gson().fromJson(json, ApplyMessageData.class);
					list=(List<ApplyMessage>)data.getApplyMessage();
					new GetPhotoTask().execute();
				}else{//����Ϣ��ӵ�����
					ApplyMessageData temp=new Gson().fromJson(json, ApplyMessageData.class);
					data.getApplyMessage().addAll(temp.getApplyMessage());
					list=data.getApplyMessage();
					new GetPhotoTask().execute();
				}
				applymsg_hint.setVisibility(View.GONE);
			}else{
				applymsg_hint.setVisibility(View.VISIBLE);
				pro.setVisibility(View.GONE);
			}
			
		} catch (JSONException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
			
	}
	
	
	/**
	 * �첽����ͷ��
	 */
	@SuppressWarnings("unused")
	private class GetPhotoTask extends AsyncTask<Void, Void, String> {

		public GetPhotoTask() {
		}

		protected String doInBackground(Void... params) {
			loadPhoto();
			return null;
		}

		

		protected void onPostExecute(String result) {
			//���滺��
			addCacheData(data);
			LogUtil.i("111111111111");
			//����л�����������ݣ����򴴽�����������
			if(hasCache){
				adapter.notifyDataSetChanged();
			}else{
				initAdapter();
				hasCache=true;
			}
			plv.onRefreshComplete();
		}
	}

	//����ͷ��ͼƬ��˳�����ͷ��
	public void loadPhoto(){
		MyMsgReceivePhoto recPho=new MyMsgReceivePhoto();
		for(ApplyMessage applyMeesage:list){
			applyMeesage.getContent().setHeadImage(recPho.getPhoto(mCache, applyMeesage.getContent().getUserId(), applyMeesage.getContent().getAvatar()));
		}
		//����Ϊnull,���ⳤʱ��ռ���ڴ棬����oom�Ĳ�������
		recPho=null;
		LogUtil.i("111111111111^^^^^^^^^^^^^");
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			LogUtil.i("click back button!");
			finish();
			break;

		default:
			break;
		}
		
	}
	
	
	@Override
	protected void onDestroy() {
		if(myMsgThread!=null){
			if(myMsgThread.isAlive()){
				myMsgThread.interrupt();
			}
		}
		super.onDestroy();
	}
	
	
	

}
