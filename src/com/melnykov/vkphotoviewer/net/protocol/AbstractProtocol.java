package com.melnykov.vkphotoviewer.net.protocol;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.util.Log;

import com.melnykov.vkphotoviewer.util.Constants;
import com.melnykov.vkphotoviewer.util.Session;

/**
 * Base protocol for sending OAuth-signed GET and POST requests.
 *
 */
public abstract class AbstractProtocol {
	
	private static final String TAG = AbstractProtocol.class.getName();
	private static final int CONNECTION_TIMEOUT = 5000;
	private static final int STATUS_CODE_OK = 200;
	
	protected static final String BASE_URI = "https://api.vkontakte.ru/method/";
	
	private final List<NameValuePair> mNameValuePairs = new ArrayList<NameValuePair>();
	
	private String mRequestUrl = BASE_URI;
	private Context mContext;
	
	public AbstractProtocol(Context context, String method) {
		this.mRequestUrl = mRequestUrl + method;
		this.mContext = context;
	}
	
	public Context getContext() {
		return mContext;
	}
	
	protected void addParam(String name, String value) {
		mNameValuePairs.add(new BasicNameValuePair(name, value));
	}
	
	protected void setParam(String name, String value) {
		NameValuePair pairToReplace = null;
		for (NameValuePair pair : mNameValuePairs) {
			if (pair.getName().equals(name)) {
				pairToReplace = pair;
				break;
			}
		}
		
		if (pairToReplace != null) {
			mNameValuePairs.remove(pairToReplace);
		}
		
		mNameValuePairs.add(new BasicNameValuePair(name, value));
	}
	
	private String constructGetRequest() {
		StringBuilder result =  new StringBuilder();
		result.append("?");
		
		for (NameValuePair pair : mNameValuePairs) {
			result.append(pair.getName())
			       .append("=")
			       .append(pair.getValue())
			       .append(mNameValuePairs.indexOf(pair) == (mNameValuePairs.size() - 1) ? "" : "&");
		}
		
		if (mNameValuePairs.size() != 0) {
			result.append("&");
		}
		// Append access token
		result.append("access_token=")
					 .append(Session.getInstance(mContext).getAccessToken());
		
		return result.toString();
	}
	
	private String convertInputStreamToString(InputStream is) throws IOException{
		StringBuilder result = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = reader.readLine()) != null) {
        	result.append(line);
        }
        
        return result.toString();
	}
	
	public String sendRequest() {
		String httpResponse = null;

		StringBuilder requestString = new StringBuilder();
		String getRequest = constructGetRequest();
		requestString.append(mRequestUrl)
		             .append(getRequest);
		if (Constants.DEBUG) Log.v(TAG, "Sending GET request " + requestString.toString());
		//HttpGet httpGet = new HttpGet(requestString.toString());
		//httpResponse = mHttpClient.execute(httpGet);
		URL url = null;
		HttpURLConnection urlConnection = null;
		try {
			url = new URL(requestString.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
		 	InputStream is = new BufferedInputStream(urlConnection.getInputStream());
		 	httpResponse = convertInputStreamToString(is); 
			
			int statusCode = urlConnection.getResponseCode();
			
			if (httpResponse != null
					&& statusCode == STATUS_CODE_OK) {
				if (Constants.DEBUG) Log.v(TAG, "Response received : " + httpResponse);
			} else {
				StringBuilder errorMsg = new StringBuilder();
				errorMsg.append("Response code: ")
				        .append(statusCode)
				        .append(". ")
				        .append("Description: ")
				        .append(convertInputStreamToString(urlConnection.getErrorStream()));
				Log.w(TAG, "Error response received : " + errorMsg.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		} 
		
		return httpResponse;
	}
}
