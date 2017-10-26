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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.yijianzhai.jue.R;
import com.yijianzhai.jue.adapter.SpecialSellingAdapter;
import com.yijianzhai.jue.connection.HttpUtils.EHttpError;
import com.yijianzhai.jue.model.DataList;
import com.yijianzhai.jue.model.DataMap;
import com.yijianzhai.jue.model.Resturant;
import com.yijianzhai.jue.service.OnQueryCompleteListener;
import com.yijianzhai.jue.service.QueryId;
import com.yijianzhai.jue.service.SpecialSellingService;
import com.yijianzhai.jue.service.Utils;
import com.yijianzhai.jue.utils.InitWedget;

public class SpecialSellingActivity extends Activity implements InitWedget,
	OnClickListener, OnItemClickListener{

	//特卖广告图片
	private ImageView special_selling_adv;
	//盛放菜品的gridview
	private GridView special_selling_griView;
	//返回按钮
	private ImageView back;
	//gridview的适配器
	private SpecialSellingAdapter sAdapter;
	//后台通信接口
	private SpecialSellingService spService;
	private OnQueryCompleteListener queryCompleteListener;
	private Resturant resturant;
	//加载等待圆环
	private ProgressBar progressBar;
	private Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.special_selling);
		initwedget();
	}

	@Override
	public void initwedget() {
		special_selling_adv = (ImageView) findViewById(R.id.special_selling_adv);
		special_selling_griView = (GridView) findViewById(R.id.special_selling_list);
		//special_selling_griView.setOnScrollListener(this);
		back = (ImageView) findViewById(R.id.special_selling_back);
		progressBar = (ProgressBar) findViewById(R.id.special_selling_wait);
		intent = new Intent(this, YiJianZhaiMakeSureActivity.class);
		
		back.setOnClickListener(this);
		
		spService = new SpecialSellingService();
		queryCompleteListener = new OnQueryCompleteListener() {
			
			@Override
			public void onQueryComplete(QueryId queryId, Object result, EHttpError error) {
				progressBar.setVisibility(View.GONE);
				if(result == null){
					Toast.makeText(SpecialSellingActivity.this, "网络连接超时", Toast.LENGTH_SHORT).show();
				}else if(result.equals("failed")){
					Toast.makeText(SpecialSellingActivity.this, "没有优惠菜品", Toast.LENGTH_SHORT).show();
				}else{
					resturant = (Resturant) result;
					sAdapter = new SpecialSellingAdapter(SpecialSellingActivity.this, resturant.getDishes());
					special_selling_griView.setAdapter(sAdapter);
					special_selling_griView.setVisibility(View.VISIBLE);
				}
			}
		};
		spService.getSpecialSelling(queryCompleteListener, Utils.LATITUDE, Utils.LONGITUDE, Utils.CITY);
		special_selling_griView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		intent.removeExtra("datalist");
		intent.removeExtra("datamap");
		DataMap dataMap = new DataMap();
		DataList dataList = new DataList();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("count", 1);
		map.put("sum", resturant.getDishes().get(position).getPrice());
		map.put("sendPrice", resturant.getDelivery_fee());
		map.put("resId", resturant.getResturant_id());
		map.put("basePrice", resturant.getDelivery_starting_fee());
		dataMap.setMap(map);
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object> listMap = new HashMap<String, Object>();
		listMap.put("childNumber", 1);
		listMap.put("childName", resturant.getDishes().get(position).getName());
		listMap.put("childPrice", resturant.getDishes().get(position).getPrice());
		listMap.put("childId", resturant.getDishes().get(position).getDish_id());
//		listMap.put("resId", resturant.getResturant_id());
		listMap.put("childImg", resturant.getDishes().get(position).getImg());
		list.add(listMap);
		dataList.setList(list);
		intent.putExtra("map", dataMap);
		intent.putExtra("data", dataList);
		startActivity(intent);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.special_selling_back:
			finish();
			break;

		default:
			break;
		}
	}
	
}
