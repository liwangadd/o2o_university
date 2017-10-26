package com.yijianzhai.jue.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yijianzhai.jue.R;
import com.yijianzhai.jue.adapter.AddToSetAdapter;
import com.yijianzhai.jue.adapter.OrderSureAdapter;
import com.yijianzhai.jue.adapter.RestDishAdapter;
import com.yijianzhai.jue.connection.HttpUtils.EHttpError;
import com.yijianzhai.jue.model.DataList;
import com.yijianzhai.jue.model.DataMap;
import com.yijianzhai.jue.model.Dish;
import com.yijianzhai.jue.model.Package;
import com.yijianzhai.jue.service.MealSetManageService;
import com.yijianzhai.jue.service.OnQueryCompleteListener;
import com.yijianzhai.jue.service.QueryId;
import com.yijianzhai.jue.service.RestaurantManager;
import com.yijianzhai.jue.utils.GetImgeLoadOption;


public class RestaurantActivity extends Activity implements OnClickListener{
	
	private ListView rightlist;

	private HashMap<String, Object> data;
	private ArrayList<ArrayList<HashMap<String, Object>>> dishList;

	private static DataList select;

	private RestDishAdapter rightAdapter;
	
	private ListView leftListView;
	private Button order;
	
	private ImageView back;
	private ImageView detail;
	private ImageView res_icon;
	private TextView titleTextView;
	private TextView typeTextView;
	private int num;
	private double money;
	private double baseprice;
	private double delivery_fee;
	private LinearLayout totalLayout;
	private TextView pricenum, delivery_time, res_adress, delivery_range;
	private TextView chamoney, delivery_mode, sale_count, delivery_for;
	private PopupWindow pop;
	private ProgressBar bar;
	//保存leftlistview点击的item
	private View leftSelectView;
	private String resturant_id;
	
	private DisplayImageOptions options;
	

	//后台通信接口
	private RestaurantManager manager;
	private MealSetManageService mealSetManageService;
	private OnQueryCompleteListener queryCompleteListener;
	//保存店铺分类
	private ArrayList<String> dishClassify;
	private AddToSetAdapter add_to_set_adapter;
	
	private HashMap<Integer, DataList> resultHashMap = new HashMap<Integer, DataList>();
	private int lastItem = 0;
	private HashMap<Integer, HashMap<Integer, Integer>> meal_sum 
	= new HashMap<Integer, HashMap<Integer,Integer>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.restaurant);
		titleTextView=(TextView)findViewById(R.id.txt_restaurant);
		typeTextView=(TextView)findViewById(R.id.txt_resttype);
		delivery_for = (TextView)findViewById(R.id.txt_delivery_for);
		
		
		options = GetImgeLoadOption.getOptions();
		data = new HashMap<String, Object>();


		leftListView = (ListView)findViewById(R.id.resturant_left);
		
		back = (ImageView) findViewById(R.id.res_img_return);
		back.setOnClickListener(this);
		
		//选好了按钮
		order = (Button)findViewById(R.id.btn_makeorder);
		order.setOnClickListener(this);
		
		detail = (ImageView) findViewById(R.id.btn_detail);
		detail.setOnClickListener(this);
		
		totalLayout = (LinearLayout) findViewById(R.id.rll_store);
		
		bar = (ProgressBar) findViewById(R.id.dialog_rest);
		
		pricenum = (TextView) findViewById(R.id.txt_count);
		chamoney = (TextView) findViewById(R.id.txt_ifdelivery);
		num = 0;
		money = 0;
		baseprice = 50;
		
		select = new DataList();
		
		rightlist = (ListView)findViewById(R.id.rest_list);
		//rightlist.setOnScrollListener(this);

		
		delivery_mode = (TextView)findViewById(R.id.res_price);
		delivery_range = (TextView)findViewById(R.id.res_range);
		delivery_time = (TextView)findViewById(R.id.res_time);
		sale_count = (TextView)findViewById(R.id.sale_count);
		res_adress = (TextView)findViewById(R.id.res_adress);
		res_icon = (ImageView)findViewById(R.id.restaurant_icon);
		
		InitScreen();
		InitListView();
	
	}
	
	private void InitListView() {
		leftListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				rightAdapter.getResult();
				DataList list = new DataList();
				list.setList(new ArrayList(select.getList()));
				resultHashMap.put(lastItem, list);
				lastItem = arg2;
				
				leftSelectView = arg1;
				leftSelectView.setBackgroundColor(Color.WHITE);
				rightAdapter.setData(dishList.get(arg2), meal_sum.get(arg2));
			}
			
		});
		rightlist.setOnItemClickListener(new OnItemClickListener() {


			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
					long arg3) {
				LayoutInflater layoutInflater = LayoutInflater.from(RestaurantActivity.this);
				final View dialogView = layoutInflater.inflate(R.layout.dish_detail,
						null);
				ImageView imageView = (ImageView) dialogView
						.findViewById(R.id.yijianzhai_dialog_img);
				ImageLoader.getInstance().displayImage(LoadingActivity.
						URL+rightAdapter.getData().get(arg2).get("img"), imageView, options);
				TextView dish_detail_name = (TextView) dialogView.findViewById(R.id.yijianzhai_dialog_dishname);
				dish_detail_name.setText(rightAdapter.getData().get(arg2).get("name")+"");
				TextView dialogDishPrice = (TextView) dialogView
						.findViewById(R.id.yijianzhai_dialog_price);
				dialogDishPrice.setText(rightAdapter.getData().get(arg2).get("price")+"");
				TextView dialogDishDetail = (TextView) dialogView
						.findViewById(R.id.yijianzhai_dialog_detail);
				dialogDishDetail.setText(rightAdapter.getData().get(arg2).get("detail")+"");
				TextView add_to_yijianzhai = (TextView)dialogView.findViewById(R.id.add_to_yijianzhai);
				TextView add_new_yijianzhai = (TextView)dialogView.findViewById(R.id.add_new_yijianzhai);
				// 设置可以滚动
				dialogDishDetail.setMovementMethod(ScrollingMovementMethod
						.getInstance());
				final Dialog dialog = new Dialog(RestaurantActivity.this);
				// 设置无标题
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(dialogView);
				dialog.setCanceledOnTouchOutside(true);
				add_new_yijianzhai.setVisibility(View.VISIBLE);
				add_to_yijianzhai.setVisibility(View.VISIBLE);
				add_new_yijianzhai.setOnClickListener(new add_new_yijianzhaiListener(arg2, dialog));
				add_to_yijianzhai.setOnClickListener(new add_to_yijianzhaiListener(arg2, dialog));
				dialog.show();
			}
			
		});
	}
	

	public class add_new_yijianzhaiListener implements OnClickListener{
		
		private int arg2;
		private Dialog dialog;
		
		public add_new_yijianzhaiListener(int arg2, Dialog dialog){
			this.arg2 = arg2;
			this.dialog = dialog;
		}

		@Override
		public void onClick(View arg0) {
			final HashMap<String, Object> yijianzhai_dish_detail = rightAdapter.getData().get(arg2);
			LayoutInflater layoutInflater = LayoutInflater.from(RestaurantActivity.this);
			final View dialogView = layoutInflater.inflate(R.layout.add_newmealset,
					null);
			ListView listView = (ListView) dialogView
					.findViewById(R.id.dialog_add_new_meal_list);
			int meal_count = rightAdapter.getMealSum(arg2)==0?1:rightAdapter.getMealSum(arg2);
			OrderSureAdapter adapter = new OrderSureAdapter(RestaurantActivity.this,
					yijianzhai_dish_detail,meal_count);
			listView.setAdapter(adapter);

			Button makeSureAdd = (Button) dialogView
					.findViewById(R.id.dialog_add_new_meal_sure);
			makeSureAdd.setOnClickListener(this);
			final EditText yijianzhai_new_name = (EditText) dialogView
					.findViewById(R.id.yijianzhai_new_name);
			final TextView yijianzhai_makesure_sure_count = (TextView) dialogView
					.findViewById(R.id.yijianzhai_makesure_sure_count);
			TextView yijianzhai_makesure_send_price = (TextView) dialogView
					.findViewById(R.id.yijianzhai_makesure_send_price);
			TextView yijianzhai_makesure_sure_sum = (TextView) dialogView
					.findViewById(R.id.yijianzhai_makesure_sure_sum);
			yijianzhai_makesure_sure_count.setText(meal_count + "份外卖");
			yijianzhai_makesure_send_price.setText("配送费 ￥" + data.get("delivery_starting_fee"));
			yijianzhai_makesure_sure_sum.setText("￥" + ((Double)yijianzhai_dish_detail.get("price"))*
					meal_count);
			final Dialog add_new_yijianzhai = new Dialog(RestaurantActivity.this);
			// 设置无标题
			add_new_yijianzhai.requestWindowFeature(Window.FEATURE_NO_TITLE);
			add_new_yijianzhai.setContentView(dialogView);
			makeSureAdd.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					if(yijianzhai_new_name.getText().toString().equals("")){
						Toast.makeText(RestaurantActivity.this, "请填写套餐名称", Toast.LENGTH_SHORT).show();
						return;
					}else if(yijianzhai_makesure_sure_count.getText().toString().equals("0份外卖")){
						Toast.makeText(RestaurantActivity.this, "菜品份数不能为零", Toast.LENGTH_SHORT).show();
						return;
					}
					Dish yijianzhai_dish = new Dish();
					yijianzhai_dish.setDish_id((String) yijianzhai_dish_detail.get("dish_id"));
					yijianzhai_dish.setNumber((rightAdapter.getMealSum(arg2)==0?1:rightAdapter.getMealSum(arg2)));
					yijianzhai_dish.setPrice((Double) yijianzhai_dish_detail.get("price"));
					yijianzhai_dish.setName(yijianzhai_dish_detail.get("name")+"");
					yijianzhai_dish.setImg(yijianzhai_dish_detail.get("img")+"");
					yijianzhai_dish.setDetail(yijianzhai_dish_detail.get("detail").toString());
					ArrayList<Dish> tempList = new ArrayList<Dish>();
					tempList.add(yijianzhai_dish);
					Package package1 = new Package();
					package1.setDishes(tempList);
					package1.setRestaurant_id((String) data.get("resturant_id"));
					package1.setTotal_price(((Double)yijianzhai_dish_detail.get("price"))*
					(rightAdapter.getMealSum(arg2)==0?1:rightAdapter.getMealSum(arg2)));
					package1.setName(yijianzhai_new_name.getText().toString());
					package1.setPrice_on_delivery((Double) data.get("delivery_fee"));
					package1.setDelivery_starting_fee((Double) data.get("delivery_starting_fee"));
					new MealSetManageService().AddMealSet(HomePageActivity.UUID, 
							package1, new OnQueryCompleteListener() {
								
								@Override
								public void onQueryComplete(QueryId queryId, Object result, EHttpError error) {
									if(result == null){
										Toast.makeText(RestaurantActivity.this, "连接超时", Toast.LENGTH_SHORT)
											.show();
									}else if(result == "failed"){
										Toast.makeText(RestaurantActivity.this, "添加失败", Toast.LENGTH_SHORT)
										.show();
										add_new_yijianzhai.dismiss();
									}else{
										Toast.makeText(RestaurantActivity.this, "添加成功", Toast.LENGTH_SHORT)
										.show();
										add_new_yijianzhai.dismiss();
									}
								}
							});
				}
			});
			add_new_yijianzhai.setCanceledOnTouchOutside(true);
			add_new_yijianzhai.show();
			dialog.dismiss();
		}
		
	}
	
	public class add_to_yijianzhaiListener implements OnClickListener{
		
		private int arg2;
		private Dialog dialog;
		
		public add_to_yijianzhaiListener(int arg2, Dialog dialog){
			this.arg2 = arg2;
			this.dialog = dialog;
		}

		@Override
		public void onClick(View arg0) {
			LayoutInflater inflater = LayoutInflater.from(RestaurantActivity.this);
			View dialogView = inflater.inflate(R.layout.add_to_set, null);
			final ListView dialog_list = (ListView) dialogView.findViewById(R.id.dialog_add_to_meal);
			final Button dialog_sure = (Button) dialogView.findViewById(R.id.dialog_add_to_meal_sure);
			
			final Dialog add_to_yijianzhai = new Dialog(RestaurantActivity.this);
			// 设置无标题
			add_to_yijianzhai.requestWindowFeature(Window.FEATURE_NO_TITLE);
			add_to_yijianzhai.setContentView(dialogView);
			
			mealSetManageService = new MealSetManageService();
			mealSetManageService.GetAllMealSet(HomePageActivity.UUID, new OnQueryCompleteListener() {
				
				@Override
				public void onQueryComplete(QueryId queryId, Object result, EHttpError error) {
					dialog_sure.setClickable(false);
					if(result.equals("failed")){
						Toast.makeText(RestaurantActivity.this, "没有套餐，请添加新套餐", Toast.LENGTH_SHORT).show();
						
						return;
					}
					ArrayList<Package> get_result = (ArrayList<Package>) result;
					ArrayList<Package> add_set_package = new ArrayList<Package>();
					for(int i = 0; i < get_result.size(); ++i){
						if(data.get("resturant_id").equals(get_result.get(i).getRestaurant_id())){
							add_set_package.add(get_result.get(i));
						}
					}
					add_to_set_adapter = new AddToSetAdapter(RestaurantActivity.this, add_set_package);
					dialog_list.setAdapter(add_to_set_adapter);
					dialog_sure.setClickable(true);
				}
			});
			
			final Dish add_to_dish = new Dish();
			add_to_dish.setDish_id((String) rightAdapter.getData().get(arg2).get("dish_id"));
			add_to_dish.setName((String) rightAdapter.getData().get(arg2).get("name"));
			add_to_dish.setNumber(rightAdapter.getMealSum(arg2)==0?1:rightAdapter.getMealSum(arg2));
			add_to_dish.setPrice((Double) rightAdapter.getData().get(arg2).get("price")*
					(rightAdapter.getMealSum(arg2)==0?1:rightAdapter.getMealSum(arg2)));
			add_to_dish.setDetail(rightAdapter.getData().get(arg2).get("detail").toString());
			add_to_dish.setImg(rightAdapter.getData().get(arg2).get("img")+"");
			//add_to_dish.setSales((Integer) rightAdapter.getData().get(arg2).get("sales"));
			
			dialog_sure.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					if(add_to_set_adapter.getPackages() == "failed"){
						Toast.makeText(RestaurantActivity.this, "请选择要加入的套餐", Toast.LENGTH_SHORT)
						.show();
					}
					Package tempPackages = (Package)add_to_set_adapter.getPackages();
					
					mealSetManageService.DishAddPackage(HomePageActivity.UUID, 
							add_to_dish, tempPackages.getId(), new OnQueryCompleteListener() {
								@Override
								public void onQueryComplete(QueryId queryId, Object result, EHttpError error) {
									if(result == null){
										Toast.makeText(RestaurantActivity.this, "连接超时", Toast.LENGTH_SHORT)
											.show();
									}else if(result.equals("failed")){
										Toast.makeText(RestaurantActivity.this, "添加失败", Toast.LENGTH_SHORT)
										.show();
										add_to_yijianzhai.dismiss();
									}else if(result.equals("repeat")){
										Toast.makeText(RestaurantActivity.this, "该套餐中已包含该菜品", Toast.LENGTH_SHORT)
										.show();
										add_to_yijianzhai.dismiss();
									}else{
										Toast.makeText(RestaurantActivity.this, "添加成功", Toast.LENGTH_SHORT)
										.show();
										add_to_yijianzhai.dismiss();
									}
								}
							});
				}
			});
			
			dialog.dismiss();
			add_to_yijianzhai.setCanceledOnTouchOutside(true);
			add_to_yijianzhai.show();
		}
		
	}

	private void InitScreen() {
		Intent intent = getIntent();
		resturant_id = intent.getStringExtra("resturant_id");
		manager = new RestaurantManager();
		queryCompleteListener = new OnQueryCompleteListener() {
			
			@Override
			public void onQueryComplete(QueryId queryId, Object result, EHttpError error) {
				if (result == null) {
					Toast.makeText(RestaurantActivity.this, "操作超时",
							Toast.LENGTH_SHORT).show();
				} else if(result.equals("failed")){
					Toast.makeText(RestaurantActivity.this, "服务器连接失败",
							Toast.LENGTH_SHORT).show();
				} else {
					data = (HashMap<String, Object>) result;
					ImageLoader.getInstance().displayImage(LoadingActivity
							.URL+data.get("img"), res_icon, options);
					
					titleTextView.setText(data.get("name")+"");
					typeTextView.setText(data.get("name")+"");
					sale_count.setText(data.get("sales")+"");
					delivery_mode.setText(data.get("delivery_mode")+"");
					delivery_time.setText(data.get("start_time")+"-"+data.get("end_time"));
					res_adress.setText(data.get("address")+"");
					delivery_for.setText(data.get("delivery_fee")+"");
					
					delivery_fee = (Double) data.get("delivery_fee");
					baseprice = (Double) data.get("delivery_starting_fee");
					chamoney.setText("还差￥"+baseprice+"配送");
					changemoney(0, 0);
					dishList = new ArrayList<ArrayList<HashMap<String,Object>>>();
					dishClassify = new ArrayList<String>();
					
					HashMap<String, Object> tempMap = (HashMap<String, Object>) data.get("dishes");
					Set<String> tempSet = tempMap.keySet();
					System.out.println(tempSet.toString());
					int flag = 0;
					for(String key : tempSet){
						dishList.add((ArrayList<HashMap<String, Object>>) tempMap.get(key));
						dishClassify.add(key);
						meal_sum.put(flag, new HashMap<Integer, Integer>());
						++flag;
					}
					
					leftListView.setAdapter(new ArrayAdapter<String>(RestaurantActivity.this,
							R.layout.mapitem, R.id.mapitem, dishClassify));
					leftListView.setVisibility(View.GONE);
					rightlist.setVisibility(View.GONE);
					Handler handler = new Handler(){

						@Override
						public void handleMessage(Message msg) {
							if(msg.what == 0x123){
								leftListView.setVisibility(View.VISIBLE);
								rightlist.setVisibility(View.VISIBLE);
								leftSelectView = leftListView.getChildAt(0);
							}
						}
						
					};
					
					handler.sendEmptyMessageDelayed(0x123, 1000);
					
					rightAdapter = new RestDishAdapter(RestaurantActivity.this, 
							dishList.get(0), select, meal_sum.get(0));
					rightlist.setAdapter(rightAdapter);
				}
			}
		};
		manager.GetDishManager(resturant_id, queryCompleteListener);

	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@SuppressLint("NewApi")
	public void changemoney(int n, double m) {
		 totalLayout.setVisibility(View.VISIBLE);
		totalLayout.setAlpha((float) 0.9);
		
		money += n * m;
		num += n;
		
		pricenum.setText(" ￥ " + money);
		if (money < baseprice) {
			//delivery_for.setText("5元");
			chamoney.setVisibility(View.VISIBLE);
			order.setVisibility(View.GONE);
			int cha = (int) (baseprice - money);
			chamoney.setText("还差 ￥ " + cha + "起送");
		} else {
			//delivery_for.setText("0元");
			chamoney.setVisibility(View.GONE);
			order.setVisibility(View.VISIBLE);
		}

	}
	
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.res_img_return:
			finish();
			break;
		case R.id.btn_detail:
			Intent intent=new Intent();
			intent.setAction(Intent.ACTION_DIAL);   //android.intent.action.DIAL
			//该处从服务器获得商店联系电话
			intent.setData(Uri.parse("tel:"+data.get("mobile")));
			startActivity(intent);
			break;
		case R.id.btn_makeorder:
			if(RestaurantActivity.this.getIntent().getBooleanExtra("ismodifyYJZ", false)){
				rightAdapter.getResult();
				Package yjzPackage = new Package();
				List<Map<String, Object>> list = select.getList();
				List<Dish> dishs = new ArrayList<Dish>();
				for (int i = 0; i < list.size(); i++) {
					Dish dish = new Dish();
					dish.setDish_id(list.get(i).get("childId").toString());
					dish.setName(list.get(i).get("childName").toString());
					dish.setNumber(Integer.valueOf(list.get(i).get("childNumber")
							.toString()));
					dish.setPrice(Double.valueOf(list.get(i).get("childPrice")
							.toString()));
					dish.setImg(list.get(i).get("childImg").toString());
					dish.setDetail(list.get(i).get("detail").toString());
					dishs.add(dish);
				}
				yjzPackage.setDishes(dishs);
				yjzPackage.setName("自定义套餐1");
				yjzPackage.setPrice_on_delivery((Double) data.get("delivery_starting_fee"));
				yjzPackage.setRestaurant_id((String) data.get("resturant_id"));
				yjzPackage.setTotal_price(money);
				yjzPackage.setDelivery_starting_fee((Double) data.get("delivery_starting_fee"));
				MealSetManageService yjzService = new MealSetManageService();
				yjzService.AddMealSet(HomePageActivity.UUID, 
						yjzPackage, new OnQueryCompleteListener() {
							
							@Override
							public void onQueryComplete(QueryId queryId, Object result, EHttpError error) {
								if(result == null){
									Toast.makeText(RestaurantActivity.this, "连接超时", Toast.LENGTH_SHORT).show();
								}else if(result == "failed"){
									Toast.makeText(RestaurantActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
								}else if(result == "success"){
									Toast.makeText(RestaurantActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
								}
								finish();
							}
						});
			}else{
				Intent detailIntent = new Intent(this,YiJianZhaiMakeSureActivity.class);
				DataMap dataMap = new DataMap();
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.put("resId", data.get("resturant_id"));
				tempMap.put("sum", money);
				tempMap.put("count", num);
				tempMap.put("sendPrice", delivery_fee);
				tempMap.put("basePrice", baseprice);
				dataMap.setMap(tempMap);
				rightAdapter.getResult();
				Set<Integer> set = resultHashMap.keySet();
				List<Map<String, Object>> list = select.getList();
				for(int key : set){
					list.addAll(resultHashMap.get(key).getList());
				}
				select.setList(list);
				detailIntent.putExtra("data", select);
				detailIntent.putExtra("map", dataMap);
				startActivity(detailIntent);
			}
			
			break;
		default:
			break;
		}
	}
}