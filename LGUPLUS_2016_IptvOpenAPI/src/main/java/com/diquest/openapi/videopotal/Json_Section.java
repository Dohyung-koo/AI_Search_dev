package com.diquest.openapi.videopotal;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class Json_Section {
	
	private String section_name="";
	private String usrspec="";
	private String idxname="";
	private String qryflag="";
	private String srtflag="";
	private String totcnt="";
	private String maxcnt="";
	private String outcnt="";
	private String pagenum="";
	private String elapse="";
		
	@JsonProperty("att_list")
	private List  json_att_list;
	
	public String getSection_name() {
		return section_name;
	}

	public void setSection_name(String section_name) {
		this.section_name = section_name;
	}

	public String getUsrspec() {
		return usrspec;
	}

	public void setUsrspec(String usrspec) {
		this.usrspec = usrspec;
	}

	public String getIdxname() {
		return idxname;
	}

	public void setIdxname(String idxname) {
		this.idxname = idxname;
	}

	public String getQryflag() {
		return qryflag;
	}

	public void setQryflag(String qryflag) {
		this.qryflag = qryflag;
	}

	public String getSrtflag() {
		return srtflag;
	}

	public void setSrtflag(String srtflag) {
		this.srtflag = srtflag;
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

	public String getElapse() {
		return elapse;
	}

	public void setElapse(String elapse) {
		this.elapse = elapse;
	}

	public List getJson_att_list() {
		return json_att_list;
	}

	public void setJson_att_list(List json_att_list) {
		this.json_att_list = json_att_list;
	}

}
