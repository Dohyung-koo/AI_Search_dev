package com.diquest.openapi.util;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class Json_Section {

	private String section_name="";
	private String totcnt="";
	private String maxcnt="";
	private String outcnt="";
	private String pagenum="";
		
	@JsonProperty("att_list")
	private List  json_att_list;
	
	public String getSection_name() {
		return section_name;
	}

	public void setSection_name(String section_name) {
		this.section_name = section_name;
	}

	public String getTotcnt() {
		return totcnt;
	}

	public void setTotcnt(String totcnt) {
		this.totcnt = totcnt;
	}

	public String getMaxcnt() {
		return maxcnt;
	}

	public void setMaxcnt(String maxcnt) {
		this.maxcnt = maxcnt;
	}

	public String getOutcnt() {
		return outcnt;
	}

	public void setOutcnt(String outcnt) {
		this.outcnt = outcnt;
	}

	public String getPagenum() {
		return pagenum;
	}

	public void setPagenum(String pagenum) {
		this.pagenum = pagenum;
	}

	public List getJson_att_list() {
		return json_att_list;
	}

	public void setJson_att_list(List json_att_list) {
		this.json_att_list = json_att_list;
	}

}
