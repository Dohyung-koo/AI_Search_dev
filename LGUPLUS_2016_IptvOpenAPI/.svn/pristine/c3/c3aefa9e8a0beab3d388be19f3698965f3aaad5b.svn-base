package com.diquest.openapi.util;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class OpenAPIErrorResponseJson {
	
	@JsonProperty("error")
	private Json_Error json_Error;

	@JsonIgnore
	private OpenAPIErrorResponseJson errorResponse;
	
	public Json_Error getJson_Error() {
		return json_Error;
	}

	public void setJson_Error(Json_Error json_Error) {
		this.json_Error = json_Error;
	}
	
	public void setErrorResponse(OpenAPIErrorResponseJson errorResponse) {
		this.errorResponse = errorResponse;
	}

}
