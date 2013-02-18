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
	
	public static final long HTTP_CACHE_SIZE = 10 * 1024 * 1024; // 10 MiB
	public static final String HTTP_CACHE_SUBDIR = "httpresponsecache";
	public static final int DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB
	public static final String DISK_CACHE_SUBDIR = "thumbnails";
	
	public static final int APP_VERSION = 1;
	
	// Prevent from sub-classing
	private Constants() {}
	
}
