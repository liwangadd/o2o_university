package com.yijianzhai.jue.model;

import java.io.Serializable;
import java.util.List;

public class DataAddress implements Serializable {
	private List<Address> addresses;

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	public DataAddress(List<com.yijianzhai.jue.model.Address> result) {
		super();
		this.addresses = result;
	}

}
