package com.diquest.openapi.iptvott.response.cha;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import com.diquest.openapi.iptvott.response.FallbackExecution;
import com.diquest.openapi.iptvott.response.IptvottErrorResponse;

public class GoogleChaRespones {

    @JsonProperty("directExecution")
    private DirectExecution directExecution;

    @JsonIgnore
    private IptvottErrorResponse googleErrorResponse;

    @JsonIgnore
    private String lgErrorCode;

    public void setGoogleErrorCode(String errorCode) {
        IptvottErrorResponse googleErrorResponse = new IptvottErrorResponse();
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

    public DirectExecution getDirectExecution() {
        return directExecution;
    }

    public void setDirectExecution(DirectExecution directExecution) {
        this.directExecution = directExecution;
    }

    public IptvottErrorResponse getGoogleErrorResponse() {
        return googleErrorResponse;
    }

    public void setGoogleErrorResponse(IptvottErrorResponse googleErrorResponse) {
        this.googleErrorResponse = googleErrorResponse;
    }
}
