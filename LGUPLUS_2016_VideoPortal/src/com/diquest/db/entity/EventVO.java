package com.diquest.db.entity;

import java.util.List;

public class EventVO {
	private String resultType;
	private String svcType;
	private String regNo;
	private String title;
	private String bannerUrl;
	private String startDt;
	private String endDt;
	private String prize;
	private String announceYn;
	private String evType;
	private String isNew;
	private String evStatus;
	private String keyword;
	private String osType;
	private String model;
	
	private String whereIdx;
	
	public String getResultType() {
		return resultType;
	}
	public void setResultType(String resultType) {
		this.resultType = resultType;
	}
	public String getSvcType() {
		return svcType;
	}
	public void setSvcType(String svcType) {
		this.svcType = svcType;
	}
	public String getRegNo() {
		return regNo;
	}
	public void setRegNo(String regNo) {
		this.regNo = regNo;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBannerUrl() {
		return bannerUrl;
	}
	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}
	public String getStartDt() {
		return startDt;
	}
	public void setStartDt(String startDt) {
		this.startDt = startDt;
	}
	public String getEndDt() {
		return endDt;
	}
	public void setEndDt(String endDt) {
		this.endDt = endDt;
	}
	public String getPrize() {
		return prize;
	}
	public void setPrize(String prize) {
		this.prize = prize;
	}
	public String getAnnounceYn() {
		return announceYn;
	}
	public void setAnnounceYn(String announceYn) {
		this.announceYn = announceYn;
	}
	public String getEvType() {
		return evType;
	}
	public void setEvType(String evType) {
		this.evType = evType;
	}
	public String getIsNew() {
		return isNew;
	}
	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}
	public String getEvStatus() {
		return evStatus;
	}
	public void setEvStatus(String evStatus) {
		this.evStatus = evStatus;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getOsType() {
		return osType;
	}
	public void setOsType(String osType) {
		this.osType = osType;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getWhereIdx() {
		return whereIdx;
	}
	public void setWhereIdx(String whereIdx) {
		this.whereIdx = whereIdx;
	}
	public void setAll(EventVO vo, List<String> list){
		vo.setResultType(list.get(0));
		vo.setSvcType(list.get(1));
		vo.setRegNo(list.get(2));
		vo.setTitle(list.get(3));
		vo.setBannerUrl(list.get(4));
		vo.setStartDt(list.get(5));
		vo.setEndDt(list.get(6));
		vo.setPrize(list.get(7));
		vo.setAnnounceYn(list.get(8));
		vo.setEvType(list.get(9));
		vo.setIsNew(list.get(10));
		vo.setEvStatus(list.get(11));
		vo.setKeyword(list.get(12));
		vo.setOsType(list.get(13));
		vo.setModel(list.get(14));
	}
	
	public void setDramaFields(EventVO vo, List<String> list){
//		vo.setResultType(list.get(0));
//		vo.setSvcType(list.get(1));
//		vo.setRegNo(list.get(2));
		vo.setTitle(list.get(3));
//		vo.setBannerUrl(list.get(4));
//		vo.setStartDt(list.get(5));
//		vo.setEndDt(list.get(6));
//		vo.setPrize(list.get(7));
//		vo.setAnnounceYn(list.get(8));
//		vo.setEvType(list.get(9));
//		vo.setIsNew(list.get(10));
//		vo.setEvStatus(list.get(11));
//		vo.setKeyword(list.get(12));
//		vo.setOsType(list.get(13));
//		vo.setModel(list.get(14));
	}
}
