package com.yijianzhai.jue.model;

import java.io.Serializable;
import java.util.Set;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	private String user_id;
	private String user_name;
	private String password;
	private String mobile;
	private String img_url;
	private String binding;

	private Set<Address> addresses;
	private Set<Collection> collections;

	public User() {

	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getImg_url() {
		return img_url;
	}

	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}

	public String getBinding() {
		return binding;
	}

	public void setBinding(String binding) {
		this.binding = binding;
	}

	public Set<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(Set<Address> addresses) {
		this.addresses = addresses;
	}

	public Set<Collection> getCollections() {
		return collections;
	}

	public void setCollections(Set<Collection> collections) {
		this.collections = collections;
	}

}
