package io.vrecon.demo.model;

public class RecognizeResponse {
    private boolean success;
    private String requestUuid;
    private String state;
    private String error;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getRequestUuid() {
        return requestUuid;
    }

    public void setRequestUuid(String requestUuid) {
        this.requestUuid = requestUuid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        if (success) {
            return String.format("RecognizeResponse{success=true, requestUuid='%s', state='%s'}",
                requestUuid, state);
        } else {
            return String.format("RecognizeResponse{success=false, error='%s'}", error);
        }
    }
}
