package com.yijianzhai.jue.service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yijianzhai.jue.R.string;
import com.yijianzhai.jue.connection.HttpUtils;
import com.yijianzhai.jue.connection.HttpUtils.EHttpError;
import com.yijianzhai.jue.model.Package;
import com.yijianzhai.jue.model.Resturant;

public class SpecialSellingService extends BaseService {
	private static final String GETPREFERENCIAL = "packagePromotion";
	private QueryId getpreferencial = new QueryId();
	private static final String GETSPECIALSELLING = "restaurantPromotion";
	private QueryId getspecialselling = new QueryId();
	
	public void getPreferencial(final OnQueryCompleteListener queryCompleteListener,
			String latitude, String longtitude, String city){
		List<BasicNameValuePair> parms = new ArrayList<BasicNameValuePair>();
		parms.add(new BasicNameValuePair("latitude", latitude));
		parms.add(new BasicNameValuePair("longitude", longtitude));
		parms.add(new BasicNameValuePair("city", city));
		HttpUtils.makeAsyncPost(GETPREFERENCIAL, parms, 
				new QueryCompleteHandler(queryCompleteListener, getpreferencial) {
			
			@Override
			public void handleResponse(String jsonResult, EHttpError error) {
				if(jsonResult == null || jsonResult.equals("failed")){
					queryCompleteListener.onQueryComplete(getpreferencial, jsonResult, error);
				}else{
					Gson gson = new Gson();
					List<Package> packages = null;
					Type type = new TypeToken<ArrayList<Package>>() {
					}.getType();
					packages = gson.fromJson(jsonResult, type);
					System.out.println(jsonResult.toString());
					queryCompleteListener.onQueryComplete(getpreferencial, packages, error);
				}
			}
		});
	}
	public void getSpecialSelling(final OnQueryCompleteListener queryCompleteListener,
			String latitude, String longtitude, String city){
		List<BasicNameValuePair> parms = new ArrayList<BasicNameValuePair>();
		parms.add(new BasicNameValuePair("latitude", latitude));
		parms.add(new BasicNameValuePair("longitude", longtitude));
		parms.add(new BasicNameValuePair("city", city));
		HttpUtils.makeAsyncPost(GETSPECIALSELLING, parms, 
				new QueryCompleteHandler(queryCompleteListener, getspecialselling) {
			
			@Override
			public void handleResponse(String jsonResult, EHttpError error) {
				if(jsonResult == null || jsonResult.equals("failed")){
					queryCompleteListener.onQueryComplete(getpreferencial, jsonResult, error);
				}else{
					System.out.println(jsonResult.toString());
					Gson gson = new Gson();
					Resturant resturant = gson.fromJson(jsonResult, Resturant.class);
					queryCompleteListener.onQueryComplete(getpreferencial, resturant, error);
				}
			}
		});
	}
}
