package com.diquest.db.entity;

import java.util.List;

public class ThemeVO {

	private String themeId;
	private String themeNmM;
	private String themeNmT;
	private String themeNmP;
	private String themeImgM;
	private String themeImgT;
	private String themeImgP;
	private String keyword;
	private String contentNm;
	private String actor;
	private String director;
	
	private String whereIdx;
	
	
	public String getWhereIdx() {
		return whereIdx;
	}
	
	public String getThemeId() {
		return themeId;
	}

	public void setThemeId(String themeId) {
		this.themeId = themeId;
	}

	public String getThemeNmM() {
		return themeNmM;
	}

	public void setThemeNmM(String themeNmM) {
		this.themeNmM = themeNmM;
	}

	public String getThemeNmT() {
		return themeNmT;
	}

	public void setThemeNmT(String themeNmT) {
		this.themeNmT = themeNmT;
	}

	public String getThemeNmP() {
		return themeNmP;
	}

	public void setThemeNmP(String themeNmP) {
		this.themeNmP = themeNmP;
	}

	public String getThemeImgM() {
		return themeImgM;
	}

	public void setThemeImgM(String themeImgM) {
		this.themeImgM = themeImgM;
	}

	public String getThemeImgT() {
		return themeImgT;
	}

	public void setThemeImgT(String themeImgT) {
		this.themeImgT = themeImgT;
	}

	public String getThemeImgP() {
		return themeImgP;
	}

	public void setThemeImgP(String themeImgP) {
		this.themeImgP = themeImgP;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getContentNm() {
		return contentNm;
	}

	public void setContentNm(String contentNm) {
		this.contentNm = contentNm;
	}

	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public void setWhereIdx(String whereIdx) {
		this.whereIdx = whereIdx;
	}

	public void setAll(ThemeVO vo, List<String> list){
		vo.setThemeId(list.get(0));
		vo.setThemeNmM(list.get(1));
		vo.setThemeNmT(list.get(2));
		vo.setThemeNmP(list.get(3));
		vo.setThemeImgM(list.get(4));
		vo.setThemeImgT(list.get(5));
		vo.setThemeImgP(list.get(6));
		vo.setKeyword(list.get(7));
		vo.setContentNm(list.get(8));
		vo.setActor(list.get(9));
		vo.setDirector(list.get(10));
	}
	
}
