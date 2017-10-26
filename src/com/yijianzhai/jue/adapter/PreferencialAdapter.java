package com.yijianzhai.jue.adapter;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yijianzhai.jue.R;
import com.yijianzhai.jue.model.Dish;
import com.yijianzhai.jue.model.Package;

public class PreferencialAdapter extends BaseAdapter {
	
	private LayoutInflater inflater;
	private List<Package> data;
	private List<Dish> dishs;
	
	public PreferencialAdapter(Context context, ArrayList<Package> data){
		inflater = LayoutInflater.from(context);
		this.data = data;
	}

	@Override
	public int getCount() {

		return data.size();
	}

	@Override
	public Object getItem(int arg0) {

		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {

		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.prefecencial_item, null);
			holder.preferencial_go_buy = (TextView) convertView.findViewById(R.id.prefecencial_go_buy);
			holder.preferencial_name = (TextView) convertView.findViewById(R.id.preference_item_name);
			holder.preferencial_price = (TextView) convertView.findViewById(R.id.preferencial_price);
			holder.preferencial_real_price = (TextView) convertView.findViewById(R.id.preferencial_real_price);
			holder.preferencial_what = (TextView) convertView.findViewById(R.id.preferencial_what);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		dishs = data.get(position).getDishes();
		double price = 0 , real_price = 0;
		StringBuffer sBuffer = new StringBuffer();
		int count = dishs.size();
		for(int i = 0; i < count; ++i){
			/*price += dishs.get(i).getPrice();
			real_price += dishs.get(i).getSale_price();*/
			sBuffer.append(dishs.get(i).getName());
		}
		holder.preferencial_name.setText(data.get(position).getName());
		holder.preferencial_price.setText(data.get(position).getSales_price()+"");
		holder.preferencial_real_price.setText(data.get(position).getTotal_price()+"");
		holder.preferencial_what.setText(sBuffer.toString());
		
		return convertView;
	}
	class ViewHolder{
		TextView preferencial_name;
		TextView preferencial_go_buy;
		TextView preferencial_what;
		TextView preferencial_real_price;
		TextView preferencial_price;
	}
}
