package com.diquest.openapi.iptvott.response;

import org.codehaus.jackson.annotate.JsonProperty;

public class FallbackExecution {

    @JsonProperty("errorCode")
    private String errorCode;
    
    @JsonProperty("errorMsg")
    private String errorMsg;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
