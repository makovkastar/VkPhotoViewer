package com.melnykov.vkphotoviewer.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.melnykov.vkphotoviewer.R;
import com.melnykov.vkphotoviewer.model.Album;
import com.melnykov.vkphotoviewer.util.ImageDownloader;

public class AlbumAdapter extends ArrayAdapter<Album> {

	private LayoutInflater mInflater;
	private ImageDownloader mImageDownloader;
	
	public AlbumAdapter(Context context) {
		super(context, 0);
		this.mInflater = LayoutInflater.from(context);
		this.mImageDownloader = ImageDownloader.getInstance(context);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Album item = getItem(position);
		
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_item_album, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.ivAlbumCover = (ImageView) convertView.findViewById(R.id.ivAlbumCover);
			viewHolder.tvAlbumTitle = (TextView) convertView.findViewById(R.id.tvAlbumTitle);
			viewHolder.pbLoading = (ProgressBar) convertView.findViewById(R.id.pbLoading);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.tvAlbumTitle.setText(item.getTitle());
		mImageDownloader.download(item.getThumbSrc(), String.valueOf(item.getId()) + "_normal", viewHolder.ivAlbumCover, viewHolder.pbLoading);
		
		return convertView;
	}

	private static class ViewHolder {
		ProgressBar pbLoading;
		ImageView ivAlbumCover;
		TextView tvAlbumTitle;
	}
}
