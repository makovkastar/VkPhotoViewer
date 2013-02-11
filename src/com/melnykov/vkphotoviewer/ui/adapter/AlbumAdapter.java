package com.melnykov.vkphotoviewer.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.melnykov.vkphotoviewer.model.Album;

public class AlbumAdapter extends ArrayAdapter<Album> {

	private LayoutInflater mInflater;
	
	public AlbumAdapter(Context context) {
		super(context, 0);
		this.mInflater = LayoutInflater.from(context);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Album item = getItem(position);
		TextView tvAlbumName = (TextView) mInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
		tvAlbumName.setText(item.getTitle());
		return tvAlbumName;
	}

}
