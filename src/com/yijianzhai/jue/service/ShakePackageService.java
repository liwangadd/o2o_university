package com.yijianzhai.jue.service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yijianzhai.jue.connection.HttpUtils;
import com.yijianzhai.jue.connection.HttpUtils.EHttpError;

/**
 * 摇一摇接口
 * 
 * @author qiuyang
 * 
 */
public class ShakePackageService extends BaseService {
	private final String SHAKEPACKAGEACTION = "shake";

	public static final QueryId shake = new QueryId();

	public void ShakePackage(String latitude, String longitude, String city, OnQueryCompleteListener onQueryCompleteListener) {
		List<BasicNameValuePair> parms = new ArrayList<BasicNameValuePair>();
		parms.add(new BasicNameValuePair("latitude", latitude));
		parms.add(new BasicNameValuePair("longitude", longitude));
		parms.add(new BasicNameValuePair("city", city));
		final String[] result = new String[1];
		HttpUtils.makeAsyncPost(SHAKEPACKAGEACTION, parms,
				new QueryCompleteHandler(onQueryCompleteListener, shake) {
					com.yijianzhai.jue.model.Package data;
					
					@Override
					public void handleResponse(String jsonResult,
							HttpUtils.EHttpError error) {
						if (jsonResult != null
								&& error == EHttpError.KErrorNone) {
							System.out.println(jsonResult);
							if (!jsonResult.equals("failed")) {
								result[0] = jsonResult.toString();
								Gson gson = new Gson();
								Type type = new TypeToken<com.yijianzhai.jue.model.Package>() {
								}.getType();
								 data = gson.fromJson(jsonResult, type);
								this.completeListener.onQueryComplete(shake,
										data, error);
									return;
							}else {
								this.completeListener.onQueryComplete(shake, "failed",
										error);
								return;
							}
						}
						this.completeListener.onQueryComplete(shake, null,
								error);
					}
				});
	}
}
