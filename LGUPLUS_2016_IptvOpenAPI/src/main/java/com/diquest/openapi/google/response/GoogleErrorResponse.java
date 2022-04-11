package com.diquest.openapi.google.response;

import org.codehaus.jackson.annotate.JsonProperty;

public class GoogleErrorResponse {

    @JsonProperty("fallbackExecution")
    private FallbackExecution fallbackExecution;

    public FallbackExecution getFallbackExecution() {
        return fallbackExecution;
    }

    public void setFallbackExecution(FallbackExecution fallbackExecution) {
        this.fallbackExecution = fallbackExecution;
    }
}
