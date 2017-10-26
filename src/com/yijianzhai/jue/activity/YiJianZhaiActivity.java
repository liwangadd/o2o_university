package com.yijianzhai.jue.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yijianzhai.jue.R;
import com.yijianzhai.jue.adapter.ExpandedListViewAdapter;
import com.yijianzhai.jue.connection.HttpUtils.EHttpError;
import com.yijianzhai.jue.model.Dish;
import com.yijianzhai.jue.service.MealSetManageService;
import com.yijianzhai.jue.service.OnQueryCompleteListener;
import com.yijianzhai.jue.service.QueryId;
import com.yijianzhai.jue.utils.GetImgeLoadOption;
import com.yijianzhai.jue.utils.InitWedget;

public class YiJianZhaiActivity extends Activity implements
		OnQueryCompleteListener, InitWedget, OnClickListener,
		OnChildClickListener{

	// 组视图数据
	private List<Map<String, Object>> groupDataList = new ArrayList<Map<String, Object>>();
	// 子视图数据
	private List<List<Map<String, Object>>> childDataList = new ArrayList<List<Map<String, Object>>>();

	private ImageView backImageView;
	// 编辑按键
	private ImageView editOrderImageView;
	private ExpandableListView expandableListView;
	private ExpandedListViewAdapter expandedListViewAdapter;
	// 添加新套餐
	private TextView addNewMealSeTextView;
	// 下单时需要的数据
	private List<com.yijianzhai.jue.model.Package> orderList;
	// 是否启用编辑模式
	public static boolean IsEditable = false;
	// 用于删除套餐的回调
	private int groupIndex = 0;
	private ProgressBar progressBar;
	
	private DisplayImageOptions options;
	
	public ProgressBar getProgressBar() {
		return progressBar;
	}

	public void setProgressBar(ProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	public List<com.yijianzhai.jue.model.Package> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<com.yijianzhai.jue.model.Package> orderList) {
		this.orderList = orderList;
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
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yijianzhai);
		// 初始化控件
		initwedget();
		// 获得数据
	}

	public void updateUi() {
		if (IsEditable == true) {
			editOrderImageView.setBackgroundResource(R.drawable.cancelbutton);
		} else {
			editOrderImageView.setBackgroundResource(R.drawable.edit);
		}
	}

	private void getData() {
		new MealSetManageService().GetAllMealSet(HomePageActivity.UUID, this);
		progressBar.setVisibility(View.VISIBLE);
	}

	/**
	 * 用于adapter的删除套餐回调
	 * 
	 * @param group
	 */
	public void DelMealSet(int group) {
		this.groupIndex = group;
	}

	@Override
	public void onQueryComplete(QueryId queryId, Object result, EHttpError error) {
		// TODO Auto-generated method stub
		// 回调回更新界面
		progressBar.setVisibility(View.GONE);
		if (result != null) {
			if (queryId == MealSetManageService.GETALLMEALSET) {
				if (!result.toString().equals("failed")) {
					orderList = (List<com.yijianzhai.jue.model.Package>) result;
					analyzeData(orderList);
					expandedListViewAdapter.setGroupDataList(groupDataList);
					expandedListViewAdapter.setChildDataList(childDataList);
					expandedListViewAdapter.notifyDataSetChanged();
				} else {
					Toast.makeText(this, "您还没有添加套餐", Toast.LENGTH_LONG).show();
				}
			} else if (queryId == MealSetManageService.DELMEALSET) {
				if (!result.toString().equals("failed")) {
					this.groupDataList.remove(groupIndex);
					this.childDataList.remove(groupIndex);
					expandedListViewAdapter
							.setGroupDataList(this.groupDataList);
					expandedListViewAdapter
							.setChildDataList(this.childDataList);
					Toast.makeText(this, "删除套餐成功", Toast.LENGTH_LONG).show();
					expandedListViewAdapter.notifyDataSetChanged();
				} else {
					Toast.makeText(this, "删除套餐失败", Toast.LENGTH_LONG).show();
				}
			} else if (queryId == MealSetManageService.CHANGEMEALSET) {
				if (!result.toString().equals("failed")) {
					YiJianZhaiActivity.IsEditable = false;
					this.expandedListViewAdapter.setIsEditable(false);
					this.expandedListViewAdapter
							.setGroupDataList(this.groupDataList);
					this.expandedListViewAdapter
							.setChildDataList(this.childDataList);
					new MealSetManageService().GetAllMealSet(
							HomePageActivity.UUID, this);
					Toast.makeText(this, "修改套餐成功", Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(this, "修改套餐失败", Toast.LENGTH_LONG).show();
				}
			}
		} else {
			// 连接超时
			Toast.makeText(this, "网络连接超时", Toast.LENGTH_LONG).show();
		}
	}

	// 解析数据回调函数
	public void analyzeData(List<com.yijianzhai.jue.model.Package> orderList) {
		// TODO Auto-generated method stub
		this.groupDataList = new ArrayList<Map<String, Object>>();
		this.childDataList = new ArrayList<List<Map<String, Object>>>();
		for (int i = 0; i < orderList.size(); i++) {
			com.yijianzhai.jue.model.Package dataPackage = orderList.get(i);
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("groupName", dataPackage.getName());
			dataMap.put("groupPrice", dataPackage.getTotal_price());
			dataMap.put("sendPrice", dataPackage.getPrice_on_delivery());
			dataMap.put("type", dataPackage.getCategory());
			dataMap.put("resId", dataPackage.getRestaurant_id());
			dataMap.put("pacId", dataPackage.getId());
			dataMap.put("basePrice", dataPackage.getDelivery_starting_fee());
			groupDataList.add(dataMap);
			List<Dish> dishs = (List<Dish>) dataPackage.getDishes();
			List<Map<String, Object>> child = new ArrayList<Map<String, Object>>();
			for (int j = 0; j < dishs.size(); j++) {
				Map<String, Object> childData = new HashMap<String, Object>();
				Dish dish = dishs.get(j);
				childData.put("childName", dish.getName());
				childData.put("childNumber", dish.getNumber());
				childData.put("childPrice", dish.getPrice());
				childData.put("childDetail", dish.getDetail());
				childData.put("childId", dish.getDish_id());
				try {
					childData.put("childImg", dish.getImg());
				} catch (Exception e) {
					// TODO: handle exception
				}
				child.add(childData);
			}
			childDataList.add(child);
		}
	}

	@Override
	public void initwedget() {
		// TODO Auto-generated method stub

		options = GetImgeLoadOption.getOptions();
		
		this.backImageView = (ImageView) findViewById(R.id.img_yijian_back);
		this.editOrderImageView = (ImageView) findViewById(R.id.img_yijian_map);
		this.expandableListView = (ExpandableListView) findViewById(R.id.yijianzhai_list);
		this.addNewMealSeTextView = (TextView) findViewById(R.id.yijianzhi_add_new_meal);
		this.progressBar = (ProgressBar) findViewById(R.id.probar_yijian);

		this.expandableListView.setGroupIndicator(null);
		this.expandableListView.setOnChildClickListener(this);
		this.backImageView.setOnClickListener(this);
		this.editOrderImageView.setOnClickListener(this);
		this.addNewMealSeTextView.setOnClickListener(this);

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.img_yijian_back:
			finish();
			break;
		case R.id.img_yijian_map:
			// 调用adapter的更新视图函数
			if (IsEditable == false) {
				IsEditable = true;
				editOrderImageView
						.setBackgroundResource(R.drawable.cancelbutton);
			} else {
				IsEditable = false;
				editOrderImageView.setBackgroundResource(R.drawable.edit);
			}
			expandedListViewAdapter.setIsEditable(IsEditable);
			expandedListViewAdapter.notifyDataSetChanged();
			break;
		case R.id.yijianzhi_add_new_meal:
			// 进入新增套餐界面
			Intent intent = new Intent().setClass(this,
					OutrestaurantActivity.class);
			intent.putExtra("ismodifyYJZ", true);
			this.startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getData();
		this.expandedListViewAdapter = new ExpandedListViewAdapter(this,
				groupDataList, childDataList);
		this.expandedListViewAdapter.setChildDataList(childDataList);
		this.expandedListViewAdapter.setGroupDataList(groupDataList);
		this.expandedListViewAdapter.setYiJianZhaiActivity(this);
		this.expandableListView.setAdapter(this.expandedListViewAdapter);
	}

	@Override
	public boolean onChildClick(ExpandableListView arg0, View arg1, int arg2,
			int arg3, long arg4) {
		// TODO Auto-generated method stub
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		Map<String, Object> data = childDataList.get(arg2).get(arg3);
		final View dialogView = layoutInflater.inflate(R.layout.dish_detail,
				null);
		TextView dialogDishName = (TextView) dialogView
				.findViewById(R.id.yijianzhai_dialog_dishname);
		ImageView imageView = (ImageView) dialogView
				.findViewById(R.id.yijianzhai_dialog_img);
		ImageLoader.getInstance().displayImage(LoadingActivity.URL
					+ data.get("childImg").toString(), imageView,options);
		TextView dialogDishPrice = (TextView) dialogView
				.findViewById(R.id.yijianzhai_dialog_price);
		TextView dialogDishDetail = (TextView) dialogView
				.findViewById(R.id.yijianzhai_dialog_detail);
		// 设置可以滚动
		dialogDishDetail.setMovementMethod(ScrollingMovementMethod
				.getInstance());
		dialogDishName.setText(data.get("childName").toString());
		dialogDishPrice.setText("￥" + data.get("childPrice").toString());
		try {
			dialogDishDetail.setText(data.get("childDetail").toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		AlertDialog alertDialog = new AlertDialog.Builder(this).setView(
				dialogView).create();
		alertDialog.setCanceledOnTouchOutside(true);
		alertDialog.show();
		return false;
	}
	
}
