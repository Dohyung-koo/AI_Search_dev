package com.diquest.openapi.iptvott.response;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.List;

public class AndroidIntent {
    private String action;
    private String contentUri;

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private String packageName;

    @JsonProperty("extras")
    private List<Extras> extrasList;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getContentUri() {
        return contentUri;
    }

    public void setContentUri(String contentUri) {
        this.contentUri = contentUri;
    }

    public List<Extras> getExtrasList() {
        return extrasList;
    }

    public void setExtrasList(List<Extras> extrasList) {
        this.extrasList = extrasList;
    }
}
