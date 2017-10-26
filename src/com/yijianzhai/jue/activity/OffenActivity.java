package com.yijianzhai.jue.activity;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.yijianzhai.jue.R;
import com.yijianzhai.jue.adapter.OffenItemAdapter;
import com.yijianzhai.jue.connection.HttpUtils.EHttpError;
import com.yijianzhai.jue.service.OnQueryCompleteListener;
import com.yijianzhai.jue.service.QueryId;
import com.yijianzhai.jue.service.RestaurantManager;
import com.yijianzhai.jue.utils.Mylocation;
import com.yijianzhai.jue.utils.Mylocation.InitSreen;

public class OffenActivity extends Activity implements OnClickListener,InitSreen{
	
	private ImageView back;
	private ImageView edit;
	private ListView listView;
	private OffenItemAdapter adapter;
	//后台通信接口
	RestaurantManager manager;
	OnQueryCompleteListener queryCompleteListener;
	//保存常去的店
	ArrayList<Map<String, Object>> offenResturant;
	//定位
	private Mylocation location;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.offen_restaurant);
		back = (ImageView)findViewById(R.id.offen_back);
		edit = (ImageView)findViewById(R.id.offen_edit);
		listView = (ListView)findViewById(R.id.offen_list);
		
		back.setOnClickListener(this);
		edit.setOnClickListener(this);
		
		location = new Mylocation(this);
		location.setOnFinishListener(this);
		location.showMylocation();
		
		manager = new RestaurantManager();
		queryCompleteListener = new OnQueryCompleteListener() {
			
			@Override
			public void onQueryComplete(QueryId queryId, Object result, EHttpError error) {
				if (result == null) {
					Toast.makeText(OffenActivity.this, "操作超时",
							Toast.LENGTH_SHORT).show();
					findViewById(R.id.offen_load).setVisibility(View.GONE);
				} else if(result.equals("failed")){
					findViewById(R.id.offen_load).setVisibility(View.GONE);
					findViewById(R.id.offen_error).setVisibility(View.VISIBLE);
				} else{
					offenResturant = (ArrayList<Map<String, Object>>) result;
					adapter = new OffenItemAdapter(OffenActivity.this, offenResturant);
					listView.setAdapter(adapter);
					listView.setVisibility(View.VISIBLE);
					findViewById(R.id.offen_load).setVisibility(View.GONE);
				}
			}
		};
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(OffenActivity.this, RestaurantActivity.class);
				intent.putExtra("resturant_id", offenResturant.get(arg2).get("resturant_id")+"");
				OffenActivity.this.startActivity(intent);
			}
			
		});
	}

	@Override
	public void UpdateScreen() {
		SharedPreferences preferences = getSharedPreferences("user_id", MODE_PRIVATE);
		String school = preferences.getString("school", null);
		manager.GetOffenResturant(HomePageActivity.UUID, location.getCity(), school, queryCompleteListener);
		
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.offen_back:
			finish();
			break;
		case R.id.offen_edit:
			adapter.setEdit();
		}
	}
	
}
