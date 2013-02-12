package com.melnykov.vkphotoviewer.net.protocol;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.melnykov.vkphotoviewer.model.Photo;
import com.melnykov.vkphotoviewer.util.Session;

public class GetPhotosProtocol extends AbstractProtocol {

	private static final String METHOD_NAME = "photos.get";
	private static final String PARAM_ALBUM_ID = "aid";
	private static final String PARAM_USER_ID = "uid";
	
	public GetPhotosProtocol(Context context) {
		super(context, METHOD_NAME);
	}
	
	public List<Photo> getPhotos(long albumId) throws JSONException {
		addParam(PARAM_ALBUM_ID, String.valueOf(albumId));
		addParam(PARAM_USER_ID, Session.getInstance(getContext()).getUserId());
		
		List<Photo> result = new ArrayList<Photo>();
		String response = sendRequest();
		JSONObject jsonObj = new JSONObject(response);
		JSONArray jsonArr = jsonObj.optJSONArray("response");
		if (jsonArr != null) {
			for (int i = 0; i < jsonArr.length(); i++) {
				JSONObject jsonPhoto = jsonArr.getJSONObject(i);
				Photo photo = Photo.fromJson(jsonPhoto);
				result.add(photo);
			}
		}
		
		return result;
	}

}
