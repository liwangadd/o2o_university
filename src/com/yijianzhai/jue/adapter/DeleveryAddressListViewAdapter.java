package com.yijianzhai.jue.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yijianzhai.jue.R;
import com.yijianzhai.jue.activity.DeleveryAddressActivity;
import com.yijianzhai.jue.activity.HomePageActivity;
import com.yijianzhai.jue.activity.NewActivity;
import com.yijianzhai.jue.model.Address;
import com.yijianzhai.jue.model.AddressForAndroid;
import com.yijianzhai.jue.service.AddressManageService;

public class DeleveryAddressListViewAdapter extends BaseAdapter {

	// 上下文变量
	private Context context;

	private List<Address> dataList;
	private boolean flag = false;

	// 后台通信接口
	AddressManageService service;

	public DeleveryAddressListViewAdapter(final Context context,
			List<Address> dataList) {
		this.context = context;
		this.dataList = dataList;
		service = new AddressManageService();

	}

	// 控制进入getiew的次数
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataList.size();
	}

	public List<Address> getDataList() {
		return dataList;
	}

	public void setDataList(List<Address> dataList) {
		this.dataList = dataList;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return dataList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.delevery_address_item, null);
			holder = new ViewHolder();

			holder.username = (TextView) convertView
					.findViewById(R.id.delevery_address_username);
			holder.phonenumber = (TextView) convertView
					.findViewById(R.id.delevery_address_phonenumber);
			holder.city = (TextView) convertView
					.findViewById(R.id.delevery_address_city);
			holder.zone = (TextView) convertView
					.findViewById(R.id.delevery_address_zone);
			holder.school = (TextView) convertView
					.findViewById(R.id.delevery_school);
			holder.detail_address = (TextView) convertView
					.findViewById(R.id.delevery_address_detail_address);
			holder.adress_change = (TextView) convertView
					.findViewById(R.id.adress_change);
			holder.adress_delete = (TextView) convertView
					.findViewById(R.id.adress_delete);
			holder.layout = (RelativeLayout) convertView
					.findViewById(R.id.adress_layout);
			holder.select = (Button) convertView
					.findViewById(R.id.delevery_address_select);
			holder.selected_address = (TextView) convertView
					.findViewById(R.id.delevery_address_select_address);
			if (dataList.get(position).getCategory() == 1) {
				holder.select.setVisibility(View.GONE);
				holder.selected_address.setVisibility(View.VISIBLE);
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (flag) {
			holder.adress_change.setVisibility(View.VISIBLE);
			holder.adress_delete.setVisibility(View.VISIBLE);
		} else {
			holder.adress_change.setVisibility(View.GONE);
			holder.adress_delete.setVisibility(View.GONE);
		}

		AdressOnclickListener listener = new AdressOnclickListener(position,
				holder);
		holder.adress_change.setOnClickListener(listener);
		holder.adress_delete.setOnClickListener(listener);
		holder.select.setOnClickListener(listener);
		holder.layout.setOnClickListener(listener);

		holder.username.setText(dataList.get(position).getName());
		holder.phonenumber.setText(dataList.get(position).getMobile());
		holder.city.setText(dataList.get(position).getCity());
		holder.zone.setText(dataList.get(position).getDistinct());
		holder.school.setText(dataList.get(position).getSchool());
		holder.detail_address.setText(dataList.get(position).getAddress());

		return convertView;
	}

	public class AdressOnclickListener implements OnClickListener {

		private int position;
		private ViewHolder holder;

		public AdressOnclickListener(int position, ViewHolder holder) {
			this.position = position;
			this.holder = holder;
		}

		@Override
		public void onClick(View arg0) {
			if (arg0.getId() == R.id.delevery_address_select) {
				String oldAddressId = null;
				for (int i = 0; i < dataList.size(); i++) {
					if (dataList.get(i).getCategory() == 1) {
						oldAddressId = dataList.get(i).getAddress_id();
						break;
					}
				}
				DeleveryAddressActivity deleveryAddressActivity=(DeleveryAddressActivity)context;
				deleveryAddressActivity.getProgressBar().setVisibility(View.VISIBLE);
				new AddressManageService().UserChangeDeaultAddress(
						oldAddressId, dataList.get(position).getAddress_id(),
						HomePageActivity.UUID,
						(DeleveryAddressActivity) context);
			}
			if (arg0.getId() == R.id.adress_delete) {
				service.UserDelAddress(HomePageActivity.UUID,
						dataList.get(position).getAddress_id(),
						(DeleveryAddressActivity) context);
				DeleveryAddressActivity deleveryAddressActivity = (DeleveryAddressActivity) context;
				// 设置contex的删除位置
				deleveryAddressActivity.setDelPosition(position);
			} else if (arg0.getId() == R.id.adress_change) {
				Intent changeIntent = new Intent(context, NewActivity.class);
				AddressForAndroid.name = holder.username.getText().toString();
				AddressForAndroid.phone = holder.phonenumber.getText()
						.toString();
				AddressForAndroid.isChange = true;
				AddressForAndroid.city = holder.city.getText().toString();
				AddressForAndroid.distinct = holder.zone.getText().toString();
				AddressForAndroid.address = holder.detail_address.getText()
						.toString();
				AddressForAndroid.school = holder.school.getText().toString();
				AddressForAndroid.adress_id = dataList.get(position)
						.getAddress_id();
				AddressForAndroid.category = dataList.get(position)
						.getCategory();
				context.startActivity(changeIntent);
				return;
			} else {

				String oldAddressId = null;
				String newAddressId = dataList.get(position).getAddress_id();

				/*new AddressManageService().UserChangeDeaultAddress(
						oldAddressId, newAddressId, HomePageActivity.UUID,
						(DeleveryAddressActivity) context);*/
				Intent intent = new Intent();
				intent.setAction("com.yijianzhai.dizhi");
				intent.putExtra("userName", dataList.get(position).getName());
				intent.putExtra("userPhone", dataList.get(position).getMobile());
				context.sendBroadcast(intent);
			}
			notifyDataSetChanged();
		}
	}

	private class ViewHolder {
		// 用户名
		public TextView username;
		// 电话号码
		public TextView phonenumber;
		// 城市
		public TextView city;
		// 街区
		public TextView zone;
		// 学校
		public TextView school;
		// 详细地址
		public TextView detail_address;

		public TextView adress_change;

		public TextView adress_delete;

		public RelativeLayout layout;
		// 设为默认地址
		Button select;

		TextView selected_address;

	}

	public void setFlag() {
		if (flag) {
			flag = false;
		} else {
			flag = true;
		}
		DeleveryAddressActivity deleveryAddressActivity = (DeleveryAddressActivity) context;
		deleveryAddressActivity.UpdateEdit(flag);
		notifyDataSetChanged();
	}
}
