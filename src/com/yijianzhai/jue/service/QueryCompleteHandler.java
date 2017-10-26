package com.yijianzhai.jue.service;

import com.yijianzhai.jue.connection.HttpUtils;

public abstract class QueryCompleteHandler implements HttpUtils.ResponseHandler{
    protected OnQueryCompleteListener completeListener = null;
    protected QueryId queryId = null;

	protected QueryCompleteHandler(OnQueryCompleteListener completeListener, QueryId queryId){
		this.completeListener = completeListener;
		this.queryId = queryId;
	}
}
