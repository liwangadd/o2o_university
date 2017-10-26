package com.yijianzhai.jue.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.yijianzhai.jue.R;



public class AboutusActivity extends Activity {
	private ImageView back_btn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutus_main);
		init();
	}

	void init(){
		back_btn=(ImageView)findViewById(R.id.aboutus_back);
		back_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(v.getId()==R.id.aboutus_back)
				finish();
				
			}
		});
	}
}
