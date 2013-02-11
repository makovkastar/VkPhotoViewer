package com.melnykov.vkphotoviewer.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Photo {

	public long id;
    public long albumId;
    public String ownerId;
    public String src;
    public String srcSmall;
    public String srcBig;
    public String srcXbig;
    public String srcXxbig;
    public String srcXxxbig;

    public static Photo fromJson(JSONObject json) throws JSONException {
        Photo photo = new Photo();
        photo.id = json.getLong("pid");
        photo.albumId = json.optLong("aid");
        photo.ownerId = json.getString("owner_id");
        photo.src = json.optString("src");
        photo.srcSmall = json.optString("src_small");
        photo.srcBig = json.optString("src_big");
        photo.srcXbig = json.optString("src_xbig");
        photo.srcXxbig = json.optString("src_xxbig");
        photo.srcXxxbig = json.optString("src_xxxbig");
        
        return photo;
    }

}