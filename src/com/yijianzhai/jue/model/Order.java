package com.yijianzhai.jue.model;

import java.util.List;

public class Order {

	private String condition;// 附加说明

	private List<Dish> dishes;

	private String order_time;

	private double price_on_delivery;

	private String restaurantId;

	private String status;

	private double total_money;

	private String addressId;

	public String getCondition() {
		return condition;
	}

	public List<Dish> getDishes() {
		return dishes;
	}

	public String getOrder_time() {
		return order_time;
	}

	public double getPrice_on_delivery() {
		return price_on_delivery;
	}

	public String getStatus() {
		return status;
	}

	public double getTotal_money() {
		return total_money;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public void setDishes(List<Dish> dishes) {
		this.dishes = dishes;
	}

	public void setOrder_time(String order_time) {
		this.order_time = order_time;
	}

	public void setPrice_on_delivery(double price_on_delivery) {
		this.price_on_delivery = price_on_delivery;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTotal_money(double total_money) {
		this.total_money = total_money;
	}

	@Override
	public String toString() {
		return "Order [restaurantId=" + restaurantId + ", total_money="
				+ total_money + ", status=" + status + ", price_on_delivery="
				+ price_on_delivery + ", order_time=" + order_time
				+ ", condition=" + condition + ", dishes=" + dishes + "]";
	}

	public String getAddressId() {
		return addressId;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}

	public String getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(String restaurantId) {
		this.restaurantId = restaurantId;
	}
}
