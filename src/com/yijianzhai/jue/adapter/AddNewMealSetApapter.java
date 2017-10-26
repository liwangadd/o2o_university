package com.yijianzhai.jue.adapter;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.yijianzhai.jue.R;
import com.yijianzhai.jue.activity.LoadingActivity;
import com.yijianzhai.jue.activity.QueryOrderActivity;
import com.yijianzhai.jue.model.Dish;

public class AddNewMealSetApapter extends BaseAdapter {

	private List<Dish> dishs;

	private Context context;
	private LayoutInflater inflater;
	
	private DisplayImageOptions options;
	private AnimateFirstDisplayListener listener;

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dishs.size();
	}

	public List<Dish> getDishs() {
		return dishs;
	}

	public AddNewMealSetApapter(List<Dish> dishs, Context context) {
		super();
		this.dishs = dishs;
		this.context = context;
		inflater = LayoutInflater.from(context);
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.danta)
		.showImageForEmptyUri(R.drawable.danta)
		.showImageOnFail(R.drawable.danta)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.displayer(new RoundedBitmapDisplayer(20))
		.build();
		listener = new AnimateFirstDisplayListener();
	}

	public void setDishs(List<Dish> dishs) {
		this.dishs = dishs;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return dishs.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if (arg1 == null) {
			arg1 = inflater.inflate(R.layout.orderdetailitem, null);
			viewHolder = new ViewHolder();
			arg1.setTag(viewHolder);
			viewHolder.img_orderdetail = (ImageView) arg1
					.findViewById(R.id.img_orderdetail);
			viewHolder.tx_orderdetailitem_name = (TextView) arg1
					.findViewById(R.id.tx_orderdetailitem_name);
			viewHolder.tx_orderdetail_price = (TextView) arg1
					.findViewById(R.id.tx_orderdetail_price);
			viewHolder.detail_add = (Button) arg1.findViewById(R.id.detail_add);
			viewHolder.detail_item_count = (Button) arg1
					.findViewById(R.id.detail_item_count);
			viewHolder.detail_decline = (Button) arg1
					.findViewById(R.id.detail_decline);
		} else {
			viewHolder = (ViewHolder) arg1.getTag();
		}

		ImageLoader.getInstance().displayImage(LoadingActivity.URL + dishs.get(arg0).getImg(), 
				viewHolder.img_orderdetail, options, listener);

		MyOnclickListener myOnclickListener = new MyOnclickListener(arg0);
		viewHolder.detail_add.setOnClickListener(myOnclickListener);
		viewHolder.detail_decline.setOnClickListener(myOnclickListener);
		try {
			viewHolder.tx_orderdetailitem_name.setText(dishs.get(arg0)
					.getName());
			System.out.println(dishs.get(arg0).getPrice());
			viewHolder.tx_orderdetail_price.setText("￥"
					+ dishs.get(arg0).getPrice());
			viewHolder.detail_item_count.setText("x"
					+ dishs.get(arg0).getNumber());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return arg1;
	}

	class ViewHolder {
		ImageView img_orderdetail;
		TextView tx_orderdetailitem_name;
		TextView tx_orderdetail_price;
		Button detail_add;
		Button detail_item_count;
		Button detail_decline;
	}

	class MyOnclickListener implements OnClickListener {

		// 所在位置
		int position;

		public MyOnclickListener(int position) {
			super();
			this.position = position;
		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {
			case R.id.detail_decline:
				// 自减
				if (dishs.get(position).getNumber() > 0) {
					dishs.get(position).setNumber(
							dishs.get(position).getNumber() - 1);
				}
				QueryOrderActivity queryOrderActivity = (QueryOrderActivity) context;
				queryOrderActivity.updatePrice(dishs.get(position).getPrice(),
						false);
				if (dishs.get(position).getNumber() == 0) {
					dishs.remove(position);
				}
				if (dishs.size() == 0) {
					queryOrderActivity.getDialog().dismiss();
				}
				notifyDataSetChanged();
				break;
			case R.id.detail_add:
				// 自增
				dishs.get(position).setNumber(
						dishs.get(position).getNumber() + 1);
				((QueryOrderActivity) context).updatePrice(dishs.get(position)
						.getPrice(), true);
				notifyDataSetChanged();
				break;
			default:
				break;
			}
		}
	}
	
	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener{
		static final List<String> displayImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if(loadedImage != null){
				ImageView imageView= (ImageView)view;
				boolean isFirst = !displayImages.contains(imageUri);
				if(isFirst){
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayImages.add(imageUri);
				}
			}
		}
		
	}
	
}
