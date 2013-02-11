package com.melnykov.vkphotoviewer.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.melnykov.vkphotoviewer.model.Album;
import com.melnykov.vkphotoviewer.net.protocol.GetAlbumsProtocol;
import com.melnykov.vkphotoviewer.ui.adapter.AlbumAdapter;

public class AlbumListFragment extends ListFragment implements LoaderCallbacks<List<Album>>{

	private AlbumAdapter mAdapter;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mAdapter = new AlbumAdapter(getActivity());
		getListView().setAdapter(mAdapter);
		
		getLoaderManager().initLoader(0, null, this);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getLoaderManager().restartLoader(0, null, this);
	}
	
	@Override
	public Loader<List<Album>> onCreateLoader(int id, Bundle args) {
		return new AlbumLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<List<Album>> loader, List<Album> albums) {
		if (albums != null) {
			setListShown(true);
			mAdapter.clear();
			for (Album album : albums) {
				mAdapter.add(album);
			}
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onLoaderReset(Loader<List<Album>> load) {
		mAdapter.clear();
	}
	
	public static class AlbumLoader extends AsyncTaskLoader<List<Album>> {

		private List<Album> mAlbums;
		
		public AlbumLoader(Context context) {
			super(context);
		}

		@Override
		public List<Album> loadInBackground() {
			GetAlbumsProtocol getAlbumsProtocol = new GetAlbumsProtocol(getContext());
			List<Album> albums = new ArrayList<Album>();
			try {
				albums.addAll(getAlbumsProtocol.getAlbums());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return albums;
		}
		
		/**
         * Called when there is new data to deliver to the client.  The
         * super class will take care of delivering it; the implementation
         * here just adds a little more logic.
         */
		@Override
		public void deliverResult(List<Album> albums) {
			mAlbums = albums;
			if (isStarted()) {
				// If the Loader is currently started, we can immediately
				// deliver its results.
				super.deliverResult(albums);
			}
		}	
		
		 /**
         * Handles a request to start the Loader.
         */
		@Override
		protected void onStartLoading() {
			if (mAlbums != null) {
				// If we currently have a result available, deliver it
				// immediately.
				deliverResult(mAlbums);
			}
			
			forceLoad();
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
            if (mAlbums != null) {
            	mAlbums = null;
            }
        }
	}
}