package com.melnykov.vkphotoviewer.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.melnykov.vkphotoviewer.R;
import com.melnykov.vkphotoviewer.ui.fragment.AlbumListFragment;
import com.melnykov.vkphotoviewer.ui.fragment.PhotoGridFragment;
import com.melnykov.vkphotoviewer.util.Constants;
import com.melnykov.vkphotoviewer.util.Session;

public class MainActivity extends FragmentActivity implements AlbumListFragment.OnAlbumSelectedListener {
	
	private static final String TAG = MainActivity.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		setContentView(R.layout.activity_main);
		
		final Session session = Session.getInstance(getApplicationContext());
		if (session.getAccessToken() == null) {
			startLoginActivity();
		} else {
			loadAlbumList();
		}
	}
	
	private void startLoginActivity() {
		Intent intent = new Intent();
        intent.setClass(this, LoginActivity.class);
        startActivityForResult(intent, Constants.REQUEST_CODE_LOGIN);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Constants.REQUEST_CODE_LOGIN) {
			if (resultCode == RESULT_OK) {
				final String accessToken = data.getStringExtra(Constants.IEXTRA_ACCESS_TOKEN);
				final String userId = data.getStringExtra(Constants.IEXTRA_USER_ID);
				if (Constants.DEBUG) Log.d(TAG, "Authorization successfull. Access token = " + accessToken + " user id = " + userId);
				Session newSession = Session.getInstance(getApplicationContext());
				newSession.setAccessToken(accessToken);
				newSession.setUserId(userId);
				newSession.save(getApplicationContext());
				loadAlbumList();
			}
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    //No call for super(). Bug on API Level > 11.
	}
	
	private void loadAlbumList() {
		AlbumListFragment albumListFragment = new AlbumListFragment();
		replaceMainFragment(albumListFragment);
	}
	
	private void loadPhotosForAlbum(long albumId) {
		PhotoGridFragment photoGridFragment = PhotoGridFragment.newInstance(albumId);
		replaceMainFragment(photoGridFragment);
	}
	
	private void replaceMainFragment(Fragment fragment) {
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.fragment_container, fragment);
		fragmentTransaction.commit();
	}

	@Override
	public void onAlbumSelected(long albumId) {
		loadPhotosForAlbum(albumId);
	}
}
