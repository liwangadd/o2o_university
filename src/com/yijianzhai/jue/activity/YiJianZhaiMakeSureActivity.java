package com.yijianzhai.jue.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yijianzhai.jue.R;
import com.yijianzhai.jue.adapter.YiJianZhaiMakeSureAdapter;
import com.yijianzhai.jue.connection.HttpUtils.EHttpError;
import com.yijianzhai.jue.model.DataAddress;
import com.yijianzhai.jue.model.DataList;
import com.yijianzhai.jue.model.DataMap;
import com.yijianzhai.jue.model.Dish;
import com.yijianzhai.jue.model.Order;
import com.yijianzhai.jue.service.AddressManageService;
import com.yijianzhai.jue.service.OnQueryCompleteListener;
import com.yijianzhai.jue.service.OrderManageService;
import com.yijianzhai.jue.service.QueryId;
import com.yijianzhai.jue.utils.InitWedget;

public class YiJianZhaiMakeSureActivity extends Activity implements InitWedget,
		OnClickListener, OnQueryCompleteListener{

	// 具体的菜链表
	private List<Map<String, Object>> dataList;
	private ListView listView;
	// 套餐信息
	private Map<String, Object> dataPackage;
	private List<com.yijianzhai.jue.model.Address> addresses;
	private YiJianZhaiMakeSureAdapter yiJianZhaiMakeSureAdapter;
	private Dialog dialog;
	private ProgressBar progressBar;

	// 返回键
	private ImageView back;
	// 收货人
	private TextView person;
	// 联系电话
	private TextView phone;
	// 详细地址
	private TextView address;
	// 下单按键
	private Button makeSure;
	// 配送费
	private TextView sendPrice;
	// 起送费
	private double basePrice;
	// 外卖份数
	private TextView number;
	// 总价钱
	private TextView sumPrice;
	// 附加说明
	private EditText addition;
	private LinearLayout linearLayout;
	private String addressId;
	private double sum = 0;
	
	public List<Map<String, Object>> getDataList() {
		return dataList;
	}

	public void setDataList(List<Map<String, Object>> dataList) {
		this.dataList = dataList;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yijianzhai_makesure);
		initwedget();
		getData();
	}
	
	public ProgressBar getProgressBar() {
		return progressBar;
	}

	public void setProgressBar(ProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	private void getData() {
		// TODO Auto-generated method stub
		// 获得点餐的数据
		Intent intent = this.getIntent();
		DataList data = (DataList) intent.getSerializableExtra("data");
		dataPackage = ((DataMap) intent.getSerializableExtra("map")).getMap();
		// String IsRecommed = intent.getStringExtra("isRecommed");
		dataList = data.getList();
		yiJianZhaiMakeSureAdapter = new YiJianZhaiMakeSureAdapter(dataList,
				this);
		// yiJianZhaiMakeSureAdapter.setIsRecommed(IsRecommed);
		listView.setAdapter(yiJianZhaiMakeSureAdapter);

		// 获得总价，配送费，份数等数据
		int number = dataList.size();
		basePrice = (Double) dataPackage.get("basePrice");
		double sendPrice = Double.valueOf(dataPackage.get("sendPrice")
				.toString());
		double sumPrice = 0;
		for (int i = 0; i < dataList.size(); i++) {
			sumPrice += Double.valueOf(dataList.get(i).get("childNumber")
					.toString())
					* Double.valueOf(dataList.get(i).get("childPrice")
							.toString());
		}
		this.updateDate(sendPrice + "", number + "", sumPrice + "");
		yiJianZhaiMakeSureAdapter.setNumber(number);
		yiJianZhaiMakeSureAdapter.setSendPrice(sendPrice);
		yiJianZhaiMakeSureAdapter.setSumPrice(sumPrice);
	}

	@Override
	public void initwedget() {
		// TODO Auto-generated method stub
		back = (ImageView) findViewById(R.id.yijianzhai_makesure_img_ordersure_back);
		person = (TextView) findViewById(R.id.yijianzhai_makesure_sure_name);
		phone = (TextView) findViewById(R.id.yijianzhai_makesure_sure_phone);
		address = (TextView) findViewById(R.id.yijianzhai_makesure_sure_adress);
		linearLayout = (LinearLayout) findViewById(R.id.yijianzhai_makesure_ordersure);
		makeSure = (Button) findViewById(R.id.btn_ordersure);
		progressBar = (ProgressBar) findViewById(R.id.probar_yijian);
		sendPrice = (TextView) findViewById(R.id.yijianzhai_makesure_send_price);
		number = (TextView) findViewById(R.id.yijianzhai_makesure_sure_count);
		sumPrice = (TextView) findViewById(R.id.yijianzhai_makesure_sure_sum);
		addition = (EditText) findViewById(R.id.yijianzhai_makesure_addition);
		listView = (ListView) findViewById(R.id.yijianzhai_makesure_ordersure_list);
		back.setOnClickListener(this);
		linearLayout.setOnClickListener(this);
		makeSure.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.yijianzhai_makesure_img_ordersure_back:
			finish();
			break;
		case R.id.yijianzhai_makesure_ordersure:
			Intent intent = new Intent();
			intent.putExtra("address", new DataAddress(addresses));
			startActivity(intent.setClass(this, DeleveryAddressActivity.class));
			break;
		// 下单
		case R.id.btn_ordersure:
			// 通知下单
			if (dataList.size() == 0) {
				Toast.makeText(this, "没有菜品不能下单", Toast.LENGTH_LONG).show();
				break;
			}else if(sum < basePrice){
				Toast.makeText(this, "价格低于起送价", Toast.LENGTH_LONG).show();
				break;
			}
			Order order = new Order();
			order.setCondition(addition.getText().toString());
			order.setPrice_on_delivery(Double.valueOf(dataPackage.get(
					"sendPrice").toString()));
			order.setRestaurantId(dataPackage.get("resId").toString());
			order.setTotal_money(this.sum);
			order.setStatus("0");
			if (addressId != null) {
				order.setAddressId(addressId);
			} else {
				Toast.makeText(this, "点击添加收货地址", Toast.LENGTH_LONG).show();
				break;
			}
			List<Map<String, Object>> list = yiJianZhaiMakeSureAdapter
					.getDataList();
			List<Dish> dishs = new ArrayList<Dish>();
			for (int i = 0; i < list.size(); i++) {
				Dish dish = new Dish();
				dish.setDish_id(list.get(i).get("childId").toString());
				dish.setName(list.get(i).get("childName").toString());
				dish.setNumber(Integer.valueOf(list.get(i).get("childNumber")
						.toString()));
				dish.setPrice(Double.valueOf(list.get(i).get("childPrice")
						.toString()));
				dish.setImg(list.get(i).get("childImg")+"");
				dishs.add(dish);
			}
			order.setDishes(dishs);
			System.out.println(order.toString());
			progressBar.setVisibility(View.VISIBLE);
			new OrderManageService().UserZhaiMakeSure(HomePageActivity.UUID,
					order, this);
			break;
		case R.id.make_sure_dialog_query_order:

			this.startActivity(new Intent().setClass(this,
					QueryOrderActivity.class));
			if (dialog != null) {
				dialog.dismiss();
			}
			break;
		case R.id.make_sure_dialog_query_back:
			Intent backIntent= new Intent(YiJianZhaiMakeSureActivity.this, HomePageActivity.class);
			backIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			//backIntent.putExtra("flag", true);
			this.startActivity(backIntent);
			if (dialog != null) {
				dialog.dismiss();
			}
			break;
		default:
			break;
		}
	}

	// 更新最下方三个text的回调函数
	public void updateDate(String sendPrice, String number, String sumPrice) {
		this.sum = Double.valueOf(sumPrice);
		this.sendPrice.setText("配送费" + sendPrice);
		this.number.setText(number + "份外卖");
		this.sumPrice.setText("￥" + sum);
	}

	@Override
	public void onQueryComplete(QueryId queryId, Object result, EHttpError error) {
		// TODO Auto-generated method stub
		progressBar.setVisibility(View.GONE);
		if (result != null) {
			if (queryId == OrderManageService.ZHAIMAKESURE) {
				if (result.equals("success")) {
					// 弹出对话框
					LayoutInflater layoutInflater = LayoutInflater.from(this);
					final View dialogView = layoutInflater.inflate(
							R.layout.make_sure, null);
					Button queryButton = (Button) dialogView
							.findViewById(R.id.make_sure_dialog_query_order);
					Button backButton = (Button) dialogView
							.findViewById(R.id.make_sure_dialog_query_back);
					queryButton.setOnClickListener(this);
					backButton.setOnClickListener(this);
					dialog = new Dialog(this);
					// 设置无标题
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(dialogView);
					dialog.setCanceledOnTouchOutside(false);
					dialog.show();
				}
			} else if (queryId == AddressManageService.getAddress) {
				if (!result.equals("failed")) {
					this.addresses = (List<com.yijianzhai.jue.model.Address>) result;
					for (int i = 0; i < addresses.size(); i++) {
						if (addresses.get(i).getCategory() == 1) {
							person.setText(addresses.get(i).getName()
									.toString());
							phone.setText(addresses.get(i).getMobile()
									.toString());
							address.setText(addresses.get(i).getAddress()
									.toString());
							this.addressId = addresses.get(i).getAddress_id()
									.toString();
							break;
						}
					}
				} else {
					Toast.makeText(this, "收货地址为空", Toast.LENGTH_LONG).show();
				}
			}
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// 获得地址数据
		new AddressManageService().UserGetAddress(HomePageActivity.UUID, this);

	}

}
