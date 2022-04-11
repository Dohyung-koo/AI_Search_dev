package com.diquest.openapi.videopotal;

import org.codehaus.jackson.annotate.JsonProperty;

public class Json_Section_List {
	
	@JsonProperty("section")
	private Json_Section json_section;
	
	public Json_Section getJson_section() {
		return json_section;
	}

	public void setJson_section(Json_Section json_section) {
		this.json_section = json_section;
	}
	
}
