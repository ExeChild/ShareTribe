package com.atm.chatonline.bbs.activity.login;
/**
 * ��ʦע����棬���������ʦע��Ļ�����Ϣ��������Ϣ���ݸ�������
 * 2015.7.24--��
 * 
 * �޸��� ֻ�����������ǰ���£��ſ���ע���˺ţ�ÿ���򿪽�ʦע������ʱ�򣬻��жϵ�ǰ�Ƿ��п��õ�����
		 * 2015-7-28-֣
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
					new ToastUtil().show(getApplication(), "��Ϣ��д������");
					break;
				case 3:
					new ToastUtil().show(getApplication(), respCode);
					break;
				case 4:
					new ToastUtil().show(getApplication(),"��֤����д����");
					break;
				case 5:
					new ToastUtil().show(getApplication(),"�˺��Ѿ���ռ�ã�");
					break;
				case 6:
					CountDownUtil countDown = new CountDownUtil(btnEmail);
					countDown.action();
					new ToastUtil().show(getApplication(), "��֤���ѷ��ͣ����¼�����ȡ��֤��");
					break;
					
				case 7:
					new ToastUtil().show(getApplication(), "�����ѱ���");
					break;
				case 8:
					new ToastUtil().show(getApplication(), "���䲻����");
					break;
				default :
						break;
				}
			}
		};
	
		/**
		 * �޸��� ֻ�����������ǰ���£��ſ���ע���˺ţ�ÿ���򿪽�ʦע������ʱ�򣬻��жϵ�ǰ�Ƿ��п��õ�����
		 * 2015-7-28-֣
		 */
		conNetwork=new IsNetworkAvailable();
		if(!conNetwork.isNetworkAvailable(RegisterTeacherView.this)){
			Toast.makeText(getApplicationContext(), "��ǰû�п������磡", Toast.LENGTH_LONG).show();
		}
		
		
	}
	//ͬ�����ť��ע�ᰴť
	public void onClick(View v){
		/**
		 * �޸��� ��û�����������£������ע�ᰴť������ʾ ��ǰû�п�������
		 * 2015-7-28-֣
		 */
		if(!conNetwork.isNetworkAvailable(RegisterTeacherView.this)){
			Toast.makeText(getApplicationContext(), "��ǰû�п������磡", Toast.LENGTH_LONG).show();
		}
		else{
		switch(v.getId())
		{
		case R.id.btn_email_t:   //-----------------------------�޸�2015.9.9-֣
			//-------------------��¼����2015.9.9-zheng
			userEmail = txtEmail.getText().toString();
			
			//���������ַ�����������
			if(txtEmail.getText().toString().equals(""))//û����������
			{
				new ToastUtil().show(getApplication(), "������������");	
			}else//���������������checkResiterMessage����������ȷ��
			{
				Log.i(tag, "�����ж���");
				
				if(new CkeckRegisterMessage().checkEmail(userEmail))//���Ϻ��������͸���������
				{
					Log.i(tag, "������Ϲ���");
					//����ʱ      										---------------------�޸�2015.9.9-֣
					
					//�������䵽������
					new Thread(emailRunnable).start();
					
				}else
				{
					Log.i(tag, "���䲻���Ϲ���");
					//���䲻�͹���
					new ToastUtil().show(getApplication(), "����дһ���淶������");
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
				Toast.makeText(getApplication(), "��ť����Ӧ", Toast.LENGTH_SHORT).show();
				break;
		}
		}
	}
	
	Runnable emailRunnable = new Runnable(){
		public void run(){
			CountDownUtil countDown = new CountDownUtil(btnEmail);
			countDown.action();
			sendRegisterTeacher = new SendRegisterTeacher(userEmail);
			emailResponse=sendRegisterTeacher.checkEmail();//�Ѿ������䷢�͸���������������Ҳ��ȡ����----------------------�޸�2015.9.9-֣
			Message msg = new Message();
			Log.i(tag, "emailResponse:"+emailResponse);
			if(emailResponse.equals("success"))
			{
				//�ߵ����˵�������ʽ��ȷ����û�б��ù������Բſ��Ե���ʱ
				
				msg.what=6;
				handler.sendMessage(msg);
				Log.i(tag,"countDown��ť���뵹��ʱ");
				emailNum = sendRegisterTeacher.getChangeJson().getEmailNum();
				Log.i(tag, "�ӷ�������ȡ��������֤����"+emailNum);
				
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
			// TODO �Զ����ɵķ������
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
					Log.i(tag, "comfirmNumber:"+comfirmNumber+"��emailNum:"+emailNum);
					if(!comfirmNumber.equals(emailNum)){
						msg.what=4;
						handler.sendMessage(msg);
						Log.i(tag, "��֤���д�");
					}else{
						respCode=new SendRegisterTeacher(Config.TEACHER,userName,pwd,userSchool,userDept,userEmail).checkRegister();
						if(respCode.equals("ע��ɹ�"))
						{
							//ע��ɹ�
							msg.what=1;
							handler.sendMessage(msg);
						}else if(respCode.equals("used")){
							msg.what=5;
							handler.sendMessage(msg);
							Log.i(tag, "�˺ű�ռ��");
						}
						else 
						{
							//���ڷ�����ԭ��ע��ʧ��
							msg.what=3;handler.sendMessage(msg);
						}
					}
				} catch (InterruptedException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
			}else
			{
				msg.what=2;handler.sendMessage(msg);
			}
		}
	};
	public void reRirectTo()//ת��������
	{
		Intent intent2=new Intent(this,ConfirmTeacher.class);
		intent2.putExtra("userName", userName);
		intent2.putExtra("pwd", pwd);
		Log.i(tag, "��ConfirmTeacher������userID:"+userName+"��pwd��"+pwd);
		startActivity(intent2);
		finish();
	}
}
