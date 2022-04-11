package com.diquest.db.entity;

import java.util.List;

public class MovieSumInfoVO {
	private String contsSeq ;
	private String regDate ;
	private String categoryId ;
	private String categoryName ;
	private String bbsId ;
	private String bbsName ;
	private String sumSale ;
	private String sumAudience ;
	private String code ;
	private String url;
	private String whereIdx;
	
	public String getContsSeq() {
		return contsSeq;
	}

	public void setContsSeq(String contsSeq) {
		this.contsSeq = contsSeq;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getBbsId() {
		return bbsId;
	}

	public void setBbsId(String bbsId) {
		this.bbsId = bbsId;
	}

	public String getBbsName() {
		return bbsName;
	}

	public void setBbsName(String bbsName) {
		this.bbsName = bbsName;
	}

	public String getSumSale() {
		return sumSale;
	}

	public void setSumSale(String sumSale) {
		this.sumSale = sumSale;
	}

	public String getSumAudience() {
		return sumAudience;
	}

	public void setSumAudience(String sumAudience) {
		this.sumAudience = sumAudience;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getWhereIdx() {
		return whereIdx;
	}

	public void setWhereIdx(String whereIdx) {
		this.whereIdx = whereIdx;
	}

	public void setAll(MovieSumInfoVO vo, List<String> list){
	}
}
