package com.diquest.db.entity;

import java.util.List;

public class KidsSpcVO {

	private String albumId;
	private String fieldId;
	private String fieldNm;
	private String depth1Id;
	private String depth1Nm;
	private String depth2Id;
	private String depth2Nm;
	private String viewingNm;
	private String searchGroup;
	
	public String getSearchGroup() {
		return searchGroup;
	}

	public void setSearchGroup(String searchGroup) {
		this.searchGroup = searchGroup;
	}

	public String getAlbumId() {
		return albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

	public String getFieldId() {
		return fieldId;
	}

	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}

	public String getFieldNm() {
		return fieldNm;
	}

	public void setFieldNm(String fieldNm) {
		this.fieldNm = fieldNm;
	}

	public String getDepth1Id() {
		return depth1Id;
	}

	public void setDepth1Id(String depth1Id) {
		this.depth1Id = depth1Id;
	}

	public String getDepth1Nm() {
		return depth1Nm;
	}

	public void setDepth1Nm(String depth1Nm) {
		this.depth1Nm = depth1Nm;
	}

	public String getDepth2Id() {
		return depth2Id;
	}

	public void setDepth2Id(String depth2Id) {
		this.depth2Id = depth2Id;
	}

	public String getDepth2Nm() {
		return depth2Nm;
	}

	public void setDepth2Nm(String depth2Nm) {
		this.depth2Nm = depth2Nm;
	}

	public String getViewingNm() {
		return viewingNm;
	}

	public void setViewingNm(String viewingNm) {
		this.viewingNm = viewingNm;
	}

	public void setAll(KidsSpcVO vo, List<String> list){
		vo.setAlbumId(list.get(0));
		vo.setFieldId(list.get(1));
		vo.setFieldNm(list.get(2));
		vo.setDepth1Id(list.get(3));
		vo.setDepth1Nm(list.get(4));
		vo.setDepth2Id(list.get(5));
		vo.setDepth2Nm(list.get(6));
		vo.setViewingNm(list.get(7));
		vo.setSearchGroup(list.get(8));
		
	}
	
}
