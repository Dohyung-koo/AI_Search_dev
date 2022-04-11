package com.diquest.db.entity;


public class DramaMgmtVO {
	private String collectionId;
	private String termId;
	private String relTermId;
	private String isInsert;
	
	public String getCollectionId() {
		return collectionId;
	}
	public void setCollectionId(String collectionId) {
		this.collectionId = collectionId;
	}
	public String getTermId() {
		return termId;
	}
	public void setTermId(String termId) {
		this.termId = termId;
	}
	public String getRelTermId() {
		return relTermId;
	}
	public void setRelTermId(String relTermId) {
		this.relTermId = relTermId;
	}
	public String getIsInsert() {
		return isInsert;
	}
	public void setIsInsert(String isInsert) {
		this.isInsert = isInsert;
	}
}
