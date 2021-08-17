package com.example.globallive.threads;

public interface IRegisterActivityCallback {
    void callBackSuccess(int userID);
    void callBackFail(String message);
}
