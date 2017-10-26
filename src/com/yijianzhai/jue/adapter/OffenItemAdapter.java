package com.yijianzhai.jue.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.yijianzhai.jue.R;
import com.yijianzhai.jue.activity.LoadingActivity;
import com.yijianzhai.jue.utils.GetImgeLoadOption;

public class OffenItemAdapter extends BaseAdapter {
	
	private LayoutInflater inflater;
	private boolean flag = false;
	private ArrayList<Map<String, Object>> data;
	private AnimateFirstListener listener;
	private DisplayImageOptions options;
	
	
	public OffenItemAdapter(Context context, ArrayList<Map<String, Object>> data){
		inflater = LayoutInflater.from(context);
		this.data = data;
		listener = new AnimateFirstListener();
		options = GetImgeLoadOption.getOptions();
	}

	@Override
	public int getCount() {
		
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder holder;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.offen_item, null);
			holder = new ViewHolder();
			holder.offen_icon = (ImageView)convertView.findViewById(R.id.offen_item_icon);
			holder.offen_name = (TextView)convertView.findViewById(R.id.offen_item_name);
			holder.offen_price = (TextView)convertView.findViewById(R.id.offen_item_price);
			holder.offen_count = (TextView)convertView.findViewById(R.id.offen_item_count);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.offen_name.setText(data.get(position).get("name")+"");
		holder.offen_price.setText(data.get(position).get("delivery_starting_fee")+"");
		holder.offen_count.setText(data.get(position).get("sales")+"");
		
		ImageLoader.getInstance().displayImage(LoadingActivity.URL + data.get(position).get("img"),
				holder.offen_icon, options, listener);
		
		return convertView;
	}

	public class OffenOnlicklistener implements OnClickListener{
		
		private int position;
		
		public OffenOnlicklistener(int position){
			this.position = position;
		}

		@Override
		public void onClick(View arg0) {
			data.remove(position);
			notifyDataSetChanged();
		}
		
	}

	public void setEdit() {
		if(flag){
			flag = false;
		}else{
			flag = true;
		}
		notifyDataSetChanged();
	}
	
	private class ViewHolder{
		public ImageView offen_icon;
		public TextView offen_name;
		public TextView offen_price;
		public TextView offen_count;
		public ImageView offen_delete;
		
	}
	
	private static class AnimateFirstListener extends SimpleImageLoadingListener{
		static final List<String> displayImages = 
				Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
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

}
