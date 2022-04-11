package com.diquest.db.entity;

import java.util.List;

public class WatchRecordVO {

	private String catGb;
	private String albumId;
	private String viewCount;

	public String getViewcount() {
		return viewCount;
	}

	public void setViewcount(String view_count) {
		this.viewCount = view_count;
	}

	private String whereIdx;


	public String getCatGb() {
		return catGb;
	}

	public void setCatGb(String catGb) {
		this.catGb = catGb;
	}

	public String getAlbumId() {
		return albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}



	public String getWhereIdx() {
		return whereIdx;
	}

	public void setWhereIdx(String whereIdx) {
		this.whereIdx = whereIdx;
	}

	public void setAll(WatchRecordVO vo, List<String> list) {
		vo.setCatGb(list.get(0));
		vo.setAlbumId(list.get(1));
		vo.setViewcount(list.get(2));

	}

}
