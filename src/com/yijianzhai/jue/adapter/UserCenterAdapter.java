package com.yijianzhai.jue.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yijianzhai.jue.R;

public class UserCenterAdapter extends BaseAdapter{
	
	private String[] data;
	private LayoutInflater inflater;
	
	public UserCenterAdapter(String[] school, Context context){
		this.data = school;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		
		return data.length;
	}

	@Override
	public Object getItem(int arg0) {
		
		return data[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		View view = inflater.inflate(R.layout.usercenter_item, null);
		TextView textView = (TextView)view.findViewById(R.id.user_center_item);
		textView.setText(data[position]);
		return view;
	}
}
