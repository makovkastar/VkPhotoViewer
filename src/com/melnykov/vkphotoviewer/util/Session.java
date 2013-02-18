package com.melnykov.vkphotoviewer.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class Session {
	
	private static Session instance;
	private String accessToken;
	private String userId;
	
	private Session(String accessToken, String userId) {
		this.accessToken = accessToken;
		this.userId = userId;
	}
	
	public static Session getInstance(Context ctx) {
		if (instance == null) {
			final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
			final String accessToken = prefs.getString(Constants.KEY_ACCESS_TOKEN, null);
			final String userId = prefs.getString(Constants.KEY_USER_ID, null);
			instance = new Session(accessToken, userId);
		}
		
		return instance;
	}
	
	/**
	 * Saves current session on shared preferences
	 * @param ctx
	 */
	public void save(Context ctx) {
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		final Editor editor = prefs.edit();
		editor.putString(Constants.KEY_ACCESS_TOKEN, accessToken);
		editor.putString(Constants.KEY_USER_ID, userId);
		editor.commit();
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	public String getAccessToken() {
		return accessToken;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getUserId() {
		return userId;
	}

}
