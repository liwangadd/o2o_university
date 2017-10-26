package com.yijianzhai.jue.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.utils.L;
import com.yijianzhai.jue.R;
import com.yijianzhai.jue.connection.HttpUtils.EHttpError;
import com.yijianzhai.jue.model.User;
import com.yijianzhai.jue.service.HomePageService;
import com.yijianzhai.jue.service.OnQueryCompleteListener;
import com.yijianzhai.jue.service.QueryId;
import com.yijianzhai.jue.service.UserManageService;
import com.yijianzhai.jue.utils.AsyncImageLoader;
import com.yijianzhai.jue.utils.CreateUUID;
import com.yijianzhai.jue.utils.GetImgeLoadOption;

public class HomePageActivity extends Activity implements OnClickListener,
		OnQueryCompleteListener {

	public static User user;
	private ViewPager logo;
	private Button home_outer, home_user_center, home_shake, home_ofen,
			home_home;
	//private ListView home_listview;
	public static ArrayList<Map<String, Object>> listData;
	//private HompageListViewAdapter hompageListViewAdapter;
	public static int imageCount = 3;
	private ArrayList<ImageView> images = new ArrayList<ImageView>();
	private int currentImage = 0;
	private Handler handler;
	private ViewGroup viewGroup;
	private ImageView[] dosViews;
	
	//优惠特卖图片
	private ImageView preferential, special_selling;

	// 首页的图片url链接
	private List<String> urlStrings;
	private List<String> WebUrl;
	// UUID
	public static String UUID = "";
	// 环境变量
	public static Context context;
	// public static boolean isFirstAddAddress = false;

	// listview item的最小高度
	public static int HEIGHT = 256;
	// 是否是已经登录状态
	public static boolean isLogin = false;
	// 提示用户是否要退出软件
	private boolean isFinish;
	private Timer timer = new Timer();
	
	private AsyncImageLoader asyncImageLoader;
	
	private static final String TEST_FILE_NAME = "Universal Image Loader @#&=+-_.,!()~'%20.png";
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homepage);
		// 设置环境变量
		context = this;
		// 初始化
		asyncImageLoader = new AsyncImageLoader();
		images = new ArrayList<ImageView>();
		
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.advertisement)
		.showImageForEmptyUri(R.drawable.advertisement)
		.showImageOnFail(R.drawable.advertisement)
		.cacheInMemory(true)
		.cacheOnDisk(false)
		.considerExifParams(true)
		.displayer(new RoundedBitmapDisplayer(0))
		.build();
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));
		File testImageOnSdCard = new File("/mnt/sdcard", TEST_FILE_NAME);
		if (!testImageOnSdCard.exists()) {
			copyTestImageToSdCard(testImageOnSdCard);
		}
		
		for (int i = 0; i < imageCount; i++) {
			ImageView imageView = new ImageView(HomePageActivity.this);
			ViewGroup.LayoutParams params = new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			imageView.setLayoutParams(params);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			imageView.setImageResource(R.drawable.advertisement);
			images.add(imageView);
		}
		init();
	}

	private void init() {

		SharedPreferences sharedPreferences = getSharedPreferences("user_id",
				MODE_PRIVATE);
		UUID = sharedPreferences.getString("mobile", "");
		if (UUID.equals("")) {
			UUID = sharedPreferences.getString("user_id", "");
			// boolean IsOnline = sharedPreferences
			// .getBoolean("user_login", false);
			if (UUID.equals("")) {
				UUID = CreateUUID.getUUID();
				new UserManageService().UserResgisterWithoutName(UUID, this);
			}
		} else {
			// 用户已经登录
			isLogin = true;
		}
		System.out.println(UUID);

		listData = new ArrayList<Map<String, Object>>();

		logo = (ViewPager) findViewById(R.id.homepage_logo);

		logo.setOnPageChangeListener(new HomePagerListener());

		viewGroup = (ViewGroup) findViewById(R.id.viewGroup);
		dosViews = new ImageView[images.size()];

		home_outer = (Button) findViewById(R.id.homepage_outer);
		home_user_center = (Button) findViewById(R.id.homepage_user_center);
		home_shake = (Button) findViewById(R.id.homepage_shake);
		home_ofen = (Button) findViewById(R.id.homepage_ofen);
		home_home = (Button) findViewById(R.id.homepage_home);
		preferential = (ImageView) findViewById(R.id.preferential);
		special_selling = (ImageView) findViewById(R.id.special_selling);

		home_ofen.setOnClickListener(this);
		home_outer.setOnClickListener(this);
		home_shake.setOnClickListener(this);
		home_home.setOnClickListener(this);
		home_user_center.setOnClickListener(this);
		preferential.setOnClickListener(this);
		special_selling.setOnClickListener(this);

		// 获得url数组
		//new HomePageService().GetHomeInfo(this);
		initIntent();

		for (int i = 0; i < images.size(); i++) {
			ImageView imageView = new ImageView(this);
			imageView.setLayoutParams(new LayoutParams(4, 4));
			dosViews[i] = imageView;
			if (i == 0) {
				dosViews[i].setBackgroundResource(R.drawable.leader_white);
			} else {
				dosViews[i].setBackgroundResource(R.drawable.leader_black);
			}

			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
			layoutParams.leftMargin = 5;
			layoutParams.rightMargin = 5;
			viewGroup.addView(imageView, layoutParams);
		}
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);

		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 0x123) {
					logo.setCurrentItem(currentImage);
				}
			}

		};

		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				handler.sendEmptyMessage(0x123);
			}
		}, 0, 5000);
	}

	private void initIntent() {
		Intent intent = getIntent();
		//boolean flag = intent.getBooleanExtra("flag", false);
		//imageAddUrl();
		/*if(flag){
			new HomePageService().GetHomeInfo(this);
			imageAddUrl();
			logo.setAdapter(new HomePagerAdapter(images));
			return;
		}*/
		urlStrings = (List<String>) intent.getSerializableExtra("UrlString");
		WebUrl = (List<String>) intent.getSerializableExtra("WebUrl");
		if(urlStrings == null){
			//new HomePageService().GetHomeInfo(this);
			SharedPreferences preferences = getSharedPreferences("url", MODE_PRIVATE);
			urlStrings = new ArrayList<String>();
			WebUrl = new ArrayList<String>();
			for(int i = 0; i < imageCount; ++i){
				urlStrings.add(preferences.getString("urlString" + i, null));
				WebUrl.add(preferences.getString("webUrl" + i, null));
			}
			logo.setAdapter(new HomePagerAdapter(images));
		}else if(urlStrings.size() < imageCount){
			new HomePageService().GetHomeInfo(this);
		}
		else{
			SharedPreferences preferences = getSharedPreferences("url", MODE_PRIVATE);
			Editor editor = preferences.edit();
			for(int i = 0; i < imageCount; ++i){
				editor.putString("urlString" + i, urlStrings.get(i));
				editor.putString("webUrl", WebUrl.get(i));
			}
			editor.commit();
			imageAddUrl();
			logo.setAdapter(new HomePagerAdapter(images));
		}
	}

	private void imageAddUrl() {
		for(int i = 0; i < imageCount; ++ i){
			final String urlString = WebUrl.get(i);
			images.get(i).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent imageIntent = new Intent(HomePageActivity.this, AgreeServiceActivity.class);
					imageIntent.putExtra("url", urlString);
					startActivity(imageIntent);
				}
			});
			
		}
	}

	private class HomePagerListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
			int current = arg0 % images.size();
			for (int i = 0; i < dosViews.length; i++) {
				if (i == current) {
					dosViews[i].setBackgroundResource(R.drawable.leader_white);
				} else {
					dosViews[i].setBackgroundResource(R.drawable.leader_black);
				}
			}
		}

	}

	private class HomePagerAdapter extends PagerAdapter {

		private List<ImageView> mList;

		public HomePagerAdapter(List<ImageView> mList) {
			this.mList = mList;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			//System.out.println("destroy");
			//container.removeView((ImageView) object);
		}

		@Override
		public int getCount() {

			return Integer.MAX_VALUE;
		}

		@Override
		public Object instantiateItem(final ViewGroup container,
				final int position) {
			ImageLoader.getInstance().displayImage(urlStrings.get(position % imageCount),
					mList.get(position%imageCount), options);
			currentImage = position;
			container.removeView(images.get(position%imageCount));
			container.addView(images.get(position % imageCount), 0);
			return images.get(position % imageCount);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {

			return arg0 == arg1;
		}

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();

		switch (view.getId()) {
		case R.id.homepage_outer:
			startActivity(new Intent(intent.setClass(HomePageActivity.this, OutrestaurantActivity.class)));
			//overridePendingTransition(R.anim.activityin, R.anim.activityout);
			break;
		case R.id.homepage_user_center:
			HomePageActivity.this.startActivity(new Intent(HomePageActivity.this, UsercenterActivity.class));
			break;
		case R.id.homepage_shake:
			HomePageActivity.this.startActivity(new Intent(HomePageActivity.this, ShakeActivity.class));
			break;
		case R.id.homepage_ofen:
			HomePageActivity.this.startActivity(new Intent(HomePageActivity.this, OffenActivity.class));
			break;
		case R.id.homepage_home:
			this.startActivity(new Intent().setClass(this,
					YiJianZhaiActivity.class));
			break;
		case R.id.preferential:
			startActivity(new Intent(HomePageActivity.this, PrefecencialActivity.class));
			break;
		case R.id.special_selling:
			startActivity(new Intent(HomePageActivity.this, SpecialSellingActivity.class));
			break;
		default:
			break;
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onQueryComplete(QueryId queryId, Object result, EHttpError error) {
		// TODO Auto-generated method stub
		if (result == null) {
			Toast.makeText(this, "网络连接失败", Toast.LENGTH_LONG).show();
		} else {
			if (queryId == UserManageService.REGISTERWITHOUTNAME) {
				if (result.toString().equals("success")) {
					SharedPreferences sharedPreferences = getSharedPreferences(
							"user_id", MODE_PRIVATE);
					Editor editor = sharedPreferences.edit();
					editor.putString("user_id", UUID);
					editor.commit();
				}
			}else if (queryId == HomePageService.home_page_img) {
				System.out.println(result.toString());
				if (!result.equals("failed")) {
					urlStrings = new ArrayList<String>();
					WebUrl = (ArrayList<String>) result;
					for (int i = 0; i < imageCount; i++) {
						String url = LoadingActivity.URL
								+ WebUrl.get(i);
						System.out.println(url);
						urlStrings.add(url);
					}
					imageAddUrl();
					logo.setAdapter(new HomePagerAdapter(images));

				} else {
					Toast.makeText(this, "网络连接超时", Toast.LENGTH_LONG).show();
				}
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				if (isFinish == false) {
					isFinish = true;
					Toast.makeText(getBaseContext(), "再按一次退出",
							Toast.LENGTH_SHORT).show();
					TimerTask task = null;
					task = new TimerTask() {
						@Override
						public void run() {
							isFinish = false;
						}
					};
					timer.schedule(task, 2000);
				} else {
					finish();
					// System.exit(0);
				}
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void copyTestImageToSdCard(final File testImageOnSdCard) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					InputStream is = getAssets().open(TEST_FILE_NAME);
					FileOutputStream fos = new FileOutputStream(testImageOnSdCard);
					byte[] buffer = new byte[8192];
					int read;
					try {
						while ((read = is.read(buffer)) != -1) {
							fos.write(buffer, 0, read);
						}
					} finally {
						fos.flush();
						fos.close();
						is.close();
					}
				} catch (IOException e) {
					L.w("Can't copy test image onto SD card");
				}
			}
		}).start();
	}

}
