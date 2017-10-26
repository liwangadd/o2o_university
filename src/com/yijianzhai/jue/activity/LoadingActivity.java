package com.yijianzhai.jue.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.yijianzhai.jue.R;
import com.yijianzhai.jue.connection.ConnectionConfigReader;
import com.yijianzhai.jue.connection.HttpUtils.EHttpError;
import com.yijianzhai.jue.service.HomePageService;
import com.yijianzhai.jue.service.OnQueryCompleteListener;
import com.yijianzhai.jue.service.QueryId;
import com.yijianzhai.jue.utils.Mylocation;
import com.yijianzhai.jue.utils.Mylocation.InitSreen;

public class LoadingActivity extends Activity implements OnQueryCompleteListener{
	
	private List<Map<String, Object>> list;
	private SharedPreferences sharedPreferences;
	private Mylocation mLocation;
	public static String URL = ConnectionConfigReader.DEFAULT_REMOTE_HOST
			+ "getImg?fileName=";
	private ArrayList<String> urlStrings;
	private ArrayList<String> WebUrl;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);
		
		mLocation = new Mylocation(this);
		mLocation.showMylocation();
		mLocation.setOnFinishListener(new InitSreen() {
			
			@Override
			public void UpdateScreen() {
			}
		});
		
		urlStrings = new ArrayList<String>();
		new HomePageService().GetHomeInfo(this);
		
		sharedPreferences=getSharedPreferences("ifwelcome", Context.MODE_WORLD_READABLE);
	    boolean ifwelcome=sharedPreferences.getBoolean("ifwelcome", false);
		if(!ifwelcome){
			Editor editor = sharedPreferences.edit();
			editor.putBoolean("ifwelcome", true);
			editor.commit();
			handler.sendEmptyMessageDelayed(1, 2000);
		}else{
			handler.sendEmptyMessageDelayed(2, 2000);
		}
	}

	@Override
	public void onQueryComplete(QueryId queryId, Object result, EHttpError error) {
		if (queryId == HomePageService.home_page_img) {
			if (result == null) {
				//Toast.makeText(this, "网络连接失败", Toast.LENGTH_LONG).show();
			} else if(!result.equals("failed")) {
				WebUrl = (ArrayList<String>) result;
				for (int i = 0; i < HomePageActivity.imageCount; i++) {
					String url = LoadingActivity.URL
							+ WebUrl.get(i);
					System.out.println(url);
					urlStrings.add(url);
				}

			}
		}
	}

	final Handler handler = new Handler() {

		public void handleMessage(Message msg) { // handle message
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				Intent intent = new Intent();
				intent.setClass(LoadingActivity.this, Welcome.class);
				intent.putExtra("UrlString", urlStrings);
				intent.putExtra("WebUrl", WebUrl);
				LoadingActivity.this.startActivity(intent);
				finish();
				break;
			case 2:
				Intent goIntent = new Intent(LoadingActivity.this,HomePageActivity.class);
				goIntent.putExtra("UrlString", urlStrings);
				goIntent.putExtra("WebUrl", WebUrl);
				LoadingActivity.this.startActivity(goIntent);
				finish();
				break;
			default:
				break;
			}

		}
	};
}
