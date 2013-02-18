package com.melnykov.vkphotoviewer.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.jakewharton.DiskLruCache;

public class LocalCache {
	
	private static volatile LocalCache instance;
	private static final String TAG = LocalCache.class.getSimpleName();
	private LruCache<String, Bitmap> mMemoryCache;
	private DiskLruCache mDiskLruCache;
	private boolean mDiskCacheStarting = true;
	private static final int DISK_CACHE_VALUE_COUNT = 1;
	
	private LocalCache(Context context) {
		initMemoryCache();
		initDiskCache(context);
	}
	
	public static LocalCache getInstance(Context context) {
		LocalCache localInstance = instance;
		if (localInstance == null) {
			synchronized (LocalCache.class) {
				localInstance = instance;
				if (localInstance == null) {
					instance = localInstance = new LocalCache(context);
				}
			}
		}
		return localInstance;
	}

	public void addBitmapToCache(String key, Bitmap bitmap) {
		if (key != null && bitmap != null) {
			addBitmapToMemoryCache(key, bitmap);
			addBitmapToDiskCache(key, bitmap);
		}
	}
	
	public Bitmap getBitmapFromCache(String key) {
		Bitmap bitmap = getBitmapFromMemCache(key);
		if (bitmap == null) {
			bitmap = getBitmapFromDiskCache(key);
		}
		
		return bitmap;
	}
	
	/**
	 * Setups memory cache: creates it and initializes the size of memory cache
	 */
	private void initMemoryCache() {
		// Get max available VM memory, exceeding this amount will throw an
	    // OutOfMemory exception. Stored in kilobytes as LruCache takes an
	    // int in its constructor.
	    final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
	    // Use 1/8th of the available memory for this memory cache.
	    final int cacheSize = maxMemory / 8;
	    mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
	        @Override
	        protected int sizeOf(String key, Bitmap bitmap) {
	            // The cache size will be measured in kilobytes rather than
	            // number of items.
	            return (bitmap.getRowBytes() * bitmap.getHeight()) / 1024;
	        }
	    };
	}
	
	private void initDiskCache(Context context) {
		new InitDiskCacheTask().execute(getDiskCacheDir(context, Constants.DISK_CACHE_SUBDIR));
	}
	
	private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
	    if (getBitmapFromMemCache(key) == null) {
    		mMemoryCache.put(key, bitmap);
    		if (Constants.DEBUG)  Log.d(TAG, "Image put on memory cache " + key);
	    }
	}

	private Bitmap getBitmapFromMemCache(String key) {
		Bitmap bitmap = mMemoryCache.get(key);
		Log.d(TAG, bitmap == null ? "" : "Image read from memory "
				+ key);
	    return bitmap;
	}
	
	private void addBitmapToDiskCache(String key, Bitmap bitmap) {
		if (!mDiskCacheStarting) {
			 DiskLruCache.Editor editor = null;
			 try {
				 editor = mDiskLruCache.edit(key);
				 if (editor == null) {
					 return;
				 }
				 if (writeBitmapToFile(bitmap, editor)) {
					 mDiskLruCache.flush();
					 editor.commit();
					 if (Constants.DEBUG)  Log.d(TAG, "Image put on disk cache " + key);
				 } else {
					 editor.abort();
					 if (Constants.DEBUG)  Log.d(TAG, "ERROR on: image put on disk cache " + key);
				 }
			 } catch (IOException e) {
				 if (Constants.DEBUG)  Log.d(TAG, "ERROR on: image put on disk cache " + key);
				 e.printStackTrace();
				 try {
					 if (editor != null) {
						 editor.abort();
					 }
				 } catch (IOException ioe) {
					 // Ignored
				 }
			 }
		}
	}
	
	private Bitmap getBitmapFromDiskCache(String key) {
		Bitmap bitmap = null;
		if (!mDiskCacheStarting) {
			DiskLruCache.Snapshot snapshot = null;
			try {
				snapshot = mDiskLruCache.get(key);
				if (snapshot == null) {
					return null;
				}
				final InputStream is = snapshot.getInputStream(0);
				if (is != null) {
					final BufferedInputStream bis = new BufferedInputStream(is);
					bitmap = BitmapFactory.decodeStream(bis);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (snapshot != null) {
					snapshot.close();
				}
			}

			if (Constants.DEBUG)
				Log.d(TAG, bitmap == null ? "" : "Image read from disk "
						+ key);
		}
		return bitmap;
	}
	
	private boolean writeBitmapToFile(Bitmap bitmap, DiskLruCache.Editor editor) throws IOException, FileNotFoundException {
	    OutputStream os = null;
	    try {
	    	os = new BufferedOutputStream(editor.newOutputStream(0));
	        return bitmap.compress(CompressFormat.JPEG, 70, os);
	    } finally {
	        if (os != null) {
	        	os.close();
	        }
	    }
	}
	
	// Creates a unique subdirectory of the designated app cache directory. Tries to use external
	// but if not mounted, falls back on internal storage.
	public static File getDiskCacheDir(Context context, String uniqueName) {
	    // Check if media is mounted or storage is built-in, if so, try and use external cache dir
	    // otherwise use internal cache dir
	    final String cachePath =
	            Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !isExternalStorageRemovable() ? context.getExternalCacheDir().getPath() :
	                            context.getCacheDir().getPath();

	    return new File(cachePath + File.separator + uniqueName);
	}
	
	@SuppressLint("NewApi") 
	public static boolean isExternalStorageRemovable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }
	
	class InitDiskCacheTask extends AsyncTask<File, Void, Void> {
	    @Override
	    protected Void doInBackground(File... params) {
            File cacheDir = params[0];
            try {
            	mDiskLruCache = DiskLruCache.open(cacheDir, Constants.APP_VERSION, DISK_CACHE_VALUE_COUNT, Constants.DISK_CACHE_SIZE);
            	mDiskCacheStarting = false; // Finished initialization
            } catch (IOException e) {
            	e.printStackTrace();
            }
            
	        return null;
	    }
	}

}
