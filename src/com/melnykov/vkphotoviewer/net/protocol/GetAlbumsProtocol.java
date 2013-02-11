package com.melnykov.vkphotoviewer.net.protocol;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.melnykov.vkphotoviewer.model.Album;

public class GetAlbumsProtocol extends AbstractProtocol {

	private static final String METHOD_NAME = "photos.getAlbums";
	private static final String DELETED_TITLE = "DELETED";
	private static final String PARAM_NEED_COVERS = "need_covers";
	
	public GetAlbumsProtocol(Context context) {
		super(context, METHOD_NAME);
	}
	
	public List<Album> getAlbums() throws JSONException {
		addParam(PARAM_NEED_COVERS, "1");
		String response = sendRequest();
		
		List<Album> result = new ArrayList<Album>();
		JSONObject jsonObj = new JSONObject(response);
		JSONArray jsonArr = jsonObj.optJSONArray("response");
		if (jsonArr != null) {
			for (int i = 0; i < jsonArr.length(); i++) {
				JSONObject jsonAlbum = jsonArr.getJSONObject(i);
				Album album = Album.fromJson(jsonAlbum);
				// Do not add deleted albums
				if (!album.getTitle().equals(DELETED_TITLE)) {
					result.add(album);
				}
			}
		}
		
		return result;
	}

}
