package com.diquest.openapi.util;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import com.diquest.openapi.util.OpenAPIErrorResponseJson;
import com.diquest.openapi.videopotal.AutoWord;

public class OpenAPIQuickResponseJson {
	
	
	@JsonProperty("word")
	private List<AutoWord> autoWord;
	

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


}
