package com.melnykov.vkphotoviewer.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Album {
	
	private long id;
    private long thumbId;
    private String thumbSrc;
    private long ownerId;
    private String title;
    private String description;
    private long createdAt;
    private long updatedAt;
    private long size;
    
    public static Album fromJson(JSONObject json) throws JSONException {
        Album album = new Album();
        album.title = json.optString("title");
        album.id = Long.parseLong(json.getString("aid"));
        album.ownerId = Long.parseLong(json.getString("owner_id"));
        album.description = json.optString("description");
        album.thumbId = Long.parseLong(json.optString("thumb_id"));
        album.thumbSrc = json.optString("thumb_src");
        album.createdAt = Long.parseLong(json.optString("created"));
        album.size = json.optLong("size");
        album.updatedAt = Long.parseLong(json.optString("updated"));
        return album;
    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getThumbId() {
		return thumbId;
	}

	public void setThumbId(long thumbId) {
		this.thumbId = thumbId;
	}

	public long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(long ownerId) {
		this.ownerId = ownerId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}

	public long getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(long updatedAt) {
		this.updatedAt = updatedAt;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}
	
	public String getThumbSrc() {
		return thumbSrc;
	}

}
