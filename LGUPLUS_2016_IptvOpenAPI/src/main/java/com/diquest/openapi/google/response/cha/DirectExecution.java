package com.diquest.openapi.google.response.cha;

import com.diquest.openapi.google.response.AndroidIntent;
import org.codehaus.jackson.annotate.JsonProperty;

public class DirectExecution {

    @JsonProperty("androidIntent")
    private AndroidIntent androidIntent;

    public AndroidIntent getAndroidIntent() {
        return androidIntent;
    }

    public void setAndroidIntent(AndroidIntent androidIntent) {
        this.androidIntent = androidIntent;
    }
}
