package com.yijianzhai.jue.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class DataList implements Serializable {

	private List<Map<String, Object>> list;

	public List<Map<String, Object>> getList() {
		return list;
	}
	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}

	public DataList(List<Map<String, Object>> list) {
		super();
		this.list = list;
	}
	
	public DataList(){
		super();
	}

}
