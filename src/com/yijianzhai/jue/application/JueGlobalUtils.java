package com.yijianzhai.jue.application;

import android.content.Context;

public class JueGlobalUtils {
    private static JueGlobalUtils instance = null;
    private static Context appContext = null;
    
    /**
     * 
     * @return
     */
    static public JueGlobalUtils getInstance(){
        if(instance == null)
            instance = new JueGlobalUtils();
        
        return instance;
    }
    
    public void init(Context context){
        appContext = context;
    }

    /**
     * 
     * @return
     */
    public Context getAppContext(){
        return appContext;
    }
}
