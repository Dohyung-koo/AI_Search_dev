package com.diquest.db.entity;

public class ArAutoCompleteVO {
	
	private String orgId = "";
	private String keyword = "";
	private String keywordChosung = "";
	private String type = "";
	private String field = "";
	private String adultYn = "";
	
	
	
	public String getAdultYn() {
		return adultYn;
	}
	public void setAdultYn(String adultYn) {
		this.adultYn = adultYn;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getKeywordChosung() {
		return keywordChosung;
	}
	public void setKeywordChosung(String keywordChosung) {
		this.keywordChosung = keywordChosung;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}


}
