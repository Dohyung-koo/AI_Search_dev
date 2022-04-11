package com.diquest.openapi.google.response.entity;


import com.diquest.openapi.google.response.FallbackExecution;
import com.diquest.openapi.google.response.GoogleErrorResponse;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class GoogleEntRespones {

    @JsonProperty("operatorMediaQueryResponse")
    private OperatorMediaQueryResponse operatorMediaQueryResponse;

    @JsonIgnore
    private GoogleErrorResponse googleErrorResponse;
    @JsonIgnore
    private String lgErrorCode;

    public void setGoogleErrorCode(String errorCode) {
        GoogleErrorResponse googleErrorResponse = new GoogleErrorResponse();
        FallbackExecution fallbackExecution = new FallbackExecution();
        fallbackExecution.setErrorCode(errorCode);
        googleErrorResponse.setFallbackExecution(fallbackExecution);
        this.googleErrorResponse = googleErrorResponse;
    }

    public String getLgErrorCode() {
        return lgErrorCode;
    }

    public void setLgErrorCode(String lgErrorCode) {
        this.lgErrorCode = lgErrorCode;
    }

    public GoogleErrorResponse getGoogleErrorResponse() {
        return googleErrorResponse;
    }

    public void setGoogleErrorResponse(GoogleErrorResponse googleErrorResponse) {
        this.googleErrorResponse = googleErrorResponse;
    }

    public OperatorMediaQueryResponse getOperatorMediaQueryResponse() {
        return operatorMediaQueryResponse;
    }

    public void setOperatorMediaQueryResponse(OperatorMediaQueryResponse operatorMediaQueryResponse) {
        this.operatorMediaQueryResponse = operatorMediaQueryResponse;
    }
}
