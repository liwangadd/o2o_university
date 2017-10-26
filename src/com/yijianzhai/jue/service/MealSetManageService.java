package com.yijianzhai.jue.service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yijianzhai.jue.connection.HttpUtils;
import com.yijianzhai.jue.connection.HttpUtils.EHttpError;
import com.yijianzhai.jue.model.Dish;

/**
 * 套餐管理服务
 * 
 * @author qiuyang
 * 
 */
public class MealSetManageService extends BaseService {
	// 查询套餐
	private final String GETALLMEALSETACTION = "package/getAllPackage";
	// 更改套餐
	private final String CHANGEMEALSETACTION = "package/changePackage";
	// 删除套餐
	private final String DELMEALSETACTION = "package/deletePackage";
	// 添加新套餐
	private final String ADDMEALSETACTION = "package/addPackage";
	// 将菜品添加到指定套餐
	private final String DISHADDPACKAGE = "package/change";

	public static final QueryId GETALLMEALSET = new QueryId();
	public static final QueryId CHANGEMEALSET = new QueryId();
	public static final QueryId DELMEALSET = new QueryId();
	public static final QueryId ADDMEALSET = new QueryId();

	/**
	 * 查看所有的套餐
	 * 
	 * @param UUID
	 * @param onQueryCompleteListener
	 */
	public void GetAllMealSet(String UUID,
			OnQueryCompleteListener onQueryCompleteListener) {
		List<BasicNameValuePair> parms = new ArrayList<BasicNameValuePair>();
		BasicNameValuePair mapBasicNameValuePair = new BasicNameValuePair(
				"uuid", UUID);
		parms.add(mapBasicNameValuePair);
		HttpUtils
				.makeAsyncPost(GETALLMEALSETACTION, parms,
						new QueryCompleteHandler(onQueryCompleteListener,
								GETALLMEALSET) {

							@Override
							public void handleResponse(String jsonResult,
									HttpUtils.EHttpError error) {
								// System.out.println(jsonResult);
								List<com.yijianzhai.jue.model.Package> list = null;
								if (jsonResult != null
										&& error == EHttpError.KErrorNone) {
									if (!jsonResult.equals("failed")) {
										Gson gson = new Gson();
										Type type = new TypeToken<ArrayList<com.yijianzhai.jue.model.Package>>() {
										}.getType();
										list = gson.fromJson(jsonResult, type);
										this.completeListener.onQueryComplete(
												GETALLMEALSET, list, error);
										return;
									} else {
										this.completeListener.onQueryComplete(
												GETALLMEALSET, "failed", error);
										return;
									}
								}
								this.completeListener.onQueryComplete(
										GETALLMEALSET, null, error);
							}
						});
	}

	/**
	 * 更改套餐信息
	 * 
	 * @param UUID
	 * @param dataPackages
	 * @param onQueryCompleteListener
	 */
	public void ChangeMealSet(String UUID,
			List<com.yijianzhai.jue.model.Package> dataPackages,
			OnQueryCompleteListener onQueryCompleteListener) {
		List<BasicNameValuePair> parms = new ArrayList<BasicNameValuePair>();
		final String[] result = new String[1];
		BasicNameValuePair mapBasicNameValuePair = new BasicNameValuePair(
				"uuid", UUID);
		parms.add(mapBasicNameValuePair);
		Gson gson = new Gson();
		parms.add(new BasicNameValuePair("pacs", gson.toJson(dataPackages)));
		HttpUtils
				.makeAsyncPost(CHANGEMEALSETACTION, parms,
						new QueryCompleteHandler(onQueryCompleteListener,
								CHANGEMEALSET) {

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
										CHANGEMEALSET, result[0], error);
							}
						});
	}

	/**
	 * 删除套餐信息
	 * 
	 * @param UUID
	 * @param pacId
	 * @param onQueryCompleteListener
	 */
	public void DelMealSet(String UUID, String pacId,
			OnQueryCompleteListener onQueryCompleteListener) {
		List<BasicNameValuePair> parms = new ArrayList<BasicNameValuePair>();
		final String[] result = new String[1];
		BasicNameValuePair mapBasicNameValuePair = new BasicNameValuePair(
				"uuid", UUID);
		parms.add(mapBasicNameValuePair);
		parms.add(new BasicNameValuePair("pacId", pacId));
		HttpUtils.makeAsyncPost(DELMEALSETACTION, parms,
				new QueryCompleteHandler(onQueryCompleteListener, DELMEALSET) {

					@Override
					public void handleResponse(String jsonResult,
							HttpUtils.EHttpError error) {
//						System.out.println(jsonResult);
						if (jsonResult != null
								&& error == EHttpError.KErrorNone) {
							if (!jsonResult.equals("failed")) {
								result[0] = "success";
								this.completeListener.onQueryComplete(
										DELMEALSET, result[0], error);
								return;
							} else {
								this.completeListener.onQueryComplete(
										DELMEALSET, "failed", error);
								return;
							}
						}
						this.completeListener.onQueryComplete(DELMEALSET, null,
								error);
					}
				});
	}

	/**
	 * 添加新套餐
	 * 
	 * @param UUID
	 * @param data
	 * @param onQueryCompleteListener
	 */
	public void AddMealSet(String UUID, com.yijianzhai.jue.model.Package data,
			OnQueryCompleteListener onQueryCompleteListener) {
		List<BasicNameValuePair> parms = new ArrayList<BasicNameValuePair>();
		final String[] result = new String[1];
		BasicNameValuePair mapBasicNameValuePair = new BasicNameValuePair(
				"uuid", UUID);
		parms.add(mapBasicNameValuePair);
		parms.add(new BasicNameValuePair("pac", new Gson().toJson(data)));
		HttpUtils.makeAsyncPost(ADDMEALSETACTION, parms,
				new QueryCompleteHandler(onQueryCompleteListener, ADDMEALSET) {

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
						this.completeListener.onQueryComplete(ADDMEALSET,
								result[0], error);
					}
				});
	}

	/**
	 * 将菜品添加到指定套餐
	 */
	public void DishAddPackage(String UUID, Dish dish, String dataPackages,
			OnQueryCompleteListener onQueryCompleteListener) {
		List<BasicNameValuePair> parms = new ArrayList<BasicNameValuePair>();
		//final String[] result = new String[1];
		BasicNameValuePair mapBasicNameValuePair = new BasicNameValuePair(
				"uuid", UUID);
		parms.add(mapBasicNameValuePair);

		Gson gson = new Gson();
		parms.add(new BasicNameValuePair("dish", gson.toJson(dish)));
		parms.add(new BasicNameValuePair("pacId", dataPackages));
		HttpUtils
				.makeAsyncPost(DISHADDPACKAGE, parms, new QueryCompleteHandler(
						onQueryCompleteListener, CHANGEMEALSET) {

					@Override
					public void handleResponse(String jsonResult,
							HttpUtils.EHttpError error) {
						// System.out.println(jsonResult);
						if(jsonResult.equals("repeat")){
							this.completeListener.onQueryComplete(CHANGEMEALSET,
									jsonResult, error);
							return;
						}
						if (jsonResult != null
								&& error == EHttpError.KErrorNone) {
							this.completeListener.onQueryComplete(CHANGEMEALSET,
									jsonResult, error);
						}
						
					}
				});
	}
}
