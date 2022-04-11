package com.diquest.openapi.iptvott.response.entity;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class Iptvott_vod {
	private String ottAlbumId;
	private String ottAlbumName;
	private String ottId;
	private String ottName;
	private String ottGrpId;
	private String ottGrpName;
	private String runTime;
	private String player;
	private String director;
	private String distrubutorName;
	private String releaseDate;
	private String createYear;
	private String ottGenreName;
	private String rating;
	private String price;
	private String posterFileName;
	private String stillFileName;
	private String countryOrigin;
	private String point;
	private String ottType;
	private String linkurl;
	private String superId;
	private String resultType;
	private String catId;
	private String seriesNo;
	private String title;
	private String closeYn;
	@JsonProperty("relOttList")
	private List<RelOttList> relOttList;
	
	public String getOttAlbumId() {
		return ottAlbumId;
	}
	public void setOttAlbumId(String ottAlbumId) {
		this.ottAlbumId = ottAlbumId;
	}
	public String getOttAlbumName() {
		return ottAlbumName;
	}
	public void setOttAlbumName(String ottAlbumName) {
		this.ottAlbumName = ottAlbumName;
	}
	public String getOttId() {
		return ottId;
	}
	public void setOttId(String ottId) {
		this.ottId = ottId;
	}
	public String getOttName() {
		return ottName;
	}
	public void setOttName(String ottName) {
		this.ottName = ottName;
	}
	public String getOttGrpId() {
		return ottGrpId;
	}
	public void setOttGrpId(String ottGrpId) {
		this.ottGrpId = ottGrpId;
	}
	public String getOttGrpName() {
		return ottGrpName;
	}
	public void setOttGrpName(String ottGrpName) {
		this.ottGrpName = ottGrpName;
	}
	public String getRunTime() {
		return runTime;
	}
	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}
	public String getPlayer() {
		return player;
	}
	public void setPlayer(String player) {
		this.player = player;
	}
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	public String getDistrubutorName() {
		return distrubutorName;
	}
	public void setDistrubutorName(String distrubutorName) {
		this.distrubutorName = distrubutorName;
	}
	public String getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
	public String getCreateYear() {
		return createYear;
	}
	public void setCreateYear(String createYear) {
		this.createYear = createYear;
	}
	public String getOttGenreName() {
		return ottGenreName;
	}
	public void setOttGenreName(String ottGenreName) {
		this.ottGenreName = ottGenreName;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getPosterFileName() {
		return posterFileName;
	}
	public void setPosterFileName(String posterFileName) {
		this.posterFileName = posterFileName;
	}
	public String getStillFileName() {
		return stillFileName;
	}
	public void setStillFileName(String stillFileName) {
		this.stillFileName = stillFileName;
	}
	public String getCountryOrigin() {
		return countryOrigin;
	}
	public void setCountryOrigin(String countryOrigin) {
		this.countryOrigin = countryOrigin;
	}
	public String getPoint() {
		return point;
	}
	public void setPoint(String point) {
		this.point = point;
	}
	public String getOttType() {
		return ottType;
	}
	public void setOttType(String ottType) {
		this.ottType = ottType;
	}
	public String getLinkurl() {
		return linkurl;
	}
	public void setLinkurl(String linkurl) {
		this.linkurl = linkurl;
	}
	public String getSuperId() {
		return superId;
	}
	public void setSuperId(String superId) {
		this.superId = superId;
	}
	public String getResultType() {
		return resultType;
	}
	public void setResultType(String resultType) {
		this.resultType = resultType;
	}
	public String getCatId() {
		return catId;
	}
	public void setCatId(String catId) {
		this.catId = catId;
	}
	public String getSeriesNo() {
		return seriesNo;
	}
	public void setSeriesNo(String seriesNo) {
		this.seriesNo = seriesNo;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCloseYn() {
		return closeYn;
	}
	public void setCloseYn(String closeYn) {
		this.closeYn = closeYn;
	}
	public List<RelOttList> getRelOttList() {
		return relOttList;
	}
	public void setRelOttList(List<RelOttList> relOttList) {
		this.relOttList = relOttList;
	}	
}
