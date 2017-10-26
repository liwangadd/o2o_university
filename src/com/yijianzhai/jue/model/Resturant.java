package com.yijianzhai.jue.model;

import java.io.Serializable;
import java.util.List;
/*
 * author zmy 2014-08-17
 * Resturant实体类,每拓展一个Resturant则像db_resturant中添加一条记录
 */

public class Resturant implements Serializable{


	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	private String resturant_id;
	private String name;                   //餐厅名称
	private String sort;                   //餐厅分类,多个分类用#隔开
	private String city;                   //餐厅所在城市
	private String block;                  //餐厅所在区
	private Double longitude;              //餐厅位置经度
	private Double latitude;               //餐厅位置纬度
	private Integer delivery;
	private String delivery_limit;
	private Double delivery_time;
	private Integer type;                  // 店铺类型，默认为0 普通点评，1为暂停营业，2为推广店铺
	private String img;
	private String delivery_mode;
	private Double delivery_fee;
	private Double delivery_starting_fee;
	private List<Dish> dishes;
	private String school_id;               //餐厅所属大学         
	private Double distance;                //餐厅到学校中心的距离
	private Integer sales;                  //餐厅销售量
	private String types;                   //餐厅提供的种类，如面食、饮品等，多个种类之间用#分割
	private String start_time;
	private String end_time;
	private String address;
	private String mobile;
	
	private String owner;         // 店主姓名
	private String introduction;  // 店铺介绍
	private double turnover;      // 营业额
	private List<String> recommendedPackages;  // 掌柜推荐套餐ID列表
	private List<String> promotedPackages;     // 推广套餐ID列表
	private String promotionIntroduction;      // 推广宣传标语
	private double promotedTime;               // 推广时长
	public String getResturant_id() {
		return resturant_id;
	}
	public void setResturant_id(String resturant_id) {
		this.resturant_id = resturant_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getBlock() {
		return block;
	}
	public void setBlock(String block) {
		this.block = block;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Integer getDelivery() {
		return delivery;
	}
	public void setDelivery(Integer delivery) {
		this.delivery = delivery;
	}
	public String getDelivery_limit() {
		return delivery_limit;
	}
	public void setDelivery_limit(String delivery_limit) {
		this.delivery_limit = delivery_limit;
	}
	public Double getDelivery_time() {
		return delivery_time;
	}
	public void setDelivery_time(Double delivery_time) {
		this.delivery_time = delivery_time;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getDelivery_mode() {
		return delivery_mode;
	}
	public void setDelivery_mode(String delivery_mode) {
		this.delivery_mode = delivery_mode;
	}
	public Double getDelivery_fee() {
		return delivery_fee;
	}
	public void setDelivery_fee(Double delivery_fee) {
		this.delivery_fee = delivery_fee;
	}
	public Double getDelivery_starting_fee() {
		return delivery_starting_fee;
	}
	public void setDelivery_starting_fee(Double delivery_starting_fee) {
		this.delivery_starting_fee = delivery_starting_fee;
	}
	public List<Dish> getDishes() {
		return dishes;
	}
	public void setDishes(List<Dish> dishes) {
		this.dishes = dishes;
	}
	public String getSchool_id() {
		return school_id;
	}
	public void setSchool_id(String school_id) {
		this.school_id = school_id;
	}
	public Double getDistance() {
		return distance;
	}
	public void setDistance(Double distance) {
		this.distance = distance;
	}
	public Integer getSales() {
		return sales;
	}
	public void setSales(Integer sales) {
		this.sales = sales;
	}
	public String getTypes() {
		return types;
	}
	public void setTypes(String types) {
		this.types = types;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public double getTurnover() {
		return turnover;
	}
	public void setTurnover(double turnover) {
		this.turnover = turnover;
	}
	public List<String> getRecommendedPackages() {
		return recommendedPackages;
	}
	public void setRecommendedPackages(List<String> recommendedPackages) {
		this.recommendedPackages = recommendedPackages;
	}
	public List<String> getPromotedPackages() {
		return promotedPackages;
	}
	public void setPromotedPackages(List<String> promotedPackages) {
		this.promotedPackages = promotedPackages;
	}
	public String getPromotionIntroduction() {
		return promotionIntroduction;
	}
	public void setPromotionIntroduction(String promotionIntroduction) {
		this.promotionIntroduction = promotionIntroduction;
	}
	public double getPromotedTime() {
		return promotedTime;
	}
	public void setPromotedTime(double promotedTime) {
		this.promotedTime = promotedTime;
	}
	
}
