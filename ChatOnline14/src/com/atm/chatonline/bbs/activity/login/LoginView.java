package com.atm.chatonline.bbs.activity.login;
/**
 * �������ڣ����ɵ�½ҳ�棬���̨�����¼��Ϣ
 * 2015.7.21,atm--��
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
	SharedPreferences preferences;//�洢С���ݣ��洢����еĴ���
	IsNetworkAvailable conNetwork;//�ж��Ƿ�����������
	private boolean flag=true;
	private int login=Config.AUTOLOGIN;//�洢intent����Я�����������ݣ�3��ʾ�Զ���¼��4��ʾ��һ�ε�¼��5��ʾ����֮���¼
	private User user;
	private ProgressDialog progressDialog;// ������

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
        initData();//��ʼ������
        user=getPreference();//��ȡuser��Ϣ
        //��ȡSharedPreference����
        preferences = getSharedPreferences("count",MODE_PRIVATE);
        //��ȡcountֵ����һ�������Ǽ�������ʱ���ĸ��������ĸ������ڶ�����Ĭ�ϲ���
        int count = preferences.getInt("count", 0);
        //�жϳ�����ڼ������У�����ǵ�һ����������ת������ҳ��
        if (count == 0) {
        	startActivity(new Intent(getApplicationContext(), WelcomeView.class));
            finish();
        }
        
        Editor editor = preferences.edit();
        //�������ݣ�����Ϊcount
        editor.putInt("count", ++count);
        //�ύ�޸�
        editor.commit();
        
        
        //ͨ��gerPreference��ȡ���û���������
       

        
//        new Thread(runIsNetWorkAvailable).start();
	
        if(!conNetwork.isNetworkAvailable(LoginView.this)){
			String tag=null;
			Log.i(tag, "1232132133");
			Toast.makeText(getApplicationContext(), "��ǰû�п������磡", Toast.LENGTH_LONG).show();
		}
        
        //����Ϊ������if��䣬���ж��Ƿ񱣴������룬���������Ϊ�Զ���¼�������߽����¼����ĵ�¼���
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
        			Log.i(tag, "��¼ʧ��");
        			btnLogin.setClickable(true);//ʧ�ܵ�ʱ�򣬰�ť�����ڵ��2016.7.16
        			showToast("��¼ʧ��");
        			break;
        		case 2:
        			Log.i(tag, "msg.what=2,��redirectTo����");
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
        			showToast("��ǰû�п�������");
        			break;
        		default :
//        			showToast("����������Ӧ");
        				break;
        		}
        	}
        };
       }
        
    
    
    private void initData() {//�¼ӵķ��� 2015.9.14-��
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		//���bundleΪnull��֤���ǰ����ؼ��˳����򣬼��ٴε������ͻ��Զ���¼������loginĬ��ֵ����ΪConfig.AUTOLOGIN
		if(bundle!=null){
		login=bundle.getInt("login");
		Log.i(tag, "logins is "+login);
		}
		Log.i(tag, "login is "+login);
		
	}


	public void onClick(View v){
		Log.i(tag, "onclick �����");
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
		case R.id.btnLogin:	//���µ�¼��ť
			
			login(t1.getText().toString(),t2.getText().toString());
			BaseActivity.getSelf().setUserID(t1.getText().toString());
			BaseActivity.getSelf().setPwd(t2.getText().toString());
			btnLogin.setClickable(false);
			Log.i(tag, "BTNLOGIN) �����");
			
			break;
		case R.id.register:	//����ע�����
			cancelToast();//������Ϣ��ʾ��
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
				//Toast.makeText(getApplication(), "��ť����Ӧ", Toast.LENGTH_SHORT).show();
				break;
		}
		}
    }
    
    
    //��ʼ��popupWindow
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
			public void onClick(View v) {//��ת�һ��˻�����
				popup.dismiss();
				Intent intent=new Intent(LoginView.this,FindUsername.class);
				startActivity(intent);
			}
        	
        });
        v.findViewById(R.id.btn_forget_password).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {//��ת�����������
				popup.dismiss();
				Intent intent=new Intent(LoginView.this,ForgetPassword.class);
				startActivity(intent);
			}
        	
        });
    }
    
    public void login(String userId,String pwd){
    	Log.i(tag, "login ��ִ��");
    	username=userId;
		this.pwd=pwd;
		Thread thread = new Thread(runnable);
		thread.start();
		try {
			Log.i(tag, "join��ִ��");
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    //��������رպ󣬲�ж�أ��ٴ�������Զ�����
    public void autoRedirectTo(){
//    	initProgressDialog();
    	Log.i(tag, "��������رպ󣬲�ж�أ��ٴ�������Զ�����");
       	cancelToast();//������Ϣ��ʾ��
    	Log.i(tag, "�����Զ���¼����");
    	if(con==null){
    		Log.i(tag, "conΪnull");
//    		BaseActivity.con=Communication.newInstance();
    	}
    	
    	BaseActivity.getSelf().setUserID(user.getUserID());
    	setPreference(user.getUserID(),user.getPwd());
    	LogUtil.p(tag, "��ǰ��userID:"+user.getUserID());
    	LogUtil.p(tag, "����BaseActivity��userID:"+BaseActivity.getSelf().getUserID());
    	Thread thread = new Thread(autoLoginRunnable);
    	thread.start();
    	
    	Log.i(tag, "��httpLogin");
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
    
    
    //����������
    public void redirectTo()
    {
    	cancelToast();//������Ϣ��ʾ��
//    	Log.i(tag, "�����Զ���¼����");
//    	Thread thread = new Thread(autoLoginRunnable);
//    	thread.start();
    	
    	Intent intent=new Intent(this,BBSMainView.class);
    	Bundle bundle=new Bundle();
    	bundle.putInt("login", login);
    	intent.putExtras(intent);
    	startActivity(intent);
//    	if(login==Config.AUTOLOGIN){
//			progressDialog.dismiss();
//			Log.i(tag, "���ȿ�����");
//			}
    	finish();
    }
    
    Runnable runnable=new Runnable(){

		@Override
		public void run() {
			Message msg=new Message();
			
			//������û���Ϊ��
			if("".equals(username)||"".equals(pwd))
			{
				respCode="�û������������";
				msg.what=1;
				handler.sendMessage(msg);
				
			} else{
				Log.i(tag, "username ="+username+"  "+" password ="+pwd);
				//��һ�ε�¼��Ҫ�½�Communication,�����ֱ�����ӣ����ǵ���networker �����connect����
				Log.i(tag, "login"+login);
				if(con==null){
					BaseActivity.con=Communication.newInstance();//���ﲻ��Ҫprivate Communication con����Ϊ�����con����WoliaoBaseActivity
				Log.i("---->>>>","con is null");
				}else{
					Log.i(tag, "openSocketChannel");
					Log.i("**************","con is not null");
					BaseActivity.con.openSocketChannel();
				}
				BaseActivity.con.reqLogin(username,pwd);
				Log.i(tag, "reqLogin ��ִ��");
				/*sendLoginInfo = new SendLoginInfo(username,pwd);
				respCode=sendLoginInfo.checkLoginInfo();
				if(respCode.equals("��¼�ɹ�"))//������û�����Ϊ��
				{
						msg.what=2;handler.sendMessage(msg);
				}else{
					//�û����������������
					msg.what=1;handler.sendMessage(msg);
				}*/
		}
	}
   };
   
   Runnable autoLoginRunnable=new Runnable(){

		@Override
		public void run() {
			//con=Communication.newInstance();//���ﲻ��Ҫprivate Communication con����Ϊ�����con����WoliaoBaseActivity
			if(con==null){
				Log.i(tag, "new con");
				BaseActivity.con=Communication.newInstance();//���ﲻ��Ҫprivate Communication con����Ϊ�����con����WoliaoBaseActivity
			}else if(!con.newNetWorker01.socketChannel.isRegistered()){
				Log.i(tag, "opensocket channel");
				BaseActivity.con.openSocketChannel();
			}
			Log.i(tag, "autoLoginRunnable--con.reqLogin:user.getUserID:"+user.getUserID());
			con.reqLogin(user.getUserID(),user.getPwd());	
			//setCookie();
			Log.i(tag, "�ѷ���");
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
			Log.i(tag, "��̳����respCode:"+respCode);
			if(respCode.equals("success"))//������û�����Ϊ��,���ҵ�¼�ɹ�
			{
					handler.sendEmptyMessage(2));//��ת����̳������
					Log.d("cookie",sendLoginInfo.getCookie());
			        SharedPreferences.Editor editor1 =  getSharedPreferences("data",Context.MODE_PRIVATE).edit();
			        editor1.putString("cookie", sendLoginInfo.getCookie());
			        editor1.commit();
					
			}else{
				//�û����������������
				handler.sendEmptyMessage(1);
			}
		} catch (InterruptedException e) {
			handler.sendEmptyMessage(4);//����������Ӧ
		}*/
		
		setCookie();
	}
	   
   };
   
   
   
   public void setCookie(){
	   Log.i(tag, "setCookie--username:"+user.getUserID());
	   sendLoginInfo = new SendLoginInfo(user.getUserID(),null);	
		try {
			respCode=sendLoginInfo.checkLoginInfo();
			Log.i(tag, "��̳����respCode:"+respCode);
			if(respCode.equals("success"))//������û�����Ϊ��,���ҵ�¼�ɹ�
			{
					
					Log.d("setCookie()",sendLoginInfo.getCookie());
			        SharedPreferences.Editor editor1 =  getSharedPreferences("data",Context.MODE_PRIVATE).edit();
			        editor1.putString("cookie", sendLoginInfo.getCookie());
			        editor1.commit();
			        handler.sendEmptyMessage(2);//��ת����̳������
					
			}else{
				//�û����������������
				handler.sendEmptyMessage(1);
			}
		} catch (InterruptedException e) {
			handler.sendEmptyMessage(4);//����������Ӧ
		}
   }
   Runnable runnableGetOfflineMessage = new Runnable(){
	 public void run(){
		 Log.i(tag, "runnableGetOfflineMessage--userID:"+user.getUserID());
		 BaseActivity.con.getOfflineMessage(user.getUserID());
	 }  
   };
 //�ж�toast�Ƿ����
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
 		//cancel��toast
 		public void cancelToast()
 		{
 			if(toast!=null){
 				toast.cancel();
 			}
 		}
 		//�û������ؼ�
 		@Override
 		public void onBackPressed(){
 			cancelToast();
 			Log.i(">>>>","onback");
 			finish();
 		}
 		
// 		private void initProgressDialog() {
// 			// ������ʾ��
// 			progressDialog = new ProgressDialog(LoginView.this);
// 			progressDialog.setTitle("����Ϊ����ת������");
// 			progressDialog.setMessage("Loading...");
// 			progressDialog.setCancelable(true);
// 			progressDialog.show();
// 		}

 		
	


		@Override
		public void processMessage(Message msg) {
			Message msg1 = new Message();//��processMessage��msg����
			if(msg.what==Config.LOGIN_SUCCESS){
				
//				Log.i(tag, "LoginActivity----�õ�LOGIN_SUCCESS��������תChatMainActivity");
				//Intent intent=new Intent(this,ChatMainActivity.class);
//				Log.i(tag, "����ɹ��ˣ������ȡ������Ϣ");
				
				setPreference(user.getUserID(),user.getPwd());
				Log.i(tag, "userID:"+user.getUserID()+"��pwd:"+user.getPwd()+"--�������ݳɹ�");
				BaseActivity.getSelf().setUserID(t1.getText().toString());
				BaseActivity.getSelf().setPwd(t2.getText().toString());
				LogUtil.p(tag, "BaseActivity.getSelf().getUserID:"+BaseActivity.getSelf().getUserID());
				new Thread(runnableGetOfflineMessage).start();
				Log.i(tag, "����ɹ��ˣ������ȡ������Ϣ");
				new Thread(httpLogin).start();
//				redirectTo();
				//startActivity(intent);
			}else if(msg.what==Config.FAILED){
				Log.i(tag, "msg.what=failed");
				msg1.what=1;
				handler.sendMessage(msg1);//2016.7.16����޸ĺ���Ҫ��handler��msg��processMessage��msgҪ��ͬ
				//Toast.makeText(getApplicationContext(), "�û���¼ʧ��", Toast.LENGTH_SHORT).show();
			}else if(msg.what==Config.USER_LOGIN_ALREADY){
				showToast("�û��ѵ�¼");
				//Toast.makeText(getApplicationContext(), "�û��ѵ�¼", Toast.LENGTH_SHORT).show();
			}else if (msg.what == Config.SEND_NOTIFICATION) {
				LogUtil.p(tag, "����Ϣ֪ͨ");
				sendNotifycation();
			}
			
		}
}
