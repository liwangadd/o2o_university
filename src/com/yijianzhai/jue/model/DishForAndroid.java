package com.yijianzhai.jue.model;

import java.io.Serializable;

public class DishForAndroid implements Serializable {

	private static final long serialVersionUID = 1L;
	private String dish_id;
	private String resturant_id;
	private String dish_name;
	private double price;
	private double sale_price;
	private double sales;
	private String img_url;
	private int dish_num;
	private int orderCount;
	public DishForAndroid() {

	}

	public String getDish_id() {
		return dish_id;
	}

	public void setDish_id(String dish_id) {
		this.dish_id = dish_id;
	}
	public int getDish_num() {
		return dish_num;
	}

	public void setDish_num(int dish_num) {
		this.dish_num = dish_num;
	}
	public String getResturant_id() {
		return resturant_id;
	}

	public void setResturant_id(String resturant_id) {
		this.resturant_id = resturant_id;
	}

	public String getDish_name() {
		return dish_name;
	}

	public void setDish_name(String dish_name) {
		this.dish_name = dish_name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getSale_price() {
		return sale_price;
	}

	public void setSale_price(double sale_price) {
		this.sale_price = sale_price;
	}

	public double getSales() {
		return sales;
	}

	public void setSales(double sales) {
		this.sales = sales;
	}

	public String getImg_url() {
		return img_url;
	}

	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}

	public int getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}
	
}
