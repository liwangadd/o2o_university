package com.yijianzhai.jue.model;

import java.io.Serializable;

public class Dish implements Serializable{

	private String dish_id;
	private String name;
	private String tag;
	private Double price;
	private Double sale_price;
	private Integer sales;
	private int number;
	private String img;
	private String detail;

	public String getDish_id() {
		return dish_id;
	}
	
	public void setDish_id(String dish_id) {
		this.dish_id = dish_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getSale_price() {
		return sale_price;
	}

	public void setSale_price(Double sale_price) {
		this.sale_price = sale_price;
	}

	public Integer getSales() {
		return sales;
	}

	public void setSales(Integer sales) {
		this.sales = sales;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	

}
