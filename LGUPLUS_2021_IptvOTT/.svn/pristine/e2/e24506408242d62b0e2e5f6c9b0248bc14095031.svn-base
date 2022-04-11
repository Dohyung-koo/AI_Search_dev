package com.diquest.openapi.iptvott.response.entity;


import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import com.diquest.openapi.iptvott.response.FallbackExecution;
import com.diquest.openapi.iptvott.response.IptvottErrorResponse;
import com.diquest.openapi.util.info.ERROR_TYPE;

public class IptvottRespones {

    private String resultCode;

    @JsonProperty("ottList")
    private OttResult_json ottList;
    
    @JsonProperty("resultList")
    private Section_json resultList;

    @JsonIgnore
    private IptvottErrorResponse iptvottErrorResponse;
    
    @JsonIgnore
    private String lgErrorCode;
    
	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

    public OttResult_json getOttList() {
		return ottList;
	}

	public void setOttList(OttResult_json ottList) {
		this.ottList = ottList;
	}

	public Section_json getResultList() {
		return resultList;
	}

	public void setResultList(Section_json resultList) {
		this.resultList = resultList;
	}

    public String getLgErrorCode() {
        return lgErrorCode;
    }

    public void setLgErrorCode(String lgErrorCode) {
        this.lgErrorCode = lgErrorCode;
    }

    public IptvottErrorResponse getIptvottErrorResponse() {
        return iptvottErrorResponse;
    }

    public void setIptvottErrorResponse(String errorCode) {
        IptvottErrorResponse iptvottErrorResponse = new IptvottErrorResponse();
        FallbackExecution fallbackExecution = new FallbackExecution();
        fallbackExecution.setErrorCode(errorCode);
        if(errorCode.equals("20001000")) {
        	fallbackExecution.setErrorMsg(ERROR_TYPE.CODE_20001000.getErrorMessage());
        }else if(errorCode.equals("3000C001")) {
        	fallbackExecution.setErrorMsg(ERROR_TYPE.CODE_3000C001.getErrorMessage());
        }else if(errorCode.equals("3000C001")) {
        	fallbackExecution.setErrorMsg(ERROR_TYPE.CODE_3000C004.getErrorMessage());
        }else if(errorCode.equals("3000S001")) {
        	fallbackExecution.setErrorMsg(ERROR_TYPE.CODE_3000S001.getErrorMessage());
        }else if(errorCode.equals("30000004")) {
        	fallbackExecution.setErrorMsg(ERROR_TYPE.CODE_30000004.getErrorMessage());
        }else if(errorCode.equals("3000C005")) {
        	fallbackExecution.setErrorMsg(ERROR_TYPE.CODE_3000C005.getErrorMessage());
        }else if(errorCode.equals("30000006")) {
        	fallbackExecution.setErrorMsg(ERROR_TYPE.CODE_30000006.getErrorMessage());
        }else if(errorCode.equals("40005000")) {
        	fallbackExecution.setErrorMsg(ERROR_TYPE.CODE_40005000.getErrorMessage());
        }else if(errorCode.equals("40004001")) {
        	fallbackExecution.setErrorMsg(ERROR_TYPE.CODE_40004001.getErrorMessage());
        }else if(errorCode.equals("40004002")) {
        	fallbackExecution.setErrorMsg(ERROR_TYPE.CODE_40004002.getErrorMessage());
        }else if(errorCode.equals("40004003")) {
        	fallbackExecution.setErrorMsg(ERROR_TYPE.CODE_40004003.getErrorMessage());
        }else if(errorCode.equals("40004004")) {
        	fallbackExecution.setErrorMsg(ERROR_TYPE.CODE_40004004.getErrorMessage());
        }else if(errorCode.equals("4000L001")) {
        	fallbackExecution.setErrorMsg(ERROR_TYPE.CODE_4000L001.getErrorMessage());
        }else if(errorCode.equals("4000L002")) {
        	fallbackExecution.setErrorMsg(ERROR_TYPE.CODE_4000L002.getErrorMessage());
        }else if(errorCode.equals("40001000")) {
        	fallbackExecution.setErrorMsg(ERROR_TYPE.CODE_40001000.getErrorMessage());
        }else if(errorCode.equals("40002000")) {
        	fallbackExecution.setErrorMsg(ERROR_TYPE.CODE_40002000.getErrorMessage());
        }else if(errorCode.equals("40003000")) {
        	fallbackExecution.setErrorMsg(ERROR_TYPE.CODE_40003000.getErrorMessage());
        }
        iptvottErrorResponse.setFallbackExecution(fallbackExecution);
        this.iptvottErrorResponse = iptvottErrorResponse;
    }
}
