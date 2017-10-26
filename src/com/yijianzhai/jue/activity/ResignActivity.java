package com.yijianzhai.jue.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yijianzhai.jue.R;
import com.yijianzhai.jue.connection.HttpUtils.EHttpError;
import com.yijianzhai.jue.service.OnQueryCompleteListener;
import com.yijianzhai.jue.service.QueryId;
import com.yijianzhai.jue.service.UserManageService;
import com.yijianzhai.jue.utils.CheckMobile;
import com.yijianzhai.jue.utils.CreateUUID;

public class ResignActivity extends Activity implements OnClickListener,
		OnQueryCompleteListener {

	private String username;
	private static String authcode;
	private String password;
	private Button getauthButton;
	private Button registerButton;
	private ImageView loginbackButton;
	private EditText usernameEditText;
	private EditText authcodeEditText;
	private EditText passwordEditText;
	private EditText passwordagainEditText;
	private CheckBox checkBox;
	private TextView serviceTextView;
	private TimeCount timeCount;
	private String captcha;
	private String tempUUID = "";
	// 后台通信接口
	private UserManageService service;
	OnQueryCompleteListener queryCompleteListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.resign_main);
		SharedPreferences sharedPreferences = getSharedPreferences("user_id",
				MODE_PRIVATE);
		boolean isRegister = sharedPreferences.getBoolean("isRegister", false);
		if (isRegister) {
			new UserManageService().UserResgisterWithoutName(
					tempUUID = CreateUUID.getUUID(), this);
		}
		init();
	}

	private void init() {
		getauthButton = (Button) findViewById(R.id.resign_getauth_button);
		registerButton = (Button) findViewById(R.id.resign_login_button);
		loginbackButton = (ImageView) findViewById(R.id.resign_back);
		usernameEditText = (EditText) findViewById(R.id.resign_phonenum_edit);
		authcodeEditText = (EditText) findViewById(R.id.resign_authercode_edit);
		checkBox = (CheckBox) findViewById(R.id.resign_login_checkbox);
		serviceTextView = (TextView) findViewById(R.id.resign_txt_check);
		timeCount = new TimeCount(60000, 1000);

		passwordEditText = (EditText) findViewById(R.id.resign_password);
		passwordagainEditText = (EditText) findViewById(R.id.resign_password_again);

		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (arg1) {
					registerButton.setClickable(true);
				} else {
					registerButton.setClickable(false);
				}
			}
		});

		service = new UserManageService();
		registerButton.setOnClickListener(this);
		getauthButton.setOnClickListener(this);
		loginbackButton.setOnClickListener(this);
		serviceTextView.setOnClickListener(this);
	}

	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			getauthButton.setText("重新验证");
			getauthButton.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			getauthButton.setClickable(false);
			getauthButton.setText(millisUntilFinished / 1000 + "");
		}
	}

	@Override
	public void onClick(View v) {
		Map<String, String> Data = new HashMap<String, String>();
		switch (v.getId()) {
		case R.id.resign_getauth_button:
			username = usernameEditText.getText().toString();
			if (username.length() != 11) {
				Toast.makeText(ResignActivity.this, "手机号输入有误",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(ResignActivity.this, "正在获取验证码",
						Toast.LENGTH_SHORT).show();
				// 加入时间
				timeCount = new TimeCount(60000, 1000);
				timeCount.start();
				new UserManageService().UserGetCaptcha(username, this);
			}
			break;
		case R.id.resign_login_button:
			String tempUserName = usernameEditText.getText().toString();
			String tempCaptcha = authcodeEditText.getText().toString();

			if (!tempUserName.equals(username)) {
				Toast.makeText(ResignActivity.this, "手机号输入有误",
						Toast.LENGTH_SHORT).show();
				break;
			}
			password = passwordEditText.getText().toString();
			if (!CheckMobile.isMobileNO(username)) {
				Toast.makeText(ResignActivity.this, "手机号输入有误",
						Toast.LENGTH_SHORT).show();
				break;
			} else if (password.equals("")) {
				Toast.makeText(ResignActivity.this, "密码输入有误",
						Toast.LENGTH_SHORT).show();
				break;
			} else if (!passwordagainEditText.getText().toString()
					.equals(password)) {
				Toast.makeText(ResignActivity.this, "两次输入密码有误",
						Toast.LENGTH_SHORT).show();
				break;
			} else if (!captcha.equals(tempCaptcha)) {
				Toast.makeText(ResignActivity.this, "验证码有误", Toast.LENGTH_SHORT)
						.show();
				break;
			} else {
				service.UserRegister(HomePageActivity.UUID, username, password,

				this);

			}
			break;
		case R.id.resign_back:
			finish();
			break;
		case R.id.resign_txt_check:
			Intent intent = new Intent(ResignActivity.this,
					AgreeServiceActivity.class);
			startActivity(intent);
			break;
		}

	}

	@Override
	public void onQueryComplete(QueryId queryId, Object result, EHttpError error) {
		// TODO Auto-generated method stub
		if (result != null) {
			if (queryId == UserManageService.REGISTERWITHNAME) {
				if (!result.equals("failed")) {
					Toast.makeText(this, "注册成功", Toast.LENGTH_LONG).show();
					SharedPreferences sharedPreferences = getSharedPreferences(
							"user_id", MODE_PRIVATE);
					Editor editor = sharedPreferences.edit();
					editor.putString("mobile", username);
					editor.putString("user_id", username);
					editor.putBoolean("isRegister", true);
					editor.commit();
					Intent againIntent = new Intent(
							"com.yijianzhai.jue.login.FINISH");
					ResignActivity.this.sendBroadcast(againIntent);
					finish();
				} else {
					Toast.makeText(this, "注册失败", Toast.LENGTH_LONG).show();
				}
			} else if (queryId == UserManageService.GETCAPTCHA) {
				if (!result.equals("failed")) {
					captcha = result.toString();
				} else {
					timeCount.cancel();
					getauthButton.setText("重新验证");
					getauthButton.setClickable(true);
					Toast.makeText(this, "该手机号已被注册过", Toast.LENGTH_LONG).show();

				}
			} else if (queryId == UserManageService.REGISTERWITHOUTNAME) {
				if (!result.equals("failed")) {
					HomePageActivity.UUID = tempUUID;
				} else {
					Toast.makeText(this, "网络连接超时", Toast.LENGTH_LONG).show();
				}
			}
		} else {
			Toast.makeText(this, "网络连接超时", Toast.LENGTH_LONG).show();
			if (timeCount != null) {
				timeCount.cancel();
				getauthButton.setText("重新验证");
				getauthButton.setClickable(true);
			}
		}
	}
}
