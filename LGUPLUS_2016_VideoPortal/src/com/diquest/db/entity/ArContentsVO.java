package com.diquest.db.entity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.diquest.app.common.Method;

public class ArContentsVO {
	 private String contentsKey;
	 private String revisionCode;
	 private String contentsName;
	 private String contentsType;
	 private String contentsDesc;
	 private String contentsDetailDesc;
	 private String contentsArtist;
	 private String contentsTag;
	 private String thumbUrl;
	 private String image1Url;
	 private String image2Url;
	 private String image3Url;
	 private String image4Url;
	 private String image5Url;
	 private String playTime;
	 private String viewCnt;
	 private String startDate;
	 private String endDate;
	 private String regDate;
	 private String contentsNameChosung;

	
	public String getContentsKey() {
		return contentsKey;
	}


	public void setContentsKey(String contentsKey) {
		this.contentsKey = contentsKey;
	}


	public String getRevisionCode() {
		return revisionCode;
	}


	public void setRevisionCode(String revisionCode) {
		this.revisionCode = revisionCode;
	}


	public String getContentsName() {
		return contentsName;
	}


	public void setContentsName(String contentsName) {
		this.contentsName = contentsName;
	}


	public String getContentsType() {
		return contentsType;
	}


	public void setContentsType(String contentsType) {
		this.contentsType = contentsType;
	}


	public String getContentsDesc() {
		return contentsDesc;
	}


	public void setContentsDesc(String contentsDesc) {
		this.contentsDesc = contentsDesc;
	}


	public String getContentsDetailDesc() {
		return contentsDetailDesc;
	}


	public void setContentsDetailDesc(String contentsDetailDesc) {
		this.contentsDetailDesc = contentsDetailDesc;
	}


	public String getContentsArtist() {
		return contentsArtist;
	}


	public void setContentsArtist(String contentsArtist) {
		this.contentsArtist = contentsArtist;
	}


	public String getContentsTag() {
		return contentsTag;
	}


	public void setContentsTag(String contentsTag) {
		this.contentsTag = contentsTag;
	}


	public String getThumbUrl() {
		return thumbUrl;
	}


	public void setThumbUrl(String thumbUrl) {
		this.thumbUrl = thumbUrl;
	}


	public String getImage1Url() {
		return image1Url;
	}


	public void setImage1Url(String image1Url) {
		this.image1Url = image1Url;
	}


	public String getImage2Url() {
		return image2Url;
	}


	public void setImage2Url(String image2Url) {
		this.image2Url = image2Url;
	}


	public String getImage3Url() {
		return image3Url;
	}


	public void setImage3Url(String image3Url) {
		this.image3Url = image3Url;
	}


	public String getImage4Url() {
		return image4Url;
	}


	public void setImage4Url(String image4Url) {
		this.image4Url = image4Url;
	}


	public String getImage5Url() {
		return image5Url;
	}


	public void setImage5Url(String image5Url) {
		this.image5Url = image5Url;
	}


	public String getPlayTime() {
		return playTime;
	}


	public void setPlayTime(String playTime) {
		this.playTime = playTime;
	}


	public String getViewCnt() {
		return viewCnt;
	}


	public void setViewCnt(String viewCnt) {
		this.viewCnt = viewCnt;
	}


	public String getStartDate() {
		return startDate;
	}


	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}


	public String getEndDate() {
		return endDate;
	}


	public void setEndDate(String endDate) {
		this.endDate = endDate;
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


	public void setAll(VrContentsVO vo, List<String> list) {
		vo.setCtgryId(list.get(0));
		vo.setCtgryNm(list.get(1));
		vo.setCntntsGroupId(list.get(2));
		vo.setCntntsGroupNm(list.get(3));
		vo.setGenre(list.get(4));
		vo.setSersCntntsAt(list.get(5));
		vo.setSersDfk(list.get(6));
		vo.setSersComptAt(list.get(7));
		vo.setMakr(list.get(8));
		vo.setUseGradCode(list.get(9));
		vo.setUseGradName(list.get(10));
		vo.setUseGradAge(list.get(11));
		vo.setContId(list.get(12));
		vo.setContNm(list.get(13));
		vo.setContTme(list.get(14));
		vo.setContDc(list.get(15));
		vo.setReprThumbUrl(list.get(16));
		vo.setContImageUrl(list.get(17));
		vo.setGameVodUrl(list.get(18));
		vo.setContUrl(list.get(19));
		vo.setContCpcty(list.get(20));
		vo.setContPrc(list.get(21));
		vo.setGameHmd(list.get(22));
		vo.setGameCntlr(list.get(23));
		vo.setGameCmtDe(list.get(24));
		vo.setMultGameAt(list.get(25));
		vo.setNtwrkUseAt(list.get(26));
		vo.setCntrlUseAt(list.get(27));
		vo.setPnrmPlace(list.get(28));
		vo.setPnrmScrinCo(list.get(29));
		vo.setPnrmPlayUrl(list.get(30));
		vo.setCntntsGroupNmChosung(Method.normalizeChosung(list.get(3)));
		vo.setContNmChosung(Method.normalizeChosung(list.get(13)));
	}

}
