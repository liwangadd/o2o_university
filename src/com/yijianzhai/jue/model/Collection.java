package com.yijianzhai.jue.model;

import java.io.Serializable;

public class Collection implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String collection_id;
	private String user_id;
	private String id;
	private int type;

	public Collection() {

	}

	public String getCollection_id() {
		return collection_id;
	}

	public void setCollection_id(String collection_id) {
		this.collection_id = collection_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}


	

}
