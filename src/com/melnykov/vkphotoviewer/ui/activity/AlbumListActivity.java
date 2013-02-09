package com.melnykov.vkphotoviewer.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.melnykov.vkphotoviewer.util.Constants;
import com.melnykov.vkphotoviewer.util.Session;

public class AlbumListActivity extends FragmentActivity {
	
	private static final String TAG = AlbumListActivity.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		
		final Session session = Session.restore(getApplicationContext());
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
			if (requestCode == RESULT_OK) {
				final String accessToken = data.getStringExtra(Constants.IEXTRA_ACCESS_TOKEN);
				final String userId = data.getStringExtra(Constants.IEXTRA_USER_ID);
				if (Constants.DEBUG) Log.d(TAG, "Authorization successfull. Access token = " + accessToken + " user id = " + userId);
				Session newSession = new Session(accessToken, userId);
				newSession.save(getApplicationContext());
				loadAlbumList();
			}
		}
	}
	
	private void loadAlbumList() {
		
	}
}
