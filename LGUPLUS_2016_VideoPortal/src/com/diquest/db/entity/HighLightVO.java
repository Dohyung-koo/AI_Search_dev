package com.diquest.db.entity;

import java.util.List;

import com.diquest.app.common.Method;

public class HighLightVO {
	
	private String resultType;
	private String svcType;
	private String catId;
	private String catName;
	private String contentsId;
	private String contentsType;
	private String contentsName;
	private String contentsInfo;
	private String imgUrl;
	private String duration;
	private String hitCnt;
	private String hAlbumId;
	private String hCategoryId;
	private String startTime;
	private String endTime;
	private String hContentName;
	private String hSeriesDesc;
	private String hSeriesNo;
	private String hSponsorName;
	private String hStillImg;
	private String regDate;
	private String contentsNameChosung;
	private String badgeData;
	private String badgeData2;
	private String onairDate;
	private String deviceData;
	private String startDt;
	private String endDt;
	
	private String whereIdx;
	/*20200603 Choihu 아이돌LIVE 메타 추가작업 MAIN_PROPERTY, SUB_PROPERTY */
	private String mainProperty;
	private String subProperty;



	public String getMainProperty() {
		return mainProperty;
	}

	public void setMainProperty(String mainProperty) {
		this.mainProperty = mainProperty;
	}
	public String getSubProperty() {
		return subProperty;
	}

	public void setSubProperty(String subProperty) {
		this.subProperty = subProperty;
	}
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
	public String getOnairDate() {
		return onairDate;
	}
	public void setOnairDate(String onairDate) {
		this.onairDate = onairDate;
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
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
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
	public String gethAlbumId() {
		return hAlbumId;
	}
	public void sethAlbumId(String hAlbumId) {
		this.hAlbumId = hAlbumId;
	}
	public String gethCategoryId() {
		return hCategoryId;
	}
	public void sethCategoryId(String hCategoryId) {
		this.hCategoryId = hCategoryId;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String gethContentName() {
		return hContentName;
	}
	public void sethContentName(String hContentName) {
		this.hContentName = hContentName;
	}
	public String gethSeriesDesc() {
		return hSeriesDesc;
	}
	public void sethSeriesDesc(String hSeriesDesc) {
		this.hSeriesDesc = hSeriesDesc;
	}
	public String gethSeriesNo() {
		return hSeriesNo;
	}
	public void sethSeriesNo(String hSeriesNo) {
		this.hSeriesNo = hSeriesNo;
	}
	public String gethSponsorName() {
		return hSponsorName;
	}
	public void sethSponsorName(String hSponsorName) {
		this.hSponsorName = hSponsorName;
	}
	public String gethStillImg() {
		return hStillImg;
	}
	public void sethStillImg(String hStillImg) {
		this.hStillImg = hStillImg;
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
	public void setAll(HighLightVO vo, List<String> list){
		vo.setResultType(list.get(0));
		vo.setSvcType(list.get(1));
		vo.setCatId(list.get(2));
		vo.setCatName(list.get(3));
		vo.setContentsId(list.get(4));
		vo.setContentsType(list.get(5));
		vo.setContentsName(list.get(6));
		vo.setContentsInfo(list.get(7));
		vo.setImgUrl(list.get(8));
		vo.setDuration(list.get(9));
		vo.setHitCnt(list.get(10));
		vo.sethAlbumId(list.get(11));
		vo.sethCategoryId(list.get(12));
		vo.setStartTime(list.get(13));
		vo.setEndTime(list.get(14));
		vo.sethContentName(list.get(15));
		vo.sethSeriesDesc(list.get(16));
		vo.sethSeriesNo(list.get(17));
		vo.sethSponsorName(list.get(18));
		vo.sethStillImg(list.get(19));
		vo.setRegDate(list.get(20));
		vo.setBadgeData(list.get(21));
		vo.setBadgeData2(list.get(22));
		vo.setOnairDate(list.get(23));
		vo.setDeviceData(list.get(24));
		vo.setStartDt(list.get(25));
		vo.setEndDt(list.get(26));
		vo.setMainProperty(list.get(27));
		vo.setSubProperty(list.get(28));
		//초성
		vo.setContentsNameChosung(Method.normalizeChosung(list.get(6)));
	}
	
	public void setDramaFields(HighLightVO vo, List<String> list){
//		vo.setResultType(list.get(0));
//		vo.setSvcType(list.get(1));
//		vo.setCatId(list.get(2));
//		vo.setCatName(list.get(3));
//		vo.setContentsId(list.get(4));
//		vo.setContentsType(list.get(5));
		vo.setContentsName(list.get(6));
//		vo.setContentsInfo(list.get(7));
//		vo.setImgUrl(list.get(8));
//		vo.setDuration(list.get(9));
//		vo.setHitCnt(list.get(10));
//		vo.sethAlbumId(list.get(11));
//		vo.sethCategoryId(list.get(12));
//		vo.setStartTime(list.get(13));
//		vo.setEndTime(list.get(14));
//		vo.sethContentName(list.get(15));
//		vo.sethSeriesDesc(list.get(16));
//		vo.sethSeriesNo(list.get(17));
//		vo.sethSponsorName(list.get(18));
//		vo.sethStillImg(list.get(19));
//		vo.setRegDate(list.get(20));
	}
	
}
