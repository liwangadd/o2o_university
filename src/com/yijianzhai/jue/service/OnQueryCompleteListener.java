package com.yijianzhai.jue.service;

import com.yijianzhai.jue.connection.HttpUtils;

public interface OnQueryCompleteListener {
    public abstract void onQueryComplete(QueryId queryId, Object result, HttpUtils.EHttpError error);
}
