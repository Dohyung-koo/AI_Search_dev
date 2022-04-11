package com.diquest.openapi.util;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import com.diquest.openapi.util.OpenAPIErrorResponseJson;

public class OpenAPIResponseJson {
	@JsonProperty("section_list")
	private List json_section_list;
	
	@JsonIgnore
	private OpenAPIErrorResponseJson errorResponse;

	public OpenAPIErrorResponseJson getErrorResponse() {
		return errorResponse;
	}
	
	public void setErrorResponse(OpenAPIErrorResponseJson errorResponse) {
		this.errorResponse = errorResponse;
	}
	
	public List getJson_section_list() {
		return json_section_list;
	}

	public void setJson_section_list(List json_section_list) {
		this.json_section_list = json_section_list;
	}

}
