package com.yijianzhai.jue.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yijianzhai.jue.R;
import com.yijianzhai.jue.adapter.AddNewMealSetApapter;
import com.yijianzhai.jue.adapter.QueryOrderAdapter;
import com.yijianzhai.jue.connection.HttpUtils.EHttpError;
import com.yijianzhai.jue.model.DataList;
import com.yijianzhai.jue.model.DataMap;
import com.yijianzhai.jue.model.Dish;
import com.yijianzhai.jue.model.Order;
import com.yijianzhai.jue.model.Package;
import com.yijianzhai.jue.service.MealSetManageService;
import com.yijianzhai.jue.service.OnQueryCompleteListener;
import com.yijianzhai.jue.service.OrderManageService;
import com.yijianzhai.jue.service.QueryId;
import com.yijianzhai.jue.utils.InitWedget;
import com.yijianzhai.jue.view.XListView;
import com.yijianzhai.jue.view.XListView.IXListViewListener;

public class QueryOrderActivity extends Activity implements OnClickListener,
		InitWedget, OnQueryCompleteListener, IXListViewListener {

	private ImageView back;
	private TextView detail_sum;
	private TextView detail_count;
	private Button order_sure;
	private XListView listView;
	// 订单数据
	private List<Order> orders;
	private QueryOrderAdapter adapter;
	private TextView titleTextView;
	private RelativeLayout layout;
	private int againCount = 0;
	private double againSum = 0;
	private int seletedIndex = -1;
	// 标记类型，如果0为普通的查询,1为加载更多2为继续加载
	private int type = 0;
	// 分页的页数
	private int page = 0;
	private double sumPrice = 0;
	private String resId;
	private double sendPrice;

	private EditText yijianzhai_new_name;
	private TextView yijianzhai_makesure_sure_count;
	private TextView yijianzhai_makesure_send_price;
	private TextView yijianzhai_makesure_sure_sum;
	private AddNewMealSetApapter addNewMealSetApapter;
	private AlertDialog alertDialog;
	private Dialog dialog;

	private ProgressBar progressBar;
	private ProgressBar progressBar_dialogBar;

	public String getResId() {
		return resId;
	}

	public double getSendPrice() {
		return sendPrice;
	}

	public void setSendPrice(double sendPrice) {
		this.sendPrice = sendPrice;
	}

	public void setResId(String resId) {
		this.resId = resId;
	}

	public Dialog getDialog() {
		return dialog;
	}

	public void setDialog(Dialog dialog) {
		this.dialog = dialog;
	}

	private List<com.yijianzhai.jue.model.Package> packages;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.queryorder);
		initwedget();
	}

	private void getData() {
		// TODO Auto-generated method stub
		progressBar.setVisibility(View.VISIBLE);
		new OrderManageService().UserGetOrderInfo(HomePageActivity.UUID,
				0 + "", this);
		type = 0;
	}

	/**
	 * 回调回来的订单序号
	 * 
	 * @param seletedIndex
	 */
	public void updateIndex(int seletedIndex) {
		this.seletedIndex = seletedIndex;
	}

	public void updatePrice(double price, boolean isAdd) {
		if (isAdd == true) {
			this.sumPrice += price;
		} else {
			this.sumPrice -= price;
		}
		yijianzhai_makesure_sure_sum.setText("￥" + sumPrice);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.img_orderdetail_back:
			onBackPressed();
			break;
		case R.id.btn_orderdetail_again:
			if (orders != null) {
				if (seletedIndex >= 0) {
					Order tempOrder = orders.get(seletedIndex);
					DataMap dataMap = new DataMap();
					DataList dataList = new DataList();
					Map<String, Object> map = new HashMap<String, Object>();
					List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
					map.put("count", 1);
					map.put("sum", sumPrice);
					map.put("resId", tempOrder.getRestaurantId());
					System.out.println(tempOrder.getPrice_on_delivery());
					map.put("sendPrice", tempOrder.getPrice_on_delivery());
					map.put("basePrice", 0.0);
					dataMap.setMap(map);
					for(Dish dish : tempOrder.getDishes()){
						Map<String, Object> tempMap = new HashMap<String, Object>();
						tempMap.put("childName", dish.getName());
						tempMap.put("childId", dish.getDish_id());
						tempMap.put("childPrice", dish.getPrice());
						tempMap.put("childNumber", dish.getNumber());
						tempMap.put("childImg", dish.getImg());
						tempMap.put("detail", dish.getDetail());
						list.add(tempMap);
					}
					dataList.setList(list);
					Intent intent = new Intent(QueryOrderActivity.this, YiJianZhaiMakeSureActivity.class);
					intent.putExtra("map", dataMap);
					intent.putExtra("data", dataList);
					startActivity(intent);
					finish();
					
				} else {
					Toast.makeText(this, "您还没有选择订单", Toast.LENGTH_LONG).show();
				}
			}
			break;
		case R.id.make_sure_dialog_query_order:
			alertDialog.dismiss();
			this.startActivity(new Intent().setClass(this,
					QueryOrderActivity.class));
			finish();
			break;
		case R.id.make_sure_dialog_query_back:
			alertDialog.dismiss();
			this.startActivity(new Intent().setClass(this,
					HomePageActivity.class));
			finish();
			break;
		case R.id.dialog_add_new_meal_sure:
			// 确认添加菜单,调用添加套餐接口

			com.yijianzhai.jue.model.Package data = new Package();
			data.setCategory("0");
			data.setDishes(addNewMealSetApapter.getDishs());
			data.setPrice_on_delivery(sendPrice);
			data.setRestaurant_id(resId);
			data.setTotal_price(sumPrice);
			String pacName = yijianzhai_new_name.getText().toString();
			if (pacName.equals("")) {
				Toast.makeText(this, "请输入套餐名", Toast.LENGTH_LONG).show();
				break;
			}
			data.setName(pacName);
			progressBar_dialogBar.setVisibility(View.VISIBLE);
			new MealSetManageService().AddMealSet(HomePageActivity.UUID, data,
					this);
			break;
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getData();
	}

	@Override
	public void initwedget() {
		back = (ImageView) findViewById(R.id.img_orderdetail_back);
		detail_count = (TextView) findViewById(R.id.detail_count);
		detail_sum = (TextView) findViewById(R.id.detail_sum);
		order_sure = (Button) findViewById(R.id.btn_orderdetail_again);

		titleTextView = (TextView) findViewById(R.id.title1);
		layout = (RelativeLayout) findViewById(R.id.detail1);
		progressBar = (ProgressBar) findViewById(R.id.probar_yijian);

		layout.setVisibility(View.GONE);
		titleTextView.setText("我的订单");
		order_sure.setText("再次购买");

		back.setOnClickListener(this);
		order_sure.setOnClickListener(this);

	}

	public void changeMoney(int count, double price) {
		againCount = count;
		againSum = count * price;
		detail_count.setText(againCount + "份外卖");
		detail_sum.setText("￥" + againSum);
	}

	private void onLoad() {
		if (listView != null) {
			listView.stopRefresh();
			listView.stopLoadMore();
			listView.setRefreshTime("刚刚");
		}
	}

	@Override
	public void onQueryComplete(QueryId queryId, Object result, EHttpError error) {
		// TODO Auto-generated method stub
		progressBar.setVisibility(View.GONE);
		if (result != null) {
			if (queryId == OrderManageService.GETORDERINFO) {
				if (result.toString().equals("failed")) {
					onLoad();
					Toast.makeText(this, "暂无任何记录", Toast.LENGTH_LONG).show();
				} else {
					if (type == 1) {
						this.orders.addAll((List<Order>) result);
						onLoad();
						type = 0;
					} else if (type == 2) {
						List<Order> list = (List<Order>) result;
						list.addAll(this.orders);
						this.orders = list;
						onLoad();
						type = 0;
					} else {
						this.orders = (List<Order>) result;
						System.out.println(this.orders.size());
					}
					if (listView == null) {
						listView = (XListView) findViewById(R.id.order_list);
						listView.setXListViewListener(this);
						listView.setPullLoadEnable(true);
						listView.setPullRefreshEnable(false);
						// 调整是否添加footer
						listView.setTotal(orders.size());
						listView.setVisibal(5);
						listView.setVisibility(View.VISIBLE);
					}

					if (adapter == null) {
						adapter = new QueryOrderAdapter(this.orders, this);
						listView.setAdapter(adapter);
					} else {
						// System.out.println(this.orders.size());
						adapter.setData(this.orders);
						adapter.notifyDataSetChanged();
					}

				}
			} else if (queryId == OrderManageService.ZHAIMAKESURE) {
				if (!result.equals("failed")) {
					LayoutInflater layoutInflater = LayoutInflater.from(this);
					final View dialogView = layoutInflater.inflate(
							R.layout.make_sure, null);
					Button queryButton = (Button) dialogView
							.findViewById(R.id.make_sure_dialog_query_order);
					Button backButton = (Button) dialogView
							.findViewById(R.id.make_sure_dialog_query_back);
					queryButton.setOnClickListener(this);
					backButton.setOnClickListener(this);
					alertDialog = new AlertDialog.Builder(this).setView(
							dialogView).create();
					alertDialog.setCanceledOnTouchOutside(false);
					alertDialog.show();
				} else {
					Toast.makeText(this, "下单失败", Toast.LENGTH_LONG).show();
				}
			} else if (queryId == MealSetManageService.ADDMEALSET) {
				progressBar_dialogBar.setVisibility(View.GONE);
				if (!result.equals("failed")) {
					Toast.makeText(this, "添加套餐成功", Toast.LENGTH_LONG).show();
					dialog.dismiss();
					startActivity(new Intent().setClass(this,
							YiJianZhaiActivity.class));
				} else {
					Toast.makeText(this, "添加套餐失败", Toast.LENGTH_LONG).show();
				}
			}
		} else {
			onLoad();
			Toast.makeText(this, "网络连接超时", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		new OrderManageService().UserGetOrderInfo(HomePageActivity.UUID,
				(++page) + "", this);
		type = 2;
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		new OrderManageService().UserGetOrderInfo(HomePageActivity.UUID,
				(++page) + "", this);
		type = 1;
	}

	// 产生对话框
	public void CreateDialog(String resid, double sendPrice, List<Dish> dishs) {
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		final View dialogView = layoutInflater.inflate(R.layout.add_newmealset,
				null);
		addNewMealSetApapter = new AddNewMealSetApapter(dishs, this);
		this.setResId(resid);
		this.setSendPrice(sendPrice);
		ListView listView = (ListView) dialogView
				.findViewById(R.id.dialog_add_new_meal_list);
		listView.setAdapter(addNewMealSetApapter);

		Button makeSureAdd = (Button) dialogView
				.findViewById(R.id.dialog_add_new_meal_sure);
		makeSureAdd.setOnClickListener(this);
		yijianzhai_new_name = (EditText) dialogView
				.findViewById(R.id.yijianzhai_new_name);
		yijianzhai_makesure_sure_count = (TextView) dialogView
				.findViewById(R.id.yijianzhai_makesure_sure_count);
		yijianzhai_makesure_send_price = (TextView) dialogView
				.findViewById(R.id.yijianzhai_makesure_send_price);
		yijianzhai_makesure_sure_sum = (TextView) dialogView
				.findViewById(R.id.yijianzhai_makesure_sure_sum);
		progressBar_dialogBar = (ProgressBar) dialogView
				.findViewById(R.id.probar_yijian_dialog);
		yijianzhai_makesure_sure_count.setText(dishs.size() + "份外卖");
		yijianzhai_makesure_send_price.setText("配送费 ￥" + this.sendPrice);
		for (int i = 0; i < dishs.size(); i++) {
			this.sumPrice += dishs.get(i).getNumber() * dishs.get(i).getPrice();
		}
		yijianzhai_makesure_sure_sum.setText("￥" + sumPrice);
		dialog = new Dialog(this);
		// 设置无标题
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(dialogView);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, UsercenterActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		//finish();
		overridePendingTransition(R.anim.activityin, R.anim.activityout);
	}
	
}
