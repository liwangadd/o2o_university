package com.yijianzhai.jue.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
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
import com.yijianzhai.jue.activity.OutrestaurantActivity;
import com.yijianzhai.jue.utils.AnimateFirstListener;
import com.yijianzhai.jue.utils.GetImgeLoadOption;

public class OutstoreAdapter extends BaseAdapter {

	private List<HashMap<String, Object>> list;

	private LayoutInflater inflate;
	// private ImageLoader mImageLoader;
	private int current = -1;
	private DecimalFormat format;

	private AnimateFirstListener listener;
	private DisplayImageOptions options;

	public OutstoreAdapter(List<HashMap<String, Object>> list, Context context,
			OutrestaurantActivity activity) {

		this.list = list;
		this.inflate = LayoutInflater.from(context);
		format = new DecimalFormat("#.0");
		//mImageLoader.getInstance(context);
		listener = new AnimateFirstListener();
		options = GetImgeLoadOption.getOptions();
	}

	/*
	 * 外卖餐厅item类
	 */
	private class Itemview {
		// 店铺图片
		private ImageView storeImageView;
		// 店铺名称
		private TextView storeTextView;
		// 店铺营业状态
		private TextView ifopenTextView;
		// 店铺距离
		private TextView distanceTextView;
		// 店铺配送费
		private TextView deliveryTextView;
		// 店铺月销量
		private TextView salesTextView;
		// 起送价
		private TextView min;

		public ImageView getImg() {
			return storeImageView;
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Itemview viewHolder;
		if (convertView == null) {
			convertView = inflate.inflate(R.layout.store_list_item, null);
			viewHolder = new Itemview();

			viewHolder.storeImageView = (ImageView) convertView
					.findViewById(R.id.img_store);
			viewHolder.storeTextView = (TextView) convertView
					.findViewById(R.id.txt_storename);
			viewHolder.ifopenTextView = (TextView) convertView
					.findViewById(R.id.txt_ifstoreopen);
			viewHolder.distanceTextView = (TextView) convertView
					.findViewById(R.id.txt_storedistance);
			viewHolder.deliveryTextView = (TextView) convertView
					.findViewById(R.id.txt_delivery);
			viewHolder.salesTextView = (TextView) convertView
					.findViewById(R.id.txt_storesales);
			viewHolder.min = (TextView) convertView.findViewById(R.id.txt_min);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (Itemview) convertView.getTag();
		}
		System.out.println(LoadingActivity.URL
				+ list.get(position).get("img").toString());
		ImageLoader.getInstance().displayImage(LoadingActivity.URL + list.get(position).
				get("img").toString(), viewHolder.storeImageView, options, listener);

		if (position == current) {
			convertView.setBackgroundColor(Color.parseColor("#f1f1f1"));
		} else {
			convertView.setBackgroundColor(Color.parseColor("#ffffff"));
		}

		viewHolder.salesTextView.setText(list.get(position).get("sales") + "");
		viewHolder.storeTextView.setText(list.get(position).get("name") + "");
		viewHolder.ifopenTextView.setText(list.get(position).get("time") + "");
		// viewHolder.distanceTextView.setText(format.format((Double)list.get(position).get("distance"))+"km");
		if ((Double) list.get(position).get("distance") > 1000) {
			viewHolder.distanceTextView.setText(format.format((Double) list
					.get(position).get("distance") / 1000) + "km");
		} else {
			viewHolder.distanceTextView.setText(format.format((Double) list
					.get(position).get("distance")) + "m");
		}
		viewHolder.deliveryTextView.setText(list.get(position).get(
				"delivery_mode")
				+ "");
		viewHolder.min.setText(list.get(position).get("delivery_fee")
				+ "");
		return convertView;
	}

	public void setDataList(ArrayList<HashMap<String, Object>> listData) {

		// TODO Auto-generated method stub
		this.list = listData;
		notifyDataSetChanged();
	}

	public void setCurrent(int current) {
		this.current = current;
	}

}