package com.atm.chatonline.bbs.activity.login;
/**
 * 该类用于，生成登陆页面，向后台传输登录信息
 * 2015.7.21,atm--李
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.atm.charonline.bbs.util.LogUtil;
import com.atm.charonline.bbs.util.SendLoginInfo;
import com.atm.chatonline.bbs.activity.bbs.BBSMainView;
import com.atm.chatonline.bbs.commom.IsNetworkAvailable;
import com.atm.chatonline.bbs.commom.MyToast;
import com.atm.chatonline.chat.info.User;
import com.atm.chatonline.chat.net.Communication;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.chat.util.ClearEditText;
import com.atm.chatonline.chat.util.Config;
import com.example.studentsystem01.R;
 
public class LoginView extends BaseActivity implements OnClickListener{
	String tag="LoginView";
	private Button btnLogin;
	private String username,pwd;
	private ClearEditText t1,t2;
	private TextView register,loginError;
	private Handler handler;
	private String respCode;
	private MyToast toast;
	private PopupWindow popup;
	private SendLoginInfo sendLoginInfo;
	SharedPreferences preferences;//存储小数据，存储活动运行的次数
	IsNetworkAvailable conNetwork;//判断是否有网络连接
	private boolean flag=true;
	private int login=Config.AUTOLOGIN;//存储intent里面携带的整型数据，3表示自动登录，4表示第一次登录，5表示下线之后登录
	private User user;
	private ProgressDialog progressDialog;// 进度条

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_view);
        btnLogin=(Button) findViewById(R.id.btnLogin);
        t1=(ClearEditText) findViewById(R.id.edit_account);
        t2=(ClearEditText) findViewById(R.id.edit_password);
        register = (TextView) findViewById(R.id.register);
        loginError=(TextView) findViewById(R.id.login_error);
        conNetwork=new IsNetworkAvailable();
        btnLogin.setOnClickListener(this);
        register.setOnClickListener(this);
        loginError.setOnClickListener(this);
        initPopupWindow();
        initData();//初始化数据
        user=getPreference();//获取user信息
        //获取SharedPreference对象
        preferences = getSharedPreferences("count",MODE_PRIVATE);
        //获取count值，第一个参数是键，存入时用哪个键，就哪个键，第二个是默认参数
        int count = preferences.getInt("count", 0);
        //判断程序与第几次运行，如果是第一次运行则跳转到引导页面
        if (count == 0) {
        	startActivity(new Intent(getApplicationContext(), WelcomeView.class));
            finish();
        }
        
        Editor editor = preferences.edit();
        //存入数据，健名为count
        editor.putInt("count", ++count);
        //提交修改
        editor.commit();
        
        
        //通过gerPreference获取，用户名和密码
       

        
//        new Thread(runIsNetWorkAvailable).start();
	
        if(!conNetwork.isNetworkAvailable(LoginView.this)){
			String tag=null;
			Log.i(tag, "1232132133");
			Toast.makeText(getApplicationContext(), "当前没有可用网络！", Toast.LENGTH_LONG).show();
		}
        
        //以下为新增加if语句，先判断是否保存有密码，若有则可能为自动登录或被迫下线进入登录界面的登录情况
        if(!user.getPwd().equals("")&&!user.getUserID().equals("")&&login!=Config.LOGIN_AFTER_REGISTER){
        	if(login==Config.AUTOLOGIN){
        		Log.i("********>>>", "userId="+user.getUserID()+",pwd="+user.getPwd());
        		autoRedirectTo();
        		Log.i(tag, "autoRedirectTo");
        	}else if(login==Config.BE_OFF_LOGIN){
        		t1.setText(user.getUserID());
        	}
        	Log.i(">>>>>","this step is going1");
        }
        Log.i(">>>>>","this step is going2");
        
        handler=new Handler(){
        	public void handleMessage(Message msg)
        	{
        		switch(msg.what)
        		{
        		case 1:
        			Log.i(tag, "登录失败");
        			btnLogin.setClickable(true);//失败的时候，按钮还能在点击2016.7.16
        			showToast("登录失败");
        			break;
        		case 2:
        			Log.i(tag, "msg.what=2,走redirectTo（）");
        			redirectTo();	
        			/*if(islogin){
        				Log.i(tag,"isLogin1 is "+islogin);
        				redirectTo();
        			}else{
        				
        			islogin=true;
        			Log.i(tag,"isLogin2 is "+islogin);
        			}*/
        			break;
        		case 3:
        			String tag=null;
        			Log.i(tag, "22222222222");
        			flag=false;
        			showToast("当前没有可用网络");
        			break;
        		default :
//        			showToast("服务器无响应");
        				break;
        		}
        	}
        };
       }
        
    
    
    private void initData() {//新加的方法 2015.9.14-李
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		//如果bundle为null，证明是按返回键退出程序，即再次点击程序就会自动登录，所以login默认值设置为Config.AUTOLOGIN
		if(bundle!=null){
		login=bundle.getInt("login");
		Log.i(tag, "logins is "+login);
		}
		Log.i(tag, "login is "+login);
		
	}


	public void onClick(View v){
		Log.i(tag, "onclick 被点击");
    	if(!flag){
    		Message msg=new Message();
    		msg.what=3;
			handler.sendMessage(msg);
		}
		else{
		String tag=null;
		Log.i(tag, "111");
		switch(v.getId())
		{
		case R.id.btnLogin:	//按下登录按钮
			
			login(t1.getText().toString(),t2.getText().toString());
			BaseActivity.getSelf().setUserID(t1.getText().toString());
			BaseActivity.getSelf().setPwd(t2.getText().toString());
			btnLogin.setClickable(false);
			Log.i(tag, "BTNLOGIN) 被点击");
			
			break;
		case R.id.register:	//按下注册界面
			cancelToast();//消除消息提示框
			Intent intent=new Intent(LoginView.this,RegisterChooseView.class);
			startActivity(intent);
			break;
			
		case R.id.login_error:
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			Log.i(tag, "mInputMethodManager.showSoftInput");
			popup.showAtLocation(v, Gravity.BOTTOM, 0, 0);
			break;
		
			default:
				//Toast.makeText(getApplication(), "按钮不响应", Toast.LENGTH_SHORT).show();
				break;
		}
		}
    }
    
    
    //初始化popupWindow
    public void initPopupWindow()
    {
    	View v=getLayoutInflater().inflate(R.layout.login_error_choose_view, null);
    	popup=new PopupWindow(v,LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        ColorDrawable cd = new ColorDrawable(-0000);
       
        popup.setBackgroundDrawable(cd);
        popup.setAnimationStyle(R.style.popup_anim_style);
        
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        popup.setHeight(340);
        v.findViewById(R.id.btn_cancel).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				popup.dismiss();
			}
        	
        });
        v.findViewById(R.id.btn_find_username).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {//跳转找回账户界面
				popup.dismiss();
				Intent intent=new Intent(LoginView.this,FindUsername.class);
				startActivity(intent);
			}
        	
        });
        v.findViewById(R.id.btn_forget_password).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {//跳转忘记密码界面
				popup.dismiss();
				Intent intent=new Intent(LoginView.this,ForgetPassword.class);
				startActivity(intent);
			}
        	
        });
    }
    
    public void login(String userId,String pwd){
    	Log.i(tag, "login 被执行");
    	username=userId;
		this.pwd=pwd;
		Thread thread = new Thread(runnable);
		thread.start();
		try {
			Log.i(tag, "join被执行");
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    //这是软件关闭后，不卸载，再打开软件，自动登入
    public void autoRedirectTo(){
//    	initProgressDialog();
    	Log.i(tag, "这是软件关闭后，不卸载，再打开软件，自动登入");
       	cancelToast();//消除消息提示框
    	Log.i(tag, "发送自动登录请求");
    	if(con==null){
    		Log.i(tag, "con为null");
//    		BaseActivity.con=Communication.newInstance();
    	}
    	
    	BaseActivity.getSelf().setUserID(user.getUserID());
    	setPreference(user.getUserID(),user.getPwd());
    	LogUtil.p(tag, "当前的userID:"+user.getUserID());
    	LogUtil.p(tag, "存入BaseActivity的userID:"+BaseActivity.getSelf().getUserID());
    	Thread thread = new Thread(autoLoginRunnable);
    	thread.start();
    	
    	Log.i(tag, "做httpLogin");
//    	Log.i(tag, "autoRedirectTo---httpLogin");
    	Thread thread2=new Thread(httpLogin);
    	Log.i(tag, "autoRedirectTo---httpLogin111");
		thread2.start();
    	
    	Intent intent=new Intent(this,BBSMainView.class);
    	Bundle bundle=new Bundle();
    	bundle.putInt("login", login);
    	LogUtil.p(tag, "login:"+login);
    	intent.putExtras(intent);
    	startActivity(intent);
//    	new Thread(runnableGetOfflineMessage).start();
    	finish();
    }
    
    
    //进入主界面
    public void redirectTo()
    {
    	cancelToast();//消除消息提示框
//    	Log.i(tag, "发送自动登录请求");
//    	Thread thread = new Thread(autoLoginRunnable);
//    	thread.start();
    	
    	Intent intent=new Intent(this,BBSMainView.class);
    	Bundle bundle=new Bundle();
    	bundle.putInt("login", login);
    	intent.putExtras(intent);
    	startActivity(intent);
//    	if(login==Config.AUTOLOGIN){
//			progressDialog.dismiss();
//			Log.i(tag, "进度框被消灭");
//			}
    	finish();
    }
    
    Runnable runnable=new Runnable(){

		@Override
		public void run() {
			Message msg=new Message();
			
			//密码或用户名为空
			if("".equals(username)||"".equals(pwd))
			{
				respCode="用户名或密码错误";
				msg.what=1;
				handler.sendMessage(msg);
				
			} else{
				Log.i(tag, "username ="+username+"  "+" password ="+pwd);
				//第一次登录就要新建Communication,否则就直接连接，就是调用networker 里面的connect方法
				Log.i(tag, "login"+login);
				if(con==null){
					BaseActivity.con=Communication.newInstance();//这里不需要private Communication con，因为会造成con不是WoliaoBaseActivity
				Log.i("---->>>>","con is null");
				}else{
					Log.i(tag, "openSocketChannel");
					Log.i("**************","con is not null");
					BaseActivity.con.openSocketChannel();
				}
				BaseActivity.con.reqLogin(username,pwd);
				Log.i(tag, "reqLogin 被执行");
				/*sendLoginInfo = new SendLoginInfo(username,pwd);
				respCode=sendLoginInfo.checkLoginInfo();
				if(respCode.equals("登录成功"))//密码或用户名不为空
				{
						msg.what=2;handler.sendMessage(msg);
				}else{
					//用户名或密码输入错误
					msg.what=1;handler.sendMessage(msg);
				}*/
		}
	}
   };
   
   Runnable autoLoginRunnable=new Runnable(){

		@Override
		public void run() {
			//con=Communication.newInstance();//这里不需要private Communication con，因为会造成con不是WoliaoBaseActivity
			if(con==null){
				Log.i(tag, "new con");
				BaseActivity.con=Communication.newInstance();//这里不需要private Communication con，因为会造成con不是WoliaoBaseActivity
			}else if(!con.newNetWorker01.socketChannel.isRegistered()){
				Log.i(tag, "opensocket channel");
				BaseActivity.con.openSocketChannel();
			}
			Log.i(tag, "autoLoginRunnable--con.reqLogin:user.getUserID:"+user.getUserID());
			con.reqLogin(user.getUserID(),user.getPwd());	
			//setCookie();
			Log.i(tag, "已发送");
		}
		
	};
   
   Runnable runIsNetWorkAvailable=new Runnable(){
	   public void run(){
		   Message msg=new Message();
			if(!conNetwork.isNetworkAvailable(LoginView.this)){
				msg.what=3;
				handler.sendMessage(msg);
				
			}
	   }
   };
   Runnable httpLogin=new Runnable(){

	@Override
	public void run() {
		/*sendLoginInfo = new SendLoginInfo(username,null);	
		try {
			respCode=sendLoginInfo.checkLoginInfo();
			Log.i(tag, "论坛登入respCode:"+respCode);
			if(respCode.equals("success"))//密码或用户名不为空,并且登录成功
			{
					handler.sendEmptyMessage(2));//跳转到论坛主界面
					Log.d("cookie",sendLoginInfo.getCookie());
			        SharedPreferences.Editor editor1 =  getSharedPreferences("data",Context.MODE_PRIVATE).edit();
			        editor1.putString("cookie", sendLoginInfo.getCookie());
			        editor1.commit();
					
			}else{
				//用户名或密码输入错误
				handler.sendEmptyMessage(1);
			}
		} catch (InterruptedException e) {
			handler.sendEmptyMessage(4);//服务器无响应
		}*/
		
		setCookie();
	}
	   
   };
   
   
   
   public void setCookie(){
	   Log.i(tag, "setCookie--username:"+user.getUserID());
	   sendLoginInfo = new SendLoginInfo(user.getUserID(),null);	
		try {
			respCode=sendLoginInfo.checkLoginInfo();
			Log.i(tag, "论坛登入respCode:"+respCode);
			if(respCode.equals("success"))//密码或用户名不为空,并且登录成功
			{
					
					Log.d("setCookie()",sendLoginInfo.getCookie());
			        SharedPreferences.Editor editor1 =  getSharedPreferences("data",Context.MODE_PRIVATE).edit();
			        editor1.putString("cookie", sendLoginInfo.getCookie());
			        editor1.commit();
			        handler.sendEmptyMessage(2);//跳转到论坛主界面
					
			}else{
				//用户名或密码输入错误
				handler.sendEmptyMessage(1);
			}
		} catch (InterruptedException e) {
			handler.sendEmptyMessage(4);//服务器无响应
		}
   }
   Runnable runnableGetOfflineMessage = new Runnable(){
	 public void run(){
		 Log.i(tag, "runnableGetOfflineMessage--userID:"+user.getUserID());
		 BaseActivity.con.getOfflineMessage(user.getUserID());
	 }  
   };
 //判断toast是否存在
 		public void showToast(String s)
 		{
 			if(toast==null)
 			{
 				toast=MyToast.makeText(getApplicationContext(), respCode, Toast.LENGTH_SHORT);
 			}else{
 				toast.setText(s);toast.setDuration(Toast.LENGTH_SHORT);
 			}
 			toast.show();
 		}
 		//cancel掉toast
 		public void cancelToast()
 		{
 			if(toast!=null){
 				toast.cancel();
 			}
 		}
 		//用户按返回键
 		@Override
 		public void onBackPressed(){
 			cancelToast();
 			Log.i(">>>>","onback");
 			finish();
 		}
 		
// 		private void initProgressDialog() {
// 			// 进度提示框
// 			progressDialog = new ProgressDialog(LoginView.this);
// 			progressDialog.setTitle("正在为你跳转主界面");
// 			progressDialog.setMessage("Loading...");
// 			progressDialog.setCancelable(true);
// 			progressDialog.show();
// 		}

 		
	


		@Override
		public void processMessage(Message msg) {
			Message msg1 = new Message();//与processMessage的msg区分
			if(msg.what==Config.LOGIN_SUCCESS){
				
//				Log.i(tag, "LoginActivity----得到LOGIN_SUCCESS，即将跳转ChatMainActivity");
				//Intent intent=new Intent(this,ChatMainActivity.class);
//				Log.i(tag, "登入成功了，请求获取离线消息");
				
				setPreference(user.getUserID(),user.getPwd());
				Log.i(tag, "userID:"+user.getUserID()+"、pwd:"+user.getPwd()+"--插入数据成功");
				BaseActivity.getSelf().setUserID(t1.getText().toString());
				BaseActivity.getSelf().setPwd(t2.getText().toString());
				LogUtil.p(tag, "BaseActivity.getSelf().getUserID:"+BaseActivity.getSelf().getUserID());
				new Thread(runnableGetOfflineMessage).start();
				Log.i(tag, "登入成功了，请求获取离线消息");
				new Thread(httpLogin).start();
//				redirectTo();
				//startActivity(intent);
			}else if(msg.what==Config.FAILED){
				Log.i(tag, "msg.what=failed");
				msg1.what=1;
				handler.sendMessage(msg1);//2016.7.16这个修改很重要，handler的msg和processMessage的msg要不同
				//Toast.makeText(getApplicationContext(), "用户登录失败", Toast.LENGTH_SHORT).show();
			}else if(msg.what==Config.USER_LOGIN_ALREADY){
				showToast("用户已登录");
				//Toast.makeText(getApplicationContext(), "用户已登录", Toast.LENGTH_SHORT).show();
			}else if (msg.what == Config.SEND_NOTIFICATION) {
				LogUtil.p(tag, "新消息通知");
				sendNotifycation();
			}
			
		}
}
