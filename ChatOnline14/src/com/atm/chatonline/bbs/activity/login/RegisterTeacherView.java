package com.atm.chatonline.bbs.activity.login;
/**
 * 教师注册界面，用于输入教师注册的基本信息，并将消息传递给处理类
 * 2015.7.24--李
 * 
 * 修改了 只有在有网络的前提下，才可以注册账号，每当打开教师注册界面的时候，会判断当前是否有可用的网络
		 * 2015-7-28-郑
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.atm.charonline.bbs.util.CkeckRegisterMessage;
import com.atm.charonline.bbs.util.SendRegisterTeacher;
import com.atm.chatonline.bbs.commom.Config;
import com.atm.chatonline.bbs.commom.CountDownUtil;
import com.atm.chatonline.bbs.commom.IsNetworkAvailable;
import com.atm.chatonline.bbs.commom.ToastUtil;
import com.example.studentsystem01.R;

public class RegisterTeacherView extends Activity implements OnClickListener{
	private CheckBox chkAgree;
	private String userName,pwd,comfirmPwd,userSchool,userDept,userEmail;
	private String emailResponse;
	private String respCode;
	private String emailNum,comfirmNumber;
	private Spinner spSchool,spDept;
	private Button btnRegister;
	private EditText txtName,txtPwd,txtComPwd,txtEmail,txtNumber;
	private Handler handler;
	private Button btnEmail;
	private TextView tvAgree;
	private SendRegisterTeacher sendRegisterTeacher;
	private String tag="RegisterTeacherView";
	IsNetworkAvailable conNetwork;
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register_teacher_view);
		btnRegister=(Button) findViewById(R.id.btn_register);
		btnEmail=(Button) findViewById(R.id.btn_email_t);
		tvAgree=(TextView) findViewById(R.id.tv_agree_t);
		spSchool=(Spinner) findViewById(R.id.spi_school_t);
		spDept=(Spinner) findViewById(R.id.spi_department_t);
		chkAgree=(CheckBox)findViewById(R.id.chk_agree_t);
		txtName=(EditText) findViewById(R.id.txt_account_t);
		txtPwd=(EditText) findViewById(R.id.txt_password_t);
		txtComPwd=(EditText) findViewById(R.id.txt_rep_password_t);
		txtEmail=(EditText) findViewById(R.id.txt_email_t);
		txtNumber=(EditText) findViewById(R.id.txt_ver_num_t);
		chkAgree.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
		tvAgree.setOnClickListener(this);
		btnEmail.setOnClickListener(this);
		handler=new Handler(){
			public void handleMessage(Message msg){
				switch(msg.what){
				case 1:
					reRirectTo();
					break;
				case 2:
					new ToastUtil().show(getApplication(), "信息填写不完整");
					break;
				case 3:
					new ToastUtil().show(getApplication(), respCode);
					break;
				case 4:
					new ToastUtil().show(getApplication(),"验证码填写有误！");
					break;
				case 5:
					new ToastUtil().show(getApplication(),"账号已经被占用！");
					break;
				case 6:
					CountDownUtil countDown = new CountDownUtil(btnEmail);
					countDown.action();
					new ToastUtil().show(getApplication(), "验证码已发送，请登录邮箱获取验证码");
					break;
					
				case 7:
					new ToastUtil().show(getApplication(), "邮箱已被用");
					break;
				case 8:
					new ToastUtil().show(getApplication(), "邮箱不可用");
					break;
				default :
						break;
				}
			}
		};
	
		/**
		 * 修改了 只有在有网络的前提下，才可以注册账号，每当打开教师注册界面的时候，会判断当前是否有可用的网络
		 * 2015-7-28-郑
		 */
		conNetwork=new IsNetworkAvailable();
		if(!conNetwork.isNetworkAvailable(RegisterTeacherView.this)){
			Toast.makeText(getApplicationContext(), "当前没有可用网络！", Toast.LENGTH_LONG).show();
		}
		
		
	}
	//同意条款按钮和注册按钮
	public void onClick(View v){
		/**
		 * 修改了 在没有网络的情况下，若点击注册按钮，则显示 当前没有可用网络
		 * 2015-7-28-郑
		 */
		if(!conNetwork.isNetworkAvailable(RegisterTeacherView.this)){
			Toast.makeText(getApplicationContext(), "当前没有可用网络！", Toast.LENGTH_LONG).show();
		}
		else{
		switch(v.getId())
		{
		case R.id.btn_email_t:   //-----------------------------修改2015.9.9-郑
			//-------------------记录邮箱2015.9.9-zheng
			userEmail = txtEmail.getText().toString();
			
			//发送邮箱字符创到服务器
			if(txtEmail.getText().toString().equals(""))//没有输入邮箱
			{
				new ToastUtil().show(getApplication(), "请输入如邮箱");	
			}else//有输入邮箱则调用checkResiterMessage检查邮箱的正确性
			{
				Log.i(tag, "邮箱有东西");
				
				if(new CkeckRegisterMessage().checkEmail(userEmail))//检查合乎规则则发送给服务器，
				{
					Log.i(tag, "邮箱符合规则");
					//倒计时      										---------------------修改2015.9.9-郑
					
					//发送邮箱到服务器
					new Thread(emailRunnable).start();
					
				}else
				{
					Log.i(tag, "邮箱不符合规则");
					//邮箱不和规则
					new ToastUtil().show(getApplication(), "请填写一个规范的邮箱");
				}
			}
			break;
			
		case R.id.tv_agree_t:
			tvAgree.setTextColor(android.graphics.Color.BLUE);
			Intent intent =new Intent(this,RegisterAgreementView.class);
			startActivity(intent);
			break;
			
		case R.id.chk_agree_t:
			btnRegister.setClickable(true);
			break;
			
			case R.id.btn_register:
				new Thread(runnable).start();
				break;
			default:
				Toast.makeText(getApplication(), "按钮不响应", Toast.LENGTH_SHORT).show();
				break;
		}
		}
	}
	
	Runnable emailRunnable = new Runnable(){
		public void run(){
			CountDownUtil countDown = new CountDownUtil(btnEmail);
			countDown.action();
			sendRegisterTeacher = new SendRegisterTeacher(userEmail);
			emailResponse=sendRegisterTeacher.checkEmail();//已经把邮箱发送给服务器，服务器也获取邮箱----------------------修改2015.9.9-郑
			Message msg = new Message();
			Log.i(tag, "emailResponse:"+emailResponse);
			if(emailResponse.equals("success"))
			{
				//走到这里，说明邮箱格式正确而且没有被用过，所以才可以倒计时
				
				msg.what=6;
				handler.sendMessage(msg);
				Log.i(tag,"countDown按钮进入倒计时");
				emailNum = sendRegisterTeacher.getChangeJson().getEmailNum();
				Log.i(tag, "从服务器获取的邮箱验证码是"+emailNum);
				
			}else if(emailResponse.equals("alreadyExit")){
				msg.what=7;
				handler.sendMessage(msg);
				
			}
			else if(Integer.parseInt(emailResponse)==201)
			{
				msg.what=8;
				handler.sendMessage(msg);
				
			}
		}
	};
	
	Runnable runnable=new Runnable(){

		@Override
		public void run() {
			// TODO 自动生成的方法存根
			Message msg=new Message();
			userName=txtName.getText().toString();
			pwd=txtPwd.getText().toString();
			comfirmPwd=txtComPwd.getText().toString();
			userSchool=spSchool.getSelectedItem().toString();
			userDept=spDept.getSelectedItem().toString();
			userEmail=txtEmail.getText().toString();
			if(!userName.equals("")&&!pwd.equals("")&&!comfirmPwd.equals("")&&!userEmail.equals("")&&pwd.equals(comfirmPwd))
			{
				try {
					Log.i(tag, "comfirmNumber:"+comfirmNumber+"、emailNum:"+emailNum);
					if(!comfirmNumber.equals(emailNum)){
						msg.what=4;
						handler.sendMessage(msg);
						Log.i(tag, "验证码有错");
					}else{
						respCode=new SendRegisterTeacher(Config.TEACHER,userName,pwd,userSchool,userDept,userEmail).checkRegister();
						if(respCode.equals("注册成功"))
						{
							//注册成功
							msg.what=1;
							handler.sendMessage(msg);
						}else if(respCode.equals("used")){
							msg.what=5;
							handler.sendMessage(msg);
							Log.i(tag, "账号被占用");
						}
						else 
						{
							//由于服务器原因，注册失败
							msg.what=3;handler.sendMessage(msg);
						}
					}
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}else
			{
				msg.what=2;handler.sendMessage(msg);
			}
		}
	};
	public void reRirectTo()//转向主界面
	{
		Intent intent2=new Intent(this,ConfirmTeacher.class);
		intent2.putExtra("userName", userName);
		intent2.putExtra("pwd", pwd);
		Log.i(tag, "向ConfirmTeacher传递了userID:"+userName+"、pwd："+pwd);
		startActivity(intent2);
		finish();
	}
}
