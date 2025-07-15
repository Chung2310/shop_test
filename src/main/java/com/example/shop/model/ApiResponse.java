package com.example.shop.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class ApiResponse<T> {
    private int status;
    private String message;
    private T result;

    public ApiResponse() {}

    public ApiResponse(int status, String message, T result) {
        this.result = result;
        this.message = message;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
