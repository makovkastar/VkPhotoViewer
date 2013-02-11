package com.melnykov.vkphotoviewer;

import java.io.IOException;

import com.melnykov.vkphotoviewer.util.Constants;

import android.app.Application;

public class VkPhotoViewerApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		
		// Initialize HttpResponseCache
		try {
			com.integralblue.httpresponsecache.HttpResponseCache.install(getCacheDir(), Constants.HTTP_CACHE_SIZE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
