package com.yijianzhai.jue.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.yijianzhai.jue.R;
import com.yijianzhai.jue.adapter.ShakeDialogAdapter;
import com.yijianzhai.jue.connection.HttpUtils.EHttpError;
import com.yijianzhai.jue.model.DataList;
import com.yijianzhai.jue.model.DataMap;
import com.yijianzhai.jue.model.Dish;
import com.yijianzhai.jue.model.ShakeListener;
import com.yijianzhai.jue.model.ShakeListener.OnShakeListener;
import com.yijianzhai.jue.service.OnQueryCompleteListener;
import com.yijianzhai.jue.service.QueryId;
import com.yijianzhai.jue.service.ShakePackageService;
import com.yijianzhai.jue.service.Utils;

@SuppressLint({ "ValidFragment", "HandlerLeak" })
public class ShakeActivity extends Activity implements OnClickListener,
		OnQueryCompleteListener {

	private ImageView shakeIcon;
	// 弹出的dialog
	private Dialog dialog;
	private ShakeListener shakeListener;
	// 记录是否可以继续发请求
	private boolean flag = false;
	Vibrator mVibrator;
	AnimationDrawable anim;
	private CountDownTimer countDownTimer;

	private com.yijianzhai.jue.model.Package dataPackage;
	
	//标记网络状态，振动结束后根据该值显示信息
	private int showFlag = 0;

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(countDownTimer != null)
			countDownTimer.cancel();
		if(shakeListener != null){
			shakeListener.stop();
		}
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		dialog = new Dialog(this);
		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams dlp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);

		WindowManager wm = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);

		int width = wm.getDefaultDisplay().getWidth();
		int height = wm.getDefaultDisplay().getHeight();

		dlp.width = width * 4 / 5; // 宽度
		dlp.height = width * 7 / 20; // 高度
		dlp.alpha = (float) 1.;

		setContentView(R.layout.shake);
		RelativeLayout rll_border = (RelativeLayout) findViewById(R.id.shake_border);
		RelativeLayout rll = (RelativeLayout) findViewById(R.id.head_shake);
		RelativeLayout rll_main = (RelativeLayout) findViewById(R.id.shake_main);
		shakeIcon = (ImageView) findViewById(R.id.img_shake_icon);
		shakeIcon.setOnClickListener(this);
		ImageView shakebowl = (ImageView) findViewById(R.id.img_shake_bowl);
		ImageView shakecate = (ImageView) findViewById(R.id.img_shake_cake);
		ImageView back = (ImageView) findViewById(R.id.img_shake_back);
		back.setOnClickListener(this);
		ImageView bottom = (ImageView) findViewById(R.id.img_shake_bottom);
		View v = (View) findViewById(R.id.view_shake_space);

		Bitmap bm = BitmapFactory.decodeResource(getResources(),
				R.drawable.shake_background);
		Bitmap bm1 = Bitmap.createBitmap(bm, 0, 0, width,
				height - rll.getLayoutParams().height);
		Drawable bd = new BitmapDrawable(getResources(), bm1);
		rll_main.setBackgroundDrawable(bd);

		v.getLayoutParams().height = width / 8;

		shakeIcon.getLayoutParams().height = width * 13 / 36;
		shakeIcon.getLayoutParams().width = width * 13 / 36;
		shakeIcon.setImageResource(R.drawable.shake_icon2);

		rll_border.setBackgroundResource(R.drawable.shake_border);
		RelativeLayout.LayoutParams lp = (LayoutParams) rll_border
				.getLayoutParams();
		lp.height = width / 2;
		lp.width = width / 2;

		bottom.setBackgroundResource(R.drawable.shake_bottom);
		bottom.getLayoutParams().height = width / 3;

		shakebowl.getLayoutParams().height = width * 12 / 36;
		shakebowl.getLayoutParams().width = width * 12 / 36;
		shakebowl.setImageResource(R.drawable.shake_bowl);
		shakebowl.bringToFront();

		shakecate.getLayoutParams().height = width * 12 / 36;
		shakecate.getLayoutParams().width = width * 12 / 36;
		shakecate.setImageResource(R.drawable.shake_cake);
		// 将控件至于上层
		shakecate.bringToFront();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (mVibrator == null)
			mVibrator = (Vibrator) this.getApplicationContext()
					.getSystemService(Context.VIBRATOR_SERVICE);

		if (shakeListener == null)
			shakeListener = new ShakeListener(this);
		shakeListener.setOnShakeListener(new MyShakeListener());
	}

	public void startVibrato() { // 定义震动

		mVibrator.vibrate(new long[] { 100, 100, 100, 1000 }, -1); 
		// 第一个｛｝里面是节奏数组，
		// // 第二个参数是重复次数，-1为不重复，非-1俄日从pattern的指定下标开始重复
	}

	public void doAction() {
		// 开始震动
		startVibrato();
		dialog.dismiss();
		shakeIcon.setImageResource(R.drawable.shake_animation);
		anim = (AnimationDrawable) shakeIcon.getDrawable();
		anim.start();
	}

	public class MyShakeListener implements OnShakeListener {

		public void onShake() {
			// TODO Auto-generated method stub
			shake();
		}

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.img_shake_back:
			finish();
			break;
		case R.id.img_shake_icon:
			shake();
			break;
		case R.id.shake_dialog_makesure:
			Intent intent = new Intent();
			DataMap map = new DataMap();
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("groupName", dataPackage.getName());
			dataMap.put("groupPrice", dataPackage.getTotal_price());
			dataMap.put("sendPrice", dataPackage.getPrice_on_delivery());
			dataMap.put("type", dataPackage.getCategory());
			dataMap.put("resId", dataPackage.getRestaurant_id());
			dataMap.put("pacId", dataPackage.getId());
			dataMap.put("basePrice", dataPackage.getDelivery_starting_fee());
			map.setMap(dataMap);
			DataList dataList = new DataList();
			intent.putExtra("map", map);
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
			dataList.setList(child);
			intent.putExtra("data", dataList);
			intent.setClass(this, YiJianZhaiMakeSureActivity.class);
			this.startActivity(intent);
			if (dialog != null) {
				dialog.dismiss();
			}
			finish();
			break;
		case R.id.shake_dialog_continue:
			dialog.dismiss();
			shake();
			break;
		}
	}

	// 判断是否震动的函数
	private void shake() {
		// TODO Auto-generated method stub
		if (flag == false) {
			flag = true;
			if (mVibrator != null) {
				countDownTimer = new CountDownTimer(1500, 500) {
					public void onTick(long millisUntilFinished) {

					}

					public void onFinish() {
						flag = false;
						anim.stop();
						if (mVibrator != null) {
							mVibrator.cancel();
						}
						countDownTimer.cancel();
						if(showFlag == 0x121){
							dialog.show();
						}else if(showFlag == 0x122){
							Toast.makeText(ShakeActivity.this, "摇不出套餐哦，亲", Toast.LENGTH_LONG).show();
						}else{
							Toast.makeText(ShakeActivity.this, "网络连接超时", Toast.LENGTH_LONG).show();
						}
					}
				}.start();
				// 通信
				new ShakePackageService().ShakePackage(Utils.LATITUDE, Utils.LONGITUDE,
						Utils.CITY, ShakeActivity.this);
				doAction();
			}

		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onQueryComplete(QueryId queryId, Object result, EHttpError error) {
		// TODO Auto-generated method stub
		if (result != null) {
			if (!result.equals("failed")) {
				showFlag = 0x121;
				dataPackage = (com.yijianzhai.jue.model.Package) result;
				LayoutInflater layoutInflater = LayoutInflater.from(this);
				final View dialogView = layoutInflater.inflate(
						R.layout.shake_dialog, null);
				ShakeDialogAdapter shakeDialogAdapter = new ShakeDialogAdapter(
						dataPackage, this);
				TextView textView = (TextView) dialogView
						.findViewById(R.id.shake_dialog_package_name);
				TextView totol_price = (TextView) dialogView
						.findViewById(R.id.total_price);
				textView.setText(dataPackage.getName());
				totol_price.setText("￥" + dataPackage.getTotal_price());
				ListView listView = (ListView) dialogView
						.findViewById(R.id.list_shake_dialog_dish);
				Button makeSure = (Button) dialogView
						.findViewById(R.id.shake_dialog_makesure);
				Button continueShake = (Button) dialogView
						.findViewById(R.id.shake_dialog_continue);
				// 跳到下单界面
				listView.setAdapter(shakeDialogAdapter);
				makeSure.setOnClickListener(this);
				continueShake.setOnClickListener(this);
				dialog = new Dialog(this);
				// 设置无标题
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(dialogView);
				dialog.setCanceledOnTouchOutside(true);
				Window dialogWindow = dialog.getWindow();
				WindowManager.LayoutParams dlp = dialogWindow.getAttributes();
				dialogWindow.setGravity(Gravity.CENTER);

				WindowManager wm = (WindowManager) this
						.getSystemService(Context.WINDOW_SERVICE);

				int width = wm.getDefaultDisplay().getWidth();
				dlp.width = width * 4 / 5; // 宽度
				dlp.height = dlp.width *7/5; // 高度
				dlp.alpha = (float) 1.;
				dialog.getWindow().setAttributes(dlp);
			} else {
				showFlag = 0x122;
				//Toast.makeText(this, "摇不出套餐哦，亲", Toast.LENGTH_LONG).show();
			}
		} else {
			showFlag = 0x123;
			//Toast.makeText(this, "网络连接超时", Toast.LENGTH_LONG).show();
		}
	}
	
	

}
