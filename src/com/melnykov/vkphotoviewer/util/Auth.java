package com.melnykov.vkphotoviewer.util;


public class Auth {

	public static String getAuthorizationUrl(String apiId){
		return new StringBuilder().append("http://oauth.vk.com/authorize?client_id=")
						   .append(apiId)
						   .append("&display=touch&scope=")
						   .append(getAccessRights())
						   .append("&redirect_uri=")
						   .append(Constants.CALLBACK_URL)
						   .append("&response_type=token")
						   .toString();
    }
	
	private static String getAccessRights() {
		// Access only to photos
		return "photos";
	}
}
