package com.yijianzhai.jue.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.yijianzhai.jue.R;
import com.yijianzhai.jue.utils.WiFiStatusUtil;

public class SearchHistory extends Activity implements OnClickListener,
	OnGetPoiSearchResultListener, OnGetSuggestionResultListener {
	
	private AutoCompleteTextView keyWorldsView;
	private ArrayAdapter<String> sugAdapter = null;
	private ImageView backImageView;
	private TextView searchImageView;
	
	private LocationClient mLocClient;
	
	private PoiSearch mPoiSearch = null;
	private SuggestionSearch mSuggestionSearch = null;
	private TextView loactionAgain;
	
	private int load_Index = 0;
	//当前城市
	private String city;
	//当前位置的经纬度
	private LatLng currentLatLng;
	
	//存储推荐的城市
	public static List<String> addressName;
	public static List<LatLng> addressLatLng;
	public static List<String> cityName;
	//控制activity跳转的handler
	private Handler handler;
	
	private boolean isUserful = false;
	private ProgressBar progressBar;
	
	ArrayList<String> searchHistory;
	//控制定位按钮点击是否返回
	private boolean isReturn = false;
	//监听网络状态
	private SDKReceiver mReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_history);
		
		backImageView=(ImageView)findViewById(R.id.img_return);
		searchImageView = (TextView)findViewById(R.id.search_fangdajing);
		searchImageView.setOnClickListener(this);
		loactionAgain = (TextView) findViewById(R.id.search_location);
		loactionAgain.setOnClickListener(this);
		progressBar = (ProgressBar)findViewById(R.id.search_progress);
        
        mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(this);
		mPoiSearch.setOnGetPoiSearchResultListener(this);
		mSuggestionSearch = SuggestionSearch.newInstance();
		mSuggestionSearch.setOnGetSuggestionResultListener(this);
		keyWorldsView = (AutoCompleteTextView) findViewById(R.id.edt_location);
		sugAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line);
		keyWorldsView.setAdapter(sugAdapter);
		
		backImageView.setOnClickListener(this);
		
		IntentFilter iFilter = new IntentFilter();  
		iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);  
		iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);  
		mReceiver = new SDKReceiver();  
		registerReceiver(mReceiver, iFilter);
		
		keyWorldsView.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				if (cs.length() <= 0) {
					return;
				}
				System.out.println(cs.toString() + "------" + city);
				mSuggestionSearch
						.requestSuggestion((new SuggestionSearchOption())
								.keyword(cs.toString()).city(city));
			}
		});
		
		mLocClient = new LocationClient(SearchHistory.this);
		mLocClient.registerLocationListener(new MyLocationListenner());
		LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); //打开GPRS
		option.setAddrType("all");//返回的定位结果包含地址信息
		option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(5000); //设置发起定位请求的间隔时间为5000ms
		option.disableCache(false);//禁止启用缓存定位
        mLocClient.setLocOption(option);
        mLocClient.start();
        
	}
	
	public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null){
                return;
            }else{
            	city = location.getCity();
            	currentLatLng = new LatLng(location.getLatitude(),location.getLongitude());
            	mLocClient.stop();
            	SharedPreferences shraPreferences=getSharedPreferences("location", Context.MODE_WORLD_READABLE);
				Editor editor=shraPreferences.edit();
				editor.putString("city", city);
				editor.putString("mylocation",location.getAddrStr());
				editor.putString("latitude", String.valueOf(location.getLatitude()));
				editor.putString("longitude",String.valueOf(location.getLongitude()));
				editor.commit();
				if(isUserful){
					loactionAgain.setText(location.getAddrStr());
					isUserful = false;
				}
            }
        }

		@Override
		public void onReceivePoi(BDLocation arg0) {
		}
	}   

    @Override
	public void onGetSuggestionResult(SuggestionResult res) {
    	if (res == null || res.getAllSuggestions() == null) {
			return;
		}
		sugAdapter.clear();
		for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
			if (info.key != null)
				sugAdapter.add(info.key);
		}
		sugAdapter.notifyDataSetChanged();
	}

	@Override
	public void onGetPoiDetailResult(PoiDetailResult result) {
		if (result.error != com.baidu.mapapi.search.core.SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(SearchHistory.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
					.show();
			return;
		} else {
			Toast.makeText(SearchHistory.this, result.getName() + ": " + result.getAddress(), Toast.LENGTH_SHORT)
			.show();
		}
		//handler.sendEmptyMessage(0x123);
	}

	@Override
	public void onGetPoiResult(PoiResult result) {
		if (result.error != com.baidu.mapapi.search.core.SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(SearchHistory.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
					.show();
			progressBar.setVisibility(View.GONE);
			searchImageView.setText("确定");
			return;
		}
		if (result.getAllPoi().size() > 0) {
        	
            // 将poi结果显示到地图上s
            	if(load_Index == 0){
            		addressName=new ArrayList<String>();
                	addressLatLng=new ArrayList<LatLng>();
                	cityName = new ArrayList<String>();
            	}
            	List<PoiInfo> infos = result.getAllPoi();
            	for(PoiInfo info : infos){
            		addressName.add(info.name);
            		addressLatLng.add(info.location);
            		cityName.add(info.city);
            	}
            	if(load_Index < 3){
            		++load_Index;
            		mPoiSearch.searchNearby(new PoiNearbySearchOption().keyword(keyWorldsView.getText().toString()
        					).location(currentLatLng).radius(10000).pageNum(load_Index));
            	}else{
            		load_Index = 0;
            		handler.sendEmptyMessage(0x123);
            	}
            	
            }
	}

	@Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.img_return:
			finish();
			break;
		case R.id.search_fangdajing:
			if(WiFiStatusUtil.getAPNType(SearchHistory.this) == -1){
				Toast.makeText(SearchHistory.this, "请检查网络设置", Toast.LENGTH_SHORT).show();
			}else if(keyWorldsView.getText().toString().equals("")){
				Toast.makeText(SearchHistory.this, "请输入要查询的地址", Toast.LENGTH_SHORT).show();
			}else{
				mPoiSearch.searchNearby(new PoiNearbySearchOption().keyword(keyWorldsView.getText().toString()
						).location(currentLatLng).radius(10000).pageNum(load_Index));
				final Intent intent = new Intent(SearchHistory.this, com.yijianzhai.jue.activity.SearchResult.class);
				handler = new Handler(){

					@Override
					public void handleMessage(Message msg) {
						if(msg.what == 0x123){
							SearchHistory.this.startActivity(intent);
							finish();
						}
					}
					
				};
				progressBar.setVisibility(View.VISIBLE);
				searchImageView.setText("");
			}
			break;
		case R.id.search_location:
			if(WiFiStatusUtil.getAPNType(SearchHistory.this) == -1){
				Toast.makeText(SearchHistory.this, "请检查网络设置", Toast.LENGTH_SHORT).show();
			}else{
				if(!isReturn){
					loactionAgain.setText("正在定位");
					mLocClient.start();
					isUserful = true;
					isReturn = true;
				}else{
					finish();
				}
			}
		default:
			break;
		}
    }

	@Override
	protected void onResume() {
		
		super.onResume();
		SharedPreferences preferences = getSharedPreferences("search_history", MODE_PRIVATE);
	}

	@Override
	protected void onDestroy() {
		
		super.onDestroy();
		mPoiSearch.destroy();
		mSuggestionSearch.destroy();
	}
	
	public class SDKReceiver extends BroadcastReceiver {  
	    public void onReceive(Context context, Intent intent) {  
	        String action = intent.getAction();  
	        if(action.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {  
	            // 网络出错，相应处理  
	        	finish();
	        }  
	    }  
	}
	
}
	