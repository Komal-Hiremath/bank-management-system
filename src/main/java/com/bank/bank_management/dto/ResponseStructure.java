package com.bank.bank_management.dto;

import java.time.Instant;

public class ResponseStructure<T> {

    private boolean success;
    private String message;
    private T data;
    private Instant timestamp;

    public ResponseStructure() {
        this.timestamp = Instant.now();
    }

    public ResponseStructure(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = Instant.now();
    }

    public static <T> ResponseStructure<T> success(
            String message, T data) {
        return new ResponseStructure<>(true, message, data);
    }

    public static <T> ResponseStructure<T> error(
            String message) {
        return new ResponseStructure<>(false, message, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}