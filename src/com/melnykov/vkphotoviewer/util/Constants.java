package com.melnykov.vkphotoviewer.util;

public abstract class Constants {

	/**
	 * Shared preferences keys
	 */
	public static final String KEY_ACCESS_TOKEN = "access_token";
	public static final String KEY_USER_ID = "user_id";
	
	/**
	 * Request codes
	 */
	public static final int REQUEST_CODE_LOGIN = 0;
	
	/**
	 * Intent extra keys
	 */
	public static final String IEXTRA_ACCESS_TOKEN = "access_token";
	public static final String IEXTRA_USER_ID = "user_id";
	
	
	/**
	 * Flag for debug purpose
	 */
	public static final boolean DEBUG = true;
	
	
	public static final String CALLBACK_URL = "http://api.vkontakte.ru/blank.html";
	public static final String API_ID = "3422904";
	
	private Constants() {}
	
}
