package com.phillip.idea.miscellaneous;

import java.util.Random;

public class SecurityUtils {

	public static final String LOWER_UPPER_NUMERIC = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	public static final String LOWER_UPPER_NUMERIC_SYMBOL = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789`~!@#$%^&*()-+={}[]\\|:;\"'<>,.?/";
	
	public static String generateRandomString(int length){
		return generateRandomString(length, null);
	}
	
	public static String generateRandomString(int length, String charset){
		if(charset == null)
			charset = LOWER_UPPER_NUMERIC;
		
		String result = "";
		Random rand = new Random();
		int charsetLength = charset.length();
		for(int i = 0; i < length; i++){
			result += charset.charAt(rand.nextInt(charsetLength - 1));
		}
		
		return result;
	}
}
