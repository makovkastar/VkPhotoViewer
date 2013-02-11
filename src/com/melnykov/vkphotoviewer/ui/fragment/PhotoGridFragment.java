package com.melnykov.vkphotoviewer.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class PhotoGridFragment extends Fragment {

	public static PhotoGridFragment newInstance(long albumId) {
		PhotoGridFragment instance = new PhotoGridFragment();
		Bundle bundle = new Bundle();
		bundle.putLong("albumId", albumId);
		instance.setArguments(bundle);
		return instance;
	}

}
