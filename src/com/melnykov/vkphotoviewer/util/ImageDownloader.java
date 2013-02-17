package com.melnykov.vkphotoviewer.util;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class ImageDownloader {
	
	private static volatile ImageDownloader instance;
	private LocalCache mCache;
	private ExecutorService mExecutorService;
	private static final int N_THREADS = 30;
	
	private ImageDownloader(Context context) {
		mCache = LocalCache.getInstance(context);
		mExecutorService = Executors.newFixedThreadPool(N_THREADS);
	}
	
	public static ImageDownloader getInstance(Context context) {
		ImageDownloader localInstance = instance;
		if (localInstance == null) {
			synchronized (ImageDownloader.class) {
				localInstance = instance;
				if (localInstance == null) {
					instance = localInstance = new ImageDownloader(context);
				}
			}
		}
		return localInstance;
	}
	
	@SuppressLint("NewApi")
	public void download(String url, String id, ImageView imageView, ProgressBar progressBar) {
		if (cancelPotentialDownload(url, imageView)) {
			BitmapDownloaderTask task = new BitmapDownloaderTask(imageView, progressBar);
			DownloadedDrawable downloadedDrawable = new DownloadedDrawable(task);
			imageView.setImageDrawable(downloadedDrawable);
			if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
				task.executeOnExecutor(mExecutorService, url, id);
			} else {
				task.execute(url, id);
			}
		}
	}
	
	private static boolean cancelPotentialDownload(String url, ImageView imageView) {
	    BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);

	    if (bitmapDownloaderTask != null) {
	        String bitmapUrl = bitmapDownloaderTask.url;
	        if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
	            bitmapDownloaderTask.cancel(true);
	        } else {
	            // The same URL is already being downloaded.
	            return false;
	        }
	    }
	    return true;
	}
	
	public static BitmapDownloaderTask getBitmapDownloaderTask(ImageView imageView) {
	    if (imageView != null) {
	        Drawable drawable = imageView.getDrawable();
	        if (drawable instanceof DownloadedDrawable) {
	            DownloadedDrawable downloadedDrawable = (DownloadedDrawable)drawable;
	            return downloadedDrawable.getBitmapDownloaderTask();
	        }
	    }
	    return null;
	}
	
	public class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
		
		String url;
		final WeakReference<ImageView> imageViewReference;
		final WeakReference<ProgressBar> progressBarReference;
		
		public BitmapDownloaderTask(ImageView imageView, ProgressBar progressBar) {
			this.imageViewReference = new WeakReference<ImageView>(imageView);
			this.progressBarReference = new WeakReference<ProgressBar>(progressBar);
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.progressBarReference.get().setVisibility(View.VISIBLE);
		}
		
		@Override
		protected Bitmap doInBackground(String... params) {
			if (isCancelled()) {
				return null;
			}
			url = params[0];
			final String id = params[1];
			
			Bitmap bitmap = mCache.getBitmapFromCache(id);
			// Not found in cache
			if (bitmap == null) {
				bitmap = downloadBitmap(url);
				// Add bitmap to cache
				mCache.addBitmapToCache(id, bitmap);
			}
			
			return bitmap;
		}
		
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (isCancelled()) {
				bitmap = null;
			}

			if (progressBarReference != null) {
				final ProgressBar progressBar = progressBarReference.get();
				if (progressBar != null) {
					progressBar.setVisibility(View.GONE);
				}
			}
			
			if (imageViewReference != null) {
				final ImageView imageView = imageViewReference.get();
				BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
				 // Change bitmap only if this process is still associated with it
			    if (this == bitmapDownloaderTask) {
			        imageView.setImageBitmap(bitmap);
			    }
			}
		}
		
		private Bitmap downloadBitmap(String bitmapUrl) {
			try {
				URL url = new URL(bitmapUrl);
				HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
				InputStream inputStream = null;
				try {
					inputStream = new BufferedInputStream(urlConnection.getInputStream());
					final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
					return bitmap;
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					urlConnection.disconnect();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
	static class DownloadedDrawable extends ColorDrawable {
		private final WeakReference<BitmapDownloaderTask> bitmapDownloaderTaskReference;
		
		public DownloadedDrawable(BitmapDownloaderTask bitmapDownloaderTask) {
			super(Color.TRANSPARENT);
			this.bitmapDownloaderTaskReference = new WeakReference<ImageDownloader.BitmapDownloaderTask>(bitmapDownloaderTask);
		}
		
		public BitmapDownloaderTask getBitmapDownloaderTask() {
			return bitmapDownloaderTaskReference.get();
		}
	}

}
