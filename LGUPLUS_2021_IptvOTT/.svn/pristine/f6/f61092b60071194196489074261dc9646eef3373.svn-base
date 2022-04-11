package com.diquest.openapi.iptvott.response.entity;


import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import com.diquest.openapi.iptvott.response.FallbackExecution;
import com.diquest.openapi.iptvott.response.IptvottErrorResponse;

public class IptvottQuickRespones {
	
	@JsonProperty("resultInfo")
	private ResultInfo resultInfo;
	
	@JsonProperty("word")
	private List<String> autoWord;
	
    @JsonIgnore
    private IptvottErrorResponse iptvottErrorResponse;
    
    @JsonIgnore
    private String lgErrorCode;

	public List<String> getAutoWord() {
		return autoWord;
	}

	public void setAutoWord(List<String> autoWord) {
		this.autoWord = autoWord;
	}

	public ResultInfo getResultInfo() {
		return resultInfo;
	}

	public void setResultInfo(ResultInfo resultInfo) {
		this.resultInfo = resultInfo;
	}

	public IptvottErrorResponse getIptvottErrorResponse() {
		return iptvottErrorResponse;
	}

	public void setIptvottErrorResponse(String errorCode) {
        IptvottErrorResponse iptvottErrorResponse = new IptvottErrorResponse();
        FallbackExecution fallbackExecution = new FallbackExecution();
        fallbackExecution.setErrorCode(errorCode);
        iptvottErrorResponse.setFallbackExecution(fallbackExecution);
		this.iptvottErrorResponse = iptvottErrorResponse;
	}

	public String getLgErrorCode() {
		return lgErrorCode;
	}

	public void setLgErrorCode(String lgErrorCode) {
		this.lgErrorCode = lgErrorCode;
	}
}
