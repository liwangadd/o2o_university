package com.yijianzhai.jue.model;

import java.util.List;

/*订单实体类
 * 
 * */
//@Document(collection = "db_package")
public class Package {

	private String id;

	private String name;

	private double total_price;

	private int buy_it_time;

	private String category;

	private List<Dish> dishes;

	private String restaurant_id;

	private double price_on_delivery;
	
	private double sales_price;
	
	private double delivery_starting_fee;

	public double getDelivery_starting_fee() {
		return delivery_starting_fee;
	}

	public void setDelivery_starting_fee(double delivery_starting_fee) {
		this.delivery_starting_fee = delivery_starting_fee;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getTotal_price() {
		return total_price;
	}

	public void setTotal_price(double total_price) {
		this.total_price = total_price;
	}

	public int getBuy_it_time() {
		return buy_it_time;
	}

	public void setBuy_it_time(int buy_it_time) {
		this.buy_it_time = buy_it_time;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<Dish> getDishes() {
		return dishes;
	}

	public void setDishes(List<Dish> dishes) {
		this.dishes = dishes;
	}

	public String getRestaurant_id() {
		return restaurant_id;
	}

	public void setRestaurant_id(String restaurant_id) {
		this.restaurant_id = restaurant_id;
	}

	public double getPrice_on_delivery() {
		return price_on_delivery;
	}

	public void setPrice_on_delivery(double price_on_delivery) {
		this.price_on_delivery = price_on_delivery;
	}

	public double getSales_price() {
		return sales_price;
	}

	public void setSales_price(double sales_price) {
		this.sales_price = sales_price;
	}

	
}
