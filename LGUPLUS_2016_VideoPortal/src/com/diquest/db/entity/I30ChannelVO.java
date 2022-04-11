package com.diquest.db.entity;

import java.util.List;

public class I30ChannelVO {

	private String serviceId;
	private String serviceName;
	private String serviceEngName;
	private String channelNo;
	private String programId;
	private String programName;
	private String thmImgUrl;
	private String thmImgFileName;
	private String rating;
	private String broadTime;
	private String day;
	private String overseerName;
	private String actor;
	private String pName;
	private String genre1;
	private String genre2;
	private String genre3;
	private String seriesNo;
	private String subName;
	private String broadDate;
	private String localArea;
	private String avResolution;
	private String captionFlag;
	private String dvsFlag;
	private String is51Ch;
	private String filteringCode;
	private String chnlKeyword;
	private String chnlIconUrl;
	private String chnlIconFileName;

	private String programNameChosung;

	private String whereIdx;

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceEngName() {
		return serviceEngName;
	}

	public void setServiceEngName(String serviceEngName) {
		this.serviceEngName = serviceEngName;
	}

	public String getChannelNo() {
		return channelNo;
	}

	public void setChannelNo(String channelNo) {
		this.channelNo = channelNo;
	}

	public String getProgramId() {
		return programId;
	}

	public void setProgramId(String programId) {
		this.programId = programId;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public String getThmImgUrl() {
		return thmImgUrl;
	}

	public void setThmImgUrl(String thmImgUrl) {
		this.thmImgUrl = thmImgUrl;
	}

	public String getThmImgFileName() {
		return thmImgFileName;
	}

	public void setThmImgFileName(String thmImgFileName) {
		this.thmImgFileName = thmImgFileName;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getBroadTime() {
		return broadTime;
	}

	public void setBroadTime(String broadTime) {
		this.broadTime = broadTime;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getOverseerName() {
		return overseerName;
	}

	public void setOverseerName(String overseerName) {
		this.overseerName = overseerName;
	}

	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.pName = pName;
	}

	public String getGenre1() {
		return genre1;
	}

	public void setGenre1(String genre1) {
		this.genre1 = genre1;
	}

	public String getGenre2() {
		return genre2;
	}

	public void setGenre2(String genre2) {
		this.genre2 = genre2;
	}

	public String getGenre3() {
		return genre3;
	}

	public void setGenre3(String genre3) {
		this.genre3 = genre3;
	}

	public String getSeriesNo() {
		return seriesNo;
	}

	public void setSeriesNo(String seriesNo) {
		this.seriesNo = seriesNo;
	}

	public String getSubName() {
		return subName;
	}

	public void setSubName(String subName) {
		this.subName = subName;
	}

	public String getBroadDate() {
		return broadDate;
	}

	public void setBroadDate(String broadDate) {
		this.broadDate = broadDate;
	}

	public String getLocalArea() {
		return localArea;
	}

	public void setLocalArea(String localArea) {
		this.localArea = localArea;
	}

	public String getAvResolution() {
		return avResolution;
	}

	public void setAvResolution(String avResolution) {
		this.avResolution = avResolution;
	}

	public String getCaptionFlag() {
		return captionFlag;
	}

	public void setCaptionFlag(String captionFlag) {
		this.captionFlag = captionFlag;
	}

	public String getDvsFlag() {
		return dvsFlag;
	}

	public void setDvsFlag(String dvsFlag) {
		this.dvsFlag = dvsFlag;
	}

	public String getIs51Ch() {
		return is51Ch;
	}

	public void setIs51Ch(String is51Ch) {
		this.is51Ch = is51Ch;
	}

	public String getFilteringCode() {
		return filteringCode;
	}

	public void setFilteringCode(String filteringCode) {
		this.filteringCode = filteringCode;
	}

	public String getChnlKeyword() {
		return chnlKeyword;
	}

	public void setChnlKeyword(String chnlKeyword) {
		this.chnlKeyword = chnlKeyword;
	}

	public String getChnlIconUrl() {
		return chnlIconUrl;
	}

	public void setChnlIconUrl(String chnlIconUrl) {
		this.chnlIconUrl = chnlIconUrl;
	}

	public String getChnlIconFileName() {
		return chnlIconFileName;
	}

	public void setChnlIconFileName(String chnlIconFileName) {
		this.chnlIconFileName = chnlIconFileName;
	}

	public String getProgramNameChosung() {
		return programNameChosung;
	}

	public void setProgramNameChosung(String programNameChosung) {
		this.programNameChosung = programNameChosung;
	}

	public String getWhereIdx() {
		return whereIdx;
	}

	public void setWhereIdx(String whereIdx) {
		this.whereIdx = whereIdx;
	}

	public void setAll(I30ChannelVO vo, List<String> list) {
		vo.setServiceId(list.get(0));
		vo.setServiceName(list.get(1));
		vo.setServiceEngName(list.get(2));
		vo.setChannelNo(list.get(3));
		vo.setProgramId(list.get(4));
		vo.setProgramName(list.get(5));
		vo.setThmImgUrl(list.get(6));
		vo.setThmImgFileName(list.get(7));
		vo.setRating(list.get(8));
		vo.setBroadTime(list.get(9));
		vo.setDay(list.get(10));
		vo.setOverseerName(list.get(11));
		vo.setActor(list.get(12));
		vo.setpName(list.get(13));
		vo.setGenre1(list.get(14));
		vo.setGenre2(list.get(15));
		vo.setGenre3(list.get(16));
		vo.setSeriesNo(list.get(17));
		vo.setSubName(list.get(18));
		vo.setBroadDate(list.get(19));
		vo.setLocalArea(list.get(20));
		vo.setAvResolution(list.get(21));
		vo.setCaptionFlag(list.get(22));
		vo.setDvsFlag(list.get(23));
		vo.setIs51Ch(list.get(24));
		vo.setFilteringCode(list.get(25));
		vo.setChnlKeyword(list.get(26));
		vo.setChnlIconUrl(list.get(27));
		vo.setChnlIconFileName(list.get(28));
	}

}
