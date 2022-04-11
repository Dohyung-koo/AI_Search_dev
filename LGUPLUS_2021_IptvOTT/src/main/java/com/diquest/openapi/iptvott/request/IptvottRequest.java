package com.diquest.openapi.iptvott.request;


import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown =  true)
public class IptvottRequest {
	
	@JsonProperty("q")
    private StructuredQuery structuredQuery;
	
	private String w = "";
	@JsonProperty("section")
    private List<String> section;
	
    private String page = "1";
    private String outmax = "20";
    private String sort = "1"; //1. 정확도순(내림차순), 2. 가나다순(오름차순), 3. 최신순(내림차순)
    private String rating = "01"; // 01 : 일반(Default), 02 : 7세이상, 03 : 12세이상, 04 : 15세이상, 05 : 19세이상, 06 : 방송불가
    
    private String errorCode="";
	private String errorResponse = "";

    private String host = "";
    private int port;
    
    private String collectionName;
    private String ottId;
    
    private String uniqueKey = "";				// 고객 특정 key(필수)
    private String authKey = "";					// 클라이언트 인증키 (필수)
    private String encryptYn = "";				// Y(필수)
	private String returnType = "";			// 테이터 타입

    public StructuredQuery getStructuredQuery() {
        return structuredQuery;
    }

    public void setStructuredQuery(StructuredQuery structuredQuery) {
        this.structuredQuery = structuredQuery;
    }
    
    public String getW() {
		return w;
	}

	public void setW(String w) {
		this.w = w;
	}

	public List<String> getSection() {
		return section;
	}

	public void setSection(List<String> section) {
		this.section = section;
	}

	public String getPage() {
		return page;
	}
	
	public void setPage(String page) {
		this.page = page;
	}
	public String getOutmax() {
		return outmax;
	}
	
	public void setOutmax(String outmax) {
		this.outmax = outmax;
	}
	
	public String getSort() {
		return sort;
	}
	
	public void setSort(String sort) {
		this.sort = sort;
	}
	
	public String getRating() {
		return rating;
	}
	
	public void setRating(String rating) {
		this.rating = rating;
	}
	
	public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
        
    public String getErrorResponse() {
		return errorResponse;
	}

	public void setErrorResponse(String errorResponse) {
		this.errorResponse = errorResponse;
	}

	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}
	
	public String getOttId() {
		return ottId;
	}

	public void setOttId(String ottId) {
		this.ottId = ottId;
	}

	public boolean hasErrorResponse() {
        if("".equals(errorCode)) {
            return false;
        }

        return true;
    }

	public String getUniqueKey() {
		return uniqueKey;
	}

	public void setUniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
	}

	public String getAuthKey() {
		return authKey;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}

	public String getEncryptYn() {
		return encryptYn;
	}

	public void setEncryptYn(String encryptYn) {
		this.encryptYn = encryptYn;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	
	
}
