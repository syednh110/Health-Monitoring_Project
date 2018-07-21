package com.respireplus.respire.models;

public class HealthResponse {
    private boolean status;
    private boolean result;

    public HealthResponse() {
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "HealthResponse{" +
                "status=" + status +
                ", result=" + result +
                '}';
    }
}
