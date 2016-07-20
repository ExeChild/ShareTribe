package com.atm.chatonline.bbs.activity.login;
/**
 *����ʵ��ע����棬��������ѡ���б�򣬻�ȡע����Ϣ����������Ϣ��������
 *2015.7.24,atm--��
 *
 *�޸��� ֻ�����������ǰ���£��ſ���ע���˺ţ�ÿ���򿪽�ʦע������ʱ�򣬻��жϵ�ǰ�Ƿ��п��õ�����
		 * 2015-7-28-֣
 */

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.atm.charonline.bbs.util.CkeckRegisterMessage;
import com.atm.charonline.bbs.util.SendRegisterStudent;
import com.atm.chatonline.bbs.adapter.OnItemSelectedAdapter;
import com.atm.chatonline.bbs.commom.Config;
import com.atm.chatonline.bbs.commom.CountDownUtil;
import com.atm.chatonline.bbs.commom.IsNetworkAvailable;
import com.atm.chatonline.bbs.commom.RegisterSet;
import com.atm.chatonline.bbs.commom.ToastUtil;
import com.example.studentsystem01.R;

public class RegisterStudentView extends Activity {
	private Spinner schoolSpinner,deptSpinner,majorSpinner;
	private String respCode;
	private String emailResponse;
	private Integer schoolId,deptId;
	private int[] department=new RegisterSet().getDepartment();
	private int[] countofJr=new RegisterSet().getCountofjr();
	private int[] countofGy=new RegisterSet().getCountofgy();
	private ArrayAdapter<CharSequence> school_adapter;
	private ArrayAdapter<CharSequence> dept_adapter;
	private ArrayAdapter<CharSequence> major_adapter;
	private Handler handler;
	private String userName,pwd,comfirmPwd,
		userSchool,userDept,userMajor,userEmail,enterSchoolTime,comfirmNumber,emailNum;
	
	private EditText txtName,txtPwd,txtComPwd,txtEmail,txtNumber;
	private Spinner spSchool,spDept,spMajor,spSchoolTime;
	private Button btnRegister,btnEmail;
	private TextView tvAgree;
	private CheckBox chkAgree;
	IsNetworkAvailable conNetwork;
	SendRegisterStudent sendRegisterStudent;
	private String tag="RegisterStudentView";
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register_student_view);	
		btnRegister=(Button) findViewById(R.id.btn_register);
		btnEmail=(Button) findViewById(R.id.btn_email);
		spSchool=(Spinner) findViewById(R.id.spi_school);
		spDept=(Spinner) findViewById(R.id.spi_department);
		spMajor=(Spinner) findViewById(R.id.spi_major);
		txtName=(EditText) findViewById(R.id.txt_account);
		txtPwd=(EditText) findViewById(R.id.txt_password);
		txtComPwd=(EditText) findViewById(R.id.txt_rep_password);
		txtEmail=(EditText) findViewById(R.id.txt_email);
		txtNumber=(EditText) findViewById(R.id.txt_ver_num);
		chkAgree=(CheckBox) findViewById(R.id.chk_agree);
		tvAgree=(TextView) findViewById(R.id.tv_agree_s);
		spSchoolTime=(Spinner) findViewById(R.id.edit_year);
		/**
		 * �޸��� ֻ�����������ǰ���£��ſ���ע���˺ţ�ÿ���򿪽�ʦע������ʱ�򣬻��жϵ�ǰ�Ƿ��п��õ�����
		 * 2015-7-28-֣
		 */
		conNetwork=new IsNetworkAvailable();
		if(!conNetwork.isNetworkAvailable(RegisterStudentView.this)){
			Toast.makeText(getApplicationContext(), "��ǰû�п������磡", Toast.LENGTH_LONG).show();
		}else{
		
		btnEmail.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				
				//-------------------��¼����2015.9.9-zheng
				userEmail = txtEmail.getText().toString();
				
				//���������ַ�����������
				if(txtEmail.getText().toString().equals(""))//û����������
				{
					new ToastUtil().show(getApplication(), "����������");	
				}else//���������������checkResiterMessage����������ȷ��
				{
					Log.i(tag, "�����ж���");
					
					if(new CkeckRegisterMessage().checkEmail(userEmail))//���Ϻ��������͸���������
					{
						Log.i(tag, "������Ϲ���");
						//����ʱ      										---------------------�޸�2015.9.9-֣
						
						
						//���临�Ϲ���
						new Thread(emailRunnable).start();
						
					}else
					{
						Log.i(tag, "���䲻���Ϲ���");
						//���䲻�͹���
						new ToastUtil().show(getApplication(), "����дһ���淶������");
					}
				}
			}
			
		});

		}
		tvAgree.setOnClickListener(new OnClickListener (){
			public void onClick(View v){
				tvAgree.setTextColor(android.graphics.Color.BLUE);
				Intent intent = new Intent(RegisterStudentView.this,RegisterAgreementView.class);
				startActivity(intent);
			}
		});
		chkAgree.setOnClickListener(new OnClickListener (){
			public void onClick(View v){
				btnRegister.setClickable(true);
			}
		});
		btnRegister.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				userName=txtName.getText().toString();	
				pwd=txtPwd.getText().toString();
				comfirmPwd=txtComPwd.getText().toString();
				userSchool=spSchool.getSelectedItem().toString();
				userDept=spDept.getSelectedItem().toString();
				userMajor=spMajor.getSelectedItem().toString();
				userEmail=txtEmail.getText().toString();
				enterSchoolTime=spSchoolTime.getSelectedItem().toString();
				comfirmNumber=txtNumber.getText().toString();
				new Thread(runnable).start();
			}
			
		});
		handler=new Handler(){
			public void handleMessage(Message msg)
			{
				switch(msg.what)
				{
				case 1:
					new ToastUtil().show(getApplication(),respCode);
					break;
				case 2:
					new ToastUtil().show(getApplication(),"��Ϣ��д��������");
					break;
				case 3:
					Toast.makeText(getApplicationContext(), "��ǰû�п������磡", Toast.LENGTH_LONG).show();
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
				}
			}
		};
		loadSpinner();
	}
	
	
	public void loadSpinner(){

		schoolSpinner=(Spinner) findViewById(R.id.spi_school);
		school_adapter=ArrayAdapter.createFromResource(this, R.array.gd_school, android.R.layout.simple_spinner_item);
		school_adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		schoolSpinner.setAdapter(school_adapter);
		schoolSpinner.setOnItemSelectedListener(new OnItemSelectedAdapter(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				schoolId=schoolSpinner.getSelectedItemPosition();
				deptSpinner=(Spinner) findViewById(R.id.spi_department);
				if(true)
				{
					select(deptSpinner,dept_adapter,department[schoolId]);
					deptSpinner=(Spinner) findViewById(R.id.spi_department);
					deptSpinner.setOnItemSelectedListener(new OnItemSelectedAdapter(){

						@Override
						public void onItemSelected(AdapterView<?> parent,
								View view, int position, long id) {
							//��ȡѡ�е�ϵ��id
							deptId=deptSpinner.getSelectedItemPosition();
							if(true)
							{
								//��ȡרҵ�������򲼾�
								majorSpinner=(Spinner) findViewById(R.id.spi_major);
								switch(schoolId)
								{
								case 0 :
									select(majorSpinner,major_adapter,countofJr[deptId]);
									break;
								case 1 :
									select(majorSpinner,major_adapter,countofGy[deptId]);
									break;
								default:
									break;
								}
							}		
						}
					});
				}	
			}
		});
	}
	
	Runnable emailRunnable = new Runnable(){
		public void run(){
			sendRegisterStudent = new SendRegisterStudent(userEmail);
			emailResponse=sendRegisterStudent.checkEmail();//�Ѿ������䷢�͸���������������Ҳ��ȡ����----------------------�޸�2015.9.9-֣
			Message msg = new Message();
			Log.i(tag, "emailResponse:"+emailResponse);
			if(emailResponse.equals("success"))
			{
				//�ߵ����˵�������ʽ��ȷ����û�б��ù������Բſ��Ե���ʱ
				
				msg.what=6;
				handler.sendMessage(msg);
				Log.i(tag,"countDown��ť���뵹��ʱ");
				emailNum = sendRegisterStudent.getChangeJson().getEmailNum();
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
			Message msg = new Message();
		/*	if(txtNumber.getText().toString().equals(""))
			{
				new ToastUtil().show(getApplication(),"����д��֤�룡");
			}else
			{
				//
			}*/
			if(!conNetwork.isNetworkAvailable(RegisterStudentView.this)){
				msg.what=3;
				handler.sendMessage(msg);
			}
			else{
			if(!userName.equals("")&&!pwd.equals("")&&!comfirmPwd.equals("")&&!userEmail.equals("")&&pwd.equals(comfirmPwd))//-------------------��¼����2015.9.9-zheng
			{
				try {
						Log.i(tag, "comfirmNumber:"+comfirmNumber+"��emailNum:"+emailNum);
						if(!comfirmNumber.equals(emailNum)){
							msg.what=4;
							handler.sendMessage(msg);
							Log.i(tag, "��֤���д�");
						}else{
							Log.i(tag, "��֤��û�����˺Ž�����֤");
							respCode=new SendRegisterStudent(Config.STUDENT,userName,pwd,userSchool,userDept,userMajor,userEmail
									,enterSchoolTime).checkRegister();
							Log.i(tag, "����һ���ķ���respCode��"+respCode);
							if(respCode.equals("success"))
							{
								//ע��ɹ�
								redirectTo();
							}else if(respCode.equals("used")){
								msg.what=5;
								handler.sendMessage(msg);
								Log.i(tag, "�˺ű�ռ��");
							}
							else 
							{
								//�������������������⣬ע��ʧ�ܣ�
								msg.what=1;
								handler.sendMessage(msg);
							}
						}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			else
			{
				msg.what=2;
				handler.sendMessage(msg);
			}
			
			}
		}
	};
	
	private void select(Spinner spin,ArrayAdapter<CharSequence> adapter,int arr)
	{
		adapter = ArrayAdapter.createFromResource(this, arr, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin.setAdapter(adapter);
	}
	public void redirectTo()//-------------------��¼����2015.9.9-zheng
	{
		Calendar cl=Calendar.getInstance();
		int currentYear=cl.get(Calendar.YEAR);
		
		int enterschooltime = Integer.parseInt(enterSchoolTime);
		Log.i(tag, "currentYear:"+currentYear+"��enterSchoolTime:"+enterSchoolTime);
		int mulYear = currentYear-enterschooltime;
		if(mulYear>3){
			Log.i(tag, "��ѧ���Ǳ�ҵ��");
			Intent intent=new Intent(this,ConfirmGraduateStudent.class);
			intent.putExtra("userName", userName);
			intent.putExtra("pwd", pwd);
			intent.putExtra("enterSchoolTime", enterSchoolTime);
			Log.i(tag, "��ConfirmGraduateStudent������userID:"+userName+"��pwd��"+pwd);
			startActivity(intent);
			
		}else{
			Log.i(tag, "��ѧ������У��");
			Intent intent=new Intent(this,ConfirmInternalStudent.class);
			intent.putExtra("userName", userName);
			intent.putExtra("pwd", pwd);
			
			Log.i(tag, "��ConfirmInternalStudent������userID:"+userName+"��pwd��"+pwd);
			startActivity(intent);
		}
		
		finish();
	}	
}
