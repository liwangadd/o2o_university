package com.yijianzhai.jue.service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yijianzhai.jue.connection.HttpUtils;
import com.yijianzhai.jue.connection.HttpUtils.EHttpError;

/**
 * 有关于用户的服务
 * 
 * @author qiuyang
 * 
 */
public class UserManageService extends BaseService {

	// 匿名注册
	private final String REGISTERWITHOUTNAMEACTION = "user/registerAnon";
	// 匿名登录
	private final String LOGINWITHOUTNAMEACTION = "user/loginAnon";
	// 注册
	private final String REHISTERWITHNAMEACTION = "user/register";
	// 验证码
	private final String GETCAPTCHAACTION = "user/captcha";
	// 登录
	private final String LOGINWITHNAMEACTION = "user/login";
	private final String GETUSERINFOACTION = "user/getUserInfo";
	private final String CHANGEUSERINFOACTION = "user/saveUserInfo";

	private final String SUGGESTIONACTION = "feedback";

	public static final QueryId REGISTERWITHOUTNAME = new QueryId();
	public static final QueryId LOGINWITHOUTNAME = new QueryId();
	public static final QueryId REGISTERWITHNAME = new QueryId();
	public static final QueryId LOGINWITHNAME = new QueryId();
	public static final QueryId GETUSERINFO = new QueryId();
	public static final QueryId CHANGEUSERINFO = new QueryId();
	public static final QueryId GETCAPTCHA = new QueryId();
	private static final QueryId SUGGESTION = new QueryId();

	/**
	 * 匿名注册
	 */
	public void UserResgisterWithoutName(String UUID,
			OnQueryCompleteListener onQueryCompleteListener) {
		List<BasicNameValuePair> parms = new ArrayList<BasicNameValuePair>();
		final String[] result = new String[1];
		BasicNameValuePair mapBasicNameValuePair = new BasicNameValuePair(
				"uuid", UUID);
		parms.add(mapBasicNameValuePair);

		HttpUtils.makeAsyncPost(REGISTERWITHOUTNAMEACTION, parms,
				new QueryCompleteHandler(onQueryCompleteListener,
						REGISTERWITHOUTNAME) {

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
								REGISTERWITHOUTNAME, result[0], error);
					}
				});
	}

	/**
	 * 匿名登录
	 */
	public void UserLoginWithoutName(String UUID,
			OnQueryCompleteListener onQueryCompleteListener) {
		List<BasicNameValuePair> parms = new ArrayList<BasicNameValuePair>();
		final String[] result = new String[1];
		result[0] = "failed";
		BasicNameValuePair mapBasicNameValuePair = new BasicNameValuePair(
				"uuid", UUID);
		parms.add(mapBasicNameValuePair);

		HttpUtils.makeAsyncPost(LOGINWITHOUTNAMEACTION, parms,
				new QueryCompleteHandler(onQueryCompleteListener,
						LOGINWITHOUTNAME) {

					@Override
					public void handleResponse(String jsonResult,
							HttpUtils.EHttpError error) {
						// System.out.println(jsonResult);
						if (jsonResult != null
								&& error == EHttpError.KErrorNone) {
							if (!jsonResult.equals("failed")) {
								result[0] = "success";
							}
						}
						this.completeListener.onQueryComplete(LOGINWITHOUTNAME,
								result[0], error);
					}
				});
	}

	/**
	 * 注册
	 */
	public void UserRegister(String UUID, String mobile, String password,
			OnQueryCompleteListener onQueryCompleteListener) {
		List<BasicNameValuePair> parms = new ArrayList<BasicNameValuePair>();
		final String[] result = new String[1];
		BasicNameValuePair mapBasicNameValuePair = new BasicNameValuePair(
				"uuid", UUID);
		parms.add(mapBasicNameValuePair);
		parms.add(new BasicNameValuePair("mobile", mobile));
		parms.add(new BasicNameValuePair("password", password));
		HttpUtils.makeAsyncPost(REHISTERWITHNAMEACTION, parms,
				new QueryCompleteHandler(onQueryCompleteListener,
						REGISTERWITHNAME) {

					@Override
					public void handleResponse(String jsonResult,
							HttpUtils.EHttpError error) {
						if (jsonResult != null
								&& error == EHttpError.KErrorNone) {
							System.out.println(jsonResult);
							if (!jsonResult.toString().equals("failed")) {
								result[0] = "success";
								this.completeListener.onQueryComplete(
										REGISTERWITHNAME, result[0], error);
								return;
							} else {
								this.completeListener.onQueryComplete(
										REGISTERWITHNAME, "failed", error);
								return;
							}
						}
						this.completeListener.onQueryComplete(REGISTERWITHNAME,
								null, error);
					}
				});
	}

	/**
	 * 登录
	 */
	public void Userlogin(String mobile, String password,
			OnQueryCompleteListener onQueryCompleteListener) {
		List<BasicNameValuePair> parms = new ArrayList<BasicNameValuePair>();
		final String[] result = new String[1];
		result[0] = "failed";
		parms.add(new BasicNameValuePair("mobile", mobile));
		parms.add(new BasicNameValuePair("password", password));
		HttpUtils
				.makeAsyncPost(LOGINWITHNAMEACTION, parms,
						new QueryCompleteHandler(onQueryCompleteListener,
								LOGINWITHNAME) {

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
										LOGINWITHNAME, result[0], error);
							}
						});
	}

	/**
	 * 获得用户信息
	 * 
	 * @param mobile
	 * @param onQueryCompleteListener
	 */
	public void UserGetInfo(String UUID,
			OnQueryCompleteListener onQueryCompleteListener) {
		List<BasicNameValuePair> parms = new ArrayList<BasicNameValuePair>();
		parms.add(new BasicNameValuePair("uuid", UUID));
		HttpUtils.makeAsyncPost(GETUSERINFOACTION, parms,
				new QueryCompleteHandler(onQueryCompleteListener, GETUSERINFO) {

					@Override
					public void handleResponse(String jsonResult,
							HttpUtils.EHttpError error) {
						Map<String, Object> map = null;
						if (jsonResult != null
								&& error == EHttpError.KErrorNone) {
							System.out.println(jsonResult);
							if (!jsonResult.equals("failed")) {
								if (!jsonResult.equals("failed")) {
									Gson gson = new Gson();
									Type type = new TypeToken<Map<String, Object>>() {
									}.getType();
									map = gson.fromJson(jsonResult, type);
								}
							}
						}
						this.completeListener.onQueryComplete(GETUSERINFO, map,
								error);
					}
				});
	}

	public void UserChangeInfo(String userName, String mobile, String school,
			String major, OnQueryCompleteListener onQueryCompleteListener) {
		List<BasicNameValuePair> parms = new ArrayList<BasicNameValuePair>();
		final String[] result = new String[1];
		result[0] = "failed";
		parms.add(new BasicNameValuePair("uuid", mobile));
		parms.add(new BasicNameValuePair("userName", userName));
		parms.add(new BasicNameValuePair("school", school));
		parms.add(new BasicNameValuePair("major", major));
		HttpUtils.makeAsyncPost(CHANGEUSERINFOACTION, parms,
				new QueryCompleteHandler(onQueryCompleteListener,
						CHANGEUSERINFO) {

					@Override
					public void handleResponse(String jsonResult,
							HttpUtils.EHttpError error) {
						// System.out.println(jsonResult);
						if (jsonResult != null
								&& error == EHttpError.KErrorNone) {
							if (!jsonResult.equals("failed")) {
								result[0] = "success";
							}
						}
						this.completeListener.onQueryComplete(CHANGEUSERINFO,
								result[0], error);
					}
				});
	}

	/**
	 * 用户获得验证码
	 * 
	 * @param mobile
	 * @param onQueryCompleteListener
	 */
	public void UserGetCaptcha(String mobile,
			OnQueryCompleteListener onQueryCompleteListener) {
		List<BasicNameValuePair> parms = new ArrayList<BasicNameValuePair>();
		final String[] result = new String[1];
		parms.add(new BasicNameValuePair("mobile", mobile));
		HttpUtils.makeAsyncPost(GETCAPTCHAACTION, parms,
				new QueryCompleteHandler(onQueryCompleteListener, GETCAPTCHA) {

					@Override
					public void handleResponse(String jsonResult,
							HttpUtils.EHttpError error) {

						if (jsonResult != null
								&& error == EHttpError.KErrorNone) {
							System.out.println(jsonResult);
							if (!jsonResult.equals("failed")) {
								result[0] = jsonResult;
								this.completeListener.onQueryComplete(
										GETCAPTCHA, result[0], error);
								return;
							} else {
								this.completeListener.onQueryComplete(
										GETCAPTCHA, "failed", error);
								return;
							}
						}
						this.completeListener.onQueryComplete(GETCAPTCHA, null,
								error);
					}
				});
	}

	public void UserSuggestion(String message,
			OnQueryCompleteListener onQueryCompleteListener) {
		List<BasicNameValuePair> parms = new ArrayList<BasicNameValuePair>();
		parms.add(new BasicNameValuePair("message", message));
		final String[] result = new String[1];
		HttpUtils.makeAsyncPost(SUGGESTIONACTION, parms,
				new QueryCompleteHandler(onQueryCompleteListener, SUGGESTION) {

					@Override
					public void handleResponse(String jsonResult,
							HttpUtils.EHttpError error) {

						if (jsonResult != null
								&& error == EHttpError.KErrorNone) {
							System.out.println(jsonResult);
							if (!jsonResult.equals("failed")) {
								result[0] = jsonResult;
								this.completeListener.onQueryComplete(
										SUGGESTION, result[0], error);
								return;
							} else {
								this.completeListener.onQueryComplete(
										SUGGESTION, "failed", error);
								return;
							}
						}
						this.completeListener.onQueryComplete(SUGGESTION, null,
								error);
					}
				});
	}
}
