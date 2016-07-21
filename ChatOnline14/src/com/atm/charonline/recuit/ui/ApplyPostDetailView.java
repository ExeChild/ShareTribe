package com.atm.charonline.recuit.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atm.charonline.bbs.util.BBSConnectNet;
import com.atm.chatonline.bbs.activity.bbs.BBSClickGoodListView;
import com.atm.chatonline.bbs.activity.bbs.BBSCommentView;
import com.atm.chatonline.bbs.adapter.MyWebChromeClient;
import com.atm.chatonline.bbs.commom.UriAPI;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.example.studentsystem01.R;

/**
 * @�� com.atm.chatonline.activity.bbs ---BBSPostDetailView
 * @���� ����������ʾ��������
 * @���� ��YD
 * @ʱ�� 2015-8-24
 * 
 * */

public class ApplyPostDetailView extends BaseActivity implements OnClickListener {
	private WebView webView;
	private WebSettings webSettings;
	private LinearLayout ll_clickGood, ll_comment, ll_share, ll_report;
	private ImageView iv_clickGood, iv_comment, iv_share, iv_report, iv_return,
			iv_collect;
	private TextView tv_clickGood, tv_comment, tv_share, tv_report;
	private boolean flag = true;
	private String essayId = "", response;
	private String relativePath = "essay_content.action";
	private BBSConnectNet httpClientGet;
	private String url = UriAPI.SUB_URL + "apply/";
	public static final int IS_CLICK = 1, IS_NOT_CLICK = 2;
	private Handler handler;
	private int replyNum;
	private static String cookie;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//ȡ��������
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bbs_post_detail);
		
		//����BBSMainView�������һƪ����ʱ���ᴫ����һ��essatId
		Bundle bundle = this.getIntent().getExtras();
		essayId = bundle.getString("id");

		initView();
		initEvent();
				
		//��ȡcookie
		SharedPreferences pref = getSharedPreferences("data",Context.MODE_PRIVATE);
		cookie = pref.getString("cookie", "");

		jsonDemo();//�����߳����������ȡ����
		
		
		// �첽��Ϣ��������
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case IS_CLICK:
					iv_clickGood.setImageResource(R.drawable.clickgood_green);
					tv_clickGood.setTextColor(0xff33cc66);
					tv_clickGood.setText("ȡ����");
					flag = false;
					break;
				case IS_NOT_CLICK:
					iv_clickGood.setImageResource(R.drawable.clickgood);
					tv_clickGood.setTextColor(0xff666666);
					tv_clickGood.setText("����");
					flag = true;
					break;
				}
				tv_comment.setText("����(" + replyNum + ")");
			}
		};
		webViewLoadUrl();//WebView������ҳ

	}
	
	//WebView������ҳ
	private void webViewLoadUrl() {
		// TODO Auto-generated method stub
		
		webSettings = webView.getSettings();
		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setJavaScriptEnabled(true);
		//������ҳ�е����������ʱ�������ø�webView������
		webView.setWebChromeClient(new MyWebChromeClient());
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				view.loadUrl(url);
				// �������Ҫ�����Ե�������¼��Ĵ�������true�����򷵻�false
				return true;
			}
		});
		webView.addJavascriptInterface(new DemoJavaScriptInterface(), "demo1");
		synCookies(this, url + essayId + ".html");//ͬ��cookie
		webView.loadUrl(url + essayId + ".html");
	}
	
	/** 
	 * ��Ϊ�ͻ��˺���ҳ��cookie�ǲ�һ���ģ���������Ҫͬ��һ��cookie 
	 */  
	public static void synCookies(Context context, String url) {  
	    CookieSyncManager.createInstance(context);  
	    CookieManager cookieManager = CookieManager.getInstance();  
	    cookieManager.setAcceptCookie(true);  
	    cookieManager.removeSessionCookie();//�Ƴ�  
	    cookieManager.setCookie(url, cookie);
	    CookieSyncManager.getInstance().sync();  
	}  

	//�����߳����������ȡ����
	private void jsonDemo() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try{
				httpClientGet = new BBSConnectNet(essayId, relativePath, cookie);
				response = httpClientGet.getResponse();				
				parseJSONWithJSONObject(response);//������ȡ��������
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}).start();

	}

	//������ȡ��������
	private void parseJSONWithJSONObject(String response) {
		// TODO Auto-generated method stub
		JSONObject json;
		Message message;
		try {
			json = new JSONObject(response);
			message = new Message();
			message.what = 0;
			boolean clickGood = json.getBoolean("clickGood");
			boolean collect = json.getBoolean("collect");
			replyNum = json.getInt("replyNum");
			if (clickGood == true) {
				message.what = IS_CLICK;
			} else {
				message.what = IS_NOT_CLICK;
			}
			handler.sendMessage(message);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// ��ʼ���ؼ�
	private void initView() {
		// TODO Auto-generated method stub
		webView = (WebView) findViewById(R.id.webView);
		ll_clickGood = (LinearLayout) findViewById(R.id.ll_clickGood);
		ll_comment = (LinearLayout) findViewById(R.id.ll_comment);
		ll_share = (LinearLayout) findViewById(R.id.ll_share);
		ll_report = (LinearLayout) findViewById(R.id.ll_report);
		iv_clickGood = (ImageView) findViewById(R.id.iv_clickGood);
		iv_comment = (ImageView) findViewById(R.id.iv_comment);
		iv_share = (ImageView) findViewById(R.id.iv_share);
		iv_report = (ImageView) findViewById(R.id.iv_report);
		iv_return = (ImageView) findViewById(R.id.iv_return);
		iv_collect = (ImageView) findViewById(R.id.iv_collect);
		tv_clickGood = (TextView) findViewById(R.id.tv_clickGood);
		tv_comment = (TextView) findViewById(R.id.tv_comment);
		tv_share = (TextView) findViewById(R.id.tv_share);
		tv_report = (TextView) findViewById(R.id.tv_report);
	}

	// ���ü�����
	private void initEvent() {
		// TODO Auto-generated method stub
		ll_clickGood.setOnClickListener(this);
		ll_comment.setOnClickListener(this);
		ll_share.setOnClickListener(this);
		ll_report.setOnClickListener(this);
		iv_return.setOnClickListener(this);
		iv_collect.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {

		switch (arg0.getId()) {
		case R.id.ll_clickGood:
			if (flag == true) {
				iv_clickGood.setImageResource(R.drawable.clickgood_green);
				tv_clickGood.setTextColor(0xff33cc66);
				tv_clickGood.setText("ȡ����");
				flag = false;
			} else {
				iv_clickGood.setImageResource(R.drawable.clickgood);
				tv_clickGood.setTextColor(0xff666666);
				tv_clickGood.setText("����");
				flag = true;
			}
			break;
		case R.id.ll_comment:
			Intent intent_comment = new Intent(this, BBSCommentView.class);
			Bundle bundle = new Bundle();
			bundle.putString("essayId", essayId);
			intent_comment.putExtras(bundle);
			startActivity(intent_comment);
			break;
		case R.id.ll_share:
			break;
		case R.id.ll_report:
			Intent intent_report = new Intent(this, RecuitReportView.class);
			startActivity(intent_report);
			break;
		case R.id.iv_return:
			ApplyPostDetailView.this.finish();
			break;
		case R.id.iv_collect:
			Toast.makeText(ApplyPostDetailView.this, "�ղسɹ�", Toast.LENGTH_SHORT).show();
			break;
		}
		
	}
	
	//Javascript���ð�׿�������ڲ���
	class DemoJavaScriptInterface{
	// ��Ϊ��ȫ���⣬��Android4.2��(���Ӧ�õ�android:targetSdkVersion��ֵΪ17+)
				// JSֻ�ܷ��ʴ��� @JavascriptInterfaceע���Java������
				// ��������Ŀ����汾�Ƚϸߣ���Ҫ�ڱ����õĺ���ǰ����@JavascriptInterfaceע�⡣
				@JavascriptInterface
				public void goClickGoodView(String url) {

					Intent intent = new Intent(ApplyPostDetailView.this, BBSClickGoodListView.class);
					Bundle bundle = new Bundle();
					bundle.putString("url", url);
					intent.putExtras(bundle);
					ApplyPostDetailView.this.startActivity(intent);
				}
				@JavascriptInterface
				public void finishPostDetailView(){
					ApplyPostDetailView.this.finish();
				}
	}

	@Override
	public void processMessage(Message msg) {
		// TODO �Զ����ɵķ������
		
	}

}