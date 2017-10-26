package com.yijianzhai.jue.service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yijianzhai.jue.connection.HttpUtils;
import com.yijianzhai.jue.connection.HttpUtils.EHttpError;


public class RestaurantManager extends BaseService{

	private final String GETRESTAURANT = "resturant/getAll";
	private final String GETDISH = "resturant/getResturantInfo";
	private final String GETOFFENRESTURANT = "resturant/getFavourite";
	
	public static QueryId addAddress = new QueryId();
		public void ResturantGet(String UUID, int mode, String city,
			String x, String y, int page, String type, int sort, int special,
			OnQueryCompleteListener onQueryCompleteListener) {
		List<BasicNameValuePair> parms = new ArrayList<BasicNameValuePair>();
		final String[] result = new String[1];
		result[0] = "failed";
		if(special == 1){
			special = 2;
		}else if(special == 2){
			special = 1;
		}
		
		Map<String, Object> location = new HashMap<String, Object>();
		location.put("x", x);
		location.put("y", y);
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("mode", mode);
		if(!city.endsWith("市")){
			city += "市";
		}
		message.put("city", city);
		message.put("location", location);
		message.put("page", page);
		Map<String, Object> reqs = new HashMap<String, Object>();
		if(sort != 0){
			message.put("sort", sort - 1);
		}
		if(!type.equals("全部分类")){
			message.put("type", type);
		}
		if(special != 0){
			message.put("special", special);
		}
		final Gson gson = new Gson();
		parms.add(new BasicNameValuePair("message", gson.toJson(message)));
		System.out.println(message.toString());
		HttpUtils.makeAsyncPost(GETRESTAURANT, parms,
				new QueryCompleteHandler(onQueryCompleteListener, addAddress) {

					@Override
					public void handleResponse(String jsonResult,
							HttpUtils.EHttpError error) {
						ArrayList<Map<String, Object>> resturants = new ArrayList<Map<String, Object>>();
						//System.out.println(jsonResult);
						if (jsonResult != null
								&& error == EHttpError.KErrorNone) {
							System.out.println("restaurant-------->"+jsonResult);
							if(jsonResult.equals("[]")){
								this.completeListener.onQueryComplete(addAddress,
										jsonResult, error);
								return;
							}
							if (!jsonResult.equals("failed")) {
								Type type = new TypeToken<ArrayList<Map<String, Object>>>() {

								}.getType();
								resturants = gson.fromJson(jsonResult, type);
								this.completeListener.onQueryComplete(addAddress,
										resturants, error);
							}
						}else{
							this.completeListener.onQueryComplete(addAddress, null, error);
							return;
						}
					}
				});
	}
	
	public void GetDishManager(String restaurantId, OnQueryCompleteListener onQueryCompleteListener){
		List<BasicNameValuePair> parms = new ArrayList<BasicNameValuePair>();
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("resturant_id", restaurantId);
		final Gson gson = new Gson();
		parms.add(new BasicNameValuePair("message", gson.toJson(message)));
		HttpUtils.makeAsyncPost(GETDISH, parms,
				new QueryCompleteHandler(onQueryCompleteListener, addAddress) {

					@Override
					public void handleResponse(String jsonResult,
							HttpUtils.EHttpError error) {
						Map<String, Object> dish = new HashMap<String, Object>();
						//System.out.println(jsonResult);
						if (jsonResult != null
								&& error == EHttpError.KErrorNone) {
							System.out.println("dish-------->"+jsonResult);
							if(jsonResult.equals("{}")){
								this.completeListener.onQueryComplete(addAddress,
										jsonResult, error);
								return;
							}
							if (!jsonResult.equals("failed")) {
								Type type = new TypeToken<Map<String, Object>>() {
								}.getType();
								dish = gson.fromJson(jsonResult, type);
								this.completeListener.onQueryComplete(addAddress,
										dish, error);
							}
						}
					}
				});
	}
	
	public void GetOffenResturant(String uuid, String city, String school, OnQueryCompleteListener onQueryCompleteListener){
		List<BasicNameValuePair> parms = new ArrayList<BasicNameValuePair>();
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("uuid", uuid);
		if(school == null){
			message.put("city", city);
			message.put("mode", 0);
		}else{
			message.put("school", school);
			message.put("mode", 1);
		}
		final Gson gson = new Gson();
		parms.add(new BasicNameValuePair("message", gson.toJson(message)));
		HttpUtils.makeAsyncPost(GETOFFENRESTURANT, parms,
				new QueryCompleteHandler(onQueryCompleteListener, addAddress) {

					@Override
					public void handleResponse(String jsonResult,
							HttpUtils.EHttpError error) {
						ArrayList<Map<String, Object>> offenResturant = new ArrayList<Map<String,Object>>();
						//System.out.println(jsonResult);
						if(jsonResult== null){
							this.completeListener.onQueryComplete(addAddress,
									null, error);
							return;
						}
						if (jsonResult != null
								&& error == EHttpError.KErrorNone) {
							//System.out.println("dish-------->"+jsonResult);
							if(jsonResult.equals("[]")){
								this.completeListener.onQueryComplete(addAddress,
										jsonResult, error);
							}
							if (!jsonResult.equals("failed")) {
								Type type = new TypeToken<ArrayList<Map<String, Object>>>() {
								}.getType();
								offenResturant = gson.fromJson(jsonResult, type);
								this.completeListener.onQueryComplete(addAddress,
										offenResturant, error);
							}else{
								this.completeListener.onQueryComplete(addAddress,
										jsonResult, error);
							}
						}
					}
				});
	}
	
}
	
