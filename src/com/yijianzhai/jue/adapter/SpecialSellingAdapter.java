package com.yijianzhai.jue.adapter;

import java.util.List;

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
import com.yijianzhai.jue.model.Dish;
import com.yijianzhai.jue.utils.AnimateFirstListener;
import com.yijianzhai.jue.utils.GetImgeLoadOption;

public class SpecialSellingAdapter extends BaseAdapter {
	
	private LayoutInflater inflater;
	private List<Dish> data;
	private AnimateFirstListener listener;
	private DisplayImageOptions options;
	
	public SpecialSellingAdapter(Context context, List<Dish> data){
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
			convertView = inflater.inflate(R.layout.special_selling_item, null);
			holder.special_selling_img = (ImageView) convertView.findViewById(R.id.special_selling_img);
			holder.special_selling_name = (TextView) convertView.findViewById(R.id.special_selling_name);
			holder.special_selling_price = (TextView) convertView.findViewById(R.id.special_selling_price);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.special_selling_name.setText(data.get(position).getName());
		holder.special_selling_price.setText(data.get(position).getPrice()+"");
		ImageLoader.getInstance().displayImage(LoadingActivity.URL + data.get(position).getImg(),
				holder.special_selling_img, options, listener);
		return convertView;
	}
	
	class ViewHolder{
		ImageView special_selling_img;
		TextView special_selling_name;
		TextView special_selling_price;
	}

}
