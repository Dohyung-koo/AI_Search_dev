package com.diquest.openapi.videopotal;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Xml_Section {
	
	@XmlElement(name = "section_name")
	private String section_name="";
	@XmlElement(name = "usrspec")
	private String usrspec="";
	@XmlElement(name = "idxname")
	private String idxname="";
	@XmlElement(name = "qryflag")
	private String qryflag="";
	@XmlElement(name = "srtflag")
	private String srtflag="";
	@XmlElement(name = "totcnt")
	private String totcnt="";
	@XmlElement(name = "maxcnt")
	private String maxcnt="";
	@XmlElement(name = "outcnt")
	private String outcnt="";
	@XmlElement(name = "pagenum")
	private String pagenum="";
	@XmlElement(name = "elapse")
	private String elapse="";
	
	@XmlElement(name = "doc_list")
	private List<V_doc> v_docs;
	
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

	public List<V_doc> getV_docs() {
		return v_docs;
	}

	public void setV_docs(List<V_doc> v_docs) {
		this.v_docs = v_docs;
	}

}
