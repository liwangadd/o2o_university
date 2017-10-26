package com.yijianzhai.jue.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yijianzhai.jue.R;
import com.yijianzhai.jue.adapter.OutstoreAdapter;
import com.yijianzhai.jue.connection.HttpUtils.EHttpError;
import com.yijianzhai.jue.service.OnQueryCompleteListener;
import com.yijianzhai.jue.service.QueryId;
import com.yijianzhai.jue.service.RestaurantManager;
import com.yijianzhai.jue.service.Utils;
import com.yijianzhai.jue.utils.Mylocation;
import com.yijianzhai.jue.utils.Mylocation.InitSreen;
import com.yijianzhai.jue.utils.WiFiStatusUtil;
import com.yijianzhai.jue.view.XListView;
import com.yijianzhai.jue.view.XListView.IXListViewListener;

public class OutrestaurantActivity extends Activity implements
		OnItemClickListener, OnClickListener, IXListViewListener, InitSreen {
	private ImageView backImageView;
	private ImageView mapImageView;
	private TextView locationTextView;
	private static ProgressBar progressBar;
	private static ProgressBar loadProgressBar;
	private static XListView listView;
	private ImageView classify_icon, classify_down, order_icon, order_down,
			special_icon, special_down;
	private Button classify, order, special;
	private OutstoreAdapter outstoreAdapter;

	public static ArrayList<HashMap<String, Object>> listData;

	// 需要加下划线的按钮
	private TextView underlineTextView;

	private View classifyView, orderView, specialView;

	private PopupWindow classifyWindow, orderWindow, specialWindow;

	private String[] classifys = new String[] { "全部分类", "中式分类", "西式分类", "港式分类",
			"奶茶分类", "烧烤分类", "日式分类", "韩式分类", "甜点分类", "汉堡分类", "清真分类" };
	private String[] orders = new String[] { "默认排序", "距离排序", "销售排序", "送餐时间" };
	private String[] specials = new String[] { "无要求", "无配送费", "30分钟送达" };
	View change;

	private RestaurantManager manager;
	private OnQueryCompleteListener queryCompleteListener;
	private int specialIndex = 0, orderIndex = 0;
	private RelativeLayout relativeLayout;
	// 控制请求发送
	private boolean isUserful = true;
	private int page = 0;

	private Mylocation mylocation;

	// 判断是否执行onCreat函数
	private boolean isOncreatUserful = true;
	// 地址
	private String mylocationString;
	private String returnLocation;
	// 图片加载缓存
	private ImageLoader mImageLoader;
	// 城市
	private String city;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_outrestaurant);

		SharedPreferences sharedPreferences = getSharedPreferences("location",
				Context.MODE_WORLD_READABLE);
		Editor editor = sharedPreferences.edit();
		editor.putString("mylocation", "");
		editor.commit();
		listData = new ArrayList<HashMap<String, Object>>();
		initwedget();
		mylocation.showMylocation();
	}

	@Override
	public void UpdateScreen() {
		if (isUserful) {
			if (WiFiStatusUtil.getAPNType(OutrestaurantActivity.this) == -1) {
				Toast.makeText(OutrestaurantActivity.this, "请检查网络设置",
						Toast.LENGTH_SHORT).show();
				progressBar.setVisibility(View.GONE);
				return;
			}
			locationTextView.setText(Utils.ADDRESS);
			mylocationString = Utils.ADDRESS;
			manager.ResturantGet(HomePageActivity.UUID, 0,
					mylocation.getCity(), Utils.LATITUDE, Utils.LONGITUDE, 0,
					"全部分类", orderIndex, specialIndex, queryCompleteListener);
			isUserful = false;
		} else {
			mylocation.getLocationClient().stop();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		loadProgressBar = (ProgressBar) findViewById(R.id.storemap_progress_bar);
		loadProgressBar.setVisibility(View.GONE);

		SharedPreferences shraPreferences = getSharedPreferences("location",
				Context.MODE_WORLD_READABLE);
		returnLocation = shraPreferences.getString("mylocation", "");
		city = Utils.CITY;
		if (!returnLocation.equals("")) {
			locationTextView.setText(returnLocation);
			Utils.LATITUDE = shraPreferences.getString("latitude",
					Utils.LATITUDE);
			Utils.LONGITUDE = shraPreferences.getString("longitude",
					Utils.LONGITUDE);

			if (!returnLocation.equals(mylocationString)
					&& returnLocation != "") {
				listData.clear();
				outstoreAdapter.setDataList(listData);
				System.out.println(returnLocation + " " + mylocationString);
				manager.ResturantGet(HomePageActivity.UUID, 0, city,
						Utils.LATITUDE, Utils.LONGITUDE, 0, "全部分类", orderIndex,
						specialIndex, queryCompleteListener);
			}
			initwedget();
		}
		// initwedget();

	}

	private void initwedget() {

		// 根据id找到下划线的textview
		underlineTextView = (TextView) findViewById(R.id.text2);
		underlineTextView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

		backImageView = (ImageView) findViewById(R.id.img_return);
		locationTextView = (TextView) findViewById(R.id.txt_location);
		mapImageView = (ImageView) findViewById(R.id.img_map);
		listView = (XListView) findViewById(R.id.list_outstore);
		listView.setPullLoadEnable(true);
		listView.setPullRefreshEnable(false);
		listView.setXListViewListener(this);
		listView.setVisibal(6);
		outstoreAdapter = new OutstoreAdapter(listData,
				OutrestaurantActivity.this, OutrestaurantActivity.this);
		progressBar = (ProgressBar) findViewById(R.id.store_progress_bar);
		progressBar.setVisibility(View.VISIBLE);
		classify = (Button) findViewById(R.id.outer_classify);
		classify.setText(classifys[0]);
		classify_down = (ImageView) findViewById(R.id.outer_classify_down);
		classify_icon = (ImageView) findViewById(R.id.outer_classify_icon);
		order = (Button) findViewById(R.id.outer_order);
		order.setText(orders[0]);
		order_down = (ImageView) findViewById(R.id.outer_order_down);
		order_icon = (ImageView) findViewById(R.id.outer_order_icon);
		special = (Button) findViewById(R.id.outer_special);
		special.setText(specials[0]);
		special_down = (ImageView) findViewById(R.id.outer_special_down);
		special_icon = (ImageView) findViewById(R.id.outer_special_icon);
		relativeLayout = (RelativeLayout) findViewById(R.id.outrestaurant_relative_layout);
		relativeLayout.setOnClickListener(this);
		InitPopupWindow();

		classify.setOnClickListener(this);
		order.setOnClickListener(this);
		special.setOnClickListener(this);
		/*
		 * outstoreAdapter = new OutstoreAdapter(listData, this, this);
		 * listView.setTotal(10); listView.setAdapter(outstoreAdapter);
		 * listView.setOnItemClickListener(this);
		 */

		backImageView.setOnClickListener(this);
		mapImageView.setOnClickListener(this);
		locationTextView.setOnClickListener(this);

		manager = new RestaurantManager();

		queryCompleteListener = new OnQueryCompleteListener() {

			@Override
			public void onQueryComplete(QueryId queryId, Object result,
					EHttpError error) {
				if (result == null) {
					Toast.makeText(OutrestaurantActivity.this, "操作超时",
							Toast.LENGTH_SHORT).show();
					progressBar.setVisibility(View.GONE);
				} else if (result.equals("failed")) {
					Toast.makeText(OutrestaurantActivity.this, "服务器连接失败",
							Toast.LENGTH_SHORT).show();
					progressBar.setVisibility(View.GONE);
				} else if (result.equals("[]")) {
					Toast.makeText(OutrestaurantActivity.this, "没有更多商家",
							Toast.LENGTH_SHORT).show();
					if (page == 0) {
						listData.clear();

						//listView.setTotal(listData.size());
						findViewById(R.id.outresturant_layout).setVisibility(
								View.GONE);
						findViewById(R.id.out_error)
								.setVisibility(View.VISIBLE);
						progressBar.setVisibility(View.GONE);
					}
				} else {
					if (page != 0) {
						listData.addAll((ArrayList<HashMap<String, Object>>) result);
						outstoreAdapter.setDataList(listData);
					} else {
						listData = (ArrayList<HashMap<String, Object>>) result;
						listView.setTotal(listData.size());
						//System.out.println(listData.size());
						outstoreAdapter.setDataList(listData);
						listView.setAdapter(outstoreAdapter);
						progressBar.setVisibility(View.GONE);
						findViewById(R.id.out_error).setVisibility(View.GONE);
						listView.setOnItemClickListener(OutrestaurantActivity.this);
					}
					findViewById(R.id.outresturant_layout).setVisibility(
							View.VISIBLE);
					listView.setVisibility(View.VISIBLE);
				}
				listView.stopLoadMore();
			}
		};

		mylocation = new Mylocation(OutrestaurantActivity.this);
		mylocation.setOnFinishListener(this);

	}

	private void InitPopupWindow() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int width = metrics.widthPixels;

		classifyView = getLayoutInflater().inflate(R.layout.popwindow, null);
		orderView = getLayoutInflater().inflate(R.layout.popwindow, null);
		specialView = getLayoutInflater().inflate(R.layout.popwindow, null);

		ListView classifylsit = ((ListView) classifyView
				.findViewById(R.id.pop_list));
		classifylsit.setAdapter(new ArrayAdapter<String>(this,
				R.layout.popwindow_item, R.id.pop_item, classifys));
		classifylsit.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				classify.setText(classifys[arg2]);
				page = 0;
				manager.ResturantGet(HomePageActivity.UUID, 0, city,
						Utils.LATITUDE, Utils.LONGITUDE, 0, classifys[arg2],
						orderIndex, specialIndex, queryCompleteListener);
				classifyWindow.dismiss();
				// progressBar.setVisibility(View.VISIBLE);

			}
		});

		ListView orderlist = ((ListView) orderView.findViewById(R.id.pop_list));
		orderlist.setAdapter(new ArrayAdapter<String>(this,
				R.layout.popwindow_item, R.id.pop_item, orders));
		orderlist.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				order.setText(orders[arg2]);
				orderWindow.dismiss();
				orderIndex = arg2;
				page = 0;
				manager.ResturantGet(HomePageActivity.UUID, 0, city,
						Utils.LATITUDE, Utils.LONGITUDE, 0, classify.getText()
								.toString(), orderIndex, specialIndex,
						queryCompleteListener);
				// progressBar.setVisibility(View.VISIBLE);

			}
		});

		ListView speciallist = ((ListView) specialView
				.findViewById(R.id.pop_list));
		speciallist.setAdapter(new ArrayAdapter<String>(this,
				R.layout.popwindow_item, R.id.pop_item, specials));
		speciallist.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				special.setText(specials[arg2]);
				specialIndex = arg2;
				specialWindow.dismiss();
				page = 0;
				manager.ResturantGet(HomePageActivity.UUID, 0, city,
						Utils.LATITUDE, Utils.LONGITUDE, 0, classify.getText()
								.toString(), orderIndex, specialIndex,
						queryCompleteListener);
				// progressBar.setVisibility(View.VISIBLE);

			}
		});

		classifyWindow = new PopupWindow(classifyView, width,
				(int) new TypedValue().applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, 160, getResources()
								.getDisplayMetrics()));
		ModifyPopupWindow(classifyWindow);

		orderWindow = new PopupWindow(orderView, width,
				(int) new TypedValue().applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, 160, getResources()
								.getDisplayMetrics()));
		ModifyPopupWindow(orderWindow);

		specialWindow = new PopupWindow(specialView, width,
				LayoutParams.WRAP_CONTENT);
		ModifyPopupWindow(specialWindow);
	}

	private void ModifyPopupWindow(PopupWindow window) {
		window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		window.setOutsideTouchable(true);
		window.setFocusable(true);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		/*
		 * Map<String, Object> map=listData.get(arg2); Utils.RESTAURANT_ID =
		 * (String) map.get("resturant_id");
		 */
		if (change != null) {
			change.setBackgroundColor(Color.parseColor("#ffffff"));
		}
		arg1.setBackgroundColor(Color.parseColor("#f1f1f1"));
		change = arg1;
		outstoreAdapter.setCurrent(arg2);
		Intent intent = new Intent(OutrestaurantActivity.this,
				RestaurantActivity.class);
		intent.putExtra("resturant_id",
				String.valueOf(listData.get(arg2 - 1).get("resturant_id")));
		if (OutrestaurantActivity.this.getIntent().getBooleanExtra(
				"ismodifyYJZ", false)) {
			intent.putExtra("ismodifyYJZ", true);
			startActivity(intent);
			finish();
		} else {
			startActivity(intent);
		}

	}

	@Override
	public void onRefresh() {
	}

	@Override
	public void onLoadMore() {
		page += 1;
		manager.ResturantGet(HomePageActivity.UUID, 0, city, Utils.LATITUDE,
				Utils.LONGITUDE, page, classify.getText().toString(),
				orderIndex, specialIndex, queryCompleteListener);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_return:
			finish();
			break;
		case R.id.img_map:
			Intent intent1 = new Intent(OutrestaurantActivity.this,
					OverlayMapActivity.class);
			intent1.putExtra("resturant", listData);
			startActivity(intent1);
			break;
		case R.id.txt_location:
			Intent intent = new Intent(OutrestaurantActivity.this,
					SearchHistory.class);
			startActivity(intent);
			break;
		case R.id.outer_classify:
			if (classifyWindow.isShowing()) {
				classifyWindow.dismiss();
			} else {
				classifyWindow.showAsDropDown(v);
			}
			break;
		case R.id.outer_order:
			if (orderWindow.isShowing()) {
				orderWindow.dismiss();
			} else {
				orderWindow.showAsDropDown(v);
			}
			break;
		case R.id.outer_special:
			if (specialWindow.isShowing()) {
				specialWindow.dismiss();
			} else {
				specialWindow.showAsDropDown(v);
			}
			break;
		case R.id.outrestaurant_relative_layout:
			this.startActivity(new Intent().setClass(this,
					QueryOrderActivity.class));
		default:
			break;
		}
	}

	public List<HashMap<String, Object>> getList() {

		return listData;
	}

}