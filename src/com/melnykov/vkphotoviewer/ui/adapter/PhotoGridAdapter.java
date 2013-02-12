package com.melnykov.vkphotoviewer.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.melnykov.vkphotoviewer.R;
import com.melnykov.vkphotoviewer.model.Photo;
import com.melnykov.vkphotoviewer.util.ImageDownloader;

public class PhotoGridAdapter extends ArrayAdapter<Photo> {

	private LayoutInflater mInflater;
	private ImageDownloader mImageDownloader;

	public PhotoGridAdapter(Context context) {
		super(context, 0);
		this.mInflater = LayoutInflater.from(context);
		this.mImageDownloader = new ImageDownloader(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Photo item = getItem(position);
	
		View rootView = mInflater.inflate(R.layout.grid_item_photo, parent, false);
		ImageView ivPhotoThumb = (ImageView) rootView.findViewById(R.id.ivPhotoThumb);
		ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.pbLoading);
		mImageDownloader.download(item.getSrc(), String.valueOf(item.getId()) + "_normal", ivPhotoThumb, progressBar);

		return rootView;
	}

}
