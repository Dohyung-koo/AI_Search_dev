package com.diquest.openapi.iptvott.request;

import org.codehaus.jackson.annotate.JsonProperty;

public class StructuredQuery {
	
	private String query = "";
	
	@JsonProperty("tagged_queries")
	private String taggedQueries = "";
	
	@JsonProperty("normalized_queries")
	private String normalizedQueries = "";
	
	@JsonProperty("meta")
	private Meta meta;
	
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getTaggedQueries() {
		return taggedQueries;
	}
	public void setTaggedQueries(String taggedQueries) {
		this.taggedQueries = taggedQueries;
	}
	public String getNormalizedQueries() {
		return normalizedQueries;
	}
	public void setNormalizedQueries(String normalizedQueries) {
		this.normalizedQueries = normalizedQueries;
	}
	public Meta getMeta() {
		return meta;
	}
	public void setMeta(Meta meta) {
		this.meta = meta;
	}
}
