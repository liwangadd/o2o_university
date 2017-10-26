package com.yijianzhai.jue.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.yijianzhai.jue.R;
import com.yijianzhai.jue.adapter.DeleveryAddressListViewAdapter;
import com.yijianzhai.jue.connection.HttpUtils.EHttpError;
import com.yijianzhai.jue.model.Address;
import com.yijianzhai.jue.model.AddressForAndroid;
import com.yijianzhai.jue.service.AddressManageService;
import com.yijianzhai.jue.service.OnQueryCompleteListener;
import com.yijianzhai.jue.service.QueryId;
import com.yijianzhai.jue.service.Utils;
import com.yijianzhai.jue.utils.Mylocation;
import com.yijianzhai.jue.utils.Mylocation.InitSreen;

public class DeleveryAddressActivity extends Activity implements
		OnClickListener, OnQueryCompleteListener, InitSreen{

	// 返回上一级
	private ImageView backButton;
	// 数据源
	private static List<Address> listData;
	// list控件
	private ListView addressListView;

	private DeleveryAddressListViewAdapter mAddressListViewAdapter;
	// 新增收货地址
	private Button newAddress;
	private ImageView adress_edit;

	// 后台通信接口
	private AddressManageService service;
	// 记录删除的位置
	private int delPosition = -1;

	// 是否是编辑状态
	private boolean isEdit = false;

	private ProgressBar progressBar;
	
	//定位当前位置
	private Button local_address;
	
	//定位接口
	private Mylocation mLocation;

	public ProgressBar getProgressBar() {
		return progressBar;
	}

	public void setProgressBar(ProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.delevery_address);
		mLocation = new Mylocation(DeleveryAddressActivity.this);
		mLocation.setOnFinishListener(this);
	}

	public int getDelPosition() {
		return delPosition;
	}

	public void setDelPosition(int delPosition) {
		this.delPosition = delPosition;
	}

	public void UpdateEdit(boolean isEdit) {
		this.isEdit = isEdit;
		if (this.isEdit == false) {
			adress_edit.setBackgroundResource(R.drawable.edit);
		} else {
			adress_edit.setBackgroundResource(R.drawable.cancelbutton);
		}
	}

	private void Init() {
		// TODO Auto-generated method stub

		newAddress = (Button) findViewById(R.id.delevery_new_address_button);
		// 手动滑动到最顶端
		backButton = (ImageView) findViewById(R.id.usercenter_delevery_backup);
		adress_edit = (ImageView) findViewById(R.id.adress_edit);
		local_address = (Button) findViewById(R.id.local_address);
		local_address.setOnClickListener(this);
		adress_edit.setOnClickListener(this);
		backButton.setOnClickListener(this);
		newAddress.setOnClickListener(this);
		addressListView = (ListView) findViewById(R.id.usercenter_delevery_address_listview);
		progressBar = (ProgressBar) findViewById(R.id.probar_yijian);
		listData = new ArrayList<Address>();
		service = new AddressManageService();
		mAddressListViewAdapter = new DeleveryAddressListViewAdapter(
				DeleveryAddressActivity.this, listData);
		addressListView.setAdapter(mAddressListViewAdapter);
		progressBar.setVisibility(View.VISIBLE);
		service.UserGetAddress(HomePageActivity.UUID, this);

	}

	public void backToOrder(Intent intent) {
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Init();
		local_address.setText("定位到当前位置");
		UpdateEdit(false);
		// communicate();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.usercenter_delevery_backup:
			finish();
			break;
		case R.id.delevery_new_address_button:
			Intent intent = new Intent(DeleveryAddressActivity.this,
					NewActivity.class);
			AddressForAndroid.name = "";
			startActivity(intent);
			break;
		case R.id.adress_edit:
			mAddressListViewAdapter.setFlag();
			break;
		case R.id.local_address:
			local_address.setText("正在定位...");
			mLocation.showMylocation();
			break;
		default:
			break;
		}
	}

	@Override
	public void UpdateScreen() {
		AddressForAndroid.address = Utils.ADDRESS;
		AddressForAndroid.name = "name";
		AddressForAndroid.isChange = false;
		Intent intent = new Intent(DeleveryAddressActivity.this, NewActivity.class);
		startActivity(intent);
	}

	@Override
	public void onQueryComplete(QueryId queryId, Object result, EHttpError error) {
		// TODO Auto-generated method stub
		progressBar.setVisibility(View.GONE);
		if (result != null) {
			if (queryId == AddressManageService.changeDefaultAddress) {
				if (!result.equals("failed")) {
					Toast.makeText(this, "修改默认地址成功", Toast.LENGTH_LONG).show();
					finish();
				} else {
					Toast.makeText(this, "修改默认地址失败", Toast.LENGTH_LONG).show();
				}
			} else if (queryId == AddressManageService.getAddress) {
				if (!result.equals("failed")) {
					listData = (List<Address>) result;
					System.out.println(listData);
					mAddressListViewAdapter.setDataList(listData);
					mAddressListViewAdapter.notifyDataSetChanged();
				} else {
					Toast.makeText(this, "您还没有添加地址", Toast.LENGTH_LONG).show();
				}
			} else if (queryId == AddressManageService.delAddress) {
				if (!result.equals("failed")) {

					Toast.makeText(this, "地址删除成功", Toast.LENGTH_SHORT).show();
					if (delPosition != -1) {
						this.listData.remove(delPosition);
					}
					delPosition = -1;
					mAddressListViewAdapter.notifyDataSetChanged();

				} else {
					Toast.makeText(this, "地址删除失败", Toast.LENGTH_LONG).show();
				}
			}
		} else {
			Toast.makeText(this, "网络连接失败", Toast.LENGTH_LONG).show();
		}
	}
}
