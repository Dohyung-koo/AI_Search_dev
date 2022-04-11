package com.diquest.openapi.videopotal;

import org.codehaus.jackson.annotate.JsonProperty;

public class V_channel {
	
	private String serviceId="";
	private String serviceName="";
	private String serviceEngName="";
	private String channelNo="";
	private String programId="";
	private String programName="";
	private String thmImgUrl="";
	private String thmImgFileName="";
	private String rating="";
	private String broadTime="";
	private String day="";
	private String overseerName="";
	private String actor="";
	private String PName="";
	private String genre1="";
	private String genre2="";
	private String genre3="";
	private String seriesNo="";
	private String subName="";
	private String broadDate="";
	private String localArea="";
	private String avResolution="";
	private String captionFlag="";
	private String dvsFlag="";
	private String is51Ch="";
	private String filteringCode="";
	private String chnlKeyword="";
	private String chnlIconUrl="";
	private String chnlIconFileName="";
	private String weight="";
	
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
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
	
	@JsonProperty("PName")
	public String getPName() {
		return PName;
	}
	public void setPName(String pName) {
		PName = pName;
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
	public String getServiceEngName() {
		return serviceEngName;
	}
	public void setServiceEngName(String serviceEngName) {
		this.serviceEngName = serviceEngName;
	}
	
}