package com.yijianzhai.jue.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.yijianzhai.jue.R;
import com.yijianzhai.jue.model.Package;



public class AddToSetAdapter extends BaseAdapter{
	
	private Context context;
	private ArrayList<Package> data;
	private LayoutInflater inflater;
	private HashMap<Integer, Boolean> state;
	private int chose = -1;
	
	public AddToSetAdapter(Context context, ArrayList<Package> data){
		this.context = context;
		this.data = data;
		inflater = LayoutInflater.from(context);
		state = new HashMap<Integer, Boolean>();
	}

	@Override
	public int getCount() {
		
		return data.size();
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
			convertView = inflater.inflate(R.layout.add_to_set_item, null);
			holder = new ViewHolder();
			holder.set_check = (CheckBox) convertView.findViewById(R.id.add_to_set_check);
			holder.set_name = (TextView) convertView.findViewById(R.id.add_to_set_name);
			convertView.setTag(holder);
//			holder.set_check.setOnClickListener(new CheckBoxListener(position));
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.set_name.setText(data.get(position).getName());
		holder.set_check.setOnClickListener(new CheckBoxListener(position));
		holder.set_check.setChecked(state.get(position));
		return convertView;
	}
	
	public class CheckBoxListener implements OnClickListener{
		
		private int position;
		
		public CheckBoxListener(int position){
			System.out.println(position);
			this.position = position;
			if(!state.containsKey(position)){
				state.put(position, false);
			}
		}

		@Override
		public void onClick(View arg0) {
			if(state.get(position)){
				state.put(position, false);
				chose = -1;
			}else{
				state.put(position, true);
				if(chose != -1){
					state.put(chose, false);
				}
				chose = position;
				notifyDataSetChanged();
			}
		}

	}
	
	public Object getPackages(){
		if(chose == -1){
			return "failed";
		}else{
			return data.get(chose);
		}
	}
	
	public class ViewHolder{
		TextView set_name;
		CheckBox set_check;
	}
	
}
