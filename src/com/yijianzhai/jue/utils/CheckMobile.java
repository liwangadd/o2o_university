package com.yijianzhai.jue.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckMobile {
	public static boolean isMobileNO(String mobiles){  
		  
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");  
		  
		Matcher m = p.matcher(mobiles);  
		  
		System.out.println(m.matches()+"---");  
		  
		return m.matches();  
	}
		
}
