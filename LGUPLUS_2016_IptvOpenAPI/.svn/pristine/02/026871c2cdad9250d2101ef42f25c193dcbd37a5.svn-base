package com.diquest.openapi.iptvpre;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import com.diquest.openapi.util.OpenAPIErrorResponseJson;

public class OpenAPIVideoResponseJson {
	
	@JsonProperty("section_list")
	private List json_section_list;
	
	@JsonProperty("category_list")
	private List category_list;
	
	@JsonProperty("corqry")
	private String corqry;
	
	@JsonProperty("directurl")
	private String directurl;
	
//	@JsonProperty("relword")
//	private List<String> relWord;
	
	@JsonProperty("stcflag")
	private String stcflag;
	
	@JsonIgnore
	private OpenAPIErrorResponseJson errorResponse;

	public OpenAPIErrorResponseJson getErrorResponse() {
		return errorResponse;
	}
	
	public String getStcflag() {
		return stcflag;
	}

	public void setStcflag(String stcflag) {
		this.stcflag = stcflag;
	}

	public void setErrorResponse(OpenAPIErrorResponseJson errorResponse) {
		this.errorResponse = errorResponse;
	}
	
	
	public List getCategory_list() {
		return category_list;
	}

	public void setCategory_list(List category_list) {
		this.category_list = category_list;
	}

	public List getJson_section_list() {
		return json_section_list;
	}

	public void setJson_section_list(List json_section_list) {
		this.json_section_list = json_section_list;
	}

	public String getCorqry() {
		return corqry;
	}

	public void setCorqry(String corqry) {
		this.corqry = corqry;
	}

//	public List<String> getRelWord() {
//		return relWord;
//	}
//
//	public void setRelWord(List<String> relWord) {
//		this.relWord = relWord;
//	}

	public String getDirecturl() {
		return directurl;
	}

	public void setDirecturl(String directurl) {
		this.directurl = directurl;
	}

}
