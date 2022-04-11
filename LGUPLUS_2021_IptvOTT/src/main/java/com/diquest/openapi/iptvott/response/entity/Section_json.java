package com.diquest.openapi.iptvott.response.entity;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class Section_json {
	
	@JsonProperty("section")
    private List sectionList;

	public List getSectionList() {
		return sectionList;
	}

	public void setSectionList(List sectionList) {
		this.sectionList = sectionList;
	}
}
