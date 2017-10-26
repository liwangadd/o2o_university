package com.yijianzhai.jue.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.yijianzhai.jue.R;
import com.yijianzhai.jue.utils.InitWedget;

public class SoftAbout extends Activity implements OnClickListener, InitWedget{
	
	private Button gradeButton, feedbackButton, updateButton,
		helpButton, protocolButton, aboutusButton;
	private ImageView backImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.softabout);
		initwedget();
	}

	@Override
	public void onClick(View arg0) {
		Intent intent = new Intent();
		switch (arg0.getId()) {
		case R.id.feedback:
			intent.setClass(SoftAbout.this, FeedBackActivity.class);
			SoftAbout.this.startActivity(intent);
			break;
		case R.id.aboutus:
			intent.setClass(SoftAbout.this, AboutusActivity.class);
			SoftAbout.this.startActivity(intent);
			break;
		case R.id.softabout_back:
			finish();
			break;
		case R.id.protocol:
			intent.setClass(SoftAbout.this, AgreeServiceActivity.class);
			intent.putExtra("url", "http://115.28.145.218:8080/jueserver/ServiceAgreement.html");
			SoftAbout.this.startActivity(intent);
			break;
		case R.id.heip:
			intent.setClass(SoftAbout.this, UserHelperActivity.class);
			SoftAbout.this.startActivity(intent);
			break;
		case R.id.grade:
			Uri uri = Uri.parse("market://details?id=" + getPackageName());
			Intent gradeIntent = new Intent(Intent.ACTION_VIEW,uri);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(gradeIntent);
			break;
		default:
			break;
		}
	}

	@Override
	public void initwedget() {
		gradeButton=(Button)findViewById(R.id.grade);
		feedbackButton = (Button)findViewById(R.id.feedback);
		protocolButton = (Button)findViewById(R.id.protocol);
		helpButton = (Button)findViewById(R.id.heip);
		aboutusButton = (Button)findViewById(R.id.aboutus);
		updateButton = (Button)findViewById(R.id.update);
		backImageView = (ImageView)findViewById(R.id.softabout_back);
		
		gradeButton.setOnClickListener(this);
		feedbackButton.setOnClickListener(this);
		protocolButton.setOnClickListener(this);
		backImageView.setOnClickListener(this);
		updateButton.setOnClickListener(this);
		helpButton.setOnClickListener(this);
		aboutusButton.setOnClickListener(this);
	}
	
}
