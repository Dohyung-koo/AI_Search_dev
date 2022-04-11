package com.diquest.openapi.iptvott.response;

import org.codehaus.jackson.annotate.JsonProperty;

public class IptvottErrorResponse {

    @JsonProperty("resultCode")
    private FallbackExecution fallbackExecution;

    public FallbackExecution getFallbackExecution() {
        return fallbackExecution;
    }

    public void setFallbackExecution(FallbackExecution fallbackExecution) {
        this.fallbackExecution = fallbackExecution;
    }
}
