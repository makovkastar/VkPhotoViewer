package com.melnykov.vkphotoviewer.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.melnykov.vkphotoviewer.R;
import com.melnykov.vkphotoviewer.model.Photo;
import com.melnykov.vkphotoviewer.net.protocol.GetPhotosProtocol;
import com.melnykov.vkphotoviewer.ui.adapter.PhotoGridAdapter;
import com.melnykov.vkphotoviewer.util.Constants;
import com.melnykov.vkphotoviewer.util.ImageDownloader;
import com.melnykov.vkphotoviewer.util.ImageDownloader.BitmapDownloaderTask;

public class PhotoGridFragment extends Fragment implements LoaderCallbacks<List<Photo>>{

	private PhotoGridAdapter mAdapter;
	private OnPhotoSelectedListener mPhotoSelectedListener;
	private long mAlbumId;
	
	public interface OnPhotoSelectedListener {
		public void onPhotoSelected(String photoUrl, String photoId);
	}
	
	public static PhotoGridFragment newInstance(long albumId) {
		PhotoGridFragment instance = new PhotoGridFragment();
		Bundle bundle = new Bundle();
		bundle.putLong("albumId", albumId);
		instance.setArguments(bundle);
		return instance;
	}
	
	public long getAlbumId() {
		return mAlbumId;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_photo_grid, container, false);;
		final GridView gridView  = (GridView) rootView.findViewById(R.id.gvPhotos);
		final TextView emptyView = (TextView) rootView.findViewById(android.R.id.empty);
		gridView.setEmptyView(emptyView);
		this.mAdapter = new PhotoGridAdapter(getActivity());
		gridView.setAdapter(mAdapter);
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position,
					long id) {
				final Photo photo = mAdapter.getItem(position);
				mPhotoSelectedListener.onPhotoSelected(photo.getSrcBig(), String.valueOf(photo.getId()));
			}
		});
		gridView.setRecyclerListener(new AbsListView.RecyclerListener() {
			
			@Override
			public void onMovedToScrapHeap(View view) {
				ImageView ivPhotoThumb = (ImageView) view.findViewById(R.id.ivPhotoThumb);
				BitmapDownloaderTask bitmapDownloaderTask = ImageDownloader.getBitmapDownloaderTask(ivPhotoThumb);
				if (bitmapDownloaderTask != null) {
					bitmapDownloaderTask.cancel(true);
				}
			}
		});
		return rootView;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Constants.DEBUG) Log.d("PhotoGridFragment", "onCreate");
		this.mAlbumId = getArguments().getLong("albumId");
		Bundle bundle = new Bundle();
		bundle.putLong("albumId", mAlbumId);
		getLoaderManager().initLoader(0, bundle, this);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mPhotoSelectedListener = (OnPhotoSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException("Parent activity must implement OnPhotoSelectedListener");
		}
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		this.mPhotoSelectedListener = null;
	}
	
	@Override
	public Loader<List<Photo>> onCreateLoader(int id, Bundle args) {
		final long albumId = args.getLong("albumId");
		return new PhotoLoader(getActivity(), albumId);
	}

	@Override
	public void onLoadFinished(Loader<List<Photo>> loader, List<Photo> photos) {
		if (photos != null) {
			mAdapter.clear();
			for (Photo photo : photos) {
				mAdapter.add(photo);
			}
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onLoaderReset(Loader<List<Photo>> loader) {
		mAdapter.clear();
	}
	
	public static class PhotoLoader extends AsyncTaskLoader<List<Photo>> {

		private List<Photo> mPhotos;
		private long albumId;
		
		public PhotoLoader(Context context, long albumId) {
			super(context);
			this.albumId = albumId;
		}

		@Override
		public List<Photo> loadInBackground() {
			GetPhotosProtocol getPhotosProtocol = new GetPhotosProtocol(getContext());
			List<Photo> photos = new ArrayList<Photo>();
			try {
				photos.addAll(getPhotosProtocol.getPhotos(albumId));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return photos;
		}
		
		/**
         * Called when there is new data to deliver to the client.  The
         * super class will take care of delivering it; the implementation
         * here just adds a little more logic.
         */
		@Override
		public void deliverResult(List<Photo> photos) {
			mPhotos = photos;
			if (isStarted()) {
				// If the Loader is currently started, we can immediately
				// deliver its results.
				super.deliverResult(photos);
			}
		}	
		
		 /**
         * Handles a request to start the Loader.
         */
		@Override
		protected void onStartLoading() {
			if (mPhotos != null) {
				// If we currently have a result available, deliver it
				// immediately.
				deliverResult(mPhotos);
			} else {
				forceLoad();
			}
		}
		
		 /**
         * Handles a request to stop the Loader.
         */
		@Override
		protected void onStopLoading() {
			// Attempt to cancel the current load task if possible.
            cancelLoad();
		}
		
		/**
         * Handles a request to completely reset the Loader.
         */
        @Override protected void onReset() {
            super.onReset();

            // Ensure the loader is stopped
            onStopLoading();

            // At this point we can release the resources.
            if (mPhotos != null) {
            	mPhotos = null;
            }
        }
	}
}
