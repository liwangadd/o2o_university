package com.yijianzhai.jue.utils;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.yijianzhai.jue.service.Utils;

public class Mylocation {
	private LocationClient mLocClient;
    private MyLocationListenner myListener = new MyLocationListenner();
    private Context context;
    private String city;
    private InitSreen initSreen;
    
    public Mylocation(Context context){
    	this.context = context;
    }
    
	public void showMylocation(){
        mLocClient = new LocationClient(context);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); //打开GPRS
		option.setAddrType("all");//返回的定位结果包含地址信息
		option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(5000); //设置发起定位请求的间隔时间为5000ms
		option.disableCache(false);//禁止启用缓存定位
        mLocClient.setLocOption(option);
        mLocClient.start();
	}

	/**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null){
                return;
            }else{
            	System.out.println("change_____location");
            	Utils.LATITUDE = String.valueOf(location.getLatitude());
            	Utils.LONGITUDE = String.valueOf(location.getLongitude());
            	
            	Utils.ADDRESS = location.getAddrStr();
            	city = location.getCity();
            	Utils.CITY = city;
            	initSreen.UpdateScreen();
            	mLocClient.stop();

            }
        }

		@Override
		public void onReceivePoi(BDLocation arg0) {
		}
	}   
    
    public void setOnFinishListener(InitSreen initSreen){
    	this.initSreen = initSreen;
    }
    
    public String getCity(){
    	return city;
    }
    
    public interface InitSreen{
    	public void UpdateScreen();
    }
    
    public LocationClient getLocationClient(){
    	return mLocClient;
    }
    
}
