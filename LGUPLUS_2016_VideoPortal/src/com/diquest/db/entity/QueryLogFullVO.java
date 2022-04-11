package com.diquest.db.entity;

public class QueryLogFullVO {
	
	private String collectionId;
	private String profile;
	private String keywords;
	private String analyzedResults;
	private String userName;
	private String extraData;
	private String cached;
	private String totalResultSize;
	private String errorCode;
	private String responseTime;
	private String registered;
	private String nanoTime;
	public String getCollectionId() {
		return collectionId;
	}
	public void setCollectionId(String collectionId) {
		this.collectionId = collectionId;
	}
	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getAnalyzedResults() {
		return analyzedResults;
	}
	public void setAnalyzedResults(String analyzedResults) {
		this.analyzedResults = analyzedResults;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getExtraData() {
		return extraData;
	}
	public void setExtraData(String extraData) {
		this.extraData = extraData;
	}
	public String getCached() {
		return cached;
	}
	public void setCached(String cached) {
		this.cached = cached;
	}
	public String getTotalResultSize() {
		return totalResultSize;
	}
	public void setTotalResultSize(String totalResultSize) {
		this.totalResultSize = totalResultSize;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}
	public String getRegistered() {
		return registered;
	}
	public void setRegistered(String registered) {
		this.registered = registered;
	}
	public String getNanoTime() {
		return nanoTime;
	}
	public void setNanoTime(String nanoTime) {
		this.nanoTime = nanoTime;
	}

}
