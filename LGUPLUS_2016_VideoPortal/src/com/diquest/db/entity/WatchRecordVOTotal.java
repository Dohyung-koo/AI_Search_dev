package com.diquest.db.entity;

import java.util.List;

public class WatchRecordVOTotal {

	private String catGb;
	private String albumId;
	private int viewCount;
	private String viewPoint;

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

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	public String getViewPoint() {
		return viewPoint;
	}

	public void setViewPoint(String viewPoint) {
		this.viewPoint = viewPoint;
	}

	
	public String getWhereIdx() {
		return whereIdx;
	}

	public void setWhereIdx(String whereIdx) {
		this.whereIdx = whereIdx;
	}
	

	public void setAll(WatchRecordVOTotal vo, List<String> list) {
		vo.setCatGb(list.get(0));
		vo.setAlbumId(list.get(1));
		String watch_cnt = list.get(2);
		int watch_cntparse=Integer.parseInt(watch_cnt);
		vo.setViewCount(watch_cntparse);
		vo.setViewPoint(list.get(3));
	}


}
