package com.melnykov.vkphotoviewer.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
		return "photos,offline";
	}
	
	public static String getAccessToken(String redirectUri) {
		return extractPattern(redirectUri, "access_token=(.*?)&");
	}
	
	public static String getUserId(String redirectUri) {
		return extractPattern(redirectUri, "user_id=(\\d*)");
	}
	
	private static String extractPattern(String string, String pattern) {
		Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(string);
        if (!m.find())
            return null;
        return m.toMatchResult().group(1);
	}
}
