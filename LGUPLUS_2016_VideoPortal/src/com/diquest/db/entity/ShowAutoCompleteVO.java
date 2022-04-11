package com.diquest.db.entity;

import com.diquest.app.common.Method;

public class ShowAutoCompleteVO {
	
	private String orgId = "";
	private String keyword = "";
	private String keywordChosung;
	private String field;
	private String deviceData = "";
	
	
	
	public String getDeviceData() {
		return deviceData;
	}
	public void setDeviceData(String deviceData) {
		this.deviceData = deviceData;
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
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}


}
