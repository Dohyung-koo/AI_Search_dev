package com.diquest.openapi.videopotal;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.diquest.openapi.util.OpenAPIErrorResponse;

@XmlRootElement(name = "meta_storage_list")
@XmlAccessorType(XmlAccessType.FIELD)
public class OpenAPIVideoResponse {
	
	@XmlElement(name = "category_list")
	private List<Category_List> category_list;
	
	@XmlElement(name = "section_list")
	private List<Section_List> section_list;

	private OpenAPIErrorResponse errorResponse;

	public OpenAPIErrorResponse getErrorResponse() {
		return errorResponse;
	}

	@XmlAttribute
	private String date="";
	
	@XmlElement(name = "corqry")
	private String corqry;
	
	@XmlElement(name = "directurl")
	private String directurl;
	
	@XmlElement(name = "stcflag")
	private String stcflag;
	
//	@XmlElementWrapper(name = "relword_list")
//	@XmlElement(name = "relword")
//	private List<String> relWord;
	
	public void setErrorResponse(OpenAPIErrorResponse errorResponse) {
		this.errorResponse = errorResponse;
	}
	
	public List<Category_List> getCategory_list() {
		return category_list;
	}

	public void setCategory_list(List<Category_List> category_list) {
		this.category_list = category_list;
	}



	public List<Section_List> getSection_list() {
		return section_list;
	}

	public void setSection_list(List<Section_List> section_list) {
		this.section_list = section_list;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCorqry() {
		return corqry;
	}

	public void setCorqry(String corqry) {
		this.corqry = corqry;
	}

	public String getDirecturl() {
		return directurl;
	}

	public void setDirecturl(String directurl) {
		this.directurl = directurl;
	}

	public String getStcflag() {
		return stcflag;
	}

	public void setStcflag(String stcflag) {
		this.stcflag = stcflag;
	}

//	public List<String> getRelWord() {
//		return relWord;
//	}
//
//	public void setRelWord(List<String> relWord) {
//		this.relWord = relWord;
//	}
	
}
