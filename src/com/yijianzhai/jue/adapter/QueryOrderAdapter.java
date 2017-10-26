package com.yijianzhai.jue.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.yijianzhai.jue.R;
import com.yijianzhai.jue.activity.QueryOrderActivity;
import com.yijianzhai.jue.model.Dish;
import com.yijianzhai.jue.model.Order;

public class QueryOrderAdapter extends BaseAdapter {

	private List<Order> data;
	private Context context;
	private LayoutInflater inflater;

	private final String WATING = "等待商家接单";
	private final String ACCEPT = "下单成功";
	private final String FAILED = "下单失败";

	// 记录选择的位置
	private int selectedIndex = -1;

	public QueryOrderAdapter(List<Order> data, Context context) {
		this.data = data;
		this.context = context;
		inflater = LayoutInflater.from(context);
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

	public List<Order> getData() {
		return data;
	}

	public void setData(List<Order> data) {
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.queryorder_item, null);
			holder.query_name = (TextView) convertView
					.findViewById(R.id.query_name);
			holder.query_price = (TextView) convertView
					.findViewById(R.id.query_price);
			holder.query_select = (TextView) convertView
					.findViewById(R.id.query_select);
			holder.query_state = (TextView) convertView
					.findViewById(R.id.query_state);
			holder.query_time = (TextView) convertView
					.findViewById(R.id.query_time);
			holder.query_cancle = (TextView) convertView
					.findViewById(R.id.query_cancel);
			holder.query_add_mealse = (Button) convertView
					.findViewById(R.id.query_add_meal_set);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		try {
			StringBuffer stringBuffer = new StringBuffer();
			for (int i = 0; i < data.get(position).getDishes().size(); i++) {
				stringBuffer.append(data.get(position).getDishes().get(i)
						.getName()
						+ " ");
			}
			holder.query_name.setText(stringBuffer.toString());
			holder.query_price.setText("￥"
					+ data.get(position).getTotal_money() + "");
			holder.query_time.setText(data.get(position).getOrder_time());
			if (data.get(position).getStatus().equals("0")) {
				holder.query_state.setTextColor(Color.parseColor("#d57e20"));
				holder.query_state.setText(WATING);
			} else if (data.get(position).getStatus().equals("1")) {
				holder.query_state.setTextColor(Color.parseColor("#4fc312"));
				holder.query_state.setText(ACCEPT);
			} else if (data.get(position).getStatus().equals("2")) {
				holder.query_state.setTextColor(Color.parseColor("#767974"));
				holder.query_state.setText(FAILED);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		// 加入下划线
		holder.query_add_mealse.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		MyClickListener myClickListener = new MyClickListener(position);
		holder.query_add_mealse.setOnClickListener(myClickListener);
		QueryOrderItemOnClicklistener listener = new QueryOrderItemOnClicklistener(
				position);
		holder.query_select.setOnClickListener(listener);
		holder.query_select.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		if (selectedIndex == position) {
			holder.query_select.setText("取消");
		} else {
			holder.query_select.setText("选择");
		}
		if (holder.query_state.getText().toString().equals("等待发货")) {
			holder.query_cancle.setVisibility(View.VISIBLE);
		}

		return convertView;
	}

	private class QueryOrderItemOnClicklistener implements OnClickListener {

		private int position;

		public QueryOrderItemOnClicklistener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.query_select:
				if (selectedIndex != position) {
					// 设为选择状态
					selectedIndex = position;
					QueryOrderActivity queryOrderActivity = (QueryOrderActivity) context;
					queryOrderActivity.changeMoney(1, data.get(position)
							.getTotal_money());
					queryOrderActivity.updateIndex(selectedIndex);
					notifyDataSetChanged();
				} else if (selectedIndex == position) {
						selectedIndex = -1;
						QueryOrderActivity queryOrderActivity = (QueryOrderActivity) context;
						queryOrderActivity.changeMoney(0, data.get(position)
								.getTotal_money());
						queryOrderActivity.updateIndex(selectedIndex);
						notifyDataSetChanged();
					}
				break;
			default:
				break;
			}
		}
	}

	class MyClickListener implements OnClickListener {

		private int position;

		public MyClickListener(int position) {
			super();
			this.position = position;
		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {
			case R.id.query_add_meal_set:

				QueryOrderActivity queryOrderActivity = (QueryOrderActivity) context;
				// 创建对话框
				queryOrderActivity.setSendPrice(data.get(position)
						.getPrice_on_delivery());
				List<Dish> dishs = new ArrayList<Dish>();
				for (int i = 0; i < data.get(position).getDishes().size(); i++) {
					Dish dish = new Dish();
					Dish source = data.get(position).getDishes().get(i);
					dish.setDetail(source.getDetail());
					dish.setDish_id(source.getDish_id());
					dish.setImg(source.getImg());
					dish.setName(source.getName());
					dish.setNumber(source.getNumber());
					dish.setPrice(source.getPrice());
					dish.setSale_price(source.getSale_price());
					dish.setSales(source.getSales());
					dish.setTag(source.getTag());
					dishs.add(dish);
				}
				queryOrderActivity.CreateDialog(data.get(position)
						.getRestaurantId(), data.get(position)
						.getPrice_on_delivery(), dishs);
				break;

			default:
				break;
			}
		}
	}

	private class ViewHolder {
		TextView query_name;
		TextView query_price;
		TextView query_state;
		TextView query_select;
		TextView query_time;
		TextView query_cancle;
		Button query_add_mealse;
	}

}
