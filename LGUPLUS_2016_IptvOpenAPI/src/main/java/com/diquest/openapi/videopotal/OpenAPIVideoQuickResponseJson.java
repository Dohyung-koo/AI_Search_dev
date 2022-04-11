package com.diquest.openapi.videopotal;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import com.diquest.openapi.util.OpenAPIErrorResponseJson;

public class OpenAPIVideoQuickResponseJson {
	
	@JsonProperty("section_list")
	private List json_section_list;
	
	@JsonProperty("word")
	private List<AutoWord> autoWord;
	
	@JsonProperty("corqry")
	private String corqry;
	
	public List<AutoWord> getAutoWord() {
		return autoWord;
	}
	
	@JsonIgnore
	private OpenAPIErrorResponseJson errorResponse;
	

	public OpenAPIErrorResponseJson getErrorResponse() {
		return errorResponse;
	}

	public void setErrorResponse(OpenAPIErrorResponseJson errorResponse) {
		this.errorResponse = errorResponse;
	}

	public void setAutoWord(List<AutoWord> autoWord) {
		this.autoWord = autoWord;
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

}
