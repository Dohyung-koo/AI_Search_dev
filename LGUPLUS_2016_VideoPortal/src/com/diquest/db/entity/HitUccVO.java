package com.diquest.db.entity;

import java.util.List;

import com.diquest.app.common.Method;

public class HitUccVO {

	private String resultType;
	private String svcType;
	private String catId;
	private String catName;
	private String contentsId;
	private String contentsType;
	private String contentsName;
	private String contentsInfo;
	private String contentsUrl;
	private String imgUrlHdtv;
	private String imgUrlIptv;
	private String duration;
	private String hitCnt;
	private String siteId;
	private String siteName;
	private String siteUrl;
	private String siteIconHdtv;
	private String siteIconIptv;
	private String regDate;
	private String contentsNameChosung;
	private String badgeData;
	private String badgeData2;
	private String deviceData;
	private String startDt;
	private String endDt;
	
	private String whereIdx;
	
	
	
	public String getDeviceData() {
		return deviceData;
	}
	public void setDeviceData(String deviceData) {
		this.deviceData = deviceData;
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
	public String getBadgeData() {
		return badgeData;
	}
	public void setBadgeData(String badgeData) {
		this.badgeData = badgeData;
	}
	public String getBadgeData2() {
		return badgeData2;
	}
	public void setBadgeData2(String badgeData2) {
		this.badgeData2 = badgeData2;
	}
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
	public String getCatId() {
		return catId;
	}
	public void setCatId(String catId) {
		this.catId = catId;
	}
	public String getCatName() {
		return catName;
	}
	public void setCatName(String catName) {
		this.catName = catName;
	}
	public String getContentsId() {
		return contentsId;
	}
	public void setContentsId(String contentsId) {
		this.contentsId = contentsId;
	}
	public String getContentsType() {
		return contentsType;
	}
	public void setContentsType(String contentsType) {
		this.contentsType = contentsType;
	}
	public String getContentsName() {
		return contentsName;
	}
	public void setContentsName(String contentsName) {
		this.contentsName = contentsName;
	}
	public String getContentsInfo() {
		return contentsInfo;
	}
	public void setContentsInfo(String contentsInfo) {
		this.contentsInfo = contentsInfo;
	}
	public String getContentsUrl() {
		return contentsUrl;
	}
	public void setContentsUrl(String contentsUrl) {
		this.contentsUrl = contentsUrl;
	}
	public String getImgUrlHdtv() {
		return imgUrlHdtv;
	}
	public void setImgUrlHdtv(String imgUrlHdtv) {
		this.imgUrlHdtv = imgUrlHdtv;
	}
	public String getImgUrlIptv() {
		return imgUrlIptv;
	}
	public void setImgUrlIptv(String imgUrlIptv) {
		this.imgUrlIptv = imgUrlIptv;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getHitCnt() {
		return hitCnt;
	}
	public void setHitCnt(String hitCnt) {
		this.hitCnt = hitCnt;
	}
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getSiteUrl() {
		return siteUrl;
	}
	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}
	public String getSiteIconHdtv() {
		return siteIconHdtv;
	}
	public void setSiteIconHdtv(String siteIconHdtv) {
		this.siteIconHdtv = siteIconHdtv;
	}
	public String getSiteIconIptv() {
		return siteIconIptv;
	}
	public void setSiteIconIptv(String siteIconIptv) {
		this.siteIconIptv = siteIconIptv;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	
	public String getContentsNameChosung() {
		return contentsNameChosung;
	}
	public void setContentsNameChosung(String contentsNameChosung) {
		this.contentsNameChosung = contentsNameChosung;
	}
	public String getWhereIdx() {
		return whereIdx;
	}
	public void setWhereIdx(String whereIdx) {
		this.whereIdx = whereIdx;
	}

	
	public void setAll(HitUccVO vo, List<String> list){
		vo.setResultType(list.get(0));
		vo.setSvcType(list.get(1));
		vo.setCatId(list.get(2));
		vo.setCatName(list.get(3));
		vo.setContentsId(list.get(4));
		vo.setContentsType(list.get(5));
		vo.setContentsName(list.get(6));
		vo.setContentsInfo(list.get(7));
		vo.setContentsUrl(list.get(8));
		vo.setImgUrlHdtv(list.get(9));
		vo.setImgUrlIptv(list.get(10));
		vo.setDuration(list.get(11));
		vo.setHitCnt(list.get(12));
		vo.setSiteId(list.get(13));
		vo.setSiteName(list.get(14));
		vo.setSiteUrl(list.get(15));
		vo.setSiteIconHdtv(list.get(16));
		vo.setSiteIconIptv(list.get(17));
		vo.setRegDate(list.get(18));
		vo.setBadgeData(list.get(19));
		vo.setBadgeData2(list.get(20));
		vo.setDeviceData(list.get(21));
		vo.setStartDt(list.get(22));
		vo.setEndDt(list.get(23));
		//초성
		vo.setContentsNameChosung(Method.normalizeChosung(list.get(6)));
	}
	
	public void setDramaFields(HitUccVO vo, List<String> list){
//		vo.setResultType(list.get(0));
//		vo.setSvcType(list.get(1));
//		vo.setCatId(list.get(2));
//		vo.setCatName(list.get(3));
//		vo.setContentsId(list.get(4));
//		vo.setContentsType(list.get(5));
		vo.setContentsName(list.get(6));
//		vo.setContentsInfo(list.get(7));
//		vo.setContentsUrl(list.get(8));
//		vo.setImgUrlHdtv(list.get(9));
//		vo.setImgUrlIptv(list.get(10));
//		vo.setDuration(list.get(11));
//		vo.setHitCnt(list.get(12));
//		vo.setSiteId(list.get(13));
//		vo.setSiteName(list.get(14));
//		vo.setSiteUrl(list.get(15));
//		vo.setSiteIconHdtv(list.get(16));
//		vo.setSiteIconIptv(list.get(17));
//		vo.setRegDate(list.get(18));
	}
	
	
	
}
