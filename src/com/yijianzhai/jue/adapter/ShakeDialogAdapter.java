package com.yijianzhai.jue.adapter;

import javax.security.auth.PrivateCredentialPermission;

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
import com.yijianzhai.jue.activity.ShakeActivity;
import com.yijianzhai.jue.model.Package;
import com.yijianzhai.jue.utils.AnimateFirstListener;
import com.yijianzhai.jue.utils.GetImgeLoadOption;

public class ShakeDialogAdapter extends BaseAdapter {

	private com.yijianzhai.jue.model.Package dataPackage;
	private ShakeActivity shakeActivity;
	private LayoutInflater mInflater;
	private AnimateFirstListener listener;
	private DisplayImageOptions options;

	public ShakeDialogAdapter(Package dataPackage, ShakeActivity shakeActivity) {
		super();
		this.dataPackage = dataPackage;
		this.shakeActivity = shakeActivity;
		this.mInflater = LayoutInflater.from(shakeActivity);
		listener = new AnimateFirstListener();
		options = new GetImgeLoadOption().getOptions();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataPackage.getDishes().size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return dataPackage.getDishes().get(arg0);
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
			arg1 = mInflater.inflate(R.layout.ordersure_item, null);
			viewHolder = new ViewHolder();
			arg1.setTag(viewHolder);
			viewHolder.img = (ImageView) arg1.findViewById(R.id.img_ordersure);
			viewHolder.dishName = (TextView) arg1
					.findViewById(R.id.tx_ordersureitem_name);
			viewHolder.dishPrice = (TextView) arg1
					.findViewById(R.id.tx_ordersure_price);
			viewHolder.dishNumber = (TextView) arg1
					.findViewById(R.id.sure_item_count);

		} else {
			viewHolder = (ViewHolder) arg1.getTag();
		}
		ImageLoader.getInstance().displayImage(LoadingActivity.URL
					+ dataPackage.getDishes().get(arg0).getImg(), 
					viewHolder.img, options, listener);
		viewHolder.dishName
				.setText(dataPackage.getDishes().get(arg0).getName());
		viewHolder.dishPrice.setText("￥"
				+ dataPackage.getDishes().get(arg0).getPrice());
		viewHolder.dishNumber.setText(dataPackage.getDishes().get(arg0)
				.getNumber()
				+ "份");
		return arg1;
	}

	class ViewHolder {
		ImageView img;
		TextView dishName;
		TextView dishPrice;
		TextView dishNumber;
	}
}
