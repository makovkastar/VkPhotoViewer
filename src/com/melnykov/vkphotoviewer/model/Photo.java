package com.melnykov.vkphotoviewer.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Photo {

	private long id;
    private long albumId;
    private String ownerId;
    private String src;
    private String srcSmall;
    private String srcBig;
    private String srcXbig;
    private String srcXxbig;
    private String srcXxxbig;

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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getAlbumId() {
		return albumId;
	}

	public void setAlbumId(long albumId) {
		this.albumId = albumId;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getSrcSmall() {
		return srcSmall;
	}

	public void setSrcSmall(String srcSmall) {
		this.srcSmall = srcSmall;
	}

	public String getSrcBig() {
		return srcBig;
	}

	public void setSrcBig(String srcBig) {
		this.srcBig = srcBig;
	}

	public String getSrcXbig() {
		return srcXbig;
	}

	public void setSrcXbig(String srcXbig) {
		this.srcXbig = srcXbig;
	}

	public String getSrcXxbig() {
		return srcXxbig;
	}

	public void setSrcXxbig(String srcXxbig) {
		this.srcXxbig = srcXxbig;
	}

	public String getSrcXxxbig() {
		return srcXxxbig;
	}

	public void setSrcXxxbig(String srcXxxbig) {
		this.srcXxxbig = srcXxxbig;
	}
}