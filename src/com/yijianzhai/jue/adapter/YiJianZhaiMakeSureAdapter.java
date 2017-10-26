 package com.yijianzhai.jue.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yijianzhai.jue.R;
import com.yijianzhai.jue.activity.LoadingActivity;
import com.yijianzhai.jue.activity.YiJianZhaiMakeSureActivity;
import com.yijianzhai.jue.model.Dish;
import com.yijianzhai.jue.utils.AnimateFirstListener;
import com.yijianzhai.jue.utils.GetImgeLoadOption;

public class YiJianZhaiMakeSureAdapter extends BaseAdapter {

	// 数据
	private List<Map<String, Object>> dataList;

	private LayoutInflater inflater;
	private Context context;
	private double sendPrice = 0;
	private int number = 0;
	private double sumPrice = 0;
	private String isRecommed = "0";
	private AnimateFirstListener listener;
	private DisplayImageOptions options;

	public String getIsRecommed() {
		return isRecommed;
	}

	public void setIsRecommed(String isRecommed) {
		this.isRecommed = isRecommed;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataList.size();
	}

	public List<Map<String, Object>> getDataList() {
		return dataList;
	}

	public void setDataList(List<Map<String, Object>> dataList) {
		this.dataList = dataList;
	}

	public YiJianZhaiMakeSureAdapter(List<Map<String, Object>> dataList,
			Context context) {
		super();
		this.dataList = dataList;
		this.context = context;
		inflater = LayoutInflater.from(context);
		listener = new AnimateFirstListener();
		options = GetImgeLoadOption.getOptions();
	}

	public double getSendPrice() {
		return sendPrice;
	}

	public void setSendPrice(double sendPrice) {
		this.sendPrice = sendPrice;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public double getSumPrice() {
		return sumPrice;
	}

	public void setSumPrice(double sumPrice) {
		this.sumPrice = sumPrice;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return dataList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if (arg1 == null) {
			arg1 = inflater.inflate(R.layout.orderdetailitem, null);
			viewHolder = new ViewHolder();
			arg1.setTag(viewHolder);
			viewHolder.img = (ImageView) arg1
					.findViewById(R.id.img_orderdetail);
			viewHolder.name = (TextView) arg1
					.findViewById(R.id.tx_orderdetailitem_name);
			viewHolder.decline = (Button) arg1
					.findViewById(R.id.detail_decline);
			viewHolder.count = (Button) arg1
					.findViewById(R.id.detail_item_count);
			viewHolder.add = (Button) arg1.findViewById(R.id.detail_add);
			viewHolder.price = (TextView) arg1
					.findViewById(R.id.tx_orderdetail_price);
		} else {
			viewHolder = (ViewHolder) arg1.getTag();
		}
		ImageLoader.getInstance().displayImage(LoadingActivity.URL
					+ dataList.get(arg0).get("childImg").toString(),
					viewHolder.img, options, listener);
		viewHolder.name.setText(dataList.get(arg0).get("childName").toString());
		MyOnclickListener myOnclickListener = new MyOnclickListener(arg0);
		viewHolder.count.setText(dataList.get(arg0).get("childNumber")
				.toString());
		viewHolder.decline.setOnClickListener(myOnclickListener);
		viewHolder.add.setOnClickListener(myOnclickListener);

		viewHolder.price.setText("￥"
				+ dataList.get(arg0).get("childPrice").toString());
		return arg1;
	}

	class ViewHolder {
		ImageView img;
		TextView name;
		TextView price;
		Button add;
		Button count;
		Button decline;
	}

	class MyOnclickListener implements OnClickListener {

		// 所在位置
		int position;

		public MyOnclickListener(int position) {
			super();
			this.position = position;
		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {
			case R.id.detail_decline:
				if (Integer.valueOf(dataList.get(position).get("childNumber")
						.toString()) > 0) {
					Integer integer = Integer.valueOf(dataList.get(position)
							.get("childNumber").toString()) - 1;
					dataList.get(position).put("childNumber", integer);
					YiJianZhaiMakeSureActivity yiJianZhaiMakeSureActivity = (YiJianZhaiMakeSureActivity) context;
					yiJianZhaiMakeSureActivity.setDataList(dataList);
					sumPrice -= Double.valueOf(dataList.get(position)
							.get("childPrice").toString());
					yiJianZhaiMakeSureActivity.updateDate(sendPrice + "",
							number + "", sumPrice + "");
				}
				// 如果菜的数量为0
				if (Integer.valueOf(dataList.get(position).get("childNumber")
						.toString()) == 0) {
					dataList.remove(position);
					YiJianZhaiMakeSureActivity yiJianZhaiMakeSureActivity = (YiJianZhaiMakeSureActivity) context;
					yiJianZhaiMakeSureActivity.setDataList(dataList);
				}
				if (dataList.size() == 0) {
					Toast.makeText(context, "没有菜品不能下单", Toast.LENGTH_LONG).show();
				}
				notifyDataSetChanged();
				break;
			case R.id.detail_add:
				Integer integer = Integer.valueOf(dataList.get(position)
						.get("childNumber").toString()) + 1;
				dataList.get(position).put("childNumber", integer);
				YiJianZhaiMakeSureActivity yiJianZhaiMakeSureActivity = (YiJianZhaiMakeSureActivity) context;
				yiJianZhaiMakeSureActivity.setDataList(dataList);
				sumPrice += Double.valueOf(dataList.get(position)
						.get("childPrice").toString());
				yiJianZhaiMakeSureActivity.updateDate(sendPrice + "", number
						+ "", sumPrice + "");
				notifyDataSetChanged();
				break;
			default:
				break;
			}
		}
	}
}
