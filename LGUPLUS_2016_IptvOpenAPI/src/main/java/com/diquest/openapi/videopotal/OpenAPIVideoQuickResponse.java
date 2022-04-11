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
public class OpenAPIVideoQuickResponse {
	
	@XmlAttribute
	private String date="";
	
	@XmlElement(name = "section_list")
	private List<Section_List> section_list;
	
	@XmlElementWrapper(name = "word_list")
	@XmlElement(name = "word")
	private List<String> autoWord;
	
	@XmlElement(name = "corqry")
	private String corqry;
	
	private OpenAPIErrorResponse errorResponse;

	public OpenAPIErrorResponse getErrorResponse() {
		return errorResponse;
	}

	public void setErrorResponse(OpenAPIErrorResponse errorResponse) {
		this.errorResponse = errorResponse;
	}

	public List<String> getAutoWord() {
		return autoWord;
	}

	public void setAutoWord(List<String> autoWord) {
		this.autoWord = autoWord;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<Section_List> getSection_list() {
		return section_list;
	}

	public void setSection_list(List<Section_List> section_list) {
		this.section_list = section_list;
	}

	public String getCorqry() {
		return corqry;
	}

	public void setCorqry(String corqry) {
		this.corqry = corqry;
	}

}
