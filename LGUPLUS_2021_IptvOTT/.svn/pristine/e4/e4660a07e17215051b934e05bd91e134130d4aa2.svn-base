package com.diquest.openapi.iptvott.request;


import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown =  true)
public class IptvottQuickRequest {

    private String uniqueKey = "";				// 고객 특정 key(필수)
    private String authKey = "";					// 클라이언트 인증키 (필수)
    private String encryptYn = "";				// Y(필수)
	private String returnType = "";			// 테이터 타입
	
	private String w = "";
	private String q = "";
	private String outmax = "";
    
    private String errorCode="";
    private String errorResponse = "";
    
    private String host = "";
    private int port;
    
    
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


	public String getW() {
		return w;
	}


	public void setW(String w) {
		this.w = w;
	}


	public String getQ() {
		return q;
	}


	public void setQ(String q) {
		this.q = q;
	}


	public String getOutmax() {
		return outmax;
	}


	public void setOutmax(String outmax) {
		this.outmax = outmax;
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

	
	public boolean hasErrorResponse() {
        if("".equals(errorCode)) {
            return false;
        }

        return true;
    }
}
