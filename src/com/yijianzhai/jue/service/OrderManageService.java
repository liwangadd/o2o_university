package com.yijianzhai.jue.service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yijianzhai.jue.connection.HttpUtils;
import com.yijianzhai.jue.connection.HttpUtils.EHttpError;
import com.yijianzhai.jue.model.Order;

/**
 * 订单管理服务
 * 
 * @author qiuyang
 * 
 */
public class OrderManageService {
	// 查看历史订单
	private final String GETORDERINFOACTION = "user/getOrderInfo";
	// 用户下单
	private final String ZHAIUSERMAKESUREACTION = "user/book";

	public static final QueryId GETORDERINFO = new QueryId();
	public static final QueryId ZHAIMAKESURE = new QueryId();

	/**
	 * 用户查询历史订单
	 * 
	 * @param UUID
	 * @param onQueryCompleteListener
	 */
	public void UserGetOrderInfo(String UUID, String page,
			OnQueryCompleteListener onQueryCompleteListener) {
		List<BasicNameValuePair> parms = new ArrayList<BasicNameValuePair>();
		BasicNameValuePair mapBasicNameValuePair = new BasicNameValuePair(
				"uuid", UUID);
		parms.add(mapBasicNameValuePair);
		parms.add(new BasicNameValuePair("page", page));
		HttpUtils
				.makeAsyncPost(GETORDERINFOACTION, parms,
						new QueryCompleteHandler(onQueryCompleteListener,
								GETORDERINFO) {

							@Override
							public void handleResponse(String jsonResult,
									HttpUtils.EHttpError error) {
								List<Order> list = null;
								if (jsonResult != null
										&& error == EHttpError.KErrorNone) {
									System.out.println(jsonResult);
									if (!jsonResult.equals("failed")) {
										Gson gson = new Gson();
										Type type = new TypeToken<ArrayList<Order>>() {
										}.getType();
										list = gson.fromJson(jsonResult, type);
										this.completeListener.onQueryComplete(
												GETORDERINFO, list, error);
										return;
									} else {
										this.completeListener.onQueryComplete(
												GETORDERINFO, "failed", error);
										return;
									}
								}
								this.completeListener.onQueryComplete(
										GETORDERINFO, null, error);
							}
						});
	}

	/**
	 * 用户一键宅下单
	 * 
	 * @param UUID
	 * @param order
	 * @param onQueryCompleteListener
	 */
	public void UserZhaiMakeSure(String UUID, Order order,
			OnQueryCompleteListener onQueryCompleteListener) {
		List<BasicNameValuePair> parms = new ArrayList<BasicNameValuePair>();
		final String[] result = new String[1];
		result[0] = "failed";
		BasicNameValuePair mapBasicNameValuePair = new BasicNameValuePair(
				"uuid", UUID);
		parms.add(mapBasicNameValuePair);
		parms.add(new BasicNameValuePair("order", new Gson().toJson(order)));
		HttpUtils
				.makeAsyncPost(ZHAIUSERMAKESUREACTION, parms,
						new QueryCompleteHandler(onQueryCompleteListener,
								ZHAIMAKESURE) {

							@Override
							public void handleResponse(String jsonResult,
									HttpUtils.EHttpError error) {
								if (jsonResult != null
										&& error == EHttpError.KErrorNone) {
									System.out.println(jsonResult);
									if (!jsonResult.equals("failed")) {
										result[0] = "success";
									}
								}
								this.completeListener.onQueryComplete(
										ZHAIMAKESURE, result[0], error);
							}
						});
	}
}
