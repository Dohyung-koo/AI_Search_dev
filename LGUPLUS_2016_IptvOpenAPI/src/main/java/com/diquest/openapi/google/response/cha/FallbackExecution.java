package com.diquest.openapi.google.response.cha;

import org.codehaus.jackson.annotate.JsonProperty;

public class FallbackExecution {

    @JsonProperty("errorCode")
    private String errorCode;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
