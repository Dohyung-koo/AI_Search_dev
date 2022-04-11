package com.diquest.openapi.iptvott.request;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class Meta {
	private String contents;
	@JsonProperty("cast")
	private List<String> cast;
	private String director; 
	private String genre;
	private String nation;
	private String season;
	private String episode;
	private String pg;
	private String pay;
	private String ott;
	@JsonProperty("keyword")
	private List<String> keyword;
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public List<String> getCast() {
		return cast;
	}
	public void setCast(List<String> cast) {
		this.cast = cast;
	}
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getSeason() {
		return season;
	}
	public void setSeason(String season) {
		this.season = season;
	}
	public String getEpisode() {
		return episode;
	}
	public void setEpisode(String episode) {
		this.episode = episode;
	}
	public String getPg() {
		return pg;
	}
	public void setPg(String pg) {
		this.pg = pg;
	}
	public String getPay() {
		return pay;
	}
	public void setPay(String pay) {
		this.pay = pay;
	}
	public String getOtt() {
		return ott;
	}
	public void setOtt(String ott) {
		this.ott = ott;
	}
	public List<String> getKeyword() {
		return keyword;
	}
	public void setKeyword(List<String> keyword) {
		this.keyword = keyword;
	}	
}