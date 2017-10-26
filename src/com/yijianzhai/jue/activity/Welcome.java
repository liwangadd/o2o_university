package com.yijianzhai.jue.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.yijianzhai.jue.R;

public class Welcome extends Activity implements OnPageChangeListener{
	
	private ViewPager pager;
	private int[] picture;
	private LinearLayout layout;
	private ImageView view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		pager = (ViewPager)findViewById(R.id.welcome);
		layout = (LinearLayout)findViewById(R.id.welcome_layout);
		view = (ImageView) layout.getChildAt(0);
		
		picture = new int[]{R.drawable.welcome01, R.drawable.welcome02, R.drawable.welcome03};
		LoadingPagerAdapter adapter = new LoadingPagerAdapter();
		pager.setAdapter(adapter);
		pager.setOnPageChangeListener(this);
	}
	
	
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
	}



	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}



	@Override
	public void onPageSelected(int arg0) {
		view.setBackgroundResource(R.drawable.leader_black);
		view = (ImageView) layout.getChildAt(arg0);
		view.setBackgroundResource(R.drawable.leader_white);
	}



	class LoadingPagerAdapter extends PagerAdapter{

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View)object);
		}

		@Override
		public int getCount() {
			
			return picture.length;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView imageView = new ImageView(Welcome.this);
			ViewGroup.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			imageView.setLayoutParams(params);
			imageView.setImageResource(picture[position]);
			imageView.setScaleType(ScaleType.FIT_XY);
			if(position == picture.length - 1){
				imageView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(Welcome.this, HomePageActivity.class);
						intent.putExtra("UrlString", getIntent().getSerializableExtra("UrlString"));
						intent.putExtra("WebUrl", getIntent().getSerializableExtra("WebUrl"));
						Welcome.this.startActivity(intent);
						finish();
					}
				});
			}
			container.addView(imageView);
			return imageView;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			
			return arg0 == arg1;
		}
		
	}
		
}
