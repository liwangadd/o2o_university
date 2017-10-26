package com.yijianzhai.jue.adapter;

import java.nio.channels.SelectableChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yijianzhai.jue.R;
import com.yijianzhai.jue.activity.LoadingActivity;
import com.yijianzhai.jue.activity.RestaurantActivity;
import com.yijianzhai.jue.model.DataList;
import com.yijianzhai.jue.utils.AnimateFirstListener;
import com.yijianzhai.jue.utils.GetImgeLoadOption;

public class RestDishAdapter extends BaseAdapter{
	private LayoutInflater inflate;
	private ArrayList<HashMap<String, Object>> data;
	private DataList result;


	private RestaurantActivity context;
	private HashMap<Integer, Integer> meal_sum = new HashMap<Integer, Integer>();
	String last;
	Dialog dd;
	int num = 0;
	private String resturantId;
	private AnimateFirstListener loadlistener;
	private DisplayImageOptions options;

	public RestDishAdapter(RestaurantActivity context, ArrayList<HashMap<String, Object>> list,
			DataList selected, HashMap<Integer, Integer> meal_sum) {
		this.data = list;
		this.inflate = LayoutInflater.from(context);
		this.meal_sum = meal_sum;
		this.context = context;
		this.result = selected;
		this.resturantId = resturantId;
		loadlistener = new AnimateFirstListener();
		options = GetImgeLoadOption.getOptions();
	}

	public RestDishAdapter(RestaurantActivity context) {

		this.inflate = LayoutInflater.from(context);
		this.context = context;
	}

	public class ViewHolder {
		public ImageView meal_icon;
		public TextView meal_name;
		public TextView meal_count;
		public TextView meal_price;
		public ImageView add;
		public ImageView decline;
		public TextView order_count;
		public TextView meal_order_count;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflate.inflate(R.layout.meal_list_item, null);
			holder.add = (ImageView)convertView.findViewById(R.id.img_resdish_choice);
			holder.decline = (ImageView)convertView.findViewById(R.id.img_resdish_sub);
			holder.meal_count = (TextView)convertView.findViewById(R.id.txt_meal_num);
			holder.meal_icon = (ImageView)convertView.findViewById(R.id.img_meal);
			holder.meal_price = (TextView)convertView.findViewById(R.id.txt_mealprice);
			holder.meal_name = (TextView)convertView.findViewById(R.id.txt_mealname);
			holder.meal_order_count = (TextView)convertView.findViewById(R.id.tx_resdish_i_num);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.meal_name.setText(data.get(position).get("name")+"");
		holder.meal_price.setText("￥"+data.get(position).get("price")+"");
		holder.meal_count.setText("月销量 ："+data.get(position).get("sales")+"");
		MyOnclickListener listener = new MyOnclickListener(position, holder);
		holder.add.setOnClickListener(listener);
		try {
			holder.meal_icon.setImageResource(R.drawable.danta);
			ImageLoader.getInstance().displayImage(LoadingActivity.URL+data.get(position)
					.get("img"), holder.meal_icon, options, loadlistener);
		} catch (Exception e) {

		}
		
		holder.decline.setOnClickListener(listener);
		System.out.println(meal_sum.get(position));
		if(meal_sum.get(position) > 0){
			holder.decline.setVisibility(View.VISIBLE);
			holder.meal_order_count.setText(meal_sum.get(position)+"");
			holder.meal_order_count.setVisibility(View.VISIBLE);
		}else{
			holder.decline.setVisibility(View.GONE);
			holder.meal_order_count.setVisibility(View.GONE);
			holder.meal_order_count.setVisibility(View.GONE);
		}
		


		return convertView;
	}

	class MyOnclickListener implements OnClickListener{
		
		private int position;
		private int count =0;
		private ViewHolder holder;
		
		public MyOnclickListener(int position, ViewHolder holder){
			System.out.println("into");
			if(!meal_sum.containsKey(position)){
				meal_sum.put(position, 0);
			}
			this.position = position;
			this.holder = holder;
			if(meal_sum.containsKey(position)){
				count = meal_sum.get(position);
			}
		}

		@Override
		public void onClick(View arg0) {
			if(arg0.getId() == R.id.img_resdish_choice){
				++count;
				meal_sum.put(position, count);
				holder.meal_order_count.setText(count+"");
				if(count == 1){
					holder.meal_order_count.setVisibility(View.VISIBLE);
					holder.decline.setVisibility(View.VISIBLE);
				}
				num += 1;
				context.changemoney(1, (Double) data.get(position).get("price"));
			}else{
				if(count == 0){
					return;
				}
				--count;
				meal_sum.put(position, count);
				if(count == 0){
					holder.meal_order_count.setVisibility(View.GONE);
					holder.decline.setVisibility(View.GONE);
				}else{
					holder.meal_order_count.setText(count+"");
				}
				num -= 1;
				context.changemoney(-1, (Double) data.get(position).get("price"));
			}
			
		}		
	}
	
	public void setSelected(ArrayList<Integer> list){
		Set<Integer> set = meal_sum.keySet();
		int index = 0;
		for(int key : set){
			if(meal_sum.get(key) != 0){
				meal_sum.put(key, list.get(index));
				++index;
			}else{
				meal_sum.put(key, 0);
			}
		}
		notifyDataSetChanged();
	}
	
	public void setData(ArrayList<HashMap<String, Object>> data, HashMap<Integer, Integer> meal_sum){
		this.data = data;
		//meal_sum.clear();
		this.meal_sum = meal_sum;
		result.getList().clear();
		notifyDataSetChanged();
	}
	
	public void getResult(){
		Set<Integer> set = meal_sum.keySet();
		ArrayList<Map<String, Object>> tempList = new ArrayList<Map<String,Object>>();
		for(int key : set){
			if(meal_sum.get(key) != 0){

				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.put("childName", data.get(key).get("name"));
				tempMap.put("childId", data.get(key).get("dish_id"));
				tempMap.put("childPrice", data.get(key).get("price"));
				tempMap.put("childNumber", meal_sum.get(key));
				tempMap.put("childImg", data.get(key).get("img"));
				tempMap.put("detail", data.get(key).get("detail"));
				tempList.add(tempMap);

			}			
		}
		result.setList(tempList);
	}
	
	public ArrayList<HashMap<String, Object>> getData(){
		return data;
	}
	
	public int getMealSum(int index){
		return meal_sum.get(index);
	}
	
}
