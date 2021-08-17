package com.example.globallive.entities;

public class OperationSuccess {
    private boolean Success;
    private String Message;

    public OperationSuccess(boolean success, String message) {
        Success = success;
        Message = message;
    }

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean success) {
        Success = success;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
