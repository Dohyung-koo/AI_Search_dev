package com.diquest.openapi.google.response.entity;


import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

public class Badgings {

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    @JsonProperty("staticBadge")
    private StaticBadge staticBadge;

    private String type;

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    @JsonProperty("runTimeBadge")
    private RunTimeBadge runTimeBadge;

    public StaticBadge getStaticBadge() {
        return staticBadge;
    }

    public void setStaticBadge(StaticBadge staticBadge) {
        this.staticBadge = staticBadge;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public RunTimeBadge getRunTimeBadge() {
        return runTimeBadge;
    }

    public void setRunTimeBadge(RunTimeBadge runTimeBadge) {
        this.runTimeBadge = runTimeBadge;
    }
}
