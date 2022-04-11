package com.diquest.db.entity;


public class DramaTermRelationVO {
	private String collectionId;
	private String indexId;
	private String termId;
	private String relTermId;
	private String relOption;
	private String relTermIdMod;
	private String score;
	private String flagAct;
	public String getCollectionId() {
		return collectionId;
	}
	public void setCollectionId(String collectionId) {
		this.collectionId = collectionId;
	}
	public String getIndexId() {
		return indexId;
	}
	public void setIndexId(String indexId) {
		this.indexId = indexId;
	}
	public String getTermId() {
		return termId;
	}
	public void setTermId(String termId) {
		this.termId = termId;
	}
	public String getRelTermId() {
		return relTermId;
	}
	public void setRelTermId(String relTermId) {
		this.relTermId = relTermId;
	}
	public String getRelOption() {
		return relOption;
	}
	public void setRelOption(String relOption) {
		this.relOption = relOption;
	}
	public String getRelTermIdMod() {
		return relTermIdMod;
	}
	public void setRelTermIdMod(String relTermIdMod) {
		this.relTermIdMod = relTermIdMod;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getFlagAct() {
		return flagAct;
	}
	public void setFlagAct(String flagAct) {
		this.flagAct = flagAct;
	}
}
