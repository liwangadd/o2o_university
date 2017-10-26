package com.yijianzhai.jue.model;

import java.io.Serializable;

public class Address implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String address_id;
	private String city;
	private String distinct;
	private String address;
	private String mobile;
	private String name;
	private String label;
	private String school;
	private int category;

	

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getAddress_id() {
		return address_id;
	}

	public void setAddress_id(String address_id) {
		this.address_id = address_id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistinct() {
		return distinct;
	}

	public void setDistinct(String distinct) {
		this.distinct = distinct;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
