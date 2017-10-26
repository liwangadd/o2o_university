package com.yijianzhai.jue.activity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.yijianzhai.jue.R;
import com.yijianzhai.jue.adapter.PreferencialAdapter;
import com.yijianzhai.jue.connection.HttpUtils.EHttpError;
import com.yijianzhai.jue.model.DataList;
import com.yijianzhai.jue.model.DataMap;
import com.yijianzhai.jue.model.Package;
import com.yijianzhai.jue.service.OnQueryCompleteListener;
import com.yijianzhai.jue.service.QueryId;
import com.yijianzhai.jue.service.SpecialSellingService;
import com.yijianzhai.jue.service.Utils;
import com.yijianzhai.jue.utils.InitWedget;

public class PrefecencialActivity extends Activity implements InitWedget,
	OnClickListener, OnItemClickListener{
	
	//广告图片
	private ImageView prefecencial_adv;
	//菜品列表
	private ListView prefecencial_list;
	//返回按钮
	private ImageView back;
	//ListView适配器
	private PreferencialAdapter pAdapter;
	//获得特卖列表
	private SpecialSellingService spService;
	private OnQueryCompleteListener queryCompleteListener;
	private ArrayList<Package> packages;
	//等待加载的圆环
	private ProgressBar progressBar;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferencial);
		initwedget();
	}

	@Override
	public void initwedget() {
		prefecencial_adv = (ImageView) findViewById(R.id.preferencial_adv);
		prefecencial_list = (ListView) findViewById(R.id.preferencial_list);
		progressBar = (ProgressBar) findViewById(R.id.preferencial_wait);
		back = (ImageView) findViewById(R.id.preferencial_back);
		back.setOnClickListener(this);
		intent = new Intent(this, YiJianZhaiMakeSureActivity.class);
		
		spService = new SpecialSellingService();
		
		queryCompleteListener = new OnQueryCompleteListener() {
			
			@Override
			public void onQueryComplete(QueryId queryId, Object result, EHttpError error) {
				progressBar.setVisibility(View.GONE);
				if(result == null){
					Toast.makeText(PrefecencialActivity.this, "网络连接超时", Toast.LENGTH_SHORT).show();
				}else if(result.equals("failed")){
					Toast.makeText(PrefecencialActivity.this, "没有优惠菜品", Toast.LENGTH_SHORT).show();
				}else{
					packages = (ArrayList<Package>) result;
					pAdapter = new PreferencialAdapter(PrefecencialActivity.this,
							 packages);
					prefecencial_list.setAdapter(pAdapter);
					prefecencial_list.setVisibility(View.VISIBLE);
				}
			}
		};
		spService.getPreferencial(queryCompleteListener, Utils.LATITUDE, Utils.LONGITUDE, Utils.CITY);
		prefecencial_list.setOnItemClickListener(this);
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		intent.removeExtra("datalist");
		intent.removeExtra("datamap");
		DataMap dataMap = new DataMap();
		DataList dataList = new DataList();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("count", 1);
		map.put("sum", packages.get(position).getSales_price());
		map.put("sendPrice", packages.get(position).getPrice_on_delivery());
		map.put("resId", packages.get(position).getRestaurant_id());
		map.put("basePrice", packages.get(position).getDelivery_starting_fee());
		dataMap.setMap(map);
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		int count = packages.get(position).getDishes().size();
		for(int i = 0; i < count; ++i){
			Map<String, Object> listMap = new HashMap<String, Object>();
			listMap.put("childNumber", 1);
			listMap.put("childName", packages.get(position).getDishes().get(i).getName());
			listMap.put("childPrice", packages.get(position).getDishes().get(i).getSale_price());
			listMap.put("childId", packages.get(position).getDishes().get(i).getDish_id());
			listMap.put("childImg", packages.get(position).getDishes().get(i).getImg());
			list.add(listMap);
		}
		dataList.setList(list);
		intent.putExtra("map", dataMap);
		intent.putExtra("data", dataList);
		startActivity(intent);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.preferencial_back:
			finish();
			break;
		default:
			break;
		}
	}
	
}
