package com.yijianzhai.jue.activity;

import java.io.File;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yijianzhai.jue.R;
import com.yijianzhai.jue.connection.HttpUtils.EHttpError;
import com.yijianzhai.jue.model.DataMap;
import com.yijianzhai.jue.service.OnQueryCompleteListener;
import com.yijianzhai.jue.service.QueryId;
import com.yijianzhai.jue.service.UpLoadHeaderService;
import com.yijianzhai.jue.service.UserManageService;
import com.yijianzhai.jue.utils.AsyncImageLoader.ImageCallback;
import com.yijianzhai.jue.utils.GetImgeLoadOption;

public class UsercenterActivity extends Activity implements OnClickListener,
		OnQueryCompleteListener, ImageCallback {

	private ImageView user_icon;
	private TextView user_name;
	private TextView user_acount;
	private Button user_detail;
	private Button my_collection;
	private Button order_query;
	private Button adress_manage;
	private Button soft_about;
	private Button user_login;
	private ImageView back;
	private TextView user_status;

	// 用户信息
	private Map<String, Object> userData;
	private static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	private static final int PHOTO_REQUEST_CUT = 3;// 结果
	private File tempFile;
	//上传头像service
	private UpLoadHeaderService upService;
	private OnQueryCompleteListener queryCompleteListener;
	private DisplayImageOptions options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.usercenter_main);
		options = GetImgeLoadOption.getOptions();
		init();
	}

	private void init() {
		user_icon = (ImageView) findViewById(R.id.userphoto);
		user_name = (TextView) findViewById(R.id.usernametext);
		user_acount = (TextView) findViewById(R.id.userphonenumtext);
		user_detail = (Button) findViewById(R.id.user_detail);
		adress_manage = (Button) findViewById(R.id.adress_manager);
		soft_about = (Button) findViewById(R.id.soft_about);
		my_collection = (Button) findViewById(R.id.my_collection);
		order_query = (Button) findViewById(R.id.order_query);
		user_login = (Button) findViewById(R.id.user_login);
		back = (ImageView) findViewById(R.id.usercenter_back);
		user_status = (TextView) findViewById(R.id.user_login_state);
		back.setOnClickListener(this);
		user_icon.setOnClickListener(this);
		user_detail.setOnClickListener(this);
		my_collection.setOnClickListener(this);
		order_query.setOnClickListener(this);
		soft_about.setOnClickListener(this);
		adress_manage.setOnClickListener(this);
		user_login.setOnClickListener(this);
		upService = new UpLoadHeaderService();
		queryCompleteListener = new OnQueryCompleteListener() {
			
			@Override
			public void onQueryComplete(QueryId queryId, Object result, EHttpError error) {
				System.out.println("clear");
				ImageLoader.getInstance().clearDiskCache();
				ImageLoader.getInstance().clearMemoryCache();
				ImageLoader.getInstance().displayImage(LoadingActivity.URL
						+ HomePageActivity.UUID, user_icon, options);
			}
		};
		
	}

	@Override
	public void imageLoaded(Drawable imageDrawable, String imageUrl) {
		if(imageDrawable != null)
			user_icon.setImageDrawable(imageDrawable);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		ImageLoader.getInstance().displayImage(LoadingActivity.URL
				+ HomePageActivity.UUID, user_icon, options);
		
		if (HomePageActivity.isLogin == false) {
			user_name.setText("未命名");
			user_acount.setText("快捷登录状态");
			user_status.setVisibility(View.VISIBLE);
		}
		if (HomePageActivity.isLogin) {
			user_login.setText("用户注销");
			user_status.setVisibility(View.GONE);
		} else {
			user_login.setText("用户登录");
		}
		new UserManageService().UserGetInfo(HomePageActivity.UUID, this);
	}

	@Override
	public void onClick(View arg0) {
		Intent intent = new Intent();
		switch (arg0.getId()) {
		case R.id.usercenter_back:
			onBackPressed();
			break;
		case R.id.user_detail:
			intent.setClass(UsercenterActivity.this, UserDataActivity.class);
			intent.putExtra("data", new DataMap(userData));
			UsercenterActivity.this.startActivity(intent);
			break;
		case R.id.adress_manager:
			intent.setClass(UsercenterActivity.this,
					DeleveryAddressActivity.class);
			UsercenterActivity.this.startActivity(intent);
			break;
		case R.id.soft_about:
			intent.setClass(UsercenterActivity.this, SoftAbout.class);
			UsercenterActivity.this.startActivity(intent);
			break;
		case R.id.my_collection:
			intent.setClass(UsercenterActivity.this, OffenActivity.class);
			UsercenterActivity.this.startActivity(intent);
			break;
		case R.id.order_query:
			intent.setClass(UsercenterActivity.this, QueryOrderActivity.class);
			UsercenterActivity.this.startActivity(intent);
			break;
		case R.id.user_login:

			if (HomePageActivity.isLogin) {
				HomePageActivity.isLogin = false;
				SharedPreferences preferences = getSharedPreferences("user_id",
						MODE_PRIVATE);
				Editor editor = preferences.edit();
				editor.putString("mobile", "");
				HomePageActivity.UUID = preferences.getString("user_id", "");
				editor.commit();
			}
			intent.setClass(this, LoginActivity.class);
			UsercenterActivity.this.startActivity(intent);
			break;
		case R.id.userphoto:
			//intent.setClass(UsercenterActivity.this, UsercenterActivity.class);
			String[] tempStrings = new String[]{"拍照上传", "从相册中选择"};
			Builder dialog = new AlertDialog.Builder(UsercenterActivity.this)
				.setTitle("上传头像").setItems(tempStrings, new MyOnItemClickListener());
			//dialog.setOnItemSelectedListener(new MyOnItemClickListener());
			dialog.show();
			//startActivity(intent);
			break;
		default:
			break;
		}
	}
	
	public class MyOnItemClickListener implements android.content.DialogInterface.OnClickListener{

		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			if(arg1 == 1){
				// 激活系统图库，选择一张图片
				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setType("image/*");
				// 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
				startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
			}else if(arg1 == 0){
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				// 判断存储卡是否可以用，可用进行存储
				if (hasSdcard()) {
					tempFile = new File(Environment.getExternalStorageDirectory(),
							HomePageActivity.UUID);
					// 从文件中创建uri
					Uri uri = Uri.fromFile(tempFile);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
				}
				// 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
				startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
			}
		}
		
	}
	
	private void crop(Uri uri) {
		// 裁剪图片意图
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// 裁剪框的比例，1：1
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// 裁剪后输出图片的尺寸大小
		intent.putExtra("outputX", 250);
		intent.putExtra("outputY", 250);

		intent.putExtra("outputFormat", "JPEG");// 图片格式
		intent.putExtra("noFaceDetection", true);// 取消人脸识别
		intent.putExtra("return-data", true);
		// 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
		startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}

	/*
	 * 判断sdcard是否被挂载
	 */
	private boolean hasSdcard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PHOTO_REQUEST_GALLERY) {
			// 从相册返回的数据
			if (data != null) {
				// 得到图片的全路径
				Uri uri = data.getData();
				crop(uri);
			}

		} else if (requestCode == PHOTO_REQUEST_CAREMA) {
			// 从相机返回的数据
			if (hasSdcard()) {
				crop(Uri.fromFile(tempFile));
			} else {
				Toast.makeText(UsercenterActivity.this, "未找到存储卡，无法存储照片！", 0).show();
			}

		} else if (requestCode == PHOTO_REQUEST_CUT) {
			// 从剪切图片返回的数据
			if (data != null) {
				Bitmap bitmap = data.getParcelableExtra("data");
				upService.UpLoadHeader(bitmap, HomePageActivity.UUID, queryCompleteListener);
				
			}
			try {
				// 将临时文件删除
				tempFile.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onQueryComplete(QueryId queryId, Object result, EHttpError error) {
		// TODO Auto-generated method stub
		if (result != null) {
			if (!result.toString().equals("failed")) {
				userData = (Map<String, Object>) result;
				if (userData.get("userName") != null) {
					if (HomePageActivity.isLogin) {
						user_name.setText(userData.get("userName").toString());
					}
				}
				if (userData.get("mobile") != null) {
					if (HomePageActivity.isLogin) {
						user_acount.setText(userData.get("mobile").toString());
					}
				}
			}
		} else {
			Toast.makeText(this, "网络连接超时", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, HomePageActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		overridePendingTransition(R.anim.activityin, R.anim.activityout);
		//finish();
	}
	
}