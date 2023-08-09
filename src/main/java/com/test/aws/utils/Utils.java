package com.test.aws.utils;

public class Utils {
	private static String getLine(String object) {
		String response = "";
		Integer size = object.length();
		for (int i = 0; i < size; i++) {
			response = response.concat("-");
		}
		return response;
	}
	
	public static String writeMessage(String message) {
		System.out.println(Utils.getLine(message));
		System.out.println(message);
		System.out.println(Utils.getLine(message));

		StringBuilder builder = new StringBuilder();
		builder.append(Utils.getLine(message));
		builder.append(message);
		builder.append(Utils.getLine(message));
		
		return builder.toString();
	}
	
	public static Exception generateException(String message) {
		return new Exception(logger(message));
	}
	
	public static String logger(String message) {
		return writeMessage(message);
	}
}
