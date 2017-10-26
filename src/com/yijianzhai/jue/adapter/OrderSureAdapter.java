package com.yijianzhai.jue.adapter;

import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yijianzhai.jue.R;
import com.yijianzhai.jue.activity.LoadingActivity;
import com.yijianzhai.jue.utils.AnimateFirstListener;
import com.yijianzhai.jue.utils.GetImgeLoadOption;

public class OrderSureAdapter extends BaseAdapter{
	
	private LayoutInflater inflater;
	private HashMap<String, Object> result;
	private int count;
	private AnimateFirstListener listener;
	private DisplayImageOptions options;
	
	public OrderSureAdapter(Context context, HashMap<String, Object> result, int count){
		this.result = result;
		inflater = LayoutInflater.from(context);
		this.count = count;
		listener = new AnimateFirstListener();
		options = GetImgeLoadOption.getOptions();
	}

	@Override
	public int getCount() {
		return 1;
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
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.ordersure_item, null);
			holder.sure_icon = (ImageView)convertView.findViewById(R.id.img_ordersure);
			holder.sure_name = (TextView)convertView.findViewById(R.id.tx_ordersureitem_name);
			holder.sure_price = (TextView)convertView.findViewById(R.id.tx_ordersure_price);
			holder.sure_count = (TextView)convertView.findViewById(R.id.sure_item_count);
			holder.sure_count.setText(count+"份");
			holder.sure_price.setText("￥"+result.get("price")+"");
			holder.sure_name.setText(result.get("name")+"");
			
			ImageLoader.getInstance().displayImage(LoadingActivity.URL+result.get("img"), 
					holder.sure_icon,options, listener);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		return convertView;
	}
	
	private class ViewHolder{
		public ImageView sure_icon;
		public TextView sure_name;
		public TextView sure_price;
		public TextView sure_count;
	}

}
