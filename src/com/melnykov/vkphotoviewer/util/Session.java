package com.melnykov.vkphotoviewer.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class Session {
	
	private String accessToken;
	private String userId;
	
	public Session(String accessToken, String userId) {
		this.accessToken = accessToken;
		this.userId = userId;
	}
	
	public static Session restore(Context ctx) {
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		final String accessToken = prefs.getString(Constants.KEY_ACCESS_TOKEN, null);
		final String userId = prefs.getString(Constants.KEY_USER_ID, null);
		return new Session(accessToken, userId);
	}
	
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
