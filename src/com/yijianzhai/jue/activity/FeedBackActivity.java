package com.yijianzhai.jue.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.yijianzhai.jue.R;
import com.yijianzhai.jue.connection.HttpUtils.EHttpError;
import com.yijianzhai.jue.service.OnQueryCompleteListener;
import com.yijianzhai.jue.service.QueryId;
import com.yijianzhai.jue.service.UserManageService;
import com.yijianzhai.jue.utils.InitWedget;

public class FeedBackActivity extends Activity implements OnClickListener,
		InitWedget, OnQueryCompleteListener {

	private Button sureButton, cancelButton;
	private EditText text;
	private ImageView backImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);
		initwedget();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.feedback_back:
			finish();
			break;
		case R.id.feedback_sure:
			String message;
			message = text.getText().toString();
			if (!message.equals("")) {
				new UserManageService().UserSuggestion(message, this);
			} else {
				Toast.makeText(this, "建议不能为空", Toast.LENGTH_LONG);
			}
			break;
		case R.id.feedback_cancle:
			text.setText("");
			break;
		default:
			break;
		}
	}

	@Override
	public void initwedget() {
		sureButton = (Button) findViewById(R.id.feedback_sure);
		cancelButton = (Button) findViewById(R.id.feedback_cancle);
		text = (EditText) findViewById(R.id.feedback_text);
		backImageView = (ImageView) findViewById(R.id.feedback_back);

		sureButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
		backImageView.setOnClickListener(this);
	}

	@Override
	public void onQueryComplete(QueryId queryId, Object result, EHttpError error) {
		// TODO Auto-generated method stub
		if (result != null) {
			if (!result.equals("failed")) {
				Toast.makeText(this, "感谢您的宝贵意见，我们将会尽快", Toast.LENGTH_LONG)
						.show();
				finish();
			} else {
				Toast.makeText(this, "意见反馈失败", Toast.LENGTH_LONG).show();
			}
		} else
			Toast.makeText(this, "网络连接超时", Toast.LENGTH_LONG).show();
	}

}
