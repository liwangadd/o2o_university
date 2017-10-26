package com.yijianzhai.jue.activity.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

/**
 * Created by Maple on 14-3-13.
 */
public class MyFragmentManager {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    
    public MyFragmentManager(Activity activity){
        fragmentManager = activity.getFragmentManager();
    }

    public void setFragment(int containerViewId,Fragment tempFragment , String tag ){
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(containerViewId,tempFragment , tag );
        fragmentTransaction.commit();
    }

    public void replaceFragment(int containerViewId,Fragment newFragment){
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerViewId,newFragment);
        fragmentTransaction.commit();
    }

    public FragmentTransaction beginTransaction(){
        return fragmentTransaction;
    }

    public void replaceFragmentByTag(int containViewId,Fragment newFragment,String tag){
    	 fragmentTransaction = fragmentManager.beginTransaction();
    	 fragmentTransaction.replace(containViewId, newFragment, tag);
         fragmentTransaction.commit();
    }
}
