package com.diquest.db.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CjContentsVO {
	
	private String resultType;
	private String svcType;
	private String catId;
	private String catName;
	private String contentId;
	private String contentType;
	private String contentName;
	private String contentInfo;
	private String imgUrl;
	private String hitCnt;
	private String channelId;
	private String programId;
	private String clipId;
	private String clipTitle;
	private String clipContentId;
	private String clipContentTitle;
	private String cornerid;
	private String cliporder;
	private String mediaurl;
	private String clipContentImg;
	private String playtime;
	private String mezzoad;
	private String isfullvod;
	private String broaddate;
	private String contentnumber;
	private String startDt;
	private String endDt;
	private String badgeData;
	private String badgeData2;
	private String deviceData;
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

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContentName() {
		return contentName;
	}

	public void setContentName(String contentName) {
		this.contentName = contentName;
	}

	public String getContentInfo() {
		return contentInfo;
	}

	public void setContentInfo(String contentInfo) {
		this.contentInfo = contentInfo;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getHitCnt() {
		return hitCnt;
	}

	public void setHitCnt(String hitCnt) {
		this.hitCnt = hitCnt;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getProgramId() {
		return programId;
	}

	public void setProgramId(String programId) {
		this.programId = programId;
	}

	public String getClipId() {
		return clipId;
	}

	public void setClipId(String clipId) {
		this.clipId = clipId;
	}

	public String getClipTitle() {
		return clipTitle;
	}

	public void setClipTitle(String clipTitle) {
		this.clipTitle = clipTitle;
	}

	public String getClipContentId() {
		return clipContentId;
	}

	public void setClipContentId(String clipContentId) {
		this.clipContentId = clipContentId;
	}

	public String getClipContentTitle() {
		return clipContentTitle;
	}

	public void setClipContentTitle(String clipContentTitle) {
		this.clipContentTitle = clipContentTitle;
	}

	public String getCornerid() {
		return cornerid;
	}

	public void setCornerid(String cornerid) {
		this.cornerid = cornerid;
	}

	public String getCliporder() {
		return cliporder;
	}

	public void setCliporder(String cliporder) {
		this.cliporder = cliporder;
	}

	public String getMediaurl() {
		return mediaurl;
	}

	public void setMediaurl(String mediaurl) {
		this.mediaurl = mediaurl;
	}

	public String getClipContentImg() {
		return clipContentImg;
	}

	public void setClipContentImg(String clipContentImg) {
		this.clipContentImg = clipContentImg;
	}

	public String getPlaytime() {
		return playtime;
	}

	public void setPlaytime(String playtime) {
		this.playtime = playtime;
	}

	public String getMezzoad() {
		return mezzoad;
	}

	public void setMezzoad(String mezzoad) {
		this.mezzoad = mezzoad;
	}

	public String getIsfullvod() {
		return isfullvod;
	}

	public void setIsfullvod(String isfullvod) {
		this.isfullvod = isfullvod;
	}

	public String getBroaddate() {
		return broaddate;
	}

	public void setBroaddate(String broaddate) {
		this.broaddate = broaddate;
	}

	public String getContentnumber() {
		return contentnumber;
	}

	public void setContentnumber(String contentnumber) {
		this.contentnumber = contentnumber;
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

	public String getDeviceData() {
		return deviceData;
	}

	public void setDeviceData(String deviceData) {
		this.deviceData = deviceData;
	}

	// public String getId() {
	// return id;
	// }
	// public void setId(String id) {
	// this.id = id;
	// }
	private String getRandomFourNumber() {
		double r = Math.random();
		double squre = 10000;
		int aa = (int) (r * squre);
		String result = String.valueOf(aa);
		while (result.length() < 8) {
			result = "0" + result;
		}
		return result;
	}

	public String getNowTime() {
		Date currentTime = new Date();
		SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		String mTime = mSimpleDateFormat.format(currentTime);

		return mTime;
	}

	public void setAll(CjContentsVO vo, List<String> list) {
		vo.setResultType(list.get(0));
		vo.setSvcType(list.get(1));
		vo.setCatId(list.get(2));
		vo.setCatName(list.get(3));
		vo.setContentId(list.get(4));
		vo.setContentType(list.get(5));
		vo.setContentName(list.get(6));
		vo.setContentInfo(list.get(7));
		vo.setImgUrl(list.get(8));
		vo.setHitCnt(list.get(9));
		vo.setChannelId(list.get(10));
		vo.setProgramId(list.get(11));
		vo.setClipId(list.get(12));
		vo.setClipTitle(list.get(13));
		vo.setClipContentId(list.get(14));
		vo.setClipContentTitle(list.get(15));
		vo.setCornerid(list.get(16));
		vo.setCliporder(list.get(17));
		vo.setMediaurl(list.get(18));
		vo.setClipContentImg(list.get(19));
		vo.setPlaytime(list.get(20));
		vo.setMezzoad(list.get(21));
		vo.setIsfullvod(list.get(22));
		vo.setBroaddate(list.get(23));
		vo.setContentnumber(list.get(24));
		vo.setStartDt(list.get(25));
		vo.setEndDt(list.get(26));
		vo.setBadgeData(list.get(27));
		vo.setBadgeData2(list.get(28));
		vo.setDeviceData(list.get(29));
		vo.setMainProperty(list.get(30));
		vo.setSubProperty(list.get(31));
		//vo.setCntntsGroupNmChosung(Method.normalizeChosung(list.get(3)));
	}

}
