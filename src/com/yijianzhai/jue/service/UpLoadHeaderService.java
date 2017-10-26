package com.yijianzhai.jue.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.yijianzhai.jue.connection.HttpUtils;
import com.yijianzhai.jue.connection.HttpUtils.EHttpError;

import android.graphics.Bitmap;
import android.util.Base64;

public class UpLoadHeaderService extends BaseService {
	private static final String UPLOADHEADER = "iconUpload";
	private QueryId uploadheader = new QueryId();
	
	public void UpLoadHeader(Bitmap bitmap, String uuid, final OnQueryCompleteListener 
			onQueryCompleteListener){
		List<BasicNameValuePair> parms = new ArrayList<BasicNameValuePair>();		  
        try {
        	ByteArrayOutputStream out = new ByteArrayOutputStream();  
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
			byte[] buffer = out.toByteArray();  
	        byte[] encode = Base64.encode(buffer, Base64.DEFAULT);
	        String photo = new String(encode);
	        parms.add(new BasicNameValuePair("file", photo));
	        parms.add(new BasicNameValuePair("name", uuid));
		} catch (IOException e) {
			e.printStackTrace();
		}      
        HttpUtils.makeAsyncPost(UPLOADHEADER, parms,
				new QueryCompleteHandler(onQueryCompleteListener, uploadheader) {

					@Override
					public void handleResponse(String jsonResult,
							HttpUtils.EHttpError error) {
						if (jsonResult != null
								&& error == EHttpError.KErrorNone) {
							if (!jsonResult.equals("failed")) {
								onQueryCompleteListener.onQueryComplete(uploadheader, jsonResult, error);
							}
						}
						
					}
				});
	}
}
