package com.diquest.db.entity;

import java.util.List;

public class KidsEpxVO {
	
	private String albumId;
	private String keywordId;
	private String keywordNm;
	private String searchFlag;
	
	

	public String getAlbumId() {
		return albumId;
	}



	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}



	public String getKeywordId() {
		return keywordId;
	}



	public void setKeywordId(String keywordId) {
		this.keywordId = keywordId;
	}



	public String getKeywordNm() {
		return keywordNm;
	}



	public void setKeywordNm(String keywordNm) {
		this.keywordNm = keywordNm;
	}



	public String getSearchFlag() {
		return searchFlag;
	}



	public void setSearchFlag(String searchFlag) {
		this.searchFlag = searchFlag;
	}



	public void setAll(KidsEpxVO vo, List<String> list){
		vo.setAlbumId(list.get(0));
		vo.setKeywordId(list.get(1));
		vo.setKeywordNm(list.get(2));
		vo.setSearchFlag(list.get(3));
		
	}
	
}
