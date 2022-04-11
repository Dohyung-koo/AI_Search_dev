package com.diquest.openapi.util;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "error")
@XmlAccessorType(XmlAccessType.FIELD)
public class OpenAPIErrorResponse {
	
	private String code="";
	private String message="";
	private String url="";
	
	@XmlElement(name = "suberror")
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<Suberror> getSuberror() {
		return suberror;
	}

	public void setSuberror(List<Suberror> suberror) {
		this.suberror = suberror;
	}

}
