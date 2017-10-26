package com.yijianzhai.jue.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yijianzhai.jue.R;
import com.yijianzhai.jue.connection.HttpUtils.EHttpError;
import com.yijianzhai.jue.model.AddressForAndroid;
import com.yijianzhai.jue.service.AddressManageService;
import com.yijianzhai.jue.service.OnQueryCompleteListener;
import com.yijianzhai.jue.service.QueryId;
import com.yijianzhai.jue.utils.CheckMobile;
import com.yijianzhai.jue.utils.InitWedget;

public class NewActivity extends Activity implements InitWedget,
		OnClickListener {
	private EditText nameEditText;
	private EditText mobileEditText;
	private EditText addressEditText;
	private Button districtButton;
	private EditText labelEditText;
	private TextView dorm;
	private TextView teachBuild;
	private TextView library;
	private TextView study;
	private ImageView backImageView;
	private Button saveBtn;
	private Handler handler;
	private static String city, distinct, school;
	private String[] citys = new String[] { "大连市" };
	private String[] distincts = new String[] { "开发区", "甘井子区", "沙河口区" };
	private String[] schools = new String[] { "大连理工大学软件学院", "大连大学", "大连外国语大学" };
	AlertDialog cityDialog, distinctDialog, schoolDialog;
	private boolean isChange = false;

	// 后台通信接口
	private AddressManageService service;
	private OnQueryCompleteListener queryCompleteListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newaddress);
		initwedget();
	}

	@SuppressWarnings("unused")
	private void init() {
		if (isChange) {
			service.UserChangeAddress(AddressForAndroid.adress_id,
					HomePageActivity.UUID, nameEditText.getText().toString(),
					mobileEditText.getText().toString(), city, distinct,
					school, addressEditText.getText().toString(), labelEditText
							.getText().toString(), AddressForAndroid.category,
					queryCompleteListener);
		} else {
			service.UserAddAddress(HomePageActivity.UUID, nameEditText
					.getText().toString(), mobileEditText.getText().toString(),
					city, distinct, school, addressEditText.getText()
							.toString(), labelEditText.getText().toString(), 0,
					queryCompleteListener);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.userdata_back:
			finish();
			break;
		case R.id.userdata_save:
			if (nameEditText.getText().toString().equals("")) {
				Toast.makeText(NewActivity.this, "请填写您的姓名", Toast.LENGTH_SHORT)
						.show();
			} else if (!CheckMobile.isMobileNO(mobileEditText.getText().toString())) {
				Toast.makeText(NewActivity.this, "请填写正确的手机号",
						Toast.LENGTH_SHORT).show();
			} else if (districtButton.getText().toString().equals("")) {
				Toast.makeText(NewActivity.this, "请填写你的地区信息",
						Toast.LENGTH_SHORT).show();
			} else if (addressEditText.getText().toString().equals("")) {
				Toast.makeText(NewActivity.this, "请填写您的详细地址",
						Toast.LENGTH_SHORT).show();
			} else {
				init();
			}
			break;
		case R.id.edt_district:
			cityDialog = new AlertDialog.Builder(NewActivity.this)
					.setTitle("请选择城市")
					.setSingleChoiceItems(citys, 0,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									city = citys[arg1];
									distinctDialog = new AlertDialog.Builder(
											NewActivity.this)
											.setTitle("请选择城市")
											.setSingleChoiceItems(
													distincts,
													0,
													new DialogInterface.OnClickListener() {
														@Override
														public void onClick(
																DialogInterface arg0,
																int arg1) {
															distinct = distincts[arg1];
															schoolDialog = new AlertDialog.Builder(
																	NewActivity.this)
																	.setTitle(
																			"请选择城市")
																	.setSingleChoiceItems(
																			schools,
																			0,
																			new DialogInterface.OnClickListener() {
																				@Override
																				public void onClick(
																						DialogInterface arg0,
																						int arg1) {
																					school = schools[arg1];
																					cityDialog
																							.dismiss();
																					distinctDialog
																							.dismiss();
																					schoolDialog
																							.dismiss();
																					districtButton
																							.setText(city
																									+ distinct
																									+ school);
																				}
																			})
																	.create();
															schoolDialog.show();
														}
													}).create();
									distinctDialog.show();
								}
							}).create();
			cityDialog.show();
			break;
		default:
			labelEditText.setText(((TextView) v).getText().toString());
			break;
		}

	}

	@Override
	public void initwedget() {
		nameEditText = (EditText) findViewById(R.id.edt_name);
		addressEditText = (EditText) findViewById(R.id.edt_address);
		districtButton = (Button) findViewById(R.id.edt_district);
		mobileEditText = (EditText) findViewById(R.id.edt_phone);
		labelEditText = (EditText) findViewById(R.id.edt_label);
		dorm = (TextView) findViewById(R.id.edt_dorm);
		teachBuild = (TextView) findViewById(R.id.edt_teachbuild);
		library = (TextView) findViewById(R.id.edt_library);
		study = (TextView) findViewById(R.id.edt_study);
		backImageView = (ImageView) findViewById(R.id.userdata_back);
		saveBtn = (Button) findViewById(R.id.userdata_save);

		service = new AddressManageService();
		queryCompleteListener = new OnQueryCompleteListener() {
			@Override
			public void onQueryComplete(QueryId queryId, Object result,
					EHttpError error) {
				System.out.println(result.toString());
				if (result.equals("success")) {
					Toast.makeText(NewActivity.this, "操作成功", Toast.LENGTH_SHORT)
							.show();
					finish();
				} else {
					Toast.makeText(NewActivity.this, "操作失败", Toast.LENGTH_SHORT)
							.show();
				}
			}
		};

		backImageView.setOnClickListener(this);
		districtButton.setOnClickListener(this);
		dorm.setOnClickListener(this);
		teachBuild.setOnClickListener(this);
		library.setOnClickListener(this);
		study.setOnClickListener(this);
		saveBtn.setOnClickListener(this);

		ReceiveIntent();
	}

	private void ReceiveIntent() {

		//Intent intent = getIntent();
		String name = AddressForAndroid.name;
		if (name != null && !name.equals("")) {
			isChange = AddressForAndroid.isChange;
			if(isChange){
				nameEditText.setText(AddressForAndroid.name);
				mobileEditText.setText(AddressForAndroid.phone);
				districtButton.setText(AddressForAndroid.city
						+ AddressForAndroid.distinct + AddressForAndroid.school);
				addressEditText.setText(AddressForAndroid.address);
				city = AddressForAndroid.city;
				distinct = AddressForAndroid.distinct;
				school = AddressForAndroid.school;
				AddressForAndroid.name = "";
			}else{
				districtButton.setText(AddressForAndroid.address);
				city = AddressForAndroid.address;
				districtButton.setClickable(false);
			}
		}
	}
}
