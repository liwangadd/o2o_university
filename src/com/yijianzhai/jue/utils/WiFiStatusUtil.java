package com.yijianzhai.jue.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * 获得Wifi状态的类
 * 
 * @author qiuyang
 * 
 */
public class WiFiStatusUtil {
	/**
	 * 
	 * 获取当前的网络状态 -1：没有网络 1：WIFI网络2：wap网络3：net网络
	 * 
	 * @param context
	 * 
	 * @return
	 */

	private final static int CMNET = 3;
	private final static int CMWAP = 2;
	private final static int WIFI = 1;

	public static int getAPNType(Context context) {

		int netType = -1;

		ConnectivityManager connMgr = null;
		NetworkInfo networkInfo = null;
		try {
			connMgr = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			networkInfo = connMgr.getActiveNetworkInfo();
		} catch (Exception e) {
			// TODO: handle exception
		}

		if (networkInfo == null) {

			return netType;

		}

		int nType = networkInfo.getType();

		if (nType == ConnectivityManager.TYPE_MOBILE) {

			Log.e("networkInfo.getExtraInfo()",
					"networkInfo.getExtraInfo() is "
							+ networkInfo.getExtraInfo());

			if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {

				netType = CMNET;

			}

			else {

				netType = CMWAP;

			}

		}

		else if (nType == ConnectivityManager.TYPE_WIFI) {

			netType = WIFI;

		}

		return netType;

	}
}
