package com.yijianzhai.jue.application;
import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

public class JueApplication extends Application{
    public boolean m_bKeyRight = true;

    public static final String strKey = "61WeEdVNR9YEVNmGIKRDGuou";
    
    @Override
    public void onCreate() {
        super.onCreate();
        JueGlobalUtils.getInstance().init(getApplicationContext());
        SDKInitializer.initialize(this);
    }
}
