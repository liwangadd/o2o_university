package com.yijianzhai.jue.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yijianzhai.jue.R;
import com.yijianzhai.jue.connection.ConnectionConfigReader;
import com.yijianzhai.jue.connection.HttpUtils.EHttpError;
import com.yijianzhai.jue.service.OnQueryCompleteListener;
import com.yijianzhai.jue.service.QueryId;
import com.yijianzhai.jue.service.UserManageService;
import com.yijianzhai.jue.utils.CheckMobile;

public class LoginActivity extends Activity {

	private String username;

	private Button loginButton;
	private Button registerButton;
	private ImageView loginbackButton;
	private EditText usernameEditText;
	private EditText authcodeEditText;
	private CheckBox checkBox;
	private TextView serviceTextView;
	private ProgressBar progressBar;

	// 后台通信接口
	private UserManageService service;
	private OnQueryCompleteListener queryCompleteListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_main);
		init();
	}

	public void init() {
		// TODO Auto-generated method stub
		loginButton = (Button) findViewById(R.id.login_button);
		registerButton = (Button) findViewById(R.id.rigister_button);
		loginbackButton = (ImageView) findViewById(R.id.login_back);
		authcodeEditText = (EditText) findViewById(R.id.authercode_edit);
		usernameEditText = (EditText) findViewById(R.id.phonenum_edit);
		checkBox = (CheckBox) findViewById(R.id.login_checkbox);
		serviceTextView = (TextView) findViewById(R.id.txt_check);
		progressBar = (ProgressBar) findViewById(R.id.probar_yijian);
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (arg1) {
					loginButton.setClickable(true);
					registerButton.setClickable(true);
				} else {
					loginButton.setClickable(false);
					registerButton.setClickable(false);
				}
			}
		});

		service = new UserManageService();
		getResult();

		// IntentFilter filter = new IntentFilter(
		// "com.yijianzhai.jue.login.FINISH");
		// FinishBroadcastReceiver receiver = new FinishBroadcastReceiver();
		// registerReceiver(receiver, filter);

		registerButton.setOnClickListener(new ButtonListener());
		loginButton.setOnClickListener(new ButtonListener());
		loginbackButton.setOnClickListener(new ButtonListener());
		serviceTextView.setOnClickListener(new ButtonListener());
	}

	private void getResult() {

		queryCompleteListener = new OnQueryCompleteListener() {

			@Override
			public void onQueryComplete(QueryId queryId, Object result,
					EHttpError error) {
				progressBar.setVisibility(View.GONE);
				if (result.equals("success")) {
					Toast.makeText(LoginActivity.this, "登陆成功",
							Toast.LENGTH_SHORT).show();
					SharedPreferences preferences = getSharedPreferences(
							"user_id", MODE_PRIVATE);
					Editor editor = preferences.edit();
					editor.putString("mobile", username);
					editor.commit();
					HomePageActivity.isLogin = true;
					HomePageActivity.UUID = username;
					progressBar.setVisibility(View.GONE);
					finish();
				} else {
					Toast.makeText(LoginActivity.this, "用户名或密码错误",
							Toast.LENGTH_SHORT).show();
					authcodeEditText.setText("");
				}
			}
		};
	}

	class ButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Map<String, String> Data = new HashMap<String, String>();
			switch (v.getId()) {
			case R.id.login_button:

				username = usernameEditText.getText().toString();
				String password = authcodeEditText.getText().toString();
				if (!CheckMobile.isMobileNO(username)) {
					Toast.makeText(LoginActivity.this, "手机号码有误",
							Toast.LENGTH_SHORT).show();
				}else if(password.equals("")){ 
					Toast.makeText(LoginActivity.this, "请输入密码",
							Toast.LENGTH_SHORT).show();
				}else {
					service.Userlogin(username, password, queryCompleteListener);
				}
				
				break;
			case R.id.rigister_button:
				Intent reIntent = new Intent(LoginActivity.this,
						ResignActivity.class);
				LoginActivity.this.startActivity(reIntent);
				break;
			case R.id.login_back:
				finish();
				break;
			case R.id.txt_check:
				Intent intent = new Intent(LoginActivity.this,
						AgreeServiceActivity.class);
				intent.putExtra("url", ConnectionConfigReader.DEFAULT_REMOTE_HOST+"ServiceAgreement.html");
				startActivity(intent);
				break;
			}

		}

	}

	public class FinishBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			if (intent.getBooleanExtra("finish", false)) {
				LoginActivity.this.finish();
			}
		}
	}

}
