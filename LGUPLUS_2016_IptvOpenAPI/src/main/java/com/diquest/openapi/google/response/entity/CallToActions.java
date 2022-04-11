package com.diquest.openapi.google.response.entity;

import com.diquest.openapi.google.response.AndroidIntent;
import org.codehaus.jackson.annotate.JsonProperty;

public class CallToActions {

    private String title;
    private String type;

    private String contentSource;

    @JsonProperty("androidIntent")
    private AndroidIntent androidIntent;

    public String getContentSource() {
        return contentSource;
    }

    public void setContentSource(String contentSource) {
        this.contentSource = contentSource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AndroidIntent getAndroidIntent() {
        return androidIntent;
    }

    public void setAndroidIntent(AndroidIntent androidIntent) {
        this.androidIntent = androidIntent;
    }
}
