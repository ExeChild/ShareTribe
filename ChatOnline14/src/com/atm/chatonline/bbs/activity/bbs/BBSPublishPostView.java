package com.atm.chatonline.bbs.activity.bbs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.atm.charonline.bbs.util.Bimp;
import com.atm.charonline.bbs.util.LogUtil;
import com.atm.charonline.bbs.util.PhotoItem;
import com.atm.charonline.bbs.util.SendDataToServer;
import com.atm.chatonline.bbs.adapter.ExpressionPagerAdapter;
import com.atm.chatonline.bbs.adapter.PhotoAdapter;
import com.atm.chatonline.bbs.commom.UriAPI;
import com.atm.chatonline.chat.ui.AttentionActivity;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.chat.util.Config;
import com.example.studentsystem01.R;

/**
 * @�� com.atm.chatonline.activity.bbs ---BBSPublishPostView
 * @���� ����������ʾ��������
 * @���� ��YD
 * @ʱ�� 2015-8-24
 * 
 */
public class BBSPublishPostView extends BaseActivity implements
		OnClickListener {

	private LinearLayout ll_function, ll_exp, ll_photo;
	private ImageView album, expression, photo_one, aite,
			iv_return;
	private TextView next;
	private EditText title, content;
	private Uri imageUri;
	private Spinner spinner;
	private static String str_title, str_department = "", str_type, str_label = "",
			str_content;
	private String cookie, tag = "BBSPublishPostView",picturePath = "",
			userID = BaseActivity.getSelf().getUserID();
	private static String response;
	private InputMethodManager mInputMethodManager;
	private ViewPager exp_pager;
	private ExpressionPagerAdapter pagerAdapter;
	private List<View> view;
	private List<Map<String, Object>> listItems1, listItems2;
	private SimpleAdapter adapter1, adapter2;
	private GridView grid1, grid2;
	private View viewPager1, viewPager2;
	private boolean isFaceShow = false;
	private Resources res;
	private JSONArray aiteID = new JSONArray();
	private static int i = 0;// ��¼@����
	private Bitmap myBitmap;
	private Uri uri;
	private static final String CHARSET = "utf-8"; // ���ñ���
	private byte[] mContent;
	private String subURL = UriAPI.SUB_URL;
	private String[] description1, description2, type;
	private SendDataToServer send = new SendDataToServer();
	private int contentCursor;
	private int[] imageIds1 = { R.drawable.exp01, R.drawable.exp2,
			R.drawable.exp3, R.drawable.exp4, R.drawable.exp5, R.drawable.exp6,
			R.drawable.exp7, R.drawable.exp8, R.drawable.exp9,
			R.drawable.exp10, R.drawable.exp11, R.drawable.exp12,
			R.drawable.exp13, R.drawable.exp14, R.drawable.exp15,
			R.drawable.exp16, R.drawable.exp17, R.drawable.exp19,
			R.drawable.exp20, R.drawable.exp21, R.drawable.delete, };
	private int[] imageIds2 = { R.drawable.exp22, R.drawable.exp23,
			R.drawable.exp24, R.drawable.exp25, R.drawable.exp26,
			R.drawable.exp27, R.drawable.exp28, R.drawable.exp29,
			R.drawable.exp30, R.drawable.exp31, R.drawable.exp32,
			R.drawable.exp33, R.drawable.delete, };

	private GridView gv_bottom;
	private List<PhotoItem> selectedPic = new ArrayList<PhotoItem>();
	private Context context;
	private PhotoAdapter select_adap;
	
	// public static final int CROP_PHOTO = 2;
//	private static final int PHOTO_REQUEST_GALLERY = 2;// �������ѡ��
//	private static final int PHOTO_REQUEST_CUT = 3;// ���
	
	private static final int RESULT_LABEL = 0;
	private static final int RESULT_PHOTO = 1;
	private static final int RESULT_DEPARTMENT = 2;
	private static final int RESULT_AITE = 3;
	
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bbs_publish_post);

		initView();
		getArray();// ����Դ�ļ����»�ȡ�Ѷ���õ�����

		mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

		accomplishExpBoard();// ʵ�ֱ������
		setListenerForViews();
		//initEvent();

//		title.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//
//			@Override
//			public void onFocusChange(View arg0, boolean arg1) {
//				// TODO Auto-generated method stub
//				if (arg1) {
//					Log.d(tag, "title");
//				}
//				if (isFaceShow) {
//					ll_exp.setVisibility(View.GONE);
//					isFaceShow = false;
//				}
//			}
//		});
//		content.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//
//			@Override
//			public void onFocusChange(View arg0, boolean arg1) {
//				// TODO Auto-generated method stub
//				if (isFaceShow) {
//					ll_exp.setVisibility(View.GONE);
//					isFaceShow = false;
//				}
//			}
//		});
		context = getApplicationContext();
		// ��ȡcookie
		SharedPreferences pref = getSharedPreferences("data",
				Context.MODE_PRIVATE);
		cookie = pref.getString("cookie", "");

		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
				.penaltyLog().penaltyDeath().build());

//		// ���͵ļ����¼�
//		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
//			public void onItemSelected(AdapterView<?> parent, View view,
//					int position, long id) {
//				str_type = type[position];
//			}
//
//			public void onNothingSelected(AdapterView<?> parent) {
//
//			}
//		});
	}

	private void setListenerForViews() {
		// TODO Auto-generated method stub
		album.setOnClickListener(this);
		expression.setOnClickListener(this);
		//department.setOnClickListener(this);
		next.setOnClickListener(this);
		aite.setOnClickListener(this);
		iv_return.setOnClickListener(this);
		title.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (arg1) {
					Log.d(tag, "title");
				}
				if (isFaceShow) {
					ll_exp.setVisibility(View.GONE);
					isFaceShow = false;
				}
			}
		});
		content.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (isFaceShow) {
					ll_exp.setVisibility(View.GONE);
					isFaceShow = false;
				}
			}
		});
		// ���͵ļ����¼�
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				str_type = type[position];
			}

			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		// ��������еı���
		grid1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				LogUtil.p(tag, "��grid1");
				// TODO Auto-generated method stub
				int index1 = Math.max(content.getSelectionStart(), 0);
				LogUtil.p(tag, "index1��" + index1);
				String oriContent1 = content.getText().toString();
				LogUtil.p(tag, "oriContent1��" + oriContent1);
				StringBuilder sBuilder1 = new StringBuilder(oriContent1);// ����StringBuffer,���ٶȸ���
				LogUtil.p(tag, "arg1:" + arg1 + ".arg2:" + arg2);// arg2������λ��
				if (arg2 == 20) {// ɾ��ͼ��
					if (content.getSelectionStart() > 0) {
						int selection = content.getSelectionStart();
						String text2 = oriContent1.substring(selection - 1);
						if (")".equals(text2)) {// ��ɾ���Ǳ����ʱ������ɾ��
							int start = oriContent1.lastIndexOf("#");
							int end = selection;
							content.getText().delete(start, end);
						}
						// input.getText().delete(selection - 1, selection);
					}
				} else {
					sBuilder1.insert(index1, description1[arg2]);
					content.setText(sBuilder1.toString());
					content.setSelection(index1 + description1[arg2].length());
				}
			}

			private void LogUtil(String tag, String string) {
				// TODO Auto-generated method stub

			}
		});

		grid2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				LogUtil.p(tag, "��grid2");
				// TODO Auto-generated method stub
				int index2 = Math.max(content.getSelectionStart(), 0);
				String oriContent2 = content.getText().toString();
				StringBuilder sBuilder2 = new StringBuilder(oriContent2);
				if (arg2 == 12) {
					if (content.getSelectionStart() > 0) {
						int selection = content.getSelectionStart();
						String text2 = oriContent2.substring(selection - 1);
						if (")".equals(text2)) {
							int start = oriContent2.lastIndexOf("#");
							int end = selection;
							content.getText().delete(start, end);
						}
						// input.getText().delete(selection - 1, selection);
					}
				} else {
					sBuilder2.insert(index2, description2[arg2]);
					content.setText(sBuilder2.toString());
					content.setSelection(index2 + description2[arg2].length());
				}

			}
		});
		
	}

//	// ���ü����¼�
//	private void initEvent() {
//		// TODO Auto-generated method stub
//		album.setOnClickListener(this);
//		expression.setOnClickListener(this);
//		department.setOnClickListener(this);
//		sendPost.setOnClickListener(this);
//		aite.setOnClickListener(this);
//		iv_return.setOnClickListener(this);
//		photo_one.setOnClickListener(this);
//
//	}

	// ʵ�ֱ������
	private void accomplishExpBoard() {
		// TODO Auto-generated method stub
		listItems1 = new ArrayList<Map<String, Object>>();
		listItems2 = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < imageIds1.length; i++) {
			Map<String, Object> listItem1 = new HashMap<String, Object>();
			listItem1.put("image1", imageIds1[i]);
			listItems1.add(listItem1);
		}
		for (int i = 0; i < imageIds2.length; i++) {
			Map<String, Object> listItem2 = new HashMap<String, Object>();
			listItem2.put("image2", imageIds2[i]);
			listItems2.add(listItem2);
		}
		adapter1 = new SimpleAdapter(this, listItems1, R.layout.simple_item,
				new String[] { "image1" }, new int[] { R.id.image });
		adapter2 = new SimpleAdapter(this, listItems2, R.layout.simple_item,
				new String[] { "image2" }, new int[] { R.id.image });
		viewPager1 = View.inflate(this, R.layout.viewpager1, null);
		viewPager2 = View.inflate(this, R.layout.viewpager2, null);
		grid1 = (GridView) viewPager1.findViewById(R.id.grid1);
		grid2 = (GridView) viewPager2.findViewById(R.id.grid2);
		grid1.setAdapter(adapter1);//���ע�ͣ��е�һҳ����û�б���
		grid2.setAdapter(adapter2);

		view = new ArrayList<View>();
		view.add(viewPager1);//���ע�ͣ���û�е�һҳ������Ͳ�������
		view.add(viewPager2);
		pagerAdapter = new ExpressionPagerAdapter(view);
		exp_pager.setAdapter(pagerAdapter);

//		//��������еı���
//		grid1.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				LogUtil.p(tag,"��grid1");
//				// TODO Auto-generated method stub
//				int index1 = Math.max(content.getSelectionStart(), 0);
//				LogUtil.p(tag,"index1��"+index1);
//				String oriContent1 = content.getText().toString();
//				LogUtil.p(tag,"oriContent1��"+oriContent1);
//				StringBuilder sBuilder1 = new StringBuilder(oriContent1);//����StringBuffer,���ٶȸ���
//				LogUtil.p(tag, "arg1:"+arg1+".arg2:"+arg2);//arg2������λ��
//				if (arg2 == 20) {//ɾ��ͼ��
//					if (content.getSelectionStart() > 0) {
//						int selection = content.getSelectionStart();
//						String text2 = oriContent1.substring(selection - 1);
//						if (")".equals(text2)) {//��ɾ���Ǳ����ʱ������ɾ��
//							int start = oriContent1.lastIndexOf("#");
//							int end = selection;
//							content.getText().delete(start, end);
//						}
//						// input.getText().delete(selection - 1, selection);
//					}
//				} else {
//					sBuilder1.insert(index1, description1[arg2]);
//					content.setText(sBuilder1.toString());
//					content.setSelection(index1 + description1[arg2].length());
//				}
//			}
//
//			private void LogUtil(String tag, String string) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//
//		grid2.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				 LogUtil.p(tag,"��grid2");
//				// TODO Auto-generated method stub
//				int index2 = Math.max(content.getSelectionStart(), 0);
//				String oriContent2 = content.getText().toString();
//				StringBuilder sBuilder2 = new StringBuilder(oriContent2);
//				if (arg2 == 12) {
//					if (content.getSelectionStart() > 0) {
//						int selection = content.getSelectionStart();
//						String text2 = oriContent2.substring(selection - 1);
//						if (")".equals(text2)) {
//							int start = oriContent2.lastIndexOf("#");
//							int end = selection;
//							content.getText().delete(start, end);
//						}
//						// input.getText().delete(selection - 1, selection);
//					}
//				} else {
//					sBuilder2.insert(index2, description2[arg2]);
//					content.setText(sBuilder2.toString());
//					content.setSelection(index2 + description2[arg2].length());
//				}
//
//			}
//
//			private void LogUtil(String tag, String string) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
	}

	// ����Դ�ļ����»�ȡ�Ѷ���õ�����
	private void getArray() {
		// TODO Auto-generated method stub
		res = BBSPublishPostView.this.getResources();
		type = res.getStringArray(R.array.type);
		description1 = res.getStringArray(R.array.description1);
		description2 = res.getStringArray(R.array.description2);
	}

	// arg0 = requestCode������,arg1 = resultCode�����
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		// ContentResolver contentResolver = getContentResolver();
		switch (arg1) {
		case RESULT_LABEL:
//			if (arg1 == RESULT_OK) {// ��ô�ϵ�����淵�ص�����
//				str_department = arg2.getStringExtra("department");
//			}
			// ������ 2015.10.30��
			if (arg1 == RESULT_OK) {// �ӹ�ע����������
				try {
					aiteID.put(i, arg2.getStringExtra("friendID"));
					i++;
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				contentCursor = content.getSelectionStart();// ��ȡ�ı���ǰ���ڹ��
				content.getText().insert(contentCursor,
						"@" + arg2.getStringExtra("nickName") + " ");// �ڵ�ǰ��괦�����ı�
			}
			break;
		case RESULT_PHOTO:
			// ����᷵�ص�����
			// ����᷵�ص�����
			if (arg2 != null) {
				try {
					selectedPic = (ArrayList<PhotoItem>) arg2
							.getSerializableExtra("selectedPic");
					select_adap = new PhotoAdapter(context, null, selectedPic);
					gv_bottom.setAdapter(select_adap);
					int size = selectedPic.size();
					DisplayMetrics dm = new DisplayMetrics();
					getWindowManager().getDefaultDisplay().getMetrics(dm);
					float density = dm.density;
					/*int allWidth = (int) (110 * size * density);
					int itemWidth = (int) (75 * density);*/
					int allWidth = (int) (160 * size * density);
					int itemWidth = (int) (100 * density);
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
							allWidth, LinearLayout.LayoutParams.FILL_PARENT);
					gv_bottom.setLayoutParams(params);
					gv_bottom.setColumnWidth(itemWidth);
					gv_bottom.setHorizontalSpacing(10);
					gv_bottom.setStretchMode(GridView.NO_STRETCH);
					gv_bottom.setNumColumns(size);
					ll_photo.setVisibility(View.VISIBLE);
				} catch (Exception e) {
					// TODO Auto-generatedcatch block
					e.printStackTrace();
				}
			}
			break;
			
			
//			if (arg2 != null) { // �޸� 2015.10.30��
//				// �õ�ͼƬ��ȫ·��
//				// uri = arg2.getData();
//				try {
//					uri = arg2.getData(); // ��ȡϵͳ���ص���Ƭ��Uri
//					String[] filePathColumn = { MediaStore.Images.Media.DATA };
//					Cursor cursor = getContentResolver().query(uri,
//							filePathColumn, null, null, null);// ��ϵͳ���в�ѯָ��Uri��Ӧ����Ƭ
//					cursor.moveToFirst();
//					int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//					picturePath = cursor.getString(columnIndex); // ��ȡ��Ƭ·��
//					cursor.close();
//					Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
//					// myBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100,
//					// true);
//					// myBitmap = Bimp.resizePhoto(bitmap);
//					myBitmap = Bimp.revitionImageSize(picturePath);
//					photo_one.setImageBitmap(myBitmap);
//					ll_photo.setVisibility(View.VISIBLE);
//					contentCursor = content.getSelectionStart();// ��ȡ�ı���ǰ���ڹ��
//					content.getText().insert(contentCursor,
//							"[#ͼƬ]");// �ڵ�ǰ��괦�����ı�
//				} catch (Exception e) {
//					// TODO Auto-generatedcatch block
//					e.printStackTrace();
//				}
//				// crop(uri);//����ͼƬ
//			}
//			break;
		case RESULT_DEPARTMENT:
			str_department = arg2.getStringExtra("department");
			break;
		case RESULT_AITE:
			break;
		}
	}

	// ��ʼ���ؼ�
	private void initView() {
		// TODO Auto-generated method stub
		next = (TextView) findViewById(R.id.next);
		album = (ImageView) findViewById(R.id.album);
		aite = (ImageView) findViewById(R.id.aite);
		//department = (ImageView) findViewById(R.id.department);
		expression = (ImageView) findViewById(R.id.expression);
		ll_function = (LinearLayout) findViewById(R.id.ll_function);
		ll_exp = (LinearLayout) findViewById(R.id.ll_expression);
		spinner = (Spinner) findViewById(R.id.spinner);
		title = (EditText) findViewById(R.id.title);
		content = (EditText) findViewById(R.id.content);
		exp_pager = (ViewPager) findViewById(R.id.exp_pager);
		photo_one = (ImageView) findViewById(R.id.photo_one);
		ll_photo = (LinearLayout) findViewById(R.id.ll_photo);
		iv_return = (ImageView) findViewById(R.id.iv_return);
		gv_bottom = (GridView) findViewById(R.id.gv_bottom);

	}

//	/*
//	 * ������ȡ
//	 */
//	public void gallery(View view) {
//		// ����ϵͳͼ�⣬ѡ��һ��ͼƬ
//		Intent intent = new Intent(Intent.ACTION_PICK);
//		intent.setType("image/*");
//		// ����һ�����з���ֵ��Activity��������ΪPHOTO_REQUEST_GALLERY
//		startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
//	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.album:
			Intent albumIntent = new Intent(BBSPublishPostView.this,
					BBSSelectPhotosView.class);
			startActivityForResult(albumIntent, 0);
			break;
		case R.id.expression:
			if (!isFaceShow) {
				mInputMethodManager.hideSoftInputFromWindow(
						content.getWindowToken(), 0);
				try {
					Thread.sleep(80);// �����ʱ���һ����Ļ������
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				ll_exp.setVisibility(View.VISIBLE);
				isFaceShow = true;
			} else {
				ll_exp.setVisibility(View.GONE);
				isFaceShow = false;
			}
			break;
		// ����� (2015.10.30��)
		case R.id.aite:
			Intent aiteIntent = new Intent(BBSPublishPostView.this,
					AttentionActivity.class);
			aiteIntent.putExtra("userID", userID);
			aiteIntent.putExtra("fromActivity", Config.BBSPOSTDETAILVIEW);
			startActivityForResult(aiteIntent, 1);
			break;
		case R.id.department:
//			Intent departmentIntent = new Intent(BBSPublishPostView.this,
//					BBSChooseDepartmentView.class);
//			startActivityForResult(departmentIntent, 0);
			break;
		// ����ӣ�2015.11.01�ӣ�
		case R.id.iv_return:
//			AlertDialog.Builder back = new AlertDialog.Builder(this);
//			back.setTitle("��ʾ��")
//					.setMessage("�˳���ǰ�༭��")
//					.setPositiveButton("�˳�",
//							new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog,
//										int which) {
//									BBSPublishPostView.this.finish();
//								}
//
//							})
//					.setNegativeButton("ȡ��",
//							new DialogInterface.OnClickListener() {
//
//								@Override
//								public void onClick(DialogInterface arg0,
//										int arg1) {
//									// TODO Auto-generated method stub
//								}
//							});
//
//			back.create().show();
			break;
		case R.id.next:
			Intent departmentIntent = new Intent(BBSPublishPostView.this,
					BBSChooseDepartmentView.class);
			startActivityForResult(departmentIntent, 2);
			break;
//		case R.id.sendPost:
//			if (!title.getText().toString().equals("")
//					&& !content.getText().toString().equals("")) {
//				if (!str_department.equals("")) {
//					str_title = title.getText().toString();
//					str_content = content.getText().toString();
//					sendDataToServer();// �����ݴ���������
//					try {
//						Thread.sleep(1000);
//					} catch (InterruptedException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}// �����߳�˯��1�룬�ȴ�����response
//
//					Log.i(tag, "response:"+response);
//					try{
//						if (response.equals("success")) {
//							Toast.makeText(BBSPublishPostView.this, "�����ɹ�",
//									Toast.LENGTH_SHORT).show();
//							finish();
//						} else {
//							Toast.makeText(BBSPublishPostView.this, response,
//									Toast.LENGTH_SHORT).show();
//						}
//					}catch(NullPointerException e){
//						return ;
//					}
//					
//				}else{
//					AlertDialog.Builder depart = new AlertDialog.Builder(this);
//					depart.setMessage("��ѡ��ϵ��").setNeutralButton("ȷ��",
//							new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog,
//										int which) {
//								}
//							});
//					depart.create().show();
//				}
//			} else {
//				// �����������Ϊ��ʱ��������ʾ��
//				AlertDialog.Builder build = new AlertDialog.Builder(this);
//				build.setMessage("���������������ٵ������").setNeutralButton("ȷ��",
//						new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog,
//									int which) {
//							}
//						});
//				build.create().show();
//			}
//			i = 0;
//			break;
		case R.id.photo_one:
			Intent photoIntent = new Intent(BBSPublishPostView.this,
					BBSPreviewPicture.class);
			photoIntent.putExtra("picturePath", picturePath);
			startActivityForResult(photoIntent, 3);
			break;
		}
	}

	private void sendDataToServer() {
		// TODO Auto-generated method stub

		new Thread(new Runnable() {
			String getResponse;

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// ������������
				Map<Object, Object> params = new HashMap<Object, Object>();
				params.put("type", str_type);
				params.put("label", str_label);
				params.put("title", str_title);
				params.put("department", str_department);
				params.put("content", str_content);
				params.put("aiteID", aiteID);
				// ��ͼƬ
				ArrayList<Bitmap> list = new ArrayList<Bitmap>();

				if (myBitmap != null) {

					list.add(myBitmap);
					Bitmap[] files = (Bitmap[]) list.toArray(new Bitmap[list
							.size()]);
					getResponse = send.post(subURL + "essay/publish.do",
							params, files, cookie);
				} else {
					getResponse = send.post(subURL + "essay/publish.do",
							params, null, cookie);
				}
				try {
					JSONObject object = new JSONObject(getResponse);
					response = object.getString("tip");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();

	}

	@Override
	public void processMessage(Message msg) {
		// TODO �Զ����ɵķ������

	}
}
