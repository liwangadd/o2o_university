package com.yijianzhai.jue.utils;

import android.app.Dialog;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.yijianzhai.jue.R;

public class GetImgeLoadOption {

	private static DisplayImageOptions options;
	
	public static DisplayImageOptions getOptions(){
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.danta)
		.showImageForEmptyUri(R.drawable.danta)
		.showImageOnFail(R.drawable.danta)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.displayer(new RoundedBitmapDisplayer(20))
		.build();
		return options;
	}
	
}
