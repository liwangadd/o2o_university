package com.yijianzhai.jue.service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yijianzhai.jue.connection.HttpUtils;
import com.yijianzhai.jue.connection.HttpUtils.EHttpError;
import com.yijianzhai.jue.model.Address;

/**
 * 地址管理的服务
 * 
 * @author qiuyang
 * 
 */
public class AddressManageService extends BaseService {
	// 收货地址的增删改查
	private final String ADDADDRESSACTION = "user/addAddress";
	private final String DELADDRESSACTION = "user/deleteAddress";
	private final String CHANGEADDRESSACTION = "user/changeAddress";
	private final String GETADDRESSACTION = "user/getAddress";
	private final String CHANGEDEAFULTADDRESSACTION = "user/changeDefaultAddress";
	public static QueryId addAddress = new QueryId();
	public static QueryId delAddress = new QueryId();
	public static QueryId changeAddress = new QueryId();
	public static QueryId getAddress = new QueryId();
	public static QueryId changeDefaultAddress = new QueryId();

	/**
	 * 增加收货地址
	 * 
	 * @param UUID
	 * @param name
	 * @param mobile
	 * @param block
	 * @param school
	 * @param address
	 * @param tag
	 * @param onQueryCompleteListener
	 */
	public void UserAddAddress(String UUID, String name, String mobile,
			String city, String block, String school, String address,
			String tag, int catagory,
			OnQueryCompleteListener onQueryCompleteListener) {
		List<BasicNameValuePair> parms = new ArrayList<BasicNameValuePair>();
		final String[] result = new String[1];
		result[0] = "failed";
		BasicNameValuePair mapBasicNameValuePair = new BasicNameValuePair(
				"uuid", UUID);
		parms.add(mapBasicNameValuePair);
		Address userAddress = new Address();
		userAddress.setName(name);
		userAddress.setLabel(tag);
		userAddress.setMobile(mobile);
		userAddress.setDistinct(block);
		userAddress.setSchool(school);
		userAddress.setAddress(address);
		userAddress.setCity(city);
		userAddress.setCategory(catagory);
		Gson gson = new Gson();
		parms.add(new BasicNameValuePair("address", gson.toJson(userAddress)));

		HttpUtils.makeAsyncPost(ADDADDRESSACTION, parms,
				new QueryCompleteHandler(onQueryCompleteListener, addAddress) {

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
						this.completeListener.onQueryComplete(addAddress,
								result[0], error);
					}
				});
	}

	/**
	 * 删除收货地址
	 * 
	 * @param UUID
	 * @param id
	 * @param onQueryCompleteListener
	 */
	public void UserDelAddress(String UUID, String id,
			OnQueryCompleteListener onQueryCompleteListener) {
		List<BasicNameValuePair> parms = new ArrayList<BasicNameValuePair>();
		final String[] result = new String[1];
		result[0] = "failed";
		BasicNameValuePair mapBasicNameValuePair = new BasicNameValuePair(
				"uuid", UUID);
		parms.add(mapBasicNameValuePair);
		parms.add(new BasicNameValuePair("addressId", id));
		HttpUtils.makeAsyncPost(DELADDRESSACTION, parms,
				new QueryCompleteHandler(onQueryCompleteListener, delAddress) {

					@Override
					public void handleResponse(String jsonResult,
							HttpUtils.EHttpError error) {
//						System.out.println(jsonResult);
						if (jsonResult != null
								&& error == EHttpError.KErrorNone) {
							if (!jsonResult.equals("failed")) {
								result[0] = "success";
							}
						}
						this.completeListener.onQueryComplete(delAddress,
								result[0], error);
					}
				});
	}

	/**
	 * 改变地址
	 * 
	 * @param id
	 * @param UUID
	 * @param name
	 * @param mobile
	 * @param block
	 * @param school
	 * @param address
	 * @param tag
	 * @param onQueryCompleteListener
	 */
	public void UserChangeAddress(String id, String UUID, String name,
			String mobile, String city, String block, String school,
			String address, String tag, int category,
			OnQueryCompleteListener onQueryCompleteListener) {
		List<BasicNameValuePair> parms = new ArrayList<BasicNameValuePair>();
		final String[] result = new String[1];
		result[0] = "failed";
		BasicNameValuePair mapBasicNameValuePair = new BasicNameValuePair(
				"uuid", UUID);
		parms.add(mapBasicNameValuePair);
		Address changeAddressq = new Address();
		changeAddressq.setName(name);
		changeAddressq.setLabel(tag);
		changeAddressq.setMobile(mobile);
		changeAddressq.setDistinct(block);
		changeAddressq.setSchool(school);
		changeAddressq.setAddress(address);
		changeAddressq.setCity(city);
		changeAddressq.setAddress_id(id);
		changeAddressq.setCategory(category);
		Gson gson = new Gson();
		parms.add(new BasicNameValuePair("address", gson.toJson(changeAddressq)));
		HttpUtils
				.makeAsyncPost(CHANGEADDRESSACTION, parms,
						new QueryCompleteHandler(onQueryCompleteListener,
								changeAddress) {

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
										changeAddress, result[0], error);
							}
						});
	}

	public void UserGetAddress(String UUID,
			OnQueryCompleteListener onQueryCompleteListener) {
		List<BasicNameValuePair> parms = new ArrayList<BasicNameValuePair>();
		BasicNameValuePair mapBasicNameValuePair = new BasicNameValuePair(
				"uuid", UUID);
		parms.add(mapBasicNameValuePair);
		HttpUtils.makeAsyncPost(GETADDRESSACTION, parms,
				new QueryCompleteHandler(onQueryCompleteListener, getAddress) {

					@Override
					public void handleResponse(String jsonResult,
							HttpUtils.EHttpError error) {
						ArrayList<Address> list = null;
						if (jsonResult != null
								&& error == EHttpError.KErrorNone) {
							if (!jsonResult.equals("failed")) {
								Gson gson = new Gson();
								Type type = new TypeToken<ArrayList<Address>>() {
								}.getType();
								list = gson.fromJson(jsonResult, type);
								// System.out.println("listsieze--------->"
								// + list.size());
								this.completeListener.onQueryComplete(
										getAddress, list, error);
							} else {
								this.completeListener.onQueryComplete(
										getAddress, "failed", error);
							}
						}
					}
				});
	}

	/**
	 * 用户修改默认地址
	 * 
	 * @param oldAddressId
	 * @param newAddressId
	 * @param UUID
	 * @param onQueryCompleteListener
	 */
	public void UserChangeDeaultAddress(String oldAddressId,
			String newAddressId, String UUID,
			OnQueryCompleteListener onQueryCompleteListener) {
		List<BasicNameValuePair> parms = new ArrayList<BasicNameValuePair>();
		final String[] result = new String[1];
		result[0] = "failed";
		BasicNameValuePair mapBasicNameValuePair = new BasicNameValuePair(
				"uuid", UUID);
		parms.add(mapBasicNameValuePair);
		parms.add(new BasicNameValuePair("exId", oldAddressId));
		parms.add(new BasicNameValuePair("newId", newAddressId));
		HttpUtils.makeAsyncPost(CHANGEDEAFULTADDRESSACTION, parms,
				new QueryCompleteHandler(onQueryCompleteListener,
						changeDefaultAddress) {

					@Override
					public void handleResponse(String jsonResult,
							HttpUtils.EHttpError error) {
						if (jsonResult != null
								&& error == EHttpError.KErrorNone) {
							if (!jsonResult.equals("failed")) {
								result[0] = "success";
							}
						}
						this.completeListener.onQueryComplete(
								changeDefaultAddress, result[0], error);
					}
				});
	}
}
