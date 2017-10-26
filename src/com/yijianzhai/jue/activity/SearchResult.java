package com.yijianzhai.jue.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.baidu.mapapi.model.LatLng;
import com.yijianzhai.jue.R;
import com.yijianzhai.jue.utils.InitWedget;

public class SearchResult extends Activity implements InitWedget, OnClickListener{
	private ImageView backImageView;
	private ListView listView;
	private List<String>data=null;
	private List<LatLng>geoPoints;
	private List<String> cityName;
	public void setData(List<String>list){
		data=list;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
         setContentView(R.layout.search_result);
         System.out.println(data);
         initwedget();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.img_return)
			finish();
	}

	@Override
	public void initwedget() {
		// TODO Auto-generated method stub
		backImageView=(ImageView)findViewById(R.id.img_return);
		listView=(ListView)findViewById(R.id.list_searchresult);
		data=SearchHistory.addressName;
		geoPoints=SearchHistory.addressLatLng;
		cityName = SearchHistory.cityName;
		if(data==null){
			data = new ArrayList<String>();
			data.add("没有搜索到相关内容");
		}
		System.out.println(data);
		backImageView.setOnClickListener(this);
		listView.setAdapter(new ArrayAdapter<String>(this, R.layout.mapitem, R.id.mapitem, data));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				 System.out.println(data.get(arg2).toString());
				 SharedPreferences shraPreferences=getSharedPreferences("location", Context.MODE_WORLD_READABLE);
				 Editor editor=shraPreferences.edit();
				 editor.putString("city", cityName.get(arg2).toString());
				 editor.putString("mylocation",data.get(arg2).toString() );
				 double latitude=(double)geoPoints.get(arg2).latitude;
				 double longitude=(double)geoPoints.get(arg2).longitude;
				 System.out.println(longitude);
				 editor.putString("latitude", String.valueOf(latitude));
				 editor.putString("longitude",String.valueOf(longitude));
				 editor.commit();
				 finish();
			}
		});
	}

}