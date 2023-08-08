package com.test.aws.utils;

public class Utils {
	public static String getLine(String object) {
		String response = "";
		Integer size = object.length();
		for (int i = 0; i < size; i++) {
			response = response.concat("-");
		}
		return response;
	}
	
	public static void writeMessage(String message) {
		System.out.println(Utils.getLine(message));
		System.out.println(message);
		System.out.println(Utils.getLine(message));
	}
}
