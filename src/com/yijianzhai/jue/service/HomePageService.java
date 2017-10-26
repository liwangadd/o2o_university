package com.yijianzhai.jue.service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yijianzhai.jue.connection.HttpUtils;
import com.yijianzhai.jue.connection.HttpUtils.EHttpError;
import com.yijianzhai.jue.model.Address;
import com.yijianzhai.jue.model.DataList;

public class HomePageService extends BaseService {

	private final String HOMEPAGEINFOACTION = "getHomeInfo";

	public static final QueryId home_page_img = new QueryId();

	public void GetHomeInfo(OnQueryCompleteListener onQueryCompleteListener) {
		List<BasicNameValuePair> parms = new ArrayList<BasicNameValuePair>();

		HttpUtils
				.makeAsyncPost(HOMEPAGEINFOACTION, parms,
						new QueryCompleteHandler(onQueryCompleteListener,
								home_page_img) {

							@Override
							public void handleResponse(String jsonResult,
									HttpUtils.EHttpError error) {
								List<String> info = null;
								if (jsonResult != null
										&& error == EHttpError.KErrorNone) {
//									System.out.println(jsonResult);
									if (!jsonResult.equals("failed")) {
										Gson gson = new Gson();
										Type type = new TypeToken<List<String>>() {
										}.getType();
										info = gson.fromJson(jsonResult, type);
										this.completeListener.onQueryComplete(
												home_page_img, info, error);
										return;
									} else {
										this.completeListener.onQueryComplete(
												home_page_img, "failed", error);
										return;
									}
								}
								this.completeListener.onQueryComplete(
										home_page_img, null, error);
							}
						});
	}
}
