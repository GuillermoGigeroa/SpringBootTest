package com.springboottest.app.utils;

public class Utils {
	public static String writeMessage(String message) {
		System.out.println(message);
		return message;
	}
	
	public static Exception generateException(String message) {
		return new Exception(logger(message));
	}
	
	public static String logger(String message) {
		return writeMessage(message);
	}
}
