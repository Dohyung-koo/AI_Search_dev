package com.diquest.openapi.util;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class Json_Error {
	
	@JsonProperty("code")
	private String code;
	@JsonProperty("message")
	private String message;
	@JsonProperty("suberror")
	private List<Suberror> suberror;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<Suberror> getSuberror() {
		return suberror;
	}
	public void setSuberror(List<Suberror> suberror) {
		this.suberror = suberror;
	}

}
