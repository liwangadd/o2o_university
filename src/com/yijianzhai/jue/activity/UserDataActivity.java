package com.yijianzhai.jue.activity;

import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.yijianzhai.jue.R;
import com.yijianzhai.jue.connection.HttpUtils.EHttpError;
import com.yijianzhai.jue.model.DataMap;
import com.yijianzhai.jue.service.OnQueryCompleteListener;
import com.yijianzhai.jue.service.QueryId;
import com.yijianzhai.jue.service.UserManageService;

public class UserDataActivity extends Activity implements OnClickListener,
		OnQueryCompleteListener {

	private ImageView backButton;
	private TextView saveButton;
	private EditText nameSpinner;
	private Button schoolSpinner;
	private Button collageSpinner;
	private PopupWindow schoolWindow;
	private View schoolView, collageView;
	private PopupWindow collageWindow;
	static private String[] school = { "大连理工大学", "哈尔滨工业大学" };
	static private String[] collage = { "数学", "软件" };

	// 选择的学校和专业
	private String seletedSchool = "";
	private String seletedmajor = "";

	// 用户个人资料数据
	private Map<String, Object> dataMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.userdata_activity);
		getData();
		init();
	}

	private void getData() {
		// TODO Auto-generated method stub
		dataMap = ((DataMap) this.getIntent().getSerializableExtra("data"))
				.getMap();

	}

	public void init() {
		// TODO Auto-generated method stub
		backButton = (ImageView) findViewById(R.id.userdata_back);
		saveButton = (TextView) findViewById(R.id.save_button);
		nameSpinner = (EditText) findViewById(R.id.user_name);
		schoolSpinner = (Button) findViewById(R.id.user_school);
		collageSpinner = (Button) findViewById(R.id.user_college);

		backButton.setOnClickListener(this);
		saveButton.setOnClickListener(this);
		schoolSpinner.setOnClickListener(this);
		collageSpinner.setOnClickListener(this);
		if (dataMap != null) {
			if (dataMap.get("school") != null) {
				schoolSpinner.setText(dataMap.get("school").toString());
				seletedSchool = dataMap.get("school").toString();
			}
			if (dataMap.get("major") != null) {
				collageSpinner.setText(dataMap.get("major").toString());
				seletedmajor = dataMap.get("major").toString();
			}
			if (dataMap.get("userName") != null) {
				nameSpinner.setText(dataMap.get("userName").toString());
			}
		}

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.userdata_back:
			finish();
			break;
		case R.id.save_button:
			// 调用更改用户信息接口
			String nameString = nameSpinner.getText().toString();
			if (!nameString.equals("")) {
				if (!seletedSchool.equals("")) {
					if (!seletedmajor.equals("")) {
						new UserManageService().UserChangeInfo(nameSpinner
								.getText().toString(), HomePageActivity.UUID,
								seletedSchool, seletedmajor, this);
					} else {
						Toast.makeText(this, "专业不能为空", Toast.LENGTH_LONG)
								.show();
						break;
					}
				} else {
					Toast.makeText(this, "学校不能为空", Toast.LENGTH_LONG).show();
					break;
				}
			} else {
				Toast.makeText(this, "用户名不能为空", Toast.LENGTH_LONG).show();
				break;
			}
			break;
		case R.id.user_school:
			// 产生学校dialog
			MySchoolClickListener mySchoolClickListener = new MySchoolClickListener();
			Builder buidler = new AlertDialog.Builder(this)
					.setSingleChoiceItems(school, -1, mySchoolClickListener);
			AlertDialog alertDialog = buidler.create();
			alertDialog.show();
			break;
		case R.id.user_college:
			// 产生专业dialog
			MyMajorClickListener myMajorClickListener = new MyMajorClickListener();
			buidler = new AlertDialog.Builder(this).setSingleChoiceItems(
					collage, -1, myMajorClickListener);
			alertDialog = buidler.create();
			alertDialog.show();
			break;
		}
	}

	class MySchoolClickListener implements
			android.content.DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			// TODO Auto-generated method stub
			arg0.dismiss();
			schoolSpinner.setText(school[arg1]);
			seletedSchool = school[arg1];
		}

	}

	class MyMajorClickListener implements
			android.content.DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			// TODO Auto-generated method stub
			arg0.dismiss();
			collageSpinner.setText(collage[arg1]);
			seletedmajor = collage[arg1];
		}

	}

	@Override
	public void onQueryComplete(QueryId queryId, Object result, EHttpError error) {
		// TODO Auto-generated method stub
		if (result != null) {
			if (!result.equals("failed")) {
				Toast.makeText(this, "修改信息成功", Toast.LENGTH_LONG).show();
				SharedPreferences sharedPreferences = getSharedPreferences(
						"user_id", MODE_PRIVATE);
				Editor editor = sharedPreferences.edit();
				editor.putString("school", seletedSchool);
				editor.commit();
				finish();
			} else
				Toast.makeText(this, "修改信息失败", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(this, "网络连接失败", Toast.LENGTH_LONG).show();
		}
	}
}
