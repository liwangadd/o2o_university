package com.yijianzhai.jue.utils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class AnimateFirstListener extends SimpleImageLoadingListener{
	private List<String> displayImages = Collections
			.synchronizedList(new LinkedList<String>());

	@Override
	public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
		if(loadedImage != null){
			ImageView imageView = (ImageView)view;
			boolean isFirst = !displayImages.contains(imageUri);
			if(isFirst){
				FadeInBitmapDisplayer.animate(imageView, 500);
				displayImages.add(imageUri);
			}
		}
	}
	
}
