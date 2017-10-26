package com.yijianzhai.jue.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.yijianzhai.jue.R;
import com.yijianzhai.jue.activity.HomePageActivity;
import com.yijianzhai.jue.activity.LoadingActivity;
import com.yijianzhai.jue.activity.YiJianZhaiActivity;
import com.yijianzhai.jue.activity.YiJianZhaiMakeSureActivity;
import com.yijianzhai.jue.model.DataList;
import com.yijianzhai.jue.model.DataMap;
import com.yijianzhai.jue.service.MealSetManageService;

/**
 * ExpandedListView的适配器
 * 
 * @author qiuyang
 * 
 */
public class ExpandedListViewAdapter extends BaseExpandableListAdapter {

	// 上下文对象
	private Context context = null;
	// 组视图数据
	private List<Map<String, Object>> groupDataList;
	// 子视图数据
	private List<List<Map<String, Object>>> childDataList;
	private LayoutInflater mInflater;
	private YiJianZhaiActivity yiJianZhaiActivity;
	private Map<String, Object> editName = new HashMap<String, Object>();
	private int childIndex;
	private int groupIndex;
	// // 数量
	// private List<List<Integer>> numbers;
	// 是否启用编辑模式
	private boolean IsEditable = false;
	
	private AnimateFirstDiaplayListener listener;
	private DisplayImageOptions options;

	public boolean isIsEditable() {
		return IsEditable;
	}

	public void setIsEditable(boolean isEditable) {
		IsEditable = isEditable;
	}

	public YiJianZhaiActivity getYiJianZhaiActivity() {
		return yiJianZhaiActivity;
	}

	public void setYiJianZhaiActivity(YiJianZhaiActivity yiJianZhaiActivity) {
		this.yiJianZhaiActivity = yiJianZhaiActivity;
	}

	public ExpandedListViewAdapter(Context context,
			List<Map<String, Object>> groupDataList,
			List<List<Map<String, Object>>> childDataList) {
		super();
		this.context = context;
		yiJianZhaiActivity = (YiJianZhaiActivity) context;
		this.groupDataList = groupDataList;
		this.childDataList = childDataList;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		listener = new AnimateFirstDiaplayListener();
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.danta)
		.showImageForEmptyUri(R.drawable.danta)
		.showImageOnFail(R.drawable.danta)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.displayer(new RoundedBitmapDisplayer(20))
		.build();
	}

	public ExpandedListViewAdapter() {
		super();
		// TODO Auto-generated constructor stub
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public Context getContext() {
		return context;

	}

	public ExpandedListViewAdapter(Context context) {
		super();
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public List<Map<String, Object>> getGroupDataList() {
		return groupDataList;
	}

	public void setGroupDataList(List<Map<String, Object>> groupDataList) {
		this.groupDataList = groupDataList;
	}

	public List<List<Map<String, Object>>> getChildDataList() {
		return childDataList;
	}

	public void setChildDataList(List<List<Map<String, Object>>> childDataList) {
		this.childDataList = childDataList;
	}

	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		// 返回指定位置的子视图数据
		return childDataList.get(arg0).get(arg1);
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		// 返回子视图的指定位置
		return arg1;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		childIndex = groupPosition;
		ChildViewHolder childViewHolder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.child, null);
			childViewHolder = new ChildViewHolder();
			convertView.setTag(childViewHolder);
			childViewHolder.img = (ImageView) convertView
					.findViewById(R.id.child_img);
			ImageLoader.getInstance().displayImage(LoadingActivity.URL
					+ childDataList.get(groupPosition)
					.get(childPosition).get("childImg")
					.toString(), childViewHolder.img, options, listener);

			childViewHolder.dishName = (TextView) convertView
					.findViewById(R.id.child_dish_name);
			childViewHolder.dishNumber = (TextView) convertView
					.findViewById(R.id.child_dish_number_text);
			childViewHolder.decline = (Button) convertView
					.findViewById(R.id.child_decline);
			childViewHolder.count = (Button) convertView
					.findViewById(R.id.child_item_count);
			childViewHolder.add = (Button) convertView
					.findViewById(R.id.child_add);
			childViewHolder.dishPrice = (TextView) convertView
					.findViewById(R.id.child_price);
		} else {
			childViewHolder = (ChildViewHolder) convertView.getTag();
		}

		try {
			childViewHolder.dishName.setText(childDataList.get(groupPosition)
					.get(childPosition).get("childName").toString());
			childViewHolder.dishNumber.setText("x"
					+ childDataList.get(groupPosition).get(childPosition)
							.get("childNumber").toString());
		} catch (Exception e) {
			// TODO: handle exception
		}

		MyClickListener myClickListener = new MyClickListener(groupPosition,
				childPosition);
		childViewHolder.decline.setOnClickListener(myClickListener);

		try {
			childViewHolder.count.setText(childDataList.get(groupPosition)
					.get(childPosition).get("childNumber").toString());
		} catch (Exception e) {
			// TODO: handle exception
		}

		childViewHolder.add.setOnClickListener(myClickListener);
		if (IsEditable == true) {
			if (groupDataList.get(groupPosition).get("type").equals("0")) {
				childViewHolder.decline.setVisibility(View.VISIBLE);
				childViewHolder.count.setVisibility(View.VISIBLE);
				childViewHolder.add.setVisibility(View.VISIBLE);
				childViewHolder.dishNumber.setVisibility(View.GONE);
			}
		} else {
			childViewHolder.decline.setVisibility(View.GONE);
			childViewHolder.count.setVisibility(View.GONE);
			childViewHolder.add.setVisibility(View.GONE);
			childViewHolder.dishNumber.setVisibility(View.VISIBLE);
		}

		try {
			childViewHolder.dishPrice.setText("￥"
					+ childDataList.get(groupPosition).get(childPosition)
							.get("childPrice").toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return convertView;
	}

	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub
		return childDataList.get(arg0).size();
	}

	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		return childDataList.get(arg0);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return childDataList.size();
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getGroupView(int arg0, boolean arg1, View arg2, ViewGroup arg3) {
		// TODO Auto-generated method stub
		groupIndex = arg0;
		GroupViewHolder groupViewHolder = null;
		if (arg2 == null) {
			arg2 = mInflater.inflate(R.layout.group, null);
			groupViewHolder = new GroupViewHolder();
			arg2.setTag(groupViewHolder);
			groupViewHolder.shrink = (Button) arg2
					.findViewById(R.id.group_button);
			groupViewHolder.mealSetName = (TextView) arg2
					.findViewById(R.id.group_mealset_name);
			groupViewHolder.nameEditText = (EditText) arg2
					.findViewById(R.id.group_mealset_name_edit);
			groupViewHolder.makeSure = (Button) arg2
					.findViewById(R.id.group_makesure);
			groupViewHolder.changeMealSet = (TextView) arg2
					.findViewById(R.id.group_change_mealset_button);
			groupViewHolder.delMealSet = (TextView) arg2
					.findViewById(R.id.group_del_mealset_button);
		} else {
			groupViewHolder = (GroupViewHolder) arg2.getTag();
		}
		MyClickListener myClickListener = new MyClickListener(arg0, 0);

		if (arg1) {
			groupViewHolder.shrink.setBackgroundResource(R.drawable.shrink);
		} else {
			groupViewHolder.shrink.setBackgroundResource(R.drawable.spread);
		}

		try {
			groupViewHolder.mealSetName.setText(groupDataList.get(arg0)
					.get("groupName").toString());
		} catch (Exception e) {
			// TODO: handle exception
		}

		if (!editName.containsKey(arg0)) {
			editName.put(arg0 + "", groupViewHolder.nameEditText);
		}
		try {
			groupViewHolder.nameEditText.setHint(groupDataList.get(arg0)
					.get("groupName").toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (IsEditable == true) {
			if (groupDataList.get(arg0).get("type").equals("0")) {
				groupViewHolder.mealSetName.setVisibility(View.GONE);
				groupViewHolder.nameEditText.setVisibility(View.VISIBLE);
			}
		} else {
			groupViewHolder.mealSetName.setVisibility(View.VISIBLE);
			groupViewHolder.nameEditText.setVisibility(View.GONE);
		}
		groupViewHolder.mealSetPrice = (TextView) arg2
				.findViewById(R.id.group_price);
		try {
			groupViewHolder.mealSetPrice.setText("￥"
					+ groupDataList.get(arg0).get("groupPrice").toString());
		} catch (Exception e) {
			// TODO: handle exception
		}

		groupViewHolder.makeSure.setOnClickListener(myClickListener);
		groupViewHolder.makeSure.setOnClickListener(myClickListener);

		// 加入下划线

		if (IsEditable == true) {

			if (groupDataList.get(arg0).get("type").equals("0")) {
				groupViewHolder.makeSure.setVisibility(View.GONE);
				groupViewHolder.changeMealSet.setVisibility(View.VISIBLE);
				groupViewHolder.delMealSet.setVisibility(View.VISIBLE);
				groupViewHolder.delMealSet.getPaint().setFlags(
						Paint.UNDERLINE_TEXT_FLAG);
				groupViewHolder.changeMealSet
						.setOnClickListener(myClickListener);
				groupViewHolder.delMealSet.setOnClickListener(myClickListener);
			}
		} else {
			groupViewHolder.makeSure.setVisibility(View.VISIBLE);
			groupViewHolder.changeMealSet.setVisibility(View.GONE);
			groupViewHolder.delMealSet.setVisibility(View.GONE);
		}
		return arg2;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		// 子视图可点击
		return true;
	}

	// 组视图的viewholder
	private class GroupViewHolder {
		// 左面的展开收缩的按钮
		Button shrink;
		// 套餐名称
		TextView mealSetName;
		// 套餐总价格
		TextView mealSetPrice;
		// 快速下单按钮
		Button makeSure;
		// 改变套餐名称的编辑框
		EditText nameEditText;
		// 修改套餐按钮
		TextView changeMealSet;
		// 删除套餐按钮
		TextView delMealSet;
	}

	// 子视图的viewholer
	private class ChildViewHolder {
		// 左面的菜品图片
		ImageView img;
		// 菜品名称
		TextView dishName;
		// 减少数量
		Button decline;
		// 显示的数量
		Button count;
		// 增加数量
		Button add;
		// 菜品价格
		TextView dishPrice;
		// 菜品数量文本框
		TextView dishNumber;

	}

	class MyClickListener implements OnClickListener {

		private int group;
		private int child;

		public MyClickListener(int groupPosition, int childPosition) {
			super();
			this.group = groupPosition;
			this.child = childPosition;
		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {
			case R.id.child_add:
				Integer integer = Integer.valueOf(childDataList.get(group)
						.get(child).get("childNumber").toString()) + 1;
				childDataList.get(group).get(child).put("childNumber", integer);
				yiJianZhaiActivity.setChildDataList(childDataList);
				notifyDataSetChanged();
				break;
			case R.id.child_decline:
				if (Integer.valueOf(childDataList.get(group).get(child)
						.get("childNumber").toString()) > 0) {
					Integer integer2 = Integer.valueOf(childDataList.get(group)
							.get(child).get("childNumber").toString()) - 1;
					childDataList.get(group).get(child)
							.put("childNumber", integer2);
					yiJianZhaiActivity.setChildDataList(childDataList);
				}
				// 如果数量为0，就在套餐中删除这个菜
				if (Integer.valueOf(childDataList.get(group).get(child)
						.get("childNumber").toString()) == 0) {
					childDataList.get(group).remove(child);

					((com.yijianzhai.jue.model.Package) yiJianZhaiActivity
							.getOrderList().get(group)).getDishes().remove(
							child);
				}
				// 如果套餐所有的菜都不存在，就删除这个套餐
				if (childDataList.get(group).size() == 0) {
					childDataList.remove(group);
					groupDataList.remove(group);
					yiJianZhaiActivity.getOrderList().remove(group);
				}
				if (childDataList.size() == 0) {
					new MealSetManageService().ChangeMealSet(
							HomePageActivity.UUID,
							new ArrayList<com.yijianzhai.jue.model.Package>(),
							yiJianZhaiActivity);
					yiJianZhaiActivity.IsEditable = false;
					yiJianZhaiActivity.updateUi();
				}
				notifyDataSetChanged();
				break;
			// 修改套餐名称
			case R.id.group_change_mealset_button:
				List<com.yijianzhai.jue.model.Package> packages = yiJianZhaiActivity
						.getOrderList();
				System.out.println(groupDataList.size());
				for (int i = 0; i < groupDataList.size(); i++) {
					double sumPrice = 0;
					EditText editText = (EditText) editName.get(i + "");
					String name = editText.getText().toString();
					if (!name.equals("")) {
						groupDataList.get(i).put("groupName", name);
						packages.get(i).setName(name);
					}
					for (int j = 0; j < childDataList.get(i).size(); j++) {
						packages.get(i)
								.getDishes()
								.get(j)
								.setNumber(
										Integer.valueOf(childDataList.get(i)
												.get(j).get("childNumber")
												.toString()));
						sumPrice += Integer.valueOf(childDataList.get(i).get(j)
								.get("childNumber").toString())
								* Double.valueOf(childDataList.get(i).get(j)
										.get("childPrice").toString());
					}
					packages.get(i).setTotal_price(sumPrice);
				}
				yiJianZhaiActivity.setOrderList(packages);
				yiJianZhaiActivity.analyzeData(packages);
				new MealSetManageService().ChangeMealSet(HomePageActivity.UUID,
						packages, yiJianZhaiActivity);
				yiJianZhaiActivity.getProgressBar().setVisibility(View.VISIBLE);
				// 更新一键宅ui界面
				yiJianZhaiActivity.IsEditable = false;
				yiJianZhaiActivity.updateUi();
				break;
			// 删除套餐
			case R.id.group_del_mealset_button:
				new MealSetManageService().DelMealSet(HomePageActivity.UUID,
						groupDataList.get(group).get("pacId").toString(),
						yiJianZhaiActivity);
				yiJianZhaiActivity.getProgressBar().setVisibility(View.VISIBLE);
				yiJianZhaiActivity.DelMealSet(group);
				break;
			// 快速下单
			case R.id.group_makesure:
				Intent intent = new Intent();
				DataList dataList = new DataList(childDataList.get(group));
				intent.putExtra("data", dataList);
				intent.putExtra("map", new DataMap(groupDataList.get(group)));
				intent.putExtra("isRecommed",
						groupDataList.get(group).get("type").toString());
				intent.setClass(context, YiJianZhaiMakeSureActivity.class);
				context.startActivity(intent);
				break;
			default:
				break;
			}
		}
	}
	
	private static class AnimateFirstDiaplayListener extends SimpleImageLoadingListener{
		private static final List<String> displayImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if(loadedImage != null){
				ImageView imageView = (ImageView)view;
				boolean isFirst = !displayImages.contains(imageUri);
				if(isFirst){
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayImages.add(imageUri);
				}
			}
		}
		
	}

}
