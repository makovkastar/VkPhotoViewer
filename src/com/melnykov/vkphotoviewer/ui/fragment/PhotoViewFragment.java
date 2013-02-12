package com.melnykov.vkphotoviewer.ui.fragment;

import com.melnykov.vkphotoviewer.R;
import com.melnykov.vkphotoviewer.util.ImageDownloader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class PhotoViewFragment extends Fragment {
	
	private String mPhotoUrl;
	private String mPhotoId;
	
	public static PhotoViewFragment newInstance(String photoUrl, String photoId) {
		PhotoViewFragment instance = new PhotoViewFragment();
		Bundle bundle = new Bundle();
		bundle.putString("photoUrl", photoUrl);
		bundle.putString("photoId", photoId);
		instance.setArguments(bundle);
		
		return instance;
	}
	
	public String getPhotoUrl() {
		return mPhotoUrl;
	}
	
	public String getPhotoId() {
		return mPhotoId;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mPhotoUrl = getArguments().getString("photoUrl");
		this.mPhotoId = getArguments().getString("photoId");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_photo_view, container, false);
		ImageView ivPhoto = (ImageView) rootView.findViewById(R.id.ivPhoto);
		ProgressBar pbLoading = (ProgressBar) rootView.findViewById(R.id.pbLoading);
		
		if (getArguments() != null) {
			String photoUrl = getArguments().getString("photoUrl");
			String photoId = getArguments().getString("photoId");
			if (photoUrl != null && photoId != null) {
				ImageDownloader imageDownloader = new ImageDownloader(getActivity());
				imageDownloader.download(photoUrl, photoId + "_big", ivPhoto, pbLoading);
			}
		}
		
		return rootView;
	}

}
