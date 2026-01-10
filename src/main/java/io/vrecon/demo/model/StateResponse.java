package io.vrecon.demo.model;

public class StateResponse {
    private boolean success;
    private String requestUuid;
    private String state;
    private RecognitionResult result;
    private String createdAt;
    private String updatedAt;
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

    public RecognitionResult getResult() {
        return result;
    }

    public void setResult(RecognitionResult result) {
        this.result = result;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("StateResponse{\n");
        sb.append("  success=").append(success).append("\n");
        if (success) {
            sb.append("  requestUuid='").append(requestUuid).append("'\n");
            sb.append("  state='").append(state).append("'\n");
            sb.append("  createdAt='").append(createdAt).append("'\n");
            sb.append("  updatedAt='").append(updatedAt).append("'\n");
            if (result != null) {
                sb.append("  result=").append(result.toString().replace("\n", "\n  ")).append("\n");
            }
            if (error != null) {
                sb.append("  error='").append(error).append("'\n");
            }
        } else {
            sb.append("  error='").append(error).append("'\n");
        }
        sb.append("}");
        return sb.toString();
    }
}
