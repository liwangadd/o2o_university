package com.yijianzhai.jue.model;

import java.io.Serializable;
import java.util.Map;

public class DataMap implements Serializable {
	private Map<String, Object> map;

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public DataMap(Map<String, Object> map) {
		super();
		this.map = map;
	}

	public DataMap(){
		super();
	}
	
}
