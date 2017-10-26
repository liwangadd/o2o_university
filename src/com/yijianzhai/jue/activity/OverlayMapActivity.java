package com.yijianzhai.jue.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yijianzhai.jue.R;
import com.yijianzhai.jue.service.Utils;
import com.yijianzhai.jue.utils.GetImgeLoadOption;
import com.yijianzhai.jue.utils.InitWedget;

public class OverlayMapActivity extends Activity implements InitWedget{
	//返回键
	private ImageView back;
	//地图主控件
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	//存储商家地址信息，用于在地图显示;

	private List<HashMap<String, Object>> resturants;

	private List<Marker> marks;
	//泡泡窗口
	private InfoWindow mInfoWindow;
	
	BitmapDescriptor bdA = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_marka);
	BitmapDescriptor bdB = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_marka);
	private TextView overlay_name;
	private TextView overlay_distance;
	private TextView overlay__time;
	private TextView overlay_depend;
	private ImageView overlay_icon;
	private View layout;
	private int current;
	private DecimalFormat format;
	private DisplayImageOptions options;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_overlay);
		
		options = GetImgeLoadOption.getOptions();
		format = new DecimalFormat("#.0");
		
		initwedget();
	}

	@Override
	public void initwedget() {
		
		back = (ImageView)findViewById(R.id.overlay_return);
		//为返回键设置监听器
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		mMapView = (MapView)findViewById(R.id.bmapView);
		layout = findViewById(R.id.overlay_layout);
		layout.setVisibility(View.GONE);
		overlay__time = (TextView)findViewById(R.id.overlay_ifstoreopen);
		overlay_depend = (TextView)findViewById(R.id.overlay_delivery);
		overlay_distance = (TextView)findViewById(R.id.overlay_storedistance);
		overlay_name = (TextView)findViewById(R.id.overlay_storename);
		overlay_icon = (ImageView) findViewById(R.id.img_store);
		
		mBaiduMap = mMapView.getMap();
        LatLng cenpt = new LatLng(Double.valueOf(Utils.LATITUDE),Double.valueOf(Utils.LONGITUDE));
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
        .target(cenpt)
        .zoom(17)
        .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        
        getResturantAddress();
        
        mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker marker) {
				for(int i =0; i < marks.size(); ++i){
					if(marker.equals(marks.get(i))){
						overlay__time.setText(resturants.get(i).get("time")+"");
						overlay_depend.setText(resturants.get(i).get("delivery_mode")+"");
						overlay_distance.setText(format.format((Double)resturants.get(i).get("distance")/1000)+"km");
						overlay_name.setText(resturants.get(i).get("name")+"");
						ImageLoader.getInstance().displayImage(LoadingActivity.URL 
								+ resturants.get(i).get("img").toString(), overlay_icon, options);
						layout.setVisibility(View.VISIBLE);
						current = i;
					}
				}
				return false;
			}
		});
        mBaiduMap.setOnMapClickListener(new OnMapClickListener() {
			
			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				
				return false;
			}
			
			@Override
			public void onMapClick(LatLng arg0) {
				layout.setVisibility(View.GONE);
			}
		});
        
        layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(OverlayMapActivity.this, RestaurantActivity.class);
				intent.putExtra("resturant_id", String.valueOf(resturants.get(current).get("resturant_id")));
				startActivity(intent);
			}
		});
	}
	
	private void getResturantAddress() {
		resturants = (List<HashMap <String, Object>>) getIntent().getSerializableExtra("resturant");
		marks = new ArrayList<Marker>();
		 LatLng MllA = new LatLng(Double.valueOf(Utils.LATITUDE), Double.valueOf(Utils.LONGITUDE));
		 OverlayOptions MooA = new MarkerOptions().position(MllA).icon(bdA)
	                .zIndex(9).draggable(true);
	     Marker MmMarkerA = (Marker) (mBaiduMap.addOverlay(MooA));
	     //marks.add(MmMarkerA);
		for(HashMap<String, Object> tempResturant : resturants){
			 LatLng llA = new LatLng((Double)tempResturant.get("latitude"), (Double)tempResturant.get("longitude"));
			 OverlayOptions ooA = new MarkerOptions().position(llA).icon(bdA)
		                .zIndex(9).draggable(true);
		     Marker mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
		     marks.add(mMarkerA);
			}
	}
    
}
