package com.yijianzhai.jue.utils;

import java.util.UUID;

public class CreateUUID {

	public CreateUUID() {
	}

	public static String getUUID() {
		String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
		return uuid;
	}

	public static String getUUIDByRules(String rules) {
		String s = UUID.randomUUID().toString();
		String value = s.substring(0, 8) + rules.substring(0, 4)
				+ s.substring(14, 18) + s.substring(5, 8) + s.substring(14);
		System.out.println(value);
		return value;
	}
}