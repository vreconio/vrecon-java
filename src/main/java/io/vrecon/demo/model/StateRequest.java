package io.vrecon.demo.model;

public class StateRequest {
    private String apiKey;
    private String requestUuid;

    public StateRequest() {}

    public StateRequest(String apiKey, String requestUuid) {
        this.apiKey = apiKey;
        this.requestUuid = requestUuid;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getRequestUuid() {
        return requestUuid;
    }

    public void setRequestUuid(String requestUuid) {
        this.requestUuid = requestUuid;
    }
}
