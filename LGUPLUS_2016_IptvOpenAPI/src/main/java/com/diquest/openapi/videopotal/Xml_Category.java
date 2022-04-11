package com.diquest.openapi.videopotal;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Xml_Category {
	
	@XmlElement(name = "cat_name")
	private String cat_name="";
	@XmlElement(name = "totcnt")
	private String totcnt="";
	public String getCat_name() {
		return cat_name;
	}
	public void setCat_name(String cat_name) {
		this.cat_name = cat_name;
	}
	public String getTotcnt() {
		return totcnt;
	}
	public void setTotcnt(String totcnt) {
		this.totcnt = totcnt;
	}


}
